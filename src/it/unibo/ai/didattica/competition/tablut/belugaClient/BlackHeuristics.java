package it.unibo.ai.didattica.competition.tablut.belugaClient;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Map;

public class BlackHeuristics  extends Heuristics {

    private final int[][] protectedEscapes = {{1, 2}, {2, 1},
            {6, 1}, {7, 2},
            {1, 6}, {2, 7},
            {6, 7}, {7, 6}};

    //Weights
    private final double[] blackWeights = {30, 45, 5, 15};
    //private final double bonusAggressive = 1.5;
    private static final int BLACK_REMAINING = 0;
    private static final int WHITE_EATEN = 1;
    private static final int PROTECT_ESCAPES = 2;
    private static final int BLACKS_NEAR_KING = 3;

    public BlackHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        double result = 0;

        checkPawns(this.state);

        result += blackWeights[BLACK_REMAINING] * getBlackRemaining();
        result += blackWeights[WHITE_EATEN] * getWhiteEaten();
        result += blackWeights[PROTECT_ESCAPES] * getBlackProtectingEscapes();
        result += blackWeights[BLACKS_NEAR_KING] * pawnsNearKing();

        return result;
    }

    private double getBlackRemaining() {
        return this.numBlackPawnRemaining/NUM_BLACK;
    }

    private double getWhiteEaten() {
        return (NUM_WHITE - this.numWhitePawnRemaining)/NUM_WHITE;
    }

    private double getBlackProtectingEscapes() {
        double result = 0;
        for (int[] protectedEscape : protectedEscapes) {
            //check how many protectedEscapes positions are occupied by blacks
            if (state.getPawn(protectedEscape[0], protectedEscape[1]).equals(State.Pawn.BLACK))
                result++;
        }
        return result;
    }

    private double pawnsNearKing() {
        double result = 0;
        if(this.kingPosition == null)
            throw new IllegalStateException("King position must be initialized.");

        Map<Map_Key, PawnsAround> pawnsAround = checkPawnsAround(state,kingPosition);

        int NumBlackNeedToEat = getNumBlackNeedToEat(state);

        int numBlackAround = pawnsAround.get(Map_Key.BLACK).count;
        int numCitadelsAround = pawnsAround.get(Map_Key.CITADELS).count;

        switch (NumBlackNeedToEat) {

            case (4):
                result = (4- numBlackAround)/4.0;
                break;

            case(3):
                result =  (3 - numBlackAround)/3.0;
                break;

            case(2):
                result = (2 - numBlackAround - numCitadelsAround)/2.0;
                break;
        }
        return result;
    }
}
