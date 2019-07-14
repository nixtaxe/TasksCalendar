package zabortceva.eventscalendar.requests;

public interface ApiStrings {
    //Most valuable string constant ever
    public static final String X_FIREBASE_AUTH = "X-Firebase-Auth";
    public static final String ACCEPT = "Accept: application/json";
    public static final String CONTENT_TYPE = "Content-Type: application/json";

    //Tasks path strings
    public static final String TASKS_PATH = "api/v1/tasks";
    public static final String TASKS_ID_PATH = "api/v1/tasks/{id}";

    //Events path strings
    public static final String EVENTS_PATH = "/api/v1/events";
    public static final String EVENTS_ID_PATH = "/api/v1/events/{id}";

    //Param strings
    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String EVENT_ID = "event_id";
    public static final String FROM = "from";
    public static final String TO = "to";
}
