package cw.gymbuddy;

public class Events {

    String username;
    String date;
    String time;
    String description;
    String nameLoc;
    String addrLoc;
    public Events(){

    }

    public Events(String username,  String description, String date, String time, String nameLoc, String addrLoc) {
        this.username = username;
        this.description = description;
        this.date = date;
        this.time = time;
        this.nameLoc = nameLoc;
        this.addrLoc = addrLoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameLoc() {
        return nameLoc;
    }

    public void setNameLoc(String nameLoc) {
        this.nameLoc = nameLoc;
    }

    public String getAddrLoc() {
        return addrLoc;
    }

    public void setAddrLoc(String addrLoc) {
        this.addrLoc = addrLoc;
    }
}
