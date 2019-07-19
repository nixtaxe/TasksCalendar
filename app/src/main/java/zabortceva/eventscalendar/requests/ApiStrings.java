package zabortceva.eventscalendar.requests;

import android.widget.ToggleButton;

public interface ApiStrings {
    //Most valuable string constant ever
    public static final String X_FIREBASE_AUTH = "X-Firebase-Auth";
    public static final String ACCEPT_JSON = "Accept: application/json";
    public static final String ACCEPT_TEXT = "Accept: text/plain";
    public static final String CONTENT_TYPE = "Content-Type: application/json";

    //Tasks path strings
    public static final String TASKS_PATH = "api/v1/tasks";
    public static final String TASKS_ID_PATH = TASKS_PATH + "/{id}";

    //Events path strings
    public static final String EVENTS_PATH = "/api/v1/events";
    public static final String EVENTS_ID_PATH = EVENTS_PATH + "/{id}";
    public static final String INSTANCES_PATH = "/api/v1/events/instances";

    //Share path strings
    public static final String SHARE_PATH = "/api/v1/share";
    public static final String SHARE_TOKEN_PATH = "/api/v1/share/{token}";

    //Permission path strings
    public static final String PERMISSIONS_PATH = "/api/v1/permissions";
    public static final String PERMISSIONS_ID_PATH = PERMISSIONS_PATH + "/{id}";
    public static final String GRANT_PERMISSION_PATH = "/api/v1/grant";
    public static final String USER_PATH = "/api/v1/user";

    //Patterns path strings
    public static final String PATTERNS_PATH = "/api/v1/patterns";
    public static final String PATTERNS_ID_PATH = PATTERNS_PATH + "/{id}";

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
    public static final String EMAIL = "email";

    public static final String[] FREQUENCIES = {"ONCE", "DAILY", "WEEKLY", "MONTHLY", "YEARLY"};
    public static final String PATTERN = "pattern";
    public interface PATTERN_OPTIONS {
        public static final String ONCE = "ONCE";
        public static final String DAILY = "DAILY";
        public static final String WEEKLY = "WEEKLY";
        public static final String MONTHLY = "MONTHLY";
        public static final String YEARLY = "YEARLY";
    }

    public static final String[] WEEKDAYS = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
    public interface WEEKDAY_OPTIONS {
        public static final String MONDAY = "MO";
        public static final String TUESDAY = "TU";
        public static final String WEDNESDAY = "WE";
        public static final String THURSDAY = "TH";
        public static final String FRIDAY = "FR";
        public static final String SATURDAY = "SA";
        public static final String SUNDAY = "SU";
    }

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
