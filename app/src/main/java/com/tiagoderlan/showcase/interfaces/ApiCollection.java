package com.tiagoderlan.showcase.interfaces;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Pichau on 28/07/2016.
 */
public interface ApiCollection {

    void fromJson(JSONArray array) throws JSONException;
}
