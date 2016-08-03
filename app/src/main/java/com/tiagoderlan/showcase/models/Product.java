package com.tiagoderlan.showcase.models;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.tiagoderlan.showcase.interfaces.ApiEntity;
import com.tiagoderlan.showcase.interfaces.DatabaseEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements ApiEntity, DatabaseEntity
{
    public int id;

    public String name;

    public String description;

    public String photo;

    public Double price;

    public Integer category_id;

    public boolean favorite;

    public Bitmap image;

    @Override
    public void fromJson(JSONObject object) throws JSONException {

        this.name = object.getString("name");
        this.description = object.getString("description");
        this.photo = object.getString("photo");
        this.price = object.getDouble("price");
        this.favorite = false;

        if(!object.isNull("category_id"))
            this.category_id = object.getInt("category_id");
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.description = cursor.getString(cursor.getColumnIndex("description"));
        this.photo = cursor.getString(cursor.getColumnIndex("photo"));
        this.price = cursor.getDouble(cursor.getColumnIndex("price"));
        this.favorite = cursor.getInt(cursor.getColumnIndex("favorite")) == 1;

        if(!cursor.isNull(cursor.getColumnIndex("category_id")))
            this.category_id = cursor.getInt(cursor.getColumnIndex("category_id"));


    }

    @Override
    public ContentValues getValues()
    {
        ContentValues values = new ContentValues();

        //values.put("_id", this.id);
        values.put("name", this.name);
        values.put("description", this.description);
        values.put("photo", this.photo);
        values.put("price", this.price);
        values.put("favorite", this.favorite);

        if(this.category_id == null)
            values.putNull("category_id");
        else
            values.put("category_id", this.category_id);

        return values;
    }
}
