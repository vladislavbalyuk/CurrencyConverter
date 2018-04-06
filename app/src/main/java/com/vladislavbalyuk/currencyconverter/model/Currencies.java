package com.vladislavbalyuk.currencyconverter.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="Valuta")
public class Currencies {

    private List<Currency> listCurrency;

    @ElementList(inline=true, name="Item")
    public List<Currency> getListCurrency() {
        return listCurrency;
    }

    @ElementList(inline=true, name="Item")
    public void setListCurrency(List<Currency> listCurrency) {
        this.listCurrency = listCurrency;
    }
}
