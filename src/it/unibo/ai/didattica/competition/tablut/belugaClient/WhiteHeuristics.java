package it.unibo.ai.didattica.competition.tablut.belugaClient;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.Map;

public class WhiteHeuristics extends Heuristics {

    //Weights
    private final double[] whiteWeights = {30, 25, 5, 10, 15};
    private final static int WHITE_REMAINING  = 0;
    private final static int BLACK_EATEN = 1;
    private final static int MOVES_TO_ESCAPE = 2;
    private final static int BLACKS_NEAR_KING = 3;
    private final static int PROTECTION_KING = 4;

    public WhiteHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        double result= 0;

        checkPawns(this.state);

        result += whiteWeights[WHITE_REMAINING] * getWhiteRemaining();
        result += whiteWeights[BLACK_EATEN] * getBlackEaten();
        result += whiteWeights[MOVES_TO_ESCAPE] * kingMovesToEscape();
        result += whiteWeights[BLACKS_NEAR_KING] * pawnsNearKing();
        result += whiteWeights[PROTECTION_KING] * protectionKing();

        return result;
    }

    private double getWhiteRemaining() {
        return numWhitePawnRemaining / NUM_WHITE;
    }

    private double getBlackEaten() {
        return (NUM_BLACK - numBlackPawnRemaining) / NUM_BLACK;
    }

    private double pawnsNearKing() {
        double res=0;
        if(this.kingPosition == null)
            throw new IllegalStateException("King position must be initialized.");

        Map<Map_Key, PawnsAround> pawnsAround = checkPawnsAround(state,kingPosition);

        int NumBlackNeedToEat = getNumBlackNeedToEat(state);

        int numBlackAround = pawnsAround.get(Map_Key.BLACK).count;
        int numCitadelsAround = pawnsAround.get(Map_Key.CITADELS).count;

        switch (NumBlackNeedToEat) {

            case (4):
                res = (4- numBlackAround)/4.0;
                break;

            case(3):
                res =  (3 - numBlackAround)/3.0;
                break;

            case(2):
                res = (2 - numBlackAround - numCitadelsAround)/2.0;
                break;
        }
        return res;
    }

    //Ho una strada diretta per l'escape
    private double kingMovesToEscape() {
        int col = 0;
        int row = 0;

        if(this.kingPosition == null)
            throw new IllegalStateException("King position must be initialized.");

        //Se sono al centro non calcolo nemmeno le possibili mosse perchè con una mossa non riesco
        if (!kingIsInCenterSquare()){ //se riesco a "vedere" un escape
            int xK = kingPosition[0];
            int yK = kingPosition[1];

            //sei nei quattro quadrati 3x3 degli angoli della board
            if (!(xK >= 3 && xK <= 5) && !(yK >= 3 && yK <= 5)){   //Vedo escape da tutti i lati     //Move row/col for escape
                //Controllo se nelle 4 direzioni ho posizioni occupate
                col = countFreeColumn(state);
                row = countFreeRow(state);
            }
            //Vedo escape solo da un lato

            if (xK >= 3 && xK <= 5){        //move only row for escape
                row = countFreeRow(state);
            }
            if(yK >= 3 && yK <= 5) {        //move only col for escape
                col = countFreeColumn(state);
            }
        }
        return  (col + row)/4.0;
    }

    private int countFreeColumn(State state){
        int row=kingPosition[0];
        int column=kingPosition[1];
        int[] currentPosition = new int[2];
        int freeWays=0;
        int countUp=0;
        int countDown=0;
        //going down
        for(int i=row+1;i<=8;i++) {
            currentPosition[0]=i;
            currentPosition[1]=column;
            if (checkOccupiedPosition(state,currentPosition)) {
                countDown++;
            }
        }
        if(countDown==0)
            freeWays++;
        //going up
        for(int i=row-1;i>=0;i--) {
            currentPosition[0]=i;
            currentPosition[1]=column;
            if (checkOccupiedPosition(state,currentPosition)){
                countUp++;
            }
        }
        if(countUp==0)
            freeWays++;

        return freeWays;
    }

    private int countFreeRow(State state){
        int row=kingPosition[0];
        int column=kingPosition[1];
        int[] currentPosition = new int[2];
        int freeWays=0;
        int countRight=0;
        int countLeft=0;
        //going right
        for(int i = column+1; i<=8; i++) {
            currentPosition[0]=row;
            currentPosition[1]=i;
            if (checkOccupiedPosition(state,currentPosition)) {
                countRight++;
            }
        }
        if(countRight==0)
            freeWays++;
        //going left
        for(int i=column-1;i>=0;i--) {
            currentPosition[0]=row;
            currentPosition[1]=i;
            if (checkOccupiedPosition(state,currentPosition)){
                countLeft++;
            }
        }
        if(countLeft==0)
            freeWays++;

        return freeWays;
    }

    private double protectionKing(){

        //Values whether there is only a white pawn near to the king
        final double VAL_NEAR = 0.6;
        final double VAL_TOT = 1.0;

        double result = 0.0;

        int[] kingPos = kingPosition;
        //Pawns near to the king
        Map<Map_Key, PawnsAround> pawnsAround = checkPawnsAround(state,kingPosition);
        ArrayList<int[]> pawnsPositions =  (ArrayList<int[]>)  pawnsAround.get(Map_Key.BLACK).occupiedPosition;
        int numWhiteAround = pawnsAround.get(Map_Key.WHITE).count;

        //There is a black pawn that threatens the king and 2 pawns are enough to eat the king
        if (pawnsPositions.size() == 1 && getNumBlackNeedToEat(state) == 2){
            int[] enemyPos = pawnsPositions.get(0);
            //Used to store other position from where king could be eaten
            int[] targetPosition = new int[2];
            //Enemy right to the king
            if(enemyPos[0] == kingPos[0] && enemyPos[1] == kingPos[1] + 1){
                //Left to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0];
                targetPosition[1] = kingPos[1] - 1;
                if (state.getPawn(targetPosition[0],targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += VAL_NEAR;
                }
                //Enemy left to the king
            }else if(enemyPos[0] == kingPos[0] && enemyPos[1] == kingPos[1] -1){
                //Right to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0];
                targetPosition[1] = kingPos[1] + 1;
                if(state.getPawn(targetPosition[0],targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += VAL_NEAR;
                }
                //Enemy up to the king
            }else if(enemyPos[1] == kingPos[1] && enemyPos[0] == kingPos[0] - 1){
                //Down to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0] + 1;
                targetPosition[1] = kingPos[1];
                if(state.getPawn(targetPosition[0], targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += VAL_NEAR;
                }
                //Enemy down to the king
            }else{
                //Up there is a white pawn and king is protected
                targetPosition[0] = kingPos[0] - 1;
                targetPosition[1] = kingPos[1];
                if(state.getPawn(targetPosition[0], targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += VAL_NEAR;
                }
            }

            //Considering whites to use as barriers for the target pawn
            double otherPoints = VAL_TOT - VAL_NEAR;
            double contributionPerN = 0.0;

            //Whether it is better to keep free the position
            if (targetPosition[0] == 0 || targetPosition[0] == 8 || targetPosition[1] == 0 || targetPosition[1] == 8){
                if(state.getPawn(targetPosition[0],targetPosition[1]).equalsPawn(State.Pawn.EMPTY.toString())){
                    result = 1.0;
                } else {
                    result = 0.0;
                }
            }else{
                //Considering a reduced number of neighbours whether target is near to citadels or throne
                if (targetPosition[0] == 4 && targetPosition[1] == 2 || targetPosition[0] == 4 && targetPosition[1] == 6
                        || targetPosition[0] == 2 && targetPosition[1] == 4 || targetPosition[0] == 6 && targetPosition[1] == 4
                        || targetPosition[0] == 3 && targetPosition[1] == 4 || targetPosition[0] == 5 && targetPosition[1] == 4
                        || targetPosition[0] == 4 && targetPosition[1] == 3 || targetPosition[0] == 4 && targetPosition[1] == 5){
                    contributionPerN = otherPoints / 2;
                }else{
                    contributionPerN = otherPoints / 3;
                }

                result += contributionPerN * numWhiteAround;
            }

        }
        return result;
    }

}
