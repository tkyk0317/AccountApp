package com.myapp.account.file_manager;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Export AccountData Table Class.
 */
public class ImportAccountDataTableImpl implements ExportImportDBTableInterface {

    protected SdCardFileManagerImpl sdCardFileManager;
    protected AccountTableAccessor accountTable;
    protected static final String ACCOUNT_DATA_FILE_NAME = "AccountData.csv";
    protected static final String CSV_DELIMITER = ",";
    protected static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ImportAccountDataTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
    }

    /**
     * @brief Export AccountTableData to CSV File.
     * @note not support.
     */
    @Override
    public boolean exportData() {
        return false;
    }

    /**
     * @brief Import Table Data.
     * @return true:Successed Export false:Failed Export.
     */
    @Override
    public boolean importData() {
        boolean ret = false;
        List<AccountTableRecord> account_record = deserialize();
        for( AccountTableRecord record : account_record ) {
            ret = true;
            this.accountTable.insert(record);
        }
        return ret;
    }

    /**
     * @brief Deserialize AccountTable Record.
     * @return AccountTableRecord List.
     */
    protected List<AccountTableRecord> deserialize() {
        List<AccountTableRecord> record = new ArrayList<AccountTableRecord>();
        String[] account_data = this.sdCardFileManager.readFile(ACCOUNT_DATA_FILE_NAME).split(LINE_END);

        for( int i = 0 ; i < account_data.length ; ++i ) {
            String[] item_data = account_data[i].split(CSV_DELIMITER);
            AccountTableRecord account_record = new AccountTableRecord();

            account_record.setId(Integer.valueOf(item_data[0]));
            account_record.setUserId(Integer.valueOf(item_data[1]));
            account_record.setCategoryId(Integer.valueOf(item_data[2]));
            account_record.setMoney(Integer.valueOf(item_data[3]));
            account_record.setMemo(item_data[4]);
            account_record.setUpdateDate(item_data[5]);
            account_record.setInsertDate(item_data[6]);

            record.add(account_record);
        }
        return record;
    }
}

