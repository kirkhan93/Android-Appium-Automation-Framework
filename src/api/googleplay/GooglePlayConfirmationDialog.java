package api.googleplay;

import api.android.Android;
import managers.QAManager;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
public class GooglePlayConfirmationDialog extends Android {

    public GooglePlayApplicationPage confirm(){
        QAManager.log.debug("Confirming App Uninstall");
        button1().tap();
        return new GooglePlayApplicationPage();
    }
}
