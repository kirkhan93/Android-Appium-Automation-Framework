package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/16/2015.
 */
public class Widget extends Android{

    private static UiObject

            ICON            = null,
            POWER_BUTTON    = null;

    private static UiObject icon(){
        if(ICON == null) ICON = new UiSelector().resourceId(getAppPackageId() + ":id/widget_start_app").makeUiObject();
        return ICON;
    }

    private static UiObject powerButton(){
        if(POWER_BUTTON == null) POWER_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/widget_connect").makeUiObject();
        return POWER_BUTTON;
    }

    public Widget tapPowerButton(){
        QAManager.log.debug("Tapping Power Button");
       powerButton().tap();
        return this;
    }

    public ViewHome tapIcon(){
        QAManager.log.debug("Tapping HSS Icon");
        icon().tap();
        return new ViewHome();
    }
}
