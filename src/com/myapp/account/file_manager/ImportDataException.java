package com.myapp.account.file_manager;

import java.lang.Exception;

/**
 * @brief Import Data Exception Class.
 */
public class ImportDataException extends Exception {

    /**
     * @brief Constractor.
     *
     * @param str Exception String.
     */
    public ImportDataException(String exception_str) {
        super(exception_str);
    }
}

