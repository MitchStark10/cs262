package edu.calvin.cs262;

/**
 * A Player class (POJO) for the player relation
 *
 * @author kvlinden
 * @version summer, 2016
 */
class Player {

    private int id;
    private String email_address, name;

    Player() { /* a default constructor, required by Gson */ }

    Player(int id, String emailaddress, String name) {
        this.id = id;
        this.email_address = emailaddress;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailaddress() {
        return email_address;
    }

    public void setEmailaddress(String emailaddress) {
        this.email_address = emailaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
