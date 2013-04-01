package com.myapp.account.database;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.myapp.account.R;

/**
 * @brief Database Helper Class.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final int INCOME_FLAG = 0;
    public static final int PAYMENT_FLAG = 1;
    private static final int DB_VERSION  = 1;
    private static final String LOG_TAG = "DatabaseHelper";
    private static final String DB_NAME = "Account.db";
    private static final String ACCOUNT_MASTER_NAME = "AccountMaster";
    private static final String ACCOUNT_TABLE_NAME = "AccountTable";
    private static final String USER_TABLE_NAME = "UserTable";
    private static final String ESTIMATE_TABLE_NAME = "EstimateTable";
    private static final String CREATE_ACCOUNT_MASTER =
        "create table "+ ACCOUNT_MASTER_NAME + "( _id integer not null primary key," +
        "kind_id integer not null, name text not null, use_date text, update_date text not null, insert_date text not null );";
    private static final String CREATE_ACCOUNT_TABLE  =
        "create table " + ACCOUNT_TABLE_NAME + "( _id integer not null primary key," + "user_id integer not null," +
        "category_id interger not null, money integer not null," + "memo text, update_date text not null, insert_date text not null);";
    private static final String CREATE_ESTIMATE_TABLE   =
        "create table " + ESTIMATE_TABLE_NAME + "(_id integer not null primary key," +
        "money integer not null, target_date text not null, update_date text not null, insert_date text not null);";
    private static final String CREATE_USER_TABLE =
        "create table " + USER_TABLE_NAME + "(_id integer not null primary key, name text not null, update_date text not null , insert_date text not null);";

    private SQLiteDatabase m_SqliteDatabase;

    /**
     * @brief Class Constructor.
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * @brief Called when create database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table & insert default item.
        createTable(db);
        insertDefaultItem(db);
    }

    /**
     * @brief Called when update database.
     */
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version ) {
        if( old_version >= new_version ) return;
    }

    /**
     * @brief Create table in database.
     * @param db SQLiteDatabase Instance.
     */
    private void createTable(SQLiteDatabase db) {
        db.execSQL( CREATE_ACCOUNT_MASTER );
        db.execSQL( CREATE_ACCOUNT_TABLE );
        db.execSQL( CREATE_ESTIMATE_TABLE );
        db.execSQL( CREATE_USER_TABLE );
    }

    /**
     * @brief Insert default item into table.
     */
    private void insertDefaultItem(SQLiteDatabase db) {
        // default item insert for AccountCategoryMaster.
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name, update_date, insert_date) values(" + INCOME_FLAG +
                ",'給料',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'貯金',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'電化製品代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'パン代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'交通費',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'日用品',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'食費(外食)',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'食費(家食)',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values("  + PAYMENT_FLAG +
                ",'お酒代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + INCOME_FLAG +
                ",'ボーナス',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + INCOME_FLAG +
                ",'臨時収入',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values("  + PAYMENT_FLAG +
                ",'書籍代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'電気代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'ガス代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'水道代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'インターネット代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'家賃',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'携帯電話代',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        db.execSQL( "insert into "+ ACCOUNT_MASTER_NAME +
                "(kind_id, name,update_date, insert_date) values(" + PAYMENT_FLAG +
                ",'雑費',strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')) );" );
        // User Table.
        db.execSQL( "insert into "+ USER_TABLE_NAME +
                "(name, update_date, insert_date) values('default_user' ,strftime('%Y/%m/%d', datetime('now', 'localtime')), strftime('%Y/%m/%d', datetime('now', 'localtime')));");
    }
}

