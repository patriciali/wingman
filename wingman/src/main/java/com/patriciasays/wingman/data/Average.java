package com.patriciasays.wingman.data;

public class Average {

    private String _id;
    private boolean advanced;
    private AverageTime average;
    private int bestIndex;
    private String competitionId;
    private String createdAt;
    private int position;
    private String registrationId;
    private String roundId;
    private SolveTime[] solves;
    private long sortableAverageValue;
    private long sortableBestValue;
    private String updatedAt;
    private int worstIndex;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public AverageTime getAverage() {
        return average;
    }

    public void setAverage(AverageTime average) {
        this.average = average;
    }

    public int getBestIndex() {
        return bestIndex;
    }

    public void setBestIndex(int bestIndex) {
        this.bestIndex = bestIndex;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public SolveTime[] getSolves() {
        return solves;
    }

    public void setSolves(SolveTime[] solves) {
        this.solves = solves;
    }

    public long getSortableAverageValue() {
        return sortableAverageValue;
    }

    public void setSortableAverageValue(long sortableAverageValue) {
        this.sortableAverageValue = sortableAverageValue;
    }

    public long getSortableBestValue() {
        return sortableBestValue;
    }

    public void setSortableBestValue(long sortableBestValue) {
        this.sortableBestValue = sortableBestValue;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWorstIndex() {
        return worstIndex;
    }

    public void setWorstIndex(int worstIndex) {
        this.worstIndex = worstIndex;
    }

    public static class AverageTime {

        private long millis;
        private int decimals;

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

    }

    public static class SolveTime {

        private long millis;
        private int decimals;
        private int moveCount;
        private String[] penalties;
        private int puzzlesSolvedCount;
        private int puzzlesAttemptedCount;
        private String updatedAt;

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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
