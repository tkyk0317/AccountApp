package com.myapp.account.file_manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

import com.myapp.account.utility.Utility;
import com.myapp.account.file_manager.FileManagerInterface;

/**
 * @brief FileManager Implement Class.
 */
public class SdCardFileManagerImpl implements FileManagerInterface {

    protected String sdCardFullPath;
    protected static final String LINE_END = "\n";
    protected BufferedReader bufReader = null;

    /**
     * @brief Constructor.
     * @param context Context Instance.
     */
    public SdCardFileManagerImpl() {
        this.sdCardFullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    /**
     * @brief Open File.
     *
     * @param file_name File Name.
     */
    public void open(String file_name) throws FileNotFoundException, UnsupportedEncodingException {
        // open file.
        File file = new File(this.sdCardFullPath + file_name);
        this.bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }

    /**
     * @brief Read Oneline from File.
     *
     * @return Read String.
     */
    @Override
    public String readOneline() throws IOException {
        String read_string = new String();

        // check bufReader.
        if( null == this.bufReader ) return read_string;
        return this.bufReader.readLine();
    }

    /**
     * @brief Close File.
     */
    public void close() throws IOException {
        this.bufReader.close();
        this.bufReader = null;
    }

    /**
     * @brief Read File.
     *
     * @return Read String from Specified FileName.
     */
    @Override
    public String readFile() throws IOException {
        String read_string = new String();

        // check bufReader.
        if( null == this.bufReader ) return read_string;

        // Read All File String.
        String line_string;
        while( null != (line_string = this.bufReader.readLine()) ) {
            read_string += line_string;
        }
        return read_string;
    }

    /**
     * @brief Write File.
     * @param file_name Write FileName.
     * @param write_string Write String to File.
     * @return true:Write Success false:Write Failed.
     */
    @Override
    public boolean writeFile(String file_name, String write_string) {
        // do not write when string is null.
        if( true == Utility.isStringNULL(write_string) ) return true;

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
       return ret;
    }
}

