package com.claranet.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static boolean checkProductType(String productType) {

        for (ProductsEnum type : ProductsEnum.values()){
            if (type.name().equals(productType)){
                return true;
            }
        }
        return false;
    }

    public static double calculateImportedTax (double price){
            return price * 0.05;
    }

    public static double calculateStandardTax (double price){
        return price * 0.10;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
