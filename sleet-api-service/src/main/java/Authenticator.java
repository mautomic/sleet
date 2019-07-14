import java.io.File;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Authenticator {

    private static Authenticator instance;
    private static final Logger LOG = LogManager.getLogger(Authenticator.class);

    public synchronized static Authenticator getInstance() {
        if (instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    private static String API_KEY = "";

    private Authenticator() {

        try {
            Scanner sc = new Scanner(new File(getClass().getClassLoader().getResource("credentials.cfg").getFile()));
            API_KEY = sc.next();
            LOG.info("API Key Retrieved");
        } catch(Exception e) {
            LOG.error("API Key Not Found");
        }
    }

    public String getApiKey() {
        return API_KEY;
    }
}