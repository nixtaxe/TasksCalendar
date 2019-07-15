package zabortceva.eventscalendar.serverdata;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    private String id;
    @Expose
    private String username;
    @Expose
    private String photo;
    @Expose
    private Boolean enabled;
    @Expose
    private Boolean credentials_non_expired;
    @Expose
    private Boolean account_non_locked;
    @Expose
    private Boolean account_non_expired;

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getCredentials_non_expired() {
        return credentials_non_expired;
    }

    public void setCredentials_non_expired(Boolean credentials_non_expired) {
        this.credentials_non_expired = credentials_non_expired;
    }

    public Boolean getAccount_non_locked() {
        return account_non_locked;
    }

    public void setAccount_non_locked(Boolean account_non_locked) {
        this.account_non_locked = account_non_locked;
    }

    public Boolean getAccount_non_expired() {
        return account_non_expired;
    }

    public void setAccount_non_expired(Boolean account_non_expired) {
        this.account_non_expired = account_non_expired;
    }
}
