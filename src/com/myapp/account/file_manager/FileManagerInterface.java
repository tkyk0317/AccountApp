package com.myapp.account.file_manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @brief FileManager Interface Class.
 */
public interface FileManagerInterface {

    /**
     * @brief Read File.
     *
     * @return Read String from Specified FileName.
     */
    public String readFile() throws IOException;

    /**
     * @brief Open File.
     *
     * @param file_name File Name.
     */
    public void open(String file_name) throws FileNotFoundException, UnsupportedEncodingException;

    /**
     * @brief Read Oneline from File.
     *
     * @return Read String.
     */
    public String readOneline() throws IOException;

    /**
     * @brief Close File.
     */
    public void close() throws IOException;

    /**
     * @brief Write File.
     * @param file_name Write FileName.
     * @param write_string Write Strng to File.
     * @return TRUE:Write Success FALSE:Write Failed.
     */
    public boolean writeFile(String file_name, String write_string);
}

