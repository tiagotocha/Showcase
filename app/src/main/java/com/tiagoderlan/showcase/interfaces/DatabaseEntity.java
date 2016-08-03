package com.tiagoderlan.showcase.interfaces;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Tiago on 28/07/2016.
 */
public interface DatabaseEntity
{

    void fromCursor(Cursor cursor);

    ContentValues getValues();
}

