package api.googlechrome.hss.esellerate;

import api.googlechrome.GoogleChrome;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class ShoppingCart extends GoogleChrome {

    //TODO get different ID, this one is shared with the one on hss eltie
    private static UiObject FRAME = null;

    private static UiObject frame(){
        if(FRAME == null) FRAME = new UiSelector().resourceId("LB_PP_c_box").makeUiObject();
        return FRAME;
    }

    public boolean isShown(){
        return frame().exists();
    }

    public ShoppingCart waitForViewToLoad(){
        QAManager.log.debug("Waiting for Pre Purchase Page to load...");
        frame().waitForSelector(25);
        return this;
    }
}
