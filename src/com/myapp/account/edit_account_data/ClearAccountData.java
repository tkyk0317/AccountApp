package com.myapp.account.edit_account_data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.myapp.account.factory.Factory;

/**
 * @brief Clear Account Data ASync Task.
 */
@SuppressLint("NewApi")
public class ClearAccountData extends AsyncTask<String, Integer, Boolean> {
    private Context context = null;
    private OnClearAccountDataInterface onClearAccountData = null;

    /**
     * @brief Constructor.
     *
     * @param context Context Instance.
     */
    public ClearAccountData(Context context, OnClearAccountDataInterface on_clear_account_data) {
        this.onClearAccountData = on_clear_account_data;
        this.context = context;
    }

    /**
     * @brief First Called from UI Thread.
     */
    @Override
    protected void onPreExecute() {
        this.onClearAccountData.onStartClearData();
    }

    /**
     * @brief BackGround Work.
     *
     * @param params String Parameters.
     */
    @Override
    protected Boolean doInBackground(String... params) {
        Factory.getAccountTableAcceessor((Activity)this.context).deleteAll();
        return Boolean.valueOf(true);
    }

    /**
     * @brief Called from Worker Thread when publishProgress called.
     *
     * @param values progress value.
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    /**
     * @brief Called when Cancel.
     */
    @Override
    protected void onCancelled() {
    }

    /**
     * @brief Called when Worker Thread is Complete.
     *
     * @param result Result Parameter.
     */
    @Override
    protected void onPostExecute(Boolean result) {
        this.onClearAccountData.onFinishClearData();
    }

    /**
     * @brief OnClearAccountData Interface Class.
     */
    static public interface OnClearAccountDataInterface {
        public void onStartClearData();
        public void onFinishClearData();
    }
}
