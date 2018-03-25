package com.vladislavbalyuk.currencyconverter.M;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB {
    private static DB instance;

    public DBHelper dbh;
    public SQLiteDatabase db;

    private DB(Context context) {
        dbh = new DBHelper(context);
        try {
            db = dbh.getWritableDatabase();
        }
        catch (SQLiteCantOpenDatabaseException e){}
    }

    public static synchronized DB getInstance(Context context) {
        if (instance == null) {
            instance = new DB(context);
        }
        return instance;
    }

    public synchronized void updateCurrency(List<CurrencyValue> listCurrencyValue) {
        db.beginTransaction();
        for(CurrencyValue currencyValue : listCurrencyValue){
            update(currencyValue);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void update(CurrencyValue currencyValue) {
        ContentValues cv = new ContentValues();
        cv.put("_id", currencyValue.getId());
        cv.put("CharCode", currencyValue.getCharCode());
        cv.put("Nominal", currencyValue.getNominal());
        cv.put("Value", currencyValue.getValue().toString());
        cv.put("Name", currencyValue.getName());

        if (db.update("CurrencyValues", cv, "_id = ?", new String[]{currencyValue.getCharCode()}) == 0) {
              insert(currencyValue);
        }
    }

    private void insert(CurrencyValue currencyValue) {
        ContentValues cv = new ContentValues();
        cv.put("_id", currencyValue.getId());
        cv.put("CharCode", currencyValue.getCharCode());
        cv.put("Nominal", currencyValue.getNominal());
        cv.put("Value", currencyValue.getValue().toString());
        cv.put("Name", currencyValue.getName());

        db.insert("CurrencyValues", null, cv);
    }

    public synchronized List<CurrencyValue> getListCurrency() {
        CurrencyValue currencyValue;
        List<CurrencyValue> listCurrencyValue;
        listCurrencyValue = new ArrayList<CurrencyValue>();

        String sqlQuery = "select CV._id, CV.CharCode, CV.Nominal, CV.Value, CV.Name from CurrencyValues as CV ";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    currencyValue = new CurrencyValue();
                    currencyValue.setId(c.getString(0));
                    currencyValue.setCharCode(c.getString(1));
                    currencyValue.setNominal(c.getInt(2));
                    currencyValue.setValueFromString(c.getString(3));
                    currencyValue.setName(c.getString(4));

                    listCurrencyValue.add(currencyValue);
                } while (c.moveToNext());
            }
        }

        return listCurrencyValue;
    }

    class DBHelper extends SQLiteOpenHelper {
        Context context;

        public DBHelper(Context context) {
            super(context, "CurrencyConverter", null, 1);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table CurrencyValues ("
                    + "_id text primary key,"
                    + "CharCode text,"
                    + "Nominal numeric,"
                    + "Value real,"
                    + "Name text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }


}
