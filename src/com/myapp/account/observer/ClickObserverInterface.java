package com.myapp.account.observer;

/**
 * ClickObserver Interface.
 */
public interface ClickObserverInterface {

    /**
     * Notify Click.
     */
    public void notifyClick(Object event);

    /**
     * Notify Long Click.
     */
    public void notifyLongClick(Object event);

    /**
     * Notify Long Click for DailyInfo.
     */
    public void notifyLongClickForDailyInfo(Object event);
}

