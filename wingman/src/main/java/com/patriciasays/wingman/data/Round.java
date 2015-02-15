package com.patriciasays.wingman.data;

public class Round {

    private String _id;
    private String competitionId;
    private int durationMinutes;
    private String eventCode;
    private String formatCode;
    private Time hardCutoff;
    private int nthDay;
    private int nthRound;
    private Progress progress;
    private String roundCode;
    private Time softCutoff;
    private String status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public Time getHardCutoff() {
        return hardCutoff;
    }

    public void setHardCutoff(Time hardCutoff) {
        this.hardCutoff = hardCutoff;
    }

    public int getNthDay() {
        return nthDay;
    }

    public void setNthDay(int nthDay) {
        this.nthDay = nthDay;
    }

    public int getNthRound() {
        return nthRound;
    }

    public void setNthRound(int nthRound) {
        this.nthRound = nthRound;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public String getRoundCode() {
        return roundCode;
    }

    public void setRoundCode(String roundCode) {
        this.roundCode = roundCode;
    }

    public Time getSoftCutoff() {
        return softCutoff;
    }

    public void setSoftCutoff(Time softCutoff) {
        this.softCutoff = softCutoff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Progress {
        private double done;
        private int total;

        public double getDone() {
            return done;
        }

        public void setDone(double done) {
            this.done = done;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

    }

    public static class HardCutoff {
        private Time time;

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }
    }

    public static class SoftCutoff {
        private String formatCode;
        private Time time;

        public String getFormatCode() {
            return formatCode;
        }

        public void setFormatCode(String formatCode) {
            this.formatCode = formatCode;
        }

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }
    }

    public static class Time {
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

}
