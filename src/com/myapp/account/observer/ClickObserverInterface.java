package com.myapp.account.observer;

import android.view.MotionEvent;

/**
 * @brief ClickObserver Interface.
 */
public interface ClickObserverInterface {

    /**
     * @brief Notify Click.
     */
    public void notifyClick(Object event);

    /**
     * @brief Notify Long Click.
     */
    public void notifyLongClick(Object event);

    /**
     * @brief Notify Long Click for DailyInfo.
     */
    public void notifyLongClickForDailyInfo(Object event);

    /**
     * @brief Notify onFiling Event.
     */
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocityX, float velocityY);
}

