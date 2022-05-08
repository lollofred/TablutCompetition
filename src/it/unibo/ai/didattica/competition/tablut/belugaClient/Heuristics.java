package it.unibo.ai.didattica.competition.tablut.belugaClient;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Heuristics {
    protected State state;

    public enum Map_Key {
        WHITE, BLACK, EMPTY, CITADELS;
    }

    protected final int[][] citadels = {{0, 3}, {0, 4}, {0, 5}, {1, 4},
            {3, 0}, {4, 0}, {5, 0}, {4, 1},
            {8, 3}, {8, 4}, {8, 5}, {7, 4},
            {3, 8}, {4, 8}, {5, 8}, {4, 7}};

    protected final int[][] escapes = {{0,1}, {0,2}, {1,0}, {2,0},
            {8,1}, {8,2}, {6,0}, {7,0},
            {0,6}, {0,7}, {1,8}, {2,8},
            {6,8}, {7,8}, {8,6}, {8,7}};

    protected final int[] throne = {4,4};

    protected final int[][] positionsNearThrone = {{4,3}, {4,5}, {3,4}, {5,4}};

    protected final static int NUM_BLACK = 16;
    protected final static int NUM_WHITE = 8;
    protected double numBlackPawnRemaining = 0;
    protected double numWhitePawnRemaining = 0;
    protected int[] kingPosition = new int[2];
    protected State.Pawn[][] board;

    public Heuristics(State state) {
        this.state = state;
    }

    /**
     *
     * @return true if king is on throne, false otherwise
     */
    public boolean checkKingPosition(State state){
        if(state.getPawn(4,4).equalsPawn("K"))
            return true;
        else
            return false;
    }

    public boolean isCitadel(int x, int y) {
        for (int[] citadel : citadels) {
            if (citadel[0] == x && citadel[1] == y)
                return true;
        }
        return false;
    }

    public boolean isInThrone(int x, int y) {
        if (throne[0] == x && throne[1] == y)
            return true;

        return false;
    }

    public boolean isEscapes(int x, int y) {
        for (int[] escape : escapes) {
            if (escape[0] == x && escape[1] == y)
                return true;
        }
        return false;
    }

    public boolean isNearThrone(int x, int y) {
        for (int[] positionNearThrone : positionsNearThrone) {
            if (positionNearThrone[0] == x && positionNearThrone[1] == y)
                return true;
        }
        return false;
    }

    public void getKingPosition(State state) {
        State.Pawn[][] board = state.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (state.getPawn(i, j).equals(State.Pawn.KING)) {
                    kingPosition[0] = i;
                    kingPosition[1] = j;
                }
            }
        }
    }

    public boolean kingIsInCenterSquare(){
        if((kingPosition[0] > 2 && kingPosition[0] < 6) &&
            (kingPosition[1] > 2 && kingPosition[1] < 6) )
                return true;

        return false;
    }

    // Updates Black remaining, White remaining and King position
    public void checkPawns(State state) {
        numWhitePawnRemaining = state.getNumberOf(State.Pawn.WHITE);
        numBlackPawnRemaining = state.getNumberOf(State.Pawn.BLACK);
        getKingPosition(state);
    }

    public double evaluateState() {
        return 1;
    }

    public Map<Map_Key, PawnsAround> checkPawnsAround(State state,int[] position) {
        int numBlack = 0;
        int numWhite = 0;
        int numEmpty = 0;
        int numCitadels = 0;
        List<int[]> occupiedPositionBlack  = new ArrayList<int[]>();
        List<int[]> occupiedPositionWhite = new ArrayList<int[]>();
        List<int[]> occupiedPositionEmpty = new ArrayList<int[]>();
        List<int[]> occupiedPositionCitadels = new ArrayList<int[]>();

        Map<Map_Key, PawnsAround> AroundPawn = new HashMap<>();

        for (GameAshtonTablut.Direction dir : GameAshtonTablut.Direction.values()) {
            int newx = position[0] + dir.getXdiff();
            int newy = position[1] + dir.getYdiff();

            if ((newx >= 0 && newx <= 8) && (newy >= 0 && newy <= 8)) {//Inside the board
                State.Pawn pawn = this.state.getPawn(newx, newy);
                switch (pawn) {
                    case BLACK:
                        numBlack++;
                        occupiedPositionBlack.add(new int[]{newx, newy});
                        break;
                    case WHITE:
                        numWhite++;
                        occupiedPositionWhite.add(new int[]{newx, newy});
                        break;
                    case EMPTY:
                        numEmpty++;
                        occupiedPositionEmpty.add(new int[]{newx, newy});
                        break;
                    default:
                        if(this.isCitadel(newx, newy)) {
                            numCitadels++;
                            occupiedPositionCitadels.add(new int[]{newx, newy});
                        }
                        break;
                }
            }
        }
        PawnsAround Black = new PawnsAround(numBlack, occupiedPositionBlack);
        PawnsAround White = new PawnsAround(numBlack, occupiedPositionBlack);
        PawnsAround Empty = new PawnsAround(numBlack, occupiedPositionBlack);
        PawnsAround Citadels = new PawnsAround(numBlack, occupiedPositionBlack);

        AroundPawn.put(Map_Key.BLACK, Black);
        AroundPawn.put(Map_Key.WHITE, White);
        AroundPawn.put(Map_Key.CITADELS, Empty);
        AroundPawn.put(Map_Key.EMPTY, Citadels);

        return AroundPawn;
    }

    public int getNumBlackNeedToEat(State state)
    {
        int x= kingPosition[0];
        int y = kingPosition[1];
        if(isInThrone(x,y))
            return 4;
        else if(isNearThrone(x,y))
            return 3;
        else
            return 2;
    }

    public boolean checkOccupiedPosition(State state,int[] position){
        return !state.getPawn(position[0], position[1]).equals(State.Pawn.EMPTY);
    }

    public class PawnsAround {
        public int count;
        public List<int[]> occupiedPosition;
        public PawnsAround(int count, List<int[]> occupiedPosition)
        {
            this.count = count;
            this.occupiedPosition = occupiedPosition;
        }
    }
}