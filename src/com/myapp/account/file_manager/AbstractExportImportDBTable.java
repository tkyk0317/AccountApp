package com.myapp.account.file_manager;

import java.io.IOException;

import android.app.Activity;

/**
 * @brief Export Database-Table Abstract Class.
 */
public abstract class AbstractExportImportDBTable {

    protected SdCardFileManagerImpl sdCardFileManager = null;
    protected static final int WRITE_RECORD_COUNT = 100;
    protected static final String CSV_DELIMITER = ",";
    protected static final String LINE_END = "\n";

    /**
     * @brief Constructor.
     *
     * @param activity Activity instance.
     */
    public AbstractExportImportDBTable(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
    }

    /**
     * @brief Export Table Data.
     */
    public void exportData() throws ExportDataException {
        // delete file.
        deleteFile();

        // write record.
        int record_count = getRecordCount();
        for( int write_count = 0 ; write_count < record_count ; write_count += WRITE_RECORD_COUNT ) {
            try {
                String write_record = getRecord(WRITE_RECORD_COUNT, write_count);
                writeFile(write_record);
                write_record = null;
            } catch(IOException exception) {
                throw new ExportDataException("ExportData Error");
            }
        }
    }

    /**
     * @brief Delete File.
     */
    protected void deleteFile() {
    }

    /**
     * @brief Write File.
     *
     * @param write_record Write Record Strings.
     */
    protected void writeFile(String write_record) throws IOException {
    }

    /**
     * @brief Get Record.
     *
     * @param count Get Record Count.
     * @param offset Start Offset.
     *
     * @return Record String.
     */
    protected String getRecord(int count, int offset) {
        return null;
    }

    /**
     * @brief Get Record Count;
     *
     * @return
     */
    protected int getRecordCount() {
        return 0;
    }

    /**
     * @brief Import Data Disposal.
     */
    public void importData() throws ImportDataException {
    }
}

