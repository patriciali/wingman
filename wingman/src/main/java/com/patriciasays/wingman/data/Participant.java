package com.patriciasays.wingman.data;

import java.util.List;

public class Participant {

    private String _id;
    private List<String> checkedInEvents;
    private String competitionId;
    private String countryId;
    private String gender;
    private List<String> registeredEvents;
    private String uniqueName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getCheckedInEvents() {
        return checkedInEvents;
    }

    public void setCheckedInEvents(List<String> checkedInEvents) {
        this.checkedInEvents = checkedInEvents;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<String> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
