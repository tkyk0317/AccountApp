package com.myapp.account.file_manager;

/**
 * @brief Export/Import Database-Table Interface Class.
 */
public interface ExportImportDBTableInterface {
    /**
     * @brief Export Table Data.
     * @return true:Successed Export false:Failed Export.
     */
    public boolean exportData();

    /**
     * @brief Import Table Data.
     * @return true:Successed Import false:Failed Import.
     */
    public boolean importData();
}

