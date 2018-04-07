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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currencies)) return false;

        Currencies that = (Currencies) o;

        return getListCurrency().equals(that.getListCurrency());

    }

    @Override
    public int hashCode() {
        return getListCurrency().hashCode();
    }
}
