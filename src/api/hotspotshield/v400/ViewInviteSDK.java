package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class ViewInviteSDK extends Android{

    private static UiObject

            TITLE               = null,
            CONTACTS            = null,
            INVITE_BUTTON       = null,
            SELECT_ALL_CHECKBOX = null,
            CONTACT_CHECK_BOX   = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId(getAppPackageId()+":id/title").makeUiObject();
        return TITLE;
    }

    private static UiObject contacts(){
        if(CONTACTS == null) CONTACTS = new UiSelector().resourceId(getAppPackageId()+":id/total_contacts").makeUiObject();
        return CONTACTS;
    }

    private static UiObject inviteButton(){
        if(INVITE_BUTTON == null) INVITE_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/invite_all").makeUiObject();
        return INVITE_BUTTON;
    }

    private static UiObject selectAllCheckbox(){
        if(SELECT_ALL_CHECKBOX == null) SELECT_ALL_CHECKBOX = new UiSelector().resourceId(getAppPackageId()+":id/cb_select_all").makeUiObject();
        return SELECT_ALL_CHECKBOX;
    }

    private static UiObject contactCheckBox(){
        if(CONTACT_CHECK_BOX == null) CONTACT_CHECK_BOX = new UiSelector().resourceId(getAppPackageId()+":id/contact_selector").makeUiObject();
        return CONTACT_CHECK_BOX;
    }

    public ViewHome inviteAllContacts(){
        QAManager.log.debug("Inviting all contacts...");
        selectAllContacts().tapInviteButton();
        return new ViewHome().waitForViewToLoad();
    }

    public ViewInviteSDK inviteOneContact(){
        QAManager.log.debug("Inviting one contact...");
        deselectAllContacts().selectOneContact().tapInviteButton();
        return this;
    }

    public ViewInviteSDK tapInviteButton(){
        QAManager.log.debug("Tapping the Invite Button");
        inviteButton().tap();
        return this;
    }

    public ViewInviteSDK selectOneContact(){
        QAManager.log.debug("Selecting a random contact");
        contactCheckBox().tap();
        return this;
    }

    public ViewInviteSDK selectAllContacts(){
        QAManager.log.debug("Selecting all contacts");
        if(!isSelectAllChecked()) selectAllCheckbox().tap();
        else QAManager.log.debug("Select All is already checked");
        return this;
    }

    public ViewInviteSDK deselectAllContacts(){
        QAManager.log.debug("Deselecting all contacts");
        if(isSelectAllChecked()) selectAllCheckbox().tap();
        else QAManager.log.debug("Select All is already unchecked");
        return this;
    }

    public int getContactCount(){
        QAManager.log.debug("Getting total contact count");
        return Integer.parseInt(contacts().text().replaceAll(" Contacts",""));
    }

    private boolean isSelectAllChecked(){
        return new UiSelector().isChecked("//*[@resource-id=\""+getAppPackageId()+":id/cb_select_all\"]");
    }

    public ViewInviteSDK waitForViewToLoad(){
        QAManager.log.debug("Waiting for Invite SDK to load...");
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        QAManager.log.debug("Exiting Invite SDK to ViewHome");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
