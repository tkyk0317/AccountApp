package com.myapp.account.dialog;

import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.edit_account_master.EditAccountMasterRecord;

/**
 * @brief Dialog Interface Class.
 */
public interface AccountDialogInterface {

    /**
     * @brief Appear Dialog.
     */
    public void appear();

    /**
     * @brief Appear Dialog.
     */
    public void appear(EditAccountMasterRecord table_row);

    /**
     * @brief Attach Observer.
     *
     * @param observer EventCompleteObserver Instance.
     */
    public void attachObserver(EventCompleteObserver observer);
}

