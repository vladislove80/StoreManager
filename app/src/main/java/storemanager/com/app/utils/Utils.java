package storemanager.com.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    public static final String LOG_TAG = "debug";

    public static final String EXTRA_TAG_MAIL = "email";
    public static final String EXTRA_TAG_ID = "id";

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd , HH:mm");
        return mdformat.format(calendar.getTime());

    }

}
