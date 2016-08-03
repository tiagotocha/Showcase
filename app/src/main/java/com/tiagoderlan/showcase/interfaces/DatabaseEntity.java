package com.tiagoderlan.showcase.interfaces;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Pichau on 28/07/2016.
 */
public interface DatabaseEntity
{

    void fromCursor(Cursor cursor);

    ContentValues getValues();
}

