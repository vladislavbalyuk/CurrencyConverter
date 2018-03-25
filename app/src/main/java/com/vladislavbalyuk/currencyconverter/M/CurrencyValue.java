package com.vladislavbalyuk.currencyconverter.M;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;

@Root(name="Valute")
public class CurrencyValue {
    private  String id;
    private  String charCode;
    private  int nominal;
    private BigDecimal value;
    private  String name;

    @Attribute(name="ID")
    public String getId() {
        return id;
    }

    @Attribute(name="ID")
    public void setId(String id) {
        this.id = id;
    }

    @Element(name="CharCode")
    public String getCharCode() {
        return charCode;
    }

    @Element(name="CharCode")
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    @Element(name="Nominal")
    public int getNominal() {
        return nominal;
    }

    @Element(name="Nominal")
    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    @Element(name="Value")
    public BigDecimal getValue() {
        return value;
    }

    @Element(name="Value")
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setValueFromString(String value) {
        this.value = new BigDecimal(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
