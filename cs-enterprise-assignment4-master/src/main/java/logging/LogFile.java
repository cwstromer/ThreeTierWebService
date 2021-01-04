package logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * rolling log file example.
 * this will run for 1000 seconds so you may want to force quit it whenever you wish.
 *
 * be sure to change the log-path property in the xml config file
 *
 * note that log4j is checking for changes in the log config every 5 seconds
 * using the monitorInterval property of the Configuration in the config file
 */
public class LogFile {
    private static Logger logger = LogManager.getLogger(LogFile.class);

    public static void main(String[] args) {
        for(int i = 0; i < 1000; i++) {
            //won't show because of the default logger config
            logger.debug(i + " - This is a debug log message");

            //but this will
            logger.error(i + " - This is an error log message");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
