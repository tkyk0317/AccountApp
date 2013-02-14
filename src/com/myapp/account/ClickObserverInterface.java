package com.myapp.account;

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
}

