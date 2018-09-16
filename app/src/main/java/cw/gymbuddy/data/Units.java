package cw.gymbuddy.data;

import org.json.JSONObject;

public class Units implements JSONpopulator {

    private String temperature;
    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString( "temperature" );

    }
}
