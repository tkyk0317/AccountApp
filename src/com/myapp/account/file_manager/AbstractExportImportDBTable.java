package com.myapp.account.file_manager;

import com.myapp.account.file_manager.ExportDataException;
import com.myapp.account.file_manager.ImportDataException;

/**
 * @brief Export/Import Database-Table Abstract Class.
 */
public abstract class AbstractExportImportDBTable {

    protected SdCardFileManagerImpl sdCardFileManager = null;
    protected static final int WRITE_RECORD_COUNT = 100;
    protected static final String CSV_DELIMITER = ",";
    protected static final String LINE_END = "\n";

    /**
     * @brief Export Table Data.
     *
     * @return void.
     */
    public void exportData() throws ExportDataException {
    }

    /**
     * @brief Import Table Data.
     *
     * @return void.
     */
    public void importData() throws ImportDataException {
    }
}

