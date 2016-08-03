package com.tiagoderlan.showcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.tiagoderlan.showcase.interfaces.ApiEntity;
import com.tiagoderlan.showcase.interfaces.DatabaseEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tiago on 28/07/2016.
 */
public class Category implements ApiEntity, DatabaseEntity
{
    public int id;

    public String name;

    public boolean selected;

    @Override
    public void fromJson(JSONObject object) throws JSONException
    {
        this.id = object.getInt("id");
        this.name = object.getString("name");

    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
    }

    @Override
    public ContentValues getValues()
    {
        ContentValues values = new ContentValues();

        values.put("_id", this.id);
        values.put("name", this.name);

        return values;
    }
}
