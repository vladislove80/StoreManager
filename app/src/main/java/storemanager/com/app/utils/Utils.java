package storemanager.com.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private Utils(){}

    public static final String LOG_TAG = "debug";

    public static final String userStatus[] = {"Администратор", "Реализатор"};

    public static final String[] cShops = {"Таврия-В", "Цветочный рынок", "Парк Приморский"};
    public static final ArrayList<String> coffeShops = new ArrayList<>(Arrays.asList(cShops));

    public static final String EXTRA_TAG_EMAIL = "email";
    public static final String EXTRA_TAG_NAME = "name";
    public static final String EXTRA_TAG_ID = "id";
    public static final String EXTRA_TAG_TEAM = "team";
    public static final String EXTRA_TAG_SHOP = "shop";

    public static final String EXTRA_TAG_ADD_NEW_MEMBER_NAME = "name";
    public static final String EXTRA_TAG_ADD_NEW_MEMBER_EMAIL = "email";
    public static final String EXTRA_TAG_ADD_NEW_MEMBER_STATUS = "status";

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm, dd / MM / yyyy");
        return mdformat.format(calendar.getTime());
    }

    public static String getCurrentDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy");
        return mdformat.format(calendar.getTime());
    }

    public static final String[] itemDataLists = {"item names", "item sizes", "item ingredient names", "item ingredient sizes", "item ingredient measure"};

    public static boolean isCurrentDate(String dateString){
        Date date;
        boolean isCurant = false;
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm, dd / MM / yyyy");
        Calendar currantDate = Calendar.getInstance();
        try {
            date = mdformat.parse(dateString);
            Calendar dateFromString = Calendar.getInstance();
            dateFromString.setTime(date);
            isCurant = currantDate.get(Calendar.YEAR) == dateFromString.get(Calendar.YEAR) &&
                    currantDate.get(Calendar.DAY_OF_YEAR) == dateFromString.get(Calendar.DAY_OF_YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isCurant;
    }

}
