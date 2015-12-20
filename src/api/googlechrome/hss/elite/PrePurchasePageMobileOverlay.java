package api.googlechrome.hss.elite;

import api.googlechrome.GoogleChrome;
import api.googlechrome.hss.esellerate.ShoppingCart;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class PrePurchasePageMobileOverlay extends GoogleChrome{

    private static UiObject

            TITLE               = null,
            CREDIT_CARD_BUTTON  = null,
            PAYPAL_BUTTON       = null,
            RATE_SELECTOR       = null,
            CONTINUE_BUTTON     = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().description("Please select your payment option").makeUiObject();
        return TITLE;
    }

    private static UiObject creditCardButton(){
        if(CREDIT_CARD_BUTTON == null) CREDIT_CARD_BUTTON = new UiSelector().resourceId("btn_pay").makeUiObject();
        return CREDIT_CARD_BUTTON;
    }

    private static UiObject payPaylButton(){
        if(PAYPAL_BUTTON == null) PAYPAL_BUTTON = new UiSelector().resourceId("btn_pay").makeUiObject();
        return PAYPAL_BUTTON;
    }

    private static UiObject rateSelector(){
        if(RATE_SELECTOR == null) RATE_SELECTOR = new UiSelector().resourceId("btn_pay").makeUiObject();
        return RATE_SELECTOR;
    }

    private static UiObject continueButton(){
        if(CONTINUE_BUTTON == null) CONTINUE_BUTTON = new UiSelector().resourceId("btn_pay").makeUiObject();
        return CONTINUE_BUTTON;
    }

    public ShoppingCart tapContinueButton(){
        QAManager.log.debug("Tapping on the Continue Button");
        continueButton().tap();
        return new ShoppingCart().waitForViewToLoad();
    }

    public PrePurchasePageMobileOverlay tapPayPalButton(){
        QAManager.log.debug("Tapping on the PayPal Button");
        payPaylButton().tap();
        return this;
    }

    public PrePurchasePageMobileOverlay tapCreditCardButton(){
        QAManager.log.debug("Tapping on the Credit Card Button");
        creditCardButton().tap();
        return this;
    }

    public PrePurchasePageMobileOverlay tapRateSelector(){
        QAManager.log.debug("Tapping on the Rate Selector");
        rateSelector().tap();
        return this;
    }

    public PrePurchasePageMobileOverlay waitForViewToLoad(){
        QAManager.log.debug("Waiting for Pre Purchase Page to load...");
        title().waitForSelector(25);
        return this;
    }
}
