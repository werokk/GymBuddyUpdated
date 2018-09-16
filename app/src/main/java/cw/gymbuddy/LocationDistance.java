package cw.gymbuddy;

import android.app.Activity;
import android.location.Location;

public class LocationDistance {
    Activity act ;
    LocationService loc;
    public LocationDistance(Activity act){
        this.act = act;
        LocationService.getInstance( act ).askForLocationPermision();
        LocationService.getInstance( act ).getLastKnowLocation();
        loc = LocationService.getInstance( act );

    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2) ;
        System.out.println("pro"+ distance);

        return Math.sqrt(distance);
    }






    public void getDistance(){

        for(Users u: UsersResource.getInstance().getList()){
            System.out.println("usr" + UsersResource.getInstance().getList());
    float[] distance = new float[2];
Location.distanceBetween( loc.getLoc().getLatitude(), loc.getLoc().getLongitude(),
    u.getLatitude(), u.getLongtitude(), distance);

u.setDistance( distance[0] );
    System.out.println("distance "+ loc.getLoc().getLatitude());


}}
}
