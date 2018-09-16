package cw.gymbuddy;

import java.util.ArrayList;

/**
 * Created by Werokk on 27/03/2018.
 */

public class UsersResource {

    private static UsersResource instance;
    public String username ;
    private ArrayList<Users> list = new ArrayList<>();

    public ArrayList<Users> getList() {
        return list;
    }
    public static UsersResource getInstance() {
        if(instance==null){
           instance = new UsersResource();
        }

        return instance;
    }
    public void addToList(Users users){
        list.add( users );

    }

    public Users findUser(String name){
        for(Users u : list){
            if (u.username.equals( name ) ){
                return u;
            }

    }                return null;
    }

}
