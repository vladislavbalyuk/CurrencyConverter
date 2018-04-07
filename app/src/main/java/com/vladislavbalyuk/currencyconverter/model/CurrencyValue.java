package com.vladislavbalyuk.currencyconverter.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;
import java.util.Comparator;

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

    public static Comparator<CurrencyValue> comparator = new Comparator<CurrencyValue>(){
        public int compare(CurrencyValue currencyValue1, CurrencyValue currencyValue2) {

            String charCode1 = currencyValue1.getCharCode().toUpperCase();
            String charCode2 = currencyValue2.getCharCode().toUpperCase();

            return charCode1.compareTo(charCode2);

        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyValue)) return false;

        CurrencyValue that = (CurrencyValue) o;

        if (getNominal() != that.getNominal()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getCharCode().equals(that.getCharCode())) return false;
        if (!getValue().equals(that.getValue())) return false;
        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCharCode().hashCode();
        result = 31 * result + getNominal();
        result = 31 * result + getValue().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
