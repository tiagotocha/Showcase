package com.tiagoderlan.showcase.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;
import com.tiagoderlan.showcase.models.collections.ProductCollection;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "showcase.db";

    private static final int DATABASE_VERSION = 1;

    private Context context;

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);

        return instance;

    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

        createTables(getWritableDatabase());

        //clearTables(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);

        //clearTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        dropTables(db);

        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        Resources res = context.getResources();
        execSql(db, res.getString(R.string.create_table_content));
        execSql(db, res.getString(R.string.create_table_category));
        execSql(db, res.getString(R.string.create_table_product));
    }

    private void dropTables(SQLiteDatabase db) {
        Resources res = context.getResources();
        execSql(db, res.getString(R.string.drop_table_product));
        execSql(db, res.getString(R.string.drop_table_category));
        execSql(db, res.getString(R.string.drop_table_content));
    }

    private void clearTables(SQLiteDatabase db)
    {
        Resources res = context.getResources();
        execSql(db, res.getString(R.string.clear_table_product));
        execSql(db, res.getString(R.string.clear_table_category));
        execSql(db, res.getString(R.string.clear_table_content));
    }

    private void execSql(SQLiteDatabase db, String sql)
    {
        db.execSQL(sql);
    }

    public void clearTables()
    {
        SQLiteDatabase db = getWritableDatabase();

        clearTables(db);
    }

    public void addProduct(Product product)
    {
        if(product == null)
            throw new NullPointerException();

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = product.getValues();

        db.insert("tabProduct", null, values);
    }

    public void updateProduct(Product product)
    {
        if(product == null)
            throw new NullPointerException();

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = product.getValues();

        db.update("tabProduct", values, "_id = ?", new String[]{ String.valueOf(product.id)});
    }

    public void addCategory(Category category)
    {
        if(category == null)
            throw new NullPointerException();

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = category.getValues();

        db.insert("tabCategory", null, values);
    }

    public void addContent(Content content)
    {
        if(content == null)
            throw new NullPointerException();

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = content.getValues();

        db.insert("tabContent", null, values);
    }


    public Category getCategory(Integer id)
    {
        if(id == null)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabCategory WHERE _id = " + String.valueOf(id), null);

        cursor.moveToFirst();

        int count  = cursor.getCount();

        Category result = new Category();

        if(count != 0)
            result.fromCursor(cursor);

        cursor.close();

        return result;
    }

    public Product getProduct(Integer id)
    {
        if(id == null)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabProduct WHERE _id = " + String.valueOf(id), null);

        cursor.moveToFirst();

        int count  = cursor.getCount();

        Product result = new Product();

        if(count != 0)
            result.fromCursor(cursor);

        cursor.close();

        return result;
    }

    public ProductCollection getProductsByCategory(Category category)
    {
        if(category == null)
            throw new NullPointerException();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabProduct WHERE category_id = " + String.valueOf(category.id), null);

        cursor.moveToFirst();

        int count  = cursor.getCount();

        ProductCollection result = new ProductCollection();

        result.fromCursor(cursor);

        cursor.close();

        return result;
    }

    public ProductCollection getProducts() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabProduct", null);

        cursor.moveToFirst();

        ProductCollection result = new ProductCollection();

        result.fromCursor(cursor);

        cursor.close();


        return result;
    }

    public CategoryCollection getCategories() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabCategory", null);

        cursor.moveToFirst();

        CategoryCollection result = new CategoryCollection();

        result.fromCursor(cursor);

        cursor.close();

        return result;
    }

    public Content getContent(String url)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tabContent WHERE bloburl = '" + url + "'", null);

        cursor.moveToFirst();

        int count  = cursor.getCount();

        Content result = null;

        if(count != 0)
        {
            result = new Content();
            result.fromCursor(cursor);
        }

        cursor.close();

        return result;
    }




}
