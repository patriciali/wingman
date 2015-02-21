package com.patriciasays.wingman.data;

/**
 * Result object that we upload to server
 */
public class Result {

    public Result() {

    }

    public Result(String registrationId_, int solveIndex_) {
        this.registrationId = registrationId_;
        this.solveIndex = solveIndex_;
    }

    private String registrationId;
    private SolveTime solveTime;
    private int solveIndex;

    @Override
    public String toString() {
        return "registrationId: " + this.registrationId + ", solveIndex: " + this.solveIndex + ", solveTime: " + this.solveTime;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public SolveTime getSolveTime() {
        return solveTime;
    }

    public void setSolveTime(SolveTime solveTime) {
        this.solveTime = solveTime;
    }

    public int getSolveIndex() {
        return solveIndex;
    }

    public void setSolveIndex(int solveIndex) {
        this.solveIndex = solveIndex;
    }

    public static class SolveTime {

        private long millis;
        private int decimals;
        private int moveCount;
        private String[] penalties;
        private int puzzlesSolvedCount;
        private int puzzlesAttemptedCount;

        @Override
        public String toString() {
            return "millis: " + millis;
        }

        public long getMillis() {
            return millis;
        }

        public void setMillis(long millis) {
            this.millis = millis;
        }

        public int getDecimals() {
            return decimals;
        }

        public void setDecimals(int decimals) {
            this.decimals = decimals;
        }

        public int getMoveCount() {
            return moveCount;
        }

        public void setMoveCount(int moveCount) {
            this.moveCount = moveCount;
        }

        public String[] getPenalties() {
            return penalties;
        }

        public void setPenalties(String[] penalties) {
            this.penalties = penalties;
        }

        public int getPuzzlesSolvedCount() {
            return puzzlesSolvedCount;
        }

        public void setPuzzlesSolvedCount(int puzzlesSolvedCount) {
            this.puzzlesSolvedCount = puzzlesSolvedCount;
        }

        public int getPuzzlesAttemptedCount() {
            return puzzlesAttemptedCount;
        }

        public void setPuzzlesAttemptedCount(int puzzlesAttemptedCount) {
            this.puzzlesAttemptedCount = puzzlesAttemptedCount;
        }
    }

}
