package com.tiagoderlan.showcase.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tiagoderlan.showcase.interfaces.DatabaseEntity;

/**
 * Created by Pichau on 01/08/2016.
 */
public class Content implements DatabaseEntity
{
    public String url;

    public byte[] data;

    @Override
    public void fromCursor(Cursor cursor)
    {
        this.url = cursor.getString(cursor.getColumnIndex("bloburl"));
        this.data = cursor.getBlob(cursor.getColumnIndex("data"));
    }

    @Override
    public ContentValues getValues()
    {
        ContentValues values = new ContentValues();

        values.put("bloburl", this.url);
        values.put("data", this.data);

        return values;
    }

    public Bitmap getImageBitmap()
    {
        if(this.data != null)
            return BitmapFactory.decodeByteArray(data, 0, data.length);

        return null;
    }
}
