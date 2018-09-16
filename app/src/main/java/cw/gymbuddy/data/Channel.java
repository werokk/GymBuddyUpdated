package cw.gymbuddy.data;

import org.json.JSONObject;

public class Channel implements JSONpopulator {
    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    private Item item;
    private Units units;

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate( data.optJSONObject( "units" ) );

        item = new Item();
        item.populate( data.optJSONObject( "item" ) );
    }
}
