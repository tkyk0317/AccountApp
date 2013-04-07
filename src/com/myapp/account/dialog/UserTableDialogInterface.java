package com.myapp.account.dialog;

import android.widget.TableRow;

import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.edit_user_table.EditUserTableRecord;

/**
 * @brief Dialog Interface Class.
 */
public interface UserTableDialogInterface {

    /**
     * @brief Appear Dialog.
     */
    public void appear();

    /**
     * @brief Appear Dialog.
     */
    public void appear(EditUserTableRecord table_row);

    /**
     * @brief Attach Observer.
     *
     * @param observer EventCompleteObserver Instance.
     */
    public void attachObserver(EventCompleteObserver observer);
}

