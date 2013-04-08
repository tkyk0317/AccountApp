package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Export Estimate Table Class.
 */
public class ExportEstimateTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private EstimateTableAccessor estimateTable = null;
    private static final String EXPORT_FILE_NAME = "Estimate.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ExportEstimateTableImpl(Activity activity) {
        AppConfigurationData app_config = new AppConfigurationData(activity);
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
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
     * @brief Export EstimateTable to CSV File.
     * @return true:Successed Export false:Failed Export.
     */
    @Override
    public boolean exportData() {
        return this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getEstimateTableData());
    }

    /**
     * @brief Get EstimateTable All Record.
     */
    private String getEstimateTableData() {
        List<EstimateTableRecord> record = this.estimateTable.getAllRecordNotSpecifiedUserId();
        return serialize(record);
    }

    /**
     * @brief Serialize EstimateTable Record Format.
     * @param record EstimateTable Record List.
     * @return Serialized EstimateTable Record Data.
     */
    private String serialize(List<EstimateTableRecord> record_list) {
        String serialize_data= new String();

        for( EstimateTableRecord record : record_list) {
            serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getEstimateMoney()) + CSV_DELIMITER);
            serialize_data += (record.getTargetDate() + CSV_DELIMITER);
            serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
            serialize_data += (record.getInsertDate() + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getUserId()) + LINE_END);
        }
        return serialize_data;
    }
}

