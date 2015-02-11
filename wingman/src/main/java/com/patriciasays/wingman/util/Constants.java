package com.patriciasays.wingman.util;

public class Constants {

    /**
     * Key names for storing information in SharedPreferences
     */
    public static final String URL_PREFERENCE_KEY = "url_key";
    public static final String PORT_NUMBER_PREFERENCE_KEY = "port_number_key";
    // TODO way to stored serialized objects?
    public static final String COMPETITION_ID_PREFERENCE_KEY = "comp_id_key";

    /**
     * Default server values
     */
    public static final String DEFAULT_SERVER_URL = "http://staging.live.jflei.com/api/v0";
    public static final String DEFAULT_PORT = "80";

    /**
     * Server API endpoints
     */
    public static final String COMPETITIONS_URL_SUFFIX = "/competitions";
    public static final String ROUNDS_URL_SUFFIX = "/competitions/%s/rounds"; // _id of competition
    public static final String PARTICIPANTS_URL_SUFFIX =
            "/competitions/%s/registrations"; // _id of competition

}
