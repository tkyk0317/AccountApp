package com.myapp.account.observer;

import android.view.MotionEvent;

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

    /**
     * @brief Notify onFiling Event.
     */
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocityX, float velocityY);
}

