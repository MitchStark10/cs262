package edu.calvin.cs262;

/**
 * A Player class (POJO) for the player relation
 *
 * @author kvlinden
 * @version summer, 2016
 */
public class Player {

    private int id;
    private String emailaddress, name;

    Player() { /* a default constructor, required by Gson */  }

    Player(int id, String emailaddress, String name) {
        this.id = id;
        this.emailaddress = emailaddress;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getEmailaddress() {
        return emailaddress;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }
    public void setName(String name) {
        this.name = name;
    }

}
