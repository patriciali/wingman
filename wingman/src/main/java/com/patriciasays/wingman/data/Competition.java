package com.patriciasays.wingman.data;

public class Competition {

    private String _id;
    private String competitionName;
    private boolean listed;
    private String wcaCompetitionId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    public String getWcaCompetitionId() {
        return wcaCompetitionId;
    }

    public void setWcaCompetitionId(String wcaCompetitionId) {
        this.wcaCompetitionId = wcaCompetitionId;
    }

}
