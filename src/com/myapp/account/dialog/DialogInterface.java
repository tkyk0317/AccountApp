package com.myapp.account.dialog;

import com.myapp.account.observer.EventCompleteObserver;

/**
 * @brief Dialog Interface Class.
 */
public interface DialogInterface {

    /**
     * @brief Appear Dialog.
     */
    public void appear();

    /**
     * @brief Attach Observer.
     *
     * @param observer EventCompleteObserver Instance.
     */
    public void attachObserver(EventCompleteObserver observer);
}

