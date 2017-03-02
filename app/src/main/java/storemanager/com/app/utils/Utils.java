package storemanager.com.app.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.Summary;

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

    public static final String[] cShops = {"Таврия-В", "Цветочный рынок", "Парк Приморский"};
    public static final ArrayList<String> coffeShops = new ArrayList<>(Arrays.asList(cShops));

    public static final String[] cItems1 = {
            "Эспрессо", "Эспрессо 2x", "Американо", "Кофе с молоком", "Кофе по Ирландски", "Кофе по Французски",  "Кофе Choco Mocco"};
    public static final String[] cItems2 = {
            "Кофе IRISH",  "Мокко",  "Капучино Nuts",  "Латте",  "Латте BANANA'S",  "Латте TIRAMISU", "Сливочный кулкам",  "Карамельный мокиато"};
    public static final String[] cItems3 = {
            "Горячий шоколад",  "Горячий шоколад BANANA'S",  "Горячий шоколад TIRAMISU",  "Горячий шоколад с зефиром",  "Какао",  "Какао с зефиром",  "Milk"};

    public static int priceForOneSizeItems[] = {14, 19, 15, 19, 0, 30, 35};
    public static int priceCoffeWithSize[] = {21,24,27,21,24,27,19,22,25,25,30,32,19,22,25,25,30,32,25,30,32,25,30,32,25,30,32};
    public static int priceChocolateCocao[] = {20,24,27,25,30,32,25,30,32,22,27,32,17,20,22,20,24,27,14,19,22};

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

    public static List<Summary> sortSummaryListByDate(List<Summary> summaryList) {

        return summaryList;
    }

}
