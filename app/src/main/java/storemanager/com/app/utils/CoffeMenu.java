package storemanager.com.app.utils;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.models.CoffeItem;

public class CoffeMenu {
    List<CoffeItem> menu = new ArrayList();

    public CoffeMenu(){}

    public List<CoffeItem> getMenu(){
        CoffeItem item;
        for (int i = 0; i < Utils.cItems1.length; i++) {
            item = new CoffeItem();
            item.setOneSize(true);
            item.setName(Utils.cItems1[i]);
            item.setSize(250);
            item.setPrice(Utils.priceForOneSizeItems[i]);
            menu.add(item);
        }
        int ii = 0;
        for (int i = 0; i < Utils.cItems2.length; i++) {
            for (int e = 0; e < Utils.cSizes.length; e++) {
                item = new CoffeItem();
                item.setName(Utils.cItems2[i]);
                item.setOneSize(false);
                item.setSize(Utils.cSizes[e]);
                item.setPrice(Utils.priceCoffeWithSize[ii]);
                ii++;
                menu.add(item);
            }
        }
        ii = 0;
        for (int i = 0; i < Utils.cItems3.length; i++) {
            for (int e = 0; e < Utils.cSizes.length; e++) {
                item = new CoffeItem();
                item.setName(Utils.cItems3[i]);
                item.setOneSize(false);
                item.setSize(Utils.cSizes[e]);
                item.setPrice(Utils.priceChocolateCocao[ii]);
                ii++;
                menu.add(item);
            }
        }
        return menu;
    }
}
