package com.vladislavbalyuk.currencyconverter.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="ValCurs")
public class CurrencyValues {

    private List<CurrencyValue> listCurrencyValue;

    @ElementList(inline=true, name="Valute")
    public List<CurrencyValue> getListCurrencyValue() {
        return listCurrencyValue;
    }

    @ElementList(inline=true, name="Valute")
    public void setListCurrencyValue(List<CurrencyValue> listCurrencyValue) {
        this.listCurrencyValue = listCurrencyValue;
    }
}
