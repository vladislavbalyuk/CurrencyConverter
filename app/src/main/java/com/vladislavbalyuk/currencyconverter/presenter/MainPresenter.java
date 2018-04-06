package com.vladislavbalyuk.currencyconverter.presenter;

import com.vladislavbalyuk.currencyconverter.model.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.model.Model;
import com.vladislavbalyuk.currencyconverter.view.MainViewInterface;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MainPresenter {

    private MainViewInterface view;
    private Model model;

    public MainPresenter(MainViewInterface view) {
        this.view = view;
        model = Model.getInstance(this);
    }

    public void loadCurrency(){
        model.loadCurrency();
    }

    public List<CurrencyValue> getListCurrency(){
        return model.getListCurrencyValue();
    }


    public CurrencyValue getCurrencyValueFrom() {
        return model.getCurrencyValueFrom();
    }

    public void setCurrencyValueFrom(CurrencyValue currencyValueFrom) {
        model.setCurrencyValueFrom(currencyValueFrom);
    }

    public CurrencyValue getCurrencyValueIn() {
        return model.getCurrencyValueIn();
    }

    public void setCurrencyValueIn(CurrencyValue currencyValueIn) {
        model.setCurrencyValueIn(currencyValueIn);
    }

    public CurrencyValue getCurrencyForCharCode(String charCode) {
        for(CurrencyValue currencyValue: getListCurrency()){
            if(currencyValue.getCharCode().equals(charCode))
                return currencyValue;
        }
        return null;
    }

    public void replaceCurrencyValue(){
        CurrencyValue temp = getCurrencyValueFrom();
        setCurrencyValueFrom(getCurrencyValueIn());
        setCurrencyValueIn(temp);
    }

    public String getResult(String strSum){
        CurrencyValue currencyValueFrom;
        CurrencyValue currencyValueIn;
        BigDecimal sum, result;

        currencyValueFrom = getCurrencyValueFrom();
        currencyValueIn = getCurrencyValueIn();

        if(strSum.isEmpty()){return "";}
        if(currencyValueFrom == null){return "";}
        if(currencyValueIn == null){return "";}

        sum = new BigDecimal(strSum);
        sum = sum.setScale(2, BigDecimal.ROUND_DOWN);

        result = sum.multiply(currencyValueFrom.getValue())
                .multiply(new BigDecimal(currencyValueIn.getNominal()))
                .divide(new BigDecimal(currencyValueFrom.getNominal()))
                .divide(currencyValueIn.getValue(),2, BigDecimal.ROUND_DOWN);

        return result.toString();
    }

    public void notifyAdapter() {
        view.notifyAdapter();
    }

    public void checkSum(String s){
        int i = s.indexOf(".");
        if(i != -1 && (s.length() - i) > 3){
            view.setEditTextSum(s.substring(0, i + 3));
        }
    }

    public Map<String, Integer> getFlagMap(){
        return model.getFlagMap();
    }
}
