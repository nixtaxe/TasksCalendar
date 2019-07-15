package zabortceva.eventscalendar.requests;

public interface ApiStrings {
    //Most valuable string constant ever
    public static final String X_FIREBASE_AUTH = "X-Firebase-Auth";
    public static final String ACCEPT_JSON = "Accept: application/json";
    public static final String ACCEPT_TEXT = "Accept: text/plain";
    public static final String CONTENT_TYPE = "Content-Type: application/json";

    //Tasks path strings
    public static final String TASKS_PATH = "api/v1/tasks";
    public static final String TASKS_ID_PATH = "api/v1/tasks/{id}";

    //Events path strings
    public static final String EVENTS_PATH = "/api/v1/events";
    public static final String EVENTS_ID_PATH = "/api/v1/events/{id}";

    //Share path strings
    public static final String SHARE_PATH = "/api/v1/share";
    public static final String SHARE_TOKEN_PATH = "/api/v1/share/{token}";

    //Permission path strings
    public static final String PERMISSIONS_PATH = "/api/v1/permissions";
    public static final String PERMISSIONS_ID_PATH = "/api/v1/permissions/{id}";
    public static final String GRANT_PERMISSION_PATH = "/api/v1/grant";

    //Param strings
    public static final String ID = "id";
    public static final String EVENT_ID = "event_id";
    public static final String ENTITY_ID = "entity_id";
    public static final String USER_ID = "user_id";

    public static final String COUNT = "count";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String TOKEN = "token";
    public static final String PERMISSIONS = "permissions";

    public static final String ENTITY_TYPE = "entity_type";
    public interface ENTITY_TYPE_OPTIONS {
        public static final String EVENT = "EVENT";
        public static final String PATTERN = "PATTERN";
        public static final String TASK = "TASK";
    }

    public static final String ACTION = "action";
    public interface ACTION_OPTIONS {
        public static final String READ = "READ";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
    }
}
