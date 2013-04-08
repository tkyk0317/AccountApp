package com.myapp.account.file_manager;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Export Estimate Table Class.
 */
public class ImportEstimateTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private EstimateTableAccessor estimateTable = null;
    private static final String IMPORT_FILE_NAME = "Estimate.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ImportEstimateTableImpl(Activity activity) {
        AppConfigurationData app_config = new AppConfigurationData(activity);
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
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
        List<EstimateTableRecord> estimate_records = deserialize();
        for( EstimateTableRecord record : estimate_records ) {
            ret = true;
            this.estimateTable.insert(record);
        }
        return ret;
    }

    /**
     * @brief Deserialize EstimateTable Record.
     * @return EstimateTableRecord List.
     */
    private List<EstimateTableRecord> deserialize() {
        List<EstimateTableRecord> record = new ArrayList<EstimateTableRecord>();
        String[] estimate_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

        for( int i = 0 ; i < estimate_data.length ; ++i ) {
            String[] item_data = estimate_data[i].split(CSV_DELIMITER);
            EstimateTableRecord estimate_record = new EstimateTableRecord();

            estimate_record.setId(Integer.valueOf(item_data[0]));
            estimate_record.setEstimateMoney(Integer.valueOf(item_data[1]));
            estimate_record.setEstimateTargetDate(item_data[2]);
            estimate_record.setUpdateDate(item_data[3]);
            estimate_record.setInsertDate(item_data[4]);
            estimate_record.setUserId(Integer.valueOf(item_data[5]));

            record.add(estimate_record);
        }
        return record;
    }
}

