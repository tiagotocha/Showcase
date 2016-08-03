package com.tiagoderlan.showcase.models.collections;

import android.database.Cursor;

import com.tiagoderlan.showcase.interfaces.ApiCollection;
import com.tiagoderlan.showcase.interfaces.DatabaseCollection;
import com.tiagoderlan.showcase.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tiago on 28/07/2016.
 */
public class ProductCollection extends ArrayList<Product> implements ApiCollection, DatabaseCollection
{
    @Override
    public void fromJson(JSONArray array) throws JSONException {
        int size = array.length();

        for(int i = 0; i < size; i++)
        {
            JSONObject object = array.getJSONObject(i);

            Product product = new Product();

            product.fromJson(object);

            this.add(product);
        }
    }

    @Override
    public void fromCursor(Cursor cursor) {

        int size = cursor.getCount();

        for(int i = 0; i < size; i++)
        {
            Product product = new Product();

            product.fromCursor(cursor);

            this.add(product);

            cursor.moveToNext();
        }

    }
}
