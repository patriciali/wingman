package com.patriciasays.wingman.util;

public class Constants {

    /**
     * Javascript script to send login
     * email, password
     */
    public static final String GET_AUTH_TOKEN_JS =
            "Meteor.loginWithPassword('%s', '%s', function(err) { if(err) { throw err; } " +
                    "AndroidFunction.jsSetToken(Accounts._storedLoginToken()); })";
    public static final String AUTH_TOKEN_STOPSHIP = "4dGr0rI5RzO3e7ily9JN_SzoyBz9baDe5usS8XpSlE6";

    /**
     * Key names for storing information in SharedPreferences
     */
    public static final String URL_PREFERENCE_KEY = "url_key";
    public static final String PORT_NUMBER_PREFERENCE_KEY = "port_number_key";
    public static final String COMPETITION_ID_PREFERENCE_KEY = "comp_id_key";
    public static final String AUTH_TOKEN_PREFERENCE_KEY = "auth_token_key";

    /**
     * Default server values
     */
    public static final String DEFAULT_SERVER_URL = "http://staging.live.jflei.com/api";
    public static final String DEFAULT_PORT = "80";

    /**
     * Server API endpoints
     */
    public static final String COMPETITIONS_URL_SUFFIX = "/v0/competitions";
    // competition _id
    public static final String ROUNDS_URL_SUFFIX = "/v0/competitions/%s/rounds";
    // competition _id
    public static final String PARTICIPANTS_URL_SUFFIX =
            "/v0/competitions/%s/registrations";
    // competition _id, eventCode, nthRound
    public static final String AVERAGE_IN_PROGRESS_URL_SUFFIX =
            "/v0/competitions/%s/rounds/%s/%s/results";
    // competition _id, eventCode, nthRound, token
    public static final String UPLOAD_TIME_URL_SUFFIX =
            "/v0/competitions/%s/rounds/%s/%d/results?token=%s";

}
