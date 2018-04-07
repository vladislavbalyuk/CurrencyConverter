package com.vladislavbalyuk.currencyconverter.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Item")
public class Currency {
    private  String id;
    private  String name;

    @Attribute(name="ID")
    public String getId() {
        return id;
    }

    @Attribute(name="ID")
    public void setId(String id) {
        this.id = id;
    }

    @Element(name="Name")
    public String getName() {
        return name;
    }

    @Element(name="Name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        if (!getId().equals(currency.getId())) return false;
        return getName().equals(currency.getName());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
