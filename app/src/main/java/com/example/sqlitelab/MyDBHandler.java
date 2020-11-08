package com.example.sqlitelab;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDBHandler extends SQLiteOpenHelper {
    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database name
    private static final String DATABASE_NAME = "productDB.db";
    //Our table name
    public static final String TABLE_PRODUCTS = "products";
    //The name of the columns in our table (ID, Product name, SKU)
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";


    public MyDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //Once we initialize our application, we create the following table using an SQL keyword.
    public void onCreate(SQLiteDatabase db){
        //Creation of string which is passed onto execute
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_SKU + " INTEGER)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    //SQL keyword used to delete table if it exists
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product){
        //Grab database in a writeable state
        SQLiteDatabase db = this.getWritableDatabase();
        //Adding values
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_SKU, product.getSku());
        db.insert(TABLE_PRODUCTS, null, values);
        //Closing the writeable db
        db.close();
    }

    public Product findProduct(String productName){
        SQLiteDatabase db = this.getWritableDatabase();
        //Select *(all) from:
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " =\"" + productName + "\"";
        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();
        if (cursor.moveToFirst()){
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }else{
            product = null;
        }
        db.close();
        return product;
    }

    public boolean deleteProduct(String productName){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        //Repeated string query
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " =\"" + productName + "\"";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String idStr = cursor.getString(0);
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = " + idStr, null);
            cursor.close();
            result = true;
        }
        return result;
    }

    //Will query all of the data in the database
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
