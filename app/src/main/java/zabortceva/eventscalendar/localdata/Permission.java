package zabortceva.eventscalendar.localdata;

import com.google.gson.annotations.Expose;

public class Permission {
    @Expose
    private Long id;
    @Expose
    private String owner_id;
    @Expose
    private Long created_at;
    @Expose
    private Long updated_at;

    @Expose
    private String user_id;
    @Expose
    private Long entity_id;
    @Expose
    private String name;

    /*Default constructor*/
    public Permission() {
    }

    public Permission(String user_id, Long entity_id, String name) {
        this.user_id = user_id;
        this.entity_id = entity_id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Long entity_id) {
        this.entity_id = entity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
