package com.myapp.account.database;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Helper Class.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION               = 1;
    private static final int INCOME_FLAG              = 0;
    private static final int PAYMENT_FLAG             = 1;
    private static final String LOG_TAG               = "DatabaseHelper";
    private static final String DB_NAME               = "Account.db";
    private static final String ACCOUNT_MASTER_NAME   = "AccountMaster";
    private static final String ACCOUNT_TABLE_NAME    = "AccountTable";
    private static final String BUDGET_TABLE_NAME     = "BudgetTable";
    private static final String CREATE_ACCOUNT_MASTER =
        "create table "+ ACCOUNT_MASTER_NAME + "( _id integer not null primary key," +
        "kind_id integer not null, name text not null, update_time text not null );";
    private static final String CREATE_ACCOUNT_TABLE  =
        "create table " + ACCOUNT_TABLE_NAME + "( _id integer not null primary key," +
        "category_id interger not null, money integer not null," +
        "memo text, update_time text not null);";
    private static final String CREATE_BUDGET_TABLE   =
        "create table " + BUDGET_TABLE_NAME + "(_id integer not null primary key," +
        "category_id interger not null, money integer not null," +
        "target_date text not null, update_time text not null);";

    private SQLiteDatabase m_SqliteDatabase;

    /**
     * Class Constructor.
     **/
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    /**
     * Called when create database.
     **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG,"[DatabaseHelper][START] onCreate");
        // create table & insert default item.
        createTable(db);
        insertDefaultItem(db);
        Log.d(LOG_TAG,"[DatabaseHelper][END] onCreate");
    }
    /**
     * Called when update database.
     **/
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version ) {
    }

    /**
     * Create table in database.
     * @param db SQLiteDatabase Instance.
     **/
    private void createTable(SQLiteDatabase db) {
        db.execSQL( CREATE_ACCOUNT_MASTER );
        db.execSQL( CREATE_ACCOUNT_TABLE );
        db.execSQL( CREATE_BUDGET_TABLE );
    }

    /**
     * Insert default item into table.
     **/
    private void insertDefaultItem(SQLiteDatabase db) {
        // default item insert for AccountCategoryMaster.
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + INCOME_FLAG +
                ",'給料',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + INCOME_FLAG +
                ",'ボーナス',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + INCOME_FLAG +
                ",'臨時収入',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'食費(外食)',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'食費(家食)',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values("  + PAYMENT_FLAG +
                ",'お酒代',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values("  + PAYMENT_FLAG +
                ",'書籍代',datetime('now', 'localtime') );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'電気代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'ガス代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'水道代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'インターネット代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'家賃',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'携帯電話代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'ローン代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'クレジットカード代',datetime('now', 'localtime'));" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_time) values(" + PAYMENT_FLAG +
                ",'雑費',datetime('now', 'localtime'));" );
    }
}

