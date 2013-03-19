package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Export AccountData Table Class.
 */
public class ExportAccountDataTableImpl implements ExportImportDBTableInterface {

    protected SdCardFileManagerImpl sdCardFileManager;
    protected AccountTableAccessor accountTable;
    protected static final String ACCOUNT_DATA_FILE_NAME = "AccountData.csv";
    protected static final String CSV_DELIMITER = ",";
    protected static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ExportAccountDataTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
    }

    /**
     * @brief Import Table Data.
     * @note not support.
     */
    @Override
    public boolean importData() {
        return false;
    }

    /**
     * @brief Export AccountTableData to CSV File.
     * @return true:Successed Export false:Failed Export.
     */
    @Override
    public boolean exportData() {
        return this.sdCardFileManager.writeFile(ACCOUNT_DATA_FILE_NAME, getAccountTableData());
    }

    /**
     * @brief Get AccountTable All Record.
     */
    protected String getAccountTableData() {
        List<AccountTableRecord> record = this.accountTable.getAllRecord();
        return serialize(record);
    }

    /**
     * @brief Serialize AccountTable Record Format.
     * @param record AccountTable Record List.
     * @return Serialized AccountTable Record Data.
     */
    protected String serialize(List<AccountTableRecord> record_list) {
        String serialize_data= new String();

        for( AccountTableRecord record : record_list) {
            serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getUserId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getCategoryId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getMoney()) + CSV_DELIMITER);
            serialize_data += (record.getMemo() + CSV_DELIMITER);
            serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
            serialize_data += (record.getInsertDate() + LINE_END);
        }
        return serialize_data;
    }
}

