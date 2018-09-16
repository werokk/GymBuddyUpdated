package cw.gymbuddy.data;

import org.json.JSONObject;

public class Item implements JSONpopulator {


    private Condition condition;
    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition =  new Condition();
        condition.populate( data.optJSONObject( "condition" ) );

    }
}
