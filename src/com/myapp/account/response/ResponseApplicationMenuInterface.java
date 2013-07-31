package com.myapp.account.response;

/**
 * @brief Response from Application Menu.
 */
public interface ResponseApplicationMenuInterface {

    /**
     * @brief Response when TableData is Imported.
     *
     * @param boolean Import Data Resule(true:successed false:failed).
     */
    public void OnResponseImportData(boolean is_result);

    /**
     * @brief Response when TableData is Exported.
     *
     * @param boolean Export Data Resule(true:successed false:failed).
     */
    public void OnResponseExportData(boolean is_result);
};

