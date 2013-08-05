package com.myapp.account.file_manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.myapp.account.file_manager.AbstractExportImportDBTable;
import com.myapp.account.response.ResponseApplicationMenuInterface;

/**
 * @brief Abstract Export/Import Data Class.
 */
public abstract class AbstractExportImportData extends AsyncTask<String, Integer, Boolean> {

    protected Activity activity = null;
    protected AbstractExportImportDBTable accountMasterTable = null;
    protected AbstractExportImportDBTable accountDataTable = null;
    protected AbstractExportImportDBTable estimateTable = null;
    protected AbstractExportImportDBTable userTable = null;
    protected ResponseApplicationMenuInterface response = null;
    protected ProgressDialog progressDialog = null;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     *
     * @return
     */
    public AbstractExportImportData(Activity activity) {
        this.activity = activity;
    }

    /**
     * @brief Execute Export/Import Data Disposal.
     *
     * @param response Response Interface Instance.
     */
    public void execute(ResponseApplicationMenuInterface response) {
        this.response = response;
        execute("");
    }

    /**
     * @brief First Called from UI Thread.
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     * @brief BackGround Work.
     *
     * @param params String Parameters.
     */
    @Override
    protected Boolean doInBackground(String... params) {
        return false;
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
        dismissProgressDialog();
        onComplete(result);
    }

    /**
     * @brief Dismiss Progress Dialog.
     */
    protected void dismissProgressDialog() {
        if( null != this.progressDialog ) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    /**
     * @brief Complete Disposal.
     *
     * @param complete_result Result Boolean Value.
     */
    protected void onComplete(Boolean complete_result) {
    }

    /**
     * @brief Start Export/Import Data.
     */
    public boolean start() {
        return false;
    }
}

