package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;

/**
 * @brief Export USerTable Class.
 */
public class ExportUserTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private UserTableAccessor userTable = null;
    private static final String EXPORT_FILE_NAME = "UserTable.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ExportUserTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
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
        return this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getUserTableData());
    }

    /**
     * @brief Get USerTableData All Record.
     */
    private String getUserTableData() {
        List<UserTableRecord> record = this.userTable.getAllRecord();
        return serialize(record);
    }

    /**
     * @brief Serialize UserTable Record Format.
     * @param record USerTable Record List.
     * @return Serialized UserTable Record Data.
     */
    private String serialize(List<UserTableRecord> record_list) {
        String serialize_data= new String();

        for( UserTableRecord record : record_list) {
            serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
            serialize_data += (record.getName() + CSV_DELIMITER);
            serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
            serialize_data += (record.getInsertDate() + CSV_DELIMITER);
            serialize_data += (record.getMemo() + LINE_END);
        }
        return serialize_data;
    }
}

