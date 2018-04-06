package com.vladislavbalyuk.currencyconverter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class DB {
    private static DB instance;

    private DBHelper dbh;
    private SQLiteDatabase db;

    private DB(Context context) {
        dbh = new DBHelper(context);
    }

    public static synchronized DB getInstance(Context context) {
        if (instance == null) {
            instance = new DB(context);
        }
        return instance;
    }

    public void updateCurrency(List<CurrencyValue> listCurrencyValue) {
        try {
            db = dbh.getWritableDatabase();
            db.beginTransaction();
            for (CurrencyValue currencyValue : listCurrencyValue) {
                update(currencyValue);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (SQLiteCantOpenDatabaseException e){}
        finally {
            dbh.close();
        }
    }

    private void update(CurrencyValue currencyValue) {
        ContentValues cv = new ContentValues();
        cv.put("_id", currencyValue.getId());
        cv.put("CharCode", currencyValue.getCharCode());
        cv.put("Nominal", currencyValue.getNominal());
        cv.put("Value", currencyValue.getValue().add(new BigDecimal(0)).toString());
        cv.put("Name", currencyValue.getName());

        if (db.update("CurrencyValues", cv, "_id = ?", new String[]{currencyValue.getId()}) == 0) {
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

        try {
            db = dbh.getWritableDatabase();
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
        }
        catch (SQLiteCantOpenDatabaseException e){}
        finally {
            dbh.close();
        }

        return listCurrencyValue;
    }

    private class DBHelper extends SQLiteOpenHelper {
        Context context;

        private DBHelper(Context context) {
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
