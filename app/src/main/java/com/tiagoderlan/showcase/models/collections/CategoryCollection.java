package com.tiagoderlan.showcase.models.collections;

import android.database.Cursor;

import com.tiagoderlan.showcase.interfaces.ApiCollection;
import com.tiagoderlan.showcase.interfaces.DatabaseCollection;
import com.tiagoderlan.showcase.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pichau on 28/07/2016.
 */
public class CategoryCollection extends ArrayList<Category> implements ApiCollection, DatabaseCollection
{
    @Override
    public void fromJson(JSONArray array) throws JSONException {
        int size = array.length();

        for(int i = 0; i < size; i++)
        {
            JSONObject object = array.getJSONObject(i);

            Category category = new Category();

            category.fromJson(object);

            this.add(category);
        }
    }

    @Override
    public void fromCursor(Cursor cursor) {
        int size = cursor.getCount();

        for(int i = 0; i < size; i++)
        {
            Category category = new Category();

            category.fromCursor(cursor);

            this.add(category);

            cursor.moveToNext();
        }

    }
}

