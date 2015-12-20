package api.hotspotshield.v400;

import api.android.Android;
import api.googleplay.GooglePlayVendingDialog;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class ViewUpSell extends Android{

    private static UiObject TITLE = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("Upgrade to Elite").makeUiObject();
        return TITLE;
    }

    private static UiObject buyButton(String value){
        return new UiSelector().text(value).makeUiObject();
    }

    public GooglePlayVendingDialog tapBuyButton(String buyButton){
        UiObject button = buyButton(buyButton);
        if(button.exists()){
            QAManager.log.debug("Tapping Buy Button: " + buyButton);
            button.tap();
        }else button.scrollTo().tap();
        return new GooglePlayVendingDialog().waitForDialogToLoad();
    }

    public ViewUpSell waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
