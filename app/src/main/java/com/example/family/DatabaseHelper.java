package com.example.family;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COST_MONEY = "cost_money";
    public static final String COST_DATE = "cost_date";
    public static final String COST_TITLE = "cost_title";
    public static final String TABLE = "family";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists family("+
                "id integer primary key autoincrement ,"+
                "cost_title varchar,"+
                "cost_date varchar,"+
                "cost_money varchar)");
    }

    public void insertCost(CostBean costBean){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COST_TITLE,costBean.costTitle);
        cv.put(COST_DATE,costBean.costDate);
        cv.put(COST_MONEY,costBean.costMoney);
        database.insert(TABLE,null,cv);
    }

    public void deleteCost(CostBean costBean){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TABLE,"cost_title=?",new String[]{costBean.costTitle});
    }

//    public void deleteCost(int position){
//        SQLiteDatabase database=getWritableDatabase();
//        database.delete(TABLE,"id=?",new String[]{Integer.toString(position)});
//    }

    public Cursor getAllCostData(){
        SQLiteDatabase database=getWritableDatabase();
        return database.query(TABLE,null,null,null,null,null,COST_DATE+" ASC");
    }

    public  void deleteAllData(){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TABLE,null,null);
    }

    public  int countTotalCost(){
        int sum=0;
        SQLiteDatabase database=getWritableDatabase();
        String sum_dbString="select sum(cost_money)from "+TABLE;
        Cursor cursor=database.rawQuery(sum_dbString,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do{
                    sum=cursor.getInt(0);
                }while (cursor.moveToNext());
            }
        }
        return sum;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
