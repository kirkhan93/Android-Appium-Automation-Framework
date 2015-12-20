package api.gmail;

import managers.QAManager;
import utilities.constants.Credentials;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class GMailReciever {

    private static boolean printDebug = true;

    public static String getEmailHeaderIp(String subjectID) throws MessagingException {
        ArrayList list = getEmailHeaders(subjectID);
        String receivedFromHeader = null;
        for(Object i : list) if (i.toString().contains("(unknown [")) receivedFromHeader = i.toString();
        if(receivedFromHeader == null) throw new NullPointerException("Headers do not contain key Received!");
        QAManager.log.debug("\n\t" + receivedFromHeader);
        String IP = receivedFromHeader.substring(receivedFromHeader.indexOf("unknown ["), receivedFromHeader.indexOf("])")).replace("unknown [", "");
        QAManager.log.debug("Found IP in the header: <" + IP + ">");
        return IP;
    }

    public static ArrayList getEmailHeaders(String subjectUID) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", Credentials.gmailUsername, Credentials.gmailPassword);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        ArrayList<String> l = new ArrayList<>();
        int totalMessages = inbox.getMessageCount();
        if(printDebug) {
            QAManager.log.debug("Total Messages in the Inbox: " + totalMessages);
            QAManager.log.debug("Looking for message with UID: " + subjectUID);
            QAManager.log.debug("Will check 10 most recent emails...");
        }
        for(int i = totalMessages, b = i-10, left = 10; i != b; i--, left--){
            if(printDebug) QAManager.log.debug("" + left + " Messages left to check...");
            String subject = inbox.getMessage(i).getSubject();
            Enumeration header = inbox.getMessage(i).getAllHeaders();
            if(subject != null && subject.equals(subjectUID)){
                QAManager.log.debug("Got a match!");
                while(header.hasMoreElements()){
                    Header h = (Header) header.nextElement();
                    String headerInfo = h.getName() + ": " + h.getValue();
                    try{
                        l.add(headerInfo);
                    }catch (Exception e){

                    }
                }
                store.close();
                return l;
            }
        }
        store.close();
        throw new NullPointerException("Email with ID: " + subjectUID + " was not delivered!");
    }

    public static void waitForEmail(String subject, int wait) throws MessagingException {
        QAManager.log.debug("Waiting for email: "+subject+" to arrive for max: "+wait+" seconds");
        printDebug = true;
        if(wait==0) throw new AssertionError("Wait arg must be greater than zero");
        ArrayList list = null;
        while (wait!=0){
            try{
                list = getEmailHeaders(subject);
                if(list != null) return;
            }catch (NullPointerException e){
                /**Ignored*/
            }
            wait--;
            printDebug = false;
            //Utilities.Wait(500);
        }
    }
}
