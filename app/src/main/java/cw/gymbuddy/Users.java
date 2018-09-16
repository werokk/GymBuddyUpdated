package cw.gymbuddy;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Werokk on 27/03/2018.
 */

public class Users {
    String id;
    String name;
    String email;
    String username;
    String password;
    double longtitude;
    double latitude;
    String address;
    String description;
    float distance;


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }



    public ArrayList<Bitmap> getImg() {
        return img;
    }

    ArrayList<Bitmap> img = new ArrayList<>();



    public Users(String id ,String name, String email, String username, String password, double longtitude, double latitude, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.address = address;
        this.description = description;


    }

    public Users(String name, String email, String username, String password, double longtitude, double latitude, String address, String description) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.address = address;
        this.description = description;

    }
    public Users(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getLongtitude(){return longtitude;}

    public double getLatitude(){return  latitude;}

    public String getAddress(){return address;}





    public void setId (String id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLongtitude(Double longtitude){this.longtitude= longtitude;}

    public void setLatitude (Double latitude){this.latitude = latitude;}

    public void setAddress (String address){this.address = address;}

    public void addToarray(Bitmap b){
        img.add( b );
    }

    public void setArray (ArrayList <Bitmap> i){
        this.img = i ;
    }
    public void removeArray(){
        img.clear();
    }
    @Override
    public boolean equals(Object object) {
        Users u = (Users)object;
        if(u.getUsername().equals( this.getUsername() )){
            return true;
        }else return false;

    }

}

