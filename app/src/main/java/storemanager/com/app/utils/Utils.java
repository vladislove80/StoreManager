package storemanager.com.app.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import storemanager.com.app.models.Summary;

public class Utils {
    public static final String LOG_TAG = "debug";

    public static final String userStatus[] = {"Администратор", "Реализатор"};

   /* public static final String[] cItems = {
            "Эспрессо", "Эспрессо 2x", "Американо", "Кофе с молоком", "Кофе по Ирландски", "Кофе по Французски",  "Кофе Choco Mocco",
            "Кофе IRISH",  "Мокко",  "Капучино Nuts",  "Латте",  "Латте BANANA'S",  "Латте TIRAMISU", "Сливочный кулкам",  "Карамельный мокиато",
            "Горячий шоколад",  "Горячий шоколад BANANA'S",  "Горячий шоколад TIRAMISU",  "Горячий шоколад с зефиром",  "Какао",  "Какао с зефиром",  "Milk"};*/

    public static final String[] cShops = {"Таврия-В", "Цветочный рынок", "Парк Приморский"};
    public static final ArrayList<String> coffeShops = new ArrayList<>(Arrays.asList(cShops));

    public static final String[] cItems1 = {
            "Эспрессо", "Эспрессо 2x", "Американо", "Кофе с молоком", "Кофе по Ирландски", "Кофе по Французски",  "Кофе Choco Mocco"};
    public static final String[] cItems2 = {
            "Кофе IRISH",  "Мокко",  "Капучино Nuts",  "Латте",  "Латте BANANA'S",  "Латте TIRAMISU", "Сливочный кулкам",  "Карамельный мокиато"};
    public static final String[] cItems3 = {
            "Горячий шоколад",  "Горячий шоколад BANANA'S",  "Горячий шоколад TIRAMISU",  "Горячий шоколад с зефиром",  "Какао",  "Какао с зефиром",  "Milk"};

    public static final String EXTRA_TAG_MAIL = "email";
    public static final String EXTRA_TAG_NAME = "name";
    public static final String EXTRA_TAG_ID = "id";
    public static final String EXTRA_TAG_SHOP = "shop";

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

}
