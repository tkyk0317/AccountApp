package com.myapp.account.file_manager;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.database.UserTableAccessor;

/**
 * @brief Export User Table Class.
 */
public class ImportUserTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private UserTableAccessor userTable = null;
    private static final String IMPORT_FILE_NAME = "UserTable.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ImportUserTableImpl(Activity activity) {
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
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
        List<UserTableRecord> user_records = deserialize();
        for( UserTableRecord record : user_records ) {
            ret = true;
            this.userTable.insert(record);
        }
        return ret;
    }

    /**
     * @brief Deserialize UserTable Record.
     * @return UserTableRecord List.
     */
    private List<UserTableRecord> deserialize() {
        List<UserTableRecord> record = new ArrayList<UserTableRecord>();
        String[] user_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

        for( int i = 0 ; i < user_data.length ; ++i ) {
            String[] item_data = user_data[i].split(CSV_DELIMITER);
            UserTableRecord user_record = new UserTableRecord();

            user_record.setId(Integer.valueOf(item_data[0]));
            user_record.setName(item_data[1]);
            user_record.setUpdateDate(item_data[2]);
            user_record.setInsertDate(item_data[3]);
            user_record.setMemo(item_data[4]);

            record.add(user_record);
        }
        return record;
    }
}

