package com.patriciasays.wingman.data;

/**
 * I hate this
 */
public class AverageInProgressRequestData {

    public AverageInProgressRequestData(String registrationId) {
        this.setData(new Data(registrationId));
    }

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        private String registrationId;

        public Data(String registrationId) {
            this.setRegistrationId(registrationId);
        }

        public String getRegistrationId() {
            return registrationId;
        }

        public void setRegistrationId(String registrationId) {
            this.registrationId = registrationId;
        }
    }

}
