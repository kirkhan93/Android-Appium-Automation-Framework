package utilities.exceptions;

/**
 * Created by Artur Spirin on 11/18/15.
 **/
public class GoogleChromeFailedToLoadPage extends RuntimeException{

    public GoogleChromeFailedToLoadPage(){
        super("Failed to load page via Google Chrome");
    }
    public GoogleChromeFailedToLoadPage(String message){
        super(message);
    }
}
