package com.myapp.account.observer;

/**
 * @brief Event Complete Observer Class.
 */
public interface EventCompleteObserver {

    /**
     * @brief Edit Complete.
     */
    public void notifyAccountEditComplete();

    /**
     * @brief AccountMaster Edit Complete.
     */
    public void notifyAccountMasterEditComplete();

    /**
     * @brief UserTable Edit Complete.
     */
    public void notifyUserTableEditComplete();
}

