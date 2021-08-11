package com.claranet.utils;

public enum ProductsEnumType {
    EXCLUDED_PRODUCT(false,false),
    GENERAL(true,false),
    IMPORTED_EXCLUDED_PRODUCT(false,true),
    IMPORTED_GENERAL(true,true);

    private boolean isTaxed;
    private boolean isImported;

    ProductsEnumType(boolean taxed, boolean imported){
        isImported = imported;
        isTaxed = taxed;
    }

    public boolean isImported(){
        return isImported;
    }
    public boolean isTaxed(){
        return isTaxed;
    }

}
