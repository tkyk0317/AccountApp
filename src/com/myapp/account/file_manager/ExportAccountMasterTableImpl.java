package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief Export AccountMaster Table Class.
 */
public class ExportAccountMasterTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private AccountMasterTableAccessor accountMaster = null;
    private static final String EXPORT_FILE_NAME = "AccountMaster.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ExportAccountMasterTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
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
        return this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getAccountMasterData());
    }

    /**
     * @brief Get AccountMaster All Record.
     */
    private String getAccountMasterData() {
        List<AccountMasterTableRecord> record = this.accountMaster.getAllRecord();
        return serialize(record);
    }

    /**
     * @brief Serialize AccountMaster Record Format.
     * @param record AccountMasterTable Record List.
     * @return Serialized AccountTable Record Data.
     */
    private String serialize(List<AccountMasterTableRecord> record_list) {
        String serialize_data= new String();

        for( AccountMasterTableRecord record : record_list) {
            serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getKindId()) + CSV_DELIMITER);
            serialize_data += (record.getName() + CSV_DELIMITER);
            serialize_data += (record.getUseDate() + CSV_DELIMITER);
            serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
            serialize_data += (record.getInsertDate() + LINE_END);
        }
        return serialize_data;
    }
}

