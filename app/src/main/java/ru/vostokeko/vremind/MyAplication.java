package ru.vostokeko.vremind;

import android.app.Application;

/**
 * Created by ilya on 07.10.2015.
 */
public class MyAplication extends Application{

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return  activityVisible;
    }

    public static  void  activityResumed() {
        activityVisible = true;

    }

    public static  void activityPaused(){
        activityVisible = false;
    }

}
