package com.claranet.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

    private static final List<String> excludedProducts =
            Collections.unmodifiableList(Arrays.asList("book", "chocolate","pills"));


    public static ProductsEnumType checkTypeOfProduct(String productName, boolean isImported){
            String[] filterExcludedProducts = excludedProducts.stream()
                    .filter(productName::contains)
                    .toArray(String[]::new);
            if(filterExcludedProducts.length > 0){
               return  isImported ? ProductsEnumType.IMPORTED_EXCLUDED_PRODUCT : ProductsEnumType.EXCLUDED_PRODUCT;
            }
            return isImported ? ProductsEnumType.IMPORTED_GENERAL : ProductsEnumType.GENERAL;
    }

    public static BigDecimal calculateImportedTax (BigDecimal price){
        BigDecimal taxStandard = new BigDecimal(".05");
        return taxStandard.multiply(price);
    }

    public static BigDecimal calculateStandardTax (BigDecimal price){
        BigDecimal taxStandard = new BigDecimal(".10");
        return taxStandard.multiply(price);
    }

    public static String roundTwoDecimal(double value) {
        NumberFormat twoDForm = new DecimalFormat("#0.00");
        return twoDForm.format(value);
    }

    public static BigDecimal round(BigDecimal value, BigDecimal rounding, RoundingMode roundingMode){
            BigDecimal divided = value.divide(rounding, 0, roundingMode);
            BigDecimal result = divided.multiply(rounding);
            return result.setScale(2, RoundingMode.UNNECESSARY);
    }
}
