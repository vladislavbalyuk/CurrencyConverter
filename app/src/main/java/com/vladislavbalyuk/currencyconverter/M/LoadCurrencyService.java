package com.vladislavbalyuk.currencyconverter.M;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vladislavbalyuk.currencyconverter.App;
import com.vladislavbalyuk.currencyconverter.R;

import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoadCurrencyService extends IntentService {

    Context context;
    public DB dataBase;


    public LoadCurrencyService() {
        super("");
        context = App.getContext();
        this.dataBase = DB.getInstance(context);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String xmlValues = "";
        String xmlCurrencies = "";
        List<CurrencyValue> listCurrencyValue;
        Map<String,String> mapCurrencyName;

        while(xmlValues.isEmpty())
            try {
                xmlValues = getXmlValues();
                if(xmlValues.isEmpty()) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        while(xmlCurrencies.isEmpty())
            try {
                xmlCurrencies = getXmlCurrencies();
                if(xmlCurrencies.isEmpty()) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        listCurrencyValue = parseXmlValues(xmlValues);
        mapCurrencyName = parseXmlCurrencies(xmlCurrencies);

        writeCurrencyName(listCurrencyValue, mapCurrencyName);

        dataBase.updateCurrency(listCurrencyValue);

        Intent intentReceiver = new Intent("com.vladislavbalyuk.currencyconverter.servicebackbroadcast");
        sendBroadcast(intentReceiver);
    }

    private String getXmlValues() throws IOException {
        BufferedReader reader=null;
        try {
            URL url=new URL(getResources().getString(R.string.api_url_values).toString());
            HttpURLConnection c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);

            c.connect();
            reader= new BufferedReader(new InputStreamReader(c.getInputStream(), "windows-1251"));
            StringBuilder buf=new StringBuilder();
            String line=null;
            while ((line=reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String getXmlCurrencies() throws IOException {
        BufferedReader reader=null;
        try {
            URL url=new URL(getResources().getString(R.string.api_url_currencies).toString());
            HttpURLConnection c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);

            c.connect();
            reader= new BufferedReader(new InputStreamReader(c.getInputStream(), "windows-1251"));
            StringBuilder buf=new StringBuilder();
            String line=null;
            while ((line=reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private List<CurrencyValue> parseXmlValues(String xml) {
        xml = xml.replaceAll(",", ".");

        Reader reader = new StringReader(xml);
        Persister serializer = new Persister();
        try
        {
            CurrencyValues currencyValues = serializer.read(CurrencyValues.class, reader, false);
            List<CurrencyValue> listCurrencyValue = currencyValues.getListCurrencyValue();
            //добавим RUB
            CurrencyValue currencyValue = new CurrencyValue();
            currencyValue.setId("000000");
            currencyValue.setCharCode("RUB");
            currencyValue.setNominal(1);
            currencyValue.setValueFromString("1");

            listCurrencyValue.add(0, currencyValue);

            return listCurrencyValue;
        }
        catch (Exception e)
        {
            Log.d("MyTag", e.getMessage());
        }
        return  null;

    }

    private Map<String,String> parseXmlCurrencies(String xml) {
        Reader reader = new StringReader(xml);
        Persister serializer = new Persister();
        try
        {
            Currencies currencies = serializer.read(Currencies.class, reader, false);
            Map<String,String> map = new HashMap<String,String>();
            map.put("000000","Российский рубль");
            for (Currency c : currencies.getListCurrency()) map.put(c.getId(),c.getName());

            return map;
        }
        catch (Exception e)
        {
            Log.d("MyTag", e.getMessage());
        }
        return  null;
    }

    private void writeCurrencyName(List<CurrencyValue> listCurrencyValue, Map<String,String> mapCurrencyName){
        for(CurrencyValue currencyValue: listCurrencyValue){
            String name = mapCurrencyName.get(currencyValue.getId());
            if(name != null && !name.isEmpty()){
                currencyValue.setName(name);
            }
        }
    }

}
