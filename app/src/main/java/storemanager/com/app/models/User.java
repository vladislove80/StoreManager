package storemanager.com.app.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String status;
    private String id;
    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String status, String id, String email) {
        this.status = status;
        this.id = id;
        this.email = email;
    }


}
