package com.vladislavbalyuk.currencyconverter.M;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.vladislavbalyuk.currencyconverter.App;
import com.vladislavbalyuk.currencyconverter.P.Presenter;

import java.util.List;

public class Model {
    private Context context;
    private DB dataBase;
    private BroadcastReceiver br;

    private Presenter presenter;

    private List<CurrencyValue> listCurrencyValue;
    private CurrencyValue currencyValueFrom;
    private CurrencyValue currencyValueIn;

    public Model(final Presenter presenter) {
        context = App.getContext();
        this.dataBase = DB.getInstance(context);
        this.presenter = presenter;

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean listIsEmpty = false;
                if(listCurrencyValue == null || listCurrencyValue.isEmpty()){
                    listIsEmpty = true;
                }
                listCurrencyValue = dataBase.getListCurrency();
                if(listIsEmpty) {
                    presenter.notifyAdapter();
                }
            }
        };

        IntentFilter intFilt = new IntentFilter("com.vladislavbalyuk.currencyconverter.servicebackbroadcast");
        context.registerReceiver(br, intFilt);
    }

    public void loadCurrency(){
        context.startService(new Intent(context, LoadCurrencyService.class));
    }

    public List<CurrencyValue> getListCurrencyValue() {
        if(listCurrencyValue == null){
            listCurrencyValue = dataBase.getListCurrency();
        }

        if(listCurrencyValue != null && listCurrencyValue.size() > 0){
            if(currencyValueFrom == null) {
                currencyValueFrom = listCurrencyValue.get(0);
            }
            if(currencyValueIn == null) {
                currencyValueIn = listCurrencyValue.get(0);
            }
        }
        return listCurrencyValue;
    }

    public CurrencyValue getCurrencyValueFrom() {
        return currencyValueFrom;
    }

    public void setCurrencyValueFrom(CurrencyValue currencyValueFrom) {
        this.currencyValueFrom = currencyValueFrom;
    }

    public CurrencyValue getCurrencyValueIn() {
        return currencyValueIn;
    }

    public void setCurrencyValueIn(CurrencyValue currencyValueIn) {
        this.currencyValueIn = currencyValueIn;
    }

}
