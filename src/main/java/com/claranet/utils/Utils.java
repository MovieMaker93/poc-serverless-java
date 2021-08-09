package com.claranet.utils;

public class Utils {

    public static boolean checkProductType(String productType) {

        for (ProductsEnum type : ProductsEnum.values()){
            if (type.name().equals(productType)){
                return true;
            }
        }
        return false;
    }

    public static double calculateImportedTax (double price, boolean isImported){
        if(isImported)
            return price + (price * 0.05);
        return price;
    }

    public static double calculateStandardTax (double price){
        return price + (price * 0.10);
    }
}
