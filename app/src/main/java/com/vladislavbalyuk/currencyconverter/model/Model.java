package com.vladislavbalyuk.currencyconverter.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.vladislavbalyuk.currencyconverter.App;
import com.vladislavbalyuk.currencyconverter.R;
import com.vladislavbalyuk.currencyconverter.presenter.MainPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Model {
    private static Model instance;

    private Context context;
    private DB dataBase;
    private BroadcastReceiver br;

    private MainPresenter mainPresenter;

    private List<CurrencyValue> listCurrencyValue;
    private volatile CurrencyValue currencyValueFrom;
    private volatile CurrencyValue currencyValueIn;

    private Model(final MainPresenter mainPresenter) {
        context = App.getContext();
        this.dataBase = DB.getInstance(context);
        this.mainPresenter = mainPresenter;

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Observable<DB> observable = Observable.just(dataBase);
                observable.map(x -> dataBase.getListCurrency())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(x -> {
                                            listCurrencyValue = x;
                                            for(CurrencyValue currencyValue: listCurrencyValue){
                                                if(currencyValueFrom != null && currencyValue.getCharCode().equals(currencyValueFrom.getCharCode()))
                                                    currencyValueFrom = currencyValue;
                                                if(currencyValueIn != null && currencyValue.getCharCode().equals(currencyValueIn.getCharCode()))
                                                    currencyValueIn = currencyValue;
                                            }

                                            mainPresenter.notifyAdapter();
                                        });
            }
        };

        IntentFilter intFilt = new IntentFilter("com.vladislavbalyuk.currencyconverter.servicebackbroadcast");
        context.registerReceiver(br, intFilt);
    }

    public static Model getInstance(Object presenter) {
        if (instance == null) {
            instance = new Model((MainPresenter) presenter);
        }
        return instance;
    }

    public void loadCurrency(){
        context.startService(new Intent(context, LoadCurrencyService.class));
    }

    public List<CurrencyValue> getListCurrencyValue() {
        if(listCurrencyValue == null){
            listCurrencyValue = dataBase.getListCurrency();
            if(listCurrencyValue != null && listCurrencyValue.size() > 0){
                if(currencyValueFrom == null) {
                    currencyValueFrom = listCurrencyValue.get(0);
                }
                if(currencyValueIn == null) {
                    currencyValueIn = listCurrencyValue.get(0);
                }
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

    public Map<String, Integer> getFlagMap(){
        Map<String, Integer> flagMap = new HashMap<>();
        flagMap.put("AMD", R.drawable.amd);
        flagMap.put("AUD", R.drawable.aud);
        flagMap.put("AZN", R.drawable.azn);
        flagMap.put("BGN", R.drawable.bgn);
        flagMap.put("BRL", R.drawable.brl);
        flagMap.put("BYN", R.drawable.byn);
        flagMap.put("CAD", R.drawable.cad);
        flagMap.put("CHF", R.drawable.chf);
        flagMap.put("CNY", R.drawable.cny);
        flagMap.put("CZK", R.drawable.czk);
        flagMap.put("DKK", R.drawable.dkk);
        flagMap.put("EUR", R.drawable.eur);
        flagMap.put("GBP", R.drawable.gbp);
        flagMap.put("HKD", R.drawable.hkd);
        flagMap.put("HUF", R.drawable.huf);
        flagMap.put("INR", R.drawable.inr);
        flagMap.put("JPY", R.drawable.jpy);
        flagMap.put("KGS", R.drawable.kgs);
        flagMap.put("KRW", R.drawable.krw);
        flagMap.put("KZT", R.drawable.kzt);
        flagMap.put("MDL", R.drawable.mdl);
        flagMap.put("NOK", R.drawable.nok);
        flagMap.put("PLN", R.drawable.pln);
        flagMap.put("RON", R.drawable.ron);
        flagMap.put("RUB", R.drawable.rub);
        flagMap.put("SEK", R.drawable.sek);
        flagMap.put("SGD", R.drawable.sgd);
        flagMap.put("TJS", R.drawable.tjs);
        flagMap.put("TMT", R.drawable.tmt);
        flagMap.put("TRY", R.drawable.try_);
        flagMap.put("UAH", R.drawable.uah);
        flagMap.put("USD", R.drawable.usd);
        flagMap.put("UZS", R.drawable.uzs);
        flagMap.put("XDR", R.drawable.xdr);
        flagMap.put("ZAR", R.drawable.zar);

        return flagMap;
    }
}
