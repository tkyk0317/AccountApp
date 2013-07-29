package com.myapp.account.file_manager;

import java.lang.Exception;

/**
 * @brief Export Data Exception Class.
 */
public class ExportDataException extends Exception {

    /**
     * @brief Constractor.
     *
     * @param str Exception String.
     */
    public ExportDataException(String exception_str) {
        super(exception_str);
    }
}

