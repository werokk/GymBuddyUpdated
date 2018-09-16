package cw.gymbuddy;

import java.util.ArrayList;

public class EventResource {
    private static EventResource instance;
    public String username ;
    private ArrayList<Events> list = new ArrayList<>();

    public ArrayList<Events> getList() {
        return list;
    }
    public static EventResource getInstance() {
        if(instance==null){
            instance = new EventResource();
        }

        return instance;
    }
    public void addToList(Events events){
        list.add( events );

    }

    public Events findUser(String name){
        for(Events u : list){
            if (u.username.equals( name ) ){
                return u;
            }

        }                return null;
    }
}
