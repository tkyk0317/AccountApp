package com.myapp.account.file_manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;
import com.myapp.account.file_manager.FileManagerInterface;

/**
 * @brief FileManager Implement Class.
 */
public class SdCardFileManagerImpl implements FileManagerInterface {

    protected String sdCardFullPath;
    protected static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     * @param context Context Instance.
     */
    public SdCardFileManagerImpl() {
        this.sdCardFullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    /**
     * @brief Read File.
     * @param file_name Read FileName.
     * @return Read String from Specified FileName.
     */
    @Override
    public String readFile(String file_name) {
        String read_string = null;
        BufferedReader read_buffer = null;
        File file = null;

        try {
            read_string = new String();
            file = new File(this.sdCardFullPath + file_name);
            read_buffer = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));

            // Read All File String.
            String line_string;
            while( null != (line_string = read_buffer.readLine()) ) {
                read_string += line_string;
                read_string += LINE_END;
            }

        } catch (FileNotFoundException file_not_found) {
            read_string = "";
            Log.d("SdCardFileManagerImpl", "readFile:File Not Found : " + file_not_found);
        } catch (IOException io_exception) {
            read_string = "";
            Log.d("SdCardFileManagerImpl", "readFile:IOException : " + io_exception);
        } finally {
            read_string = "";
            if( null != read_buffer ) {
                try {
                    read_buffer.close();
                    read_buffer = null;
                } catch (IOException io_exception) {
                    Log.d("SdCardFileManagerImpl", "readFile:IOException(BufferedReader.close) : " + io_exception);
                }
            }
        }
        return read_string;
    }

    /**
     * @brief Write File.
     * @param file_name Write FileName.
     * @param write_string Write Strng to File.
     * @return true:Write Success false:Write Failed.
     */
    @Override
    public boolean writeFile(String file_name, String write_string) {
        boolean ret = true;
        BufferedWriter write_buffer = null;
        String file_path = sdCardFullPath + file_name;
        File file = new File(file_path);

        try {
            write_buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            write_buffer.write(write_string);
        } catch (IOException io_exception) {
            ret = false;
            Log.d("SdCardFileManagerImpl", "writeFile:IOException : " + io_exception);
        } finally {
            if( null != write_buffer ) {
                try {
                    write_buffer.close();
                    write_buffer = null;
                } catch (IOException io_exception) {
                    ret = false;
                    Log.d("SdCardFileManagerImpl", "writeFile:IOException(BufferedWriter.close) : " + io_exception);
                }
            }
        }
        return true;
    }
}

