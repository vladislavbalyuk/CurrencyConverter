package com.vladislavbalyuk.currencyconverter.P;

import com.vladislavbalyuk.currencyconverter.M.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.M.Model;
import com.vladislavbalyuk.currencyconverter.V.ViewInterface;

import java.math.BigDecimal;
import java.util.List;

public class Presenter {

    ViewInterface view;
    Model model;

    public Presenter(ViewInterface view) {
        this.view = view;
        model = new Model(this);
    }

    public void loadCurrency(){
        model.loadCurrency();
    }

    public List<CurrencyValue> getlistCurrency(){
        return model.getListCurrencyValue();
    }

    public CurrencyValue getCurrencyFrom() {
        return model.getCurrencyValueFrom();
    }

    public void setCurrencyFrom(CurrencyValue currencyValueFrom) {
        model.setCurrencyValueFrom(currencyValueFrom);
    }

    public CurrencyValue getCurrencyIn() {
        return model.getCurrencyValueIn();
    }

    public String getResult(String strSum){
        CurrencyValue currencyValueFrom;
        CurrencyValue currencyValueIn;
        BigDecimal sum, result;

        currencyValueFrom = getCurrencyFrom();
        currencyValueIn = getCurrencyIn();

        if(strSum.isEmpty()){return "";}
        if(currencyValueFrom == null){return "";}
        if(currencyValueIn == null){return "";}

        sum = new BigDecimal(strSum);
        sum.setScale(2, BigDecimal.ROUND_DOWN);

        result = sum.multiply(currencyValueFrom.getValue())
                .multiply(new BigDecimal(currencyValueIn.getNominal()))
                .divide(new BigDecimal(currencyValueFrom.getNominal()))
                .divide(currencyValueIn.getValue(),2, BigDecimal.ROUND_DOWN);

        return result.toString();
    }

    public void setCurrencyIn(CurrencyValue currencyValueIn) {
        model.setCurrencyValueIn(currencyValueIn);
    }

    public void notifyAdapter() {
        view.notifyAdapter();
    }

}
