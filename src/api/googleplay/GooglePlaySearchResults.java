package api.googleplay;

import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/8/2015.
 */
public class GooglePlaySearchResults {

    private static UiObject SEARCH_ITEM = null;

    private static UiObject specificSearchItem(String value){
        return new UiSelector().textContains(value).resourceId("com.android.vending:id/li_title").makeUiObject();
    }

    private static UiObject searchItem(){
        if(SEARCH_ITEM == null) SEARCH_ITEM = new UiSelector().resourceId("com.android.vending:id/li_title").makeUiObject();
        return SEARCH_ITEM;
    }

    public GooglePlayApplicationPage tapOn(String result){
        UiObject specificItem = specificSearchItem(result);
        if(specificItem.exists()){
            specificItem.tap();
        }else{
            if(searchItem().exists()) throw new AssertionError(result+" was not part of the results that loaded");
            else throw new AssertionError("Google Play did not load results");
        }
        return new GooglePlayApplicationPage().waitForViewToLoad();
    }
}
