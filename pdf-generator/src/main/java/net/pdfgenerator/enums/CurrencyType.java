package net.pdfgenerator.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CurrencyType {
    USD("$"),
    EUR("€");


    public static final Map<String,CurrencyType> ENUM_MAP;
    String currencySymbol;

    CurrencyType(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    static {
        Map<String, CurrencyType> map = new ConcurrentHashMap<String, CurrencyType>();
        for (CurrencyType instance : CurrencyType.values()) {
            map.put(instance.name().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static CurrencyType get (String name) {
        return ENUM_MAP.get(name);
    }
}
