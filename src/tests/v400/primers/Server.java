package tests.v400.primers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.v400.*;

/**
 * Created by Artur Spirin on 11/18/15.
 **/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SmokeCheck.class,
        IPv4.class,
        //IPv6.class,
        Headers.class,
        Fallback.class,
        FunctionalityEmail.class
})
public class Server {}
