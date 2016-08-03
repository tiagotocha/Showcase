package com.tiagoderlan.showcase.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tiago on 28/07/2016.
 */
public interface ApiEntity {

    void fromJson(JSONObject object) throws JSONException;
}
