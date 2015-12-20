package api.android;

import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 12/4/15.
 **/
public class AndroidSystemUpdate extends Android {

    private static UiObject

            MOTOROLLA_TIME_DELAY    = null,
            MOTOROLLA_SET_TIME      = null,
            TIME_PICKER             = null,
            DONE                    = null,
            AM                      = null,
            PM                      = null;

    private static UiObject motorollaTimeDelay(){
        if(MOTOROLLA_TIME_DELAY == null) MOTOROLLA_TIME_DELAY = new UiSelector().resourceId("com.motorola.ccc.ota:id/show_time").makeUiObject();
        return MOTOROLLA_TIME_DELAY;
    }

    private static UiObject motorollaSetTime(){
        if(MOTOROLLA_SET_TIME == null) MOTOROLLA_SET_TIME = new UiSelector().resourceId("com.motorola.ccc.ota:id/set_button").makeUiObject();
        return MOTOROLLA_SET_TIME;
    }

    private static UiObject timePicker(){
        if(TIME_PICKER == null) TIME_PICKER = new UiSelector().resourceId("android:id/numberpicker_input").makeUiObject();
        return TIME_PICKER;
    }

    private static UiObject done(){
        if(DONE == null) DONE = new UiSelector().text("Done").makeUiObject();
        return DONE;
    }

    private static UiObject am(){
        if(AM == null) AM = new UiSelector().text("AM").makeUiObject();
        return AM;
    }

    private static UiObject pm(){
        if(PM == null) PM = new UiSelector().text("PM").makeUiObject();
        return PM;
    }

    public static boolean isShown(){
        return getActivityInFocus() != null && getActivityInFocus().contains("com.motorola.ccc.ota/com.motorola.ccc.ota.ui.InstallationActivity");
    }

    public AndroidSystemUpdate tapInstallLater(){
        new UiSelector().text("Later ").tap();
        return this;
    }

    public AndroidSystemUpdate tapInstallNow(){
        new UiSelector().text("Install now ").tap();
        return this;
    }

    public AndroidSystemUpdate selectTime(){
        motorollaTimeDelay().tap();
        if(timePicker().text().contains("PM")) am().tap();
        else pm().tap();
        motorollaSetTime().tap();
        return this;
    }

    public void tapDone(){
        done().tap();
    }
}