package storemanager.com.app.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Utils {
    public static final String LOG_TAG = "debug";

    public static final String[] cItems = {
            "Эспрессо", "Эспрессо 2x", "Американо", "Кофе с молоком", "Кофе по Ирландски", "Кофе по Французски",  "Кофе Choco Mocco",
            "Кофе IRISH",  "Мокко",  "Капучино Nuts",  "Латте",  "Латте BANANA'S",  "Латте TIRAMISU", "Сливочный кулкам",  "Карамельный мокиато",
            "Горячий шоколад",  "Горячий шоколад BANANA'S",  "Горячий шоколад TIRAMISU",  "Горячий шоколад с зефиром",  "Какао",  "Какао с зефиром",  "Milk"};

    public static final Integer[] cSizes = {250, 350, 450};
    public static final Integer[] cNumber = {1, 2, 3, 4, 5};

    public static final ArrayList<String> coffeItems = new ArrayList<>(Arrays.asList(cItems));
    public static final ArrayList<Integer> coffeSizes = new ArrayList<>(Arrays.asList(cSizes));
    public static final ArrayList<Integer> coffeNumber = new ArrayList<>(Arrays.asList(cNumber));

    public static final String EXTRA_TAG_MAIL = "email";
    public static final String EXTRA_TAG_ID = "id";

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd , HH:mm");
        return mdformat.format(calendar.getTime());

    }

}
