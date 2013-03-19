package com.myapp.account.file_manager;

/**
 * @brief FileManager Interface Class.
 */
public interface FileManagerInterface {

    /**
     * @brief Read File.
     * @param file_name Read FileName.
     * @return Read String from Specified FileName.
     */
    public String readFile(String file_name);

    /**
     * @brief Write File.
     * @param file_name Write FileName.
     * @param write_string Write Strng to File.
     * @return TRUE:Write Success FALSE:Write Failed.
     */
    public boolean writeFile(String file_name, String write_string);
}

