package com.myapp.account.file_manager;

import java.lang.Exception;

/**
 * @brief Import Data Exception Class.
 */
@SuppressWarnings("serial")
public class ImportDataException extends Exception {

    /**
     * @brief Constructor.
     *
     * @param str Exception String.
     */
    public ImportDataException(String exception_str) {
        super(exception_str);
    }
}

