package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ViewInviteSDK;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class FunctionalityInviteSDK extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Invite SDK").execType("criticClient");
    }

    @Test
    public void AC4_558() {
        testInfo.name("Invite One Contact").expected("Invite sent and contact count decreased by 1");
        int contacts = Android.openHotspotShield().openInviteSDK().getContactCount();
        int contacts2 = new ViewInviteSDK().inviteOneContact().getContactCount();
        testInfo.actual(String.valueOf(contacts2));
        Assert.assertTrue("Contact count: "+contacts+" did not decrease by 1", contacts2==contacts-1);
    }

    //@Test
    public void AC4_526(){
        testInfo.name("Invite all Contacts").expected("Invites sent, ViewHome opened");
        Android.openHotspotShield().openInviteSDK().inviteAllContacts();
        testInfo.actual("Invites sent");
    }

    @Test
    public void AC4_710(){
        testInfo.name("Invite No Contacts").expected("Invites not sent, contact count the same");
        int contacts = Android.openHotspotShield().openInviteSDK().getContactCount();
        int contacts2 = new ViewInviteSDK().deselectAllContacts().tapInviteButton().getContactCount();
        testInfo.actual(String.valueOf(contacts2));
        Assert.assertTrue("Contact count "+contacts+" changed to: " +contacts2, contacts==contacts2);
    }
}
