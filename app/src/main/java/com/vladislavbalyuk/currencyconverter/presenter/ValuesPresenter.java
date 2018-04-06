package com.vladislavbalyuk.currencyconverter.presenter;

import com.vladislavbalyuk.currencyconverter.model.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.model.Model;
import com.vladislavbalyuk.currencyconverter.view.ValuesInterface;

import java.util.List;
import java.util.Map;

public class ValuesPresenter {
    private ValuesInterface view;
    private Model model;

    public ValuesPresenter(ValuesInterface view) {
        this.view = view;
        model = Model.getInstance(null);
    }

    public List<CurrencyValue> getListCurrency(){
        return model.getListCurrencyValue();
    }

    public Map<String, Integer> getFlagMap(){
        return model.getFlagMap();
    }

}
