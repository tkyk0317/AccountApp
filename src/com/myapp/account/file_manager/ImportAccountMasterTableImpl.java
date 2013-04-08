package com.myapp.account.file_manager;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;

/**
 * @brief Export AccountMaster Table Class.
 */
public class ImportAccountMasterTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private AccountMasterTableAccessor accountMaster = null;
    private static final String IMPORT_FILE_NAME = "AccountMaster.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ImportAccountMasterTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
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
        List<AccountMasterTableRecord> master_records = deserialize();
        for( AccountMasterTableRecord record : master_records ) {
            ret = true;
            this.accountMaster.insert(record);
        }
        return ret;
    }

    /**
     * @brief Deserialize AccountMaster Record.
     * @return AccountMasterTableRecord List.
     */
    private List<AccountMasterTableRecord> deserialize() {
        List<AccountMasterTableRecord> record = new ArrayList<AccountMasterTableRecord>();
        String[] master_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

        for( int i = 0 ; i < master_data.length ; ++i ) {
            String[] item_data = master_data[i].split(CSV_DELIMITER);
            AccountMasterTableRecord master_record = new AccountMasterTableRecord();

            master_record.setId(Integer.valueOf(item_data[0]));
            master_record.setKindId(Integer.valueOf(item_data[1]));
            master_record.setName(item_data[2]);
            master_record.setUseDate(item_data[3]);
            master_record.setUpdateDate(item_data[4]);
            master_record.setInsertDate(item_data[5]);

            record.add(master_record);
        }
        return record;
    }
}

