package edu.calvin.cs262.lab06;

/**
 * Created by mitchstark on 10/16/16.
 */

public class Player {
    private String id, name, email;

    public Player(String new_id, String new_name, String new_email) {
        id = new_id;
        name = new_name;
        email = new_email;
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}
}
