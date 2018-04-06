package com.vladislavbalyuk.currencyconverter.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vladislavbalyuk.currencyconverter.App;
import com.vladislavbalyuk.currencyconverter.model.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.presenter.MainPresenter;
import com.vladislavbalyuk.currencyconverter.R;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainViewInterface, View.OnClickListener {

    private View view;

    private MainPresenter presenter;

    private TextInputEditText editTextSum;
    private TextView textViewResult;
    private Spinner spinnerFrom, spinnerIn;
    private ImageButton buttonReplace;

    private CurrencyAdapter adapter;

    private boolean updateDone = false;

    private Map<String, Integer> flagMap;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        flagMap = presenter.getFlagMap();
        setRetainInstance(true);

        presenter.loadCurrency();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_main, container, false);

            editTextSum = (TextInputEditText)view.findViewById(R.id.TextSumFrom);
            textViewResult = (TextView)view.findViewById(R.id.TextResult);

            spinnerFrom = (Spinner) view.findViewById(R.id.SpinnerFrom);
            spinnerIn = (Spinner) view.findViewById(R.id.SpinnerIn);

            buttonReplace = (ImageButton) view.findViewById(R.id.btnReplace);
            buttonReplace.setOnClickListener(this);

            Observable<MainPresenter> observable = Observable.just(presenter);
            observable.map(x -> {List<CurrencyValue> list = x.getListCurrency();
                        Collections.sort(list, CurrencyValue.comparator);
                        return list;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(x -> {
                        adapter = new CurrencyAdapter(getActivity(), R.layout.item_spinner, x);
                        adapter.setDropDownViewResource(R.layout.item_spinner);
                        spinnerFrom.setAdapter(adapter);
                        spinnerIn.setAdapter(adapter);
                        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                presenter.setCurrencyValueFrom((CurrencyValue) parent.getItemAtPosition(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        spinnerIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                presenter.setCurrencyValueIn((CurrencyValue) parent.getItemAtPosition(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        SharedPreferences sPref = App.getContext().getSharedPreferences(getResources().getString(R.string.name_preferences),MODE_PRIVATE);
                        String charCodeFrom = sPref.getString("charCodeCurrencyFrom", "RUB");
                        String charCodeIn = sPref.getString("charCodeCurrencyIn", "RUB");
                        presenter.setCurrencyValueFrom(presenter.getCurrencyForCharCode(charCodeFrom));
                        presenter.setCurrencyValueIn(presenter.getCurrencyForCharCode(charCodeIn));
                        setSpinner();
                    });


            editTextSum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    presenter.checkSum(editTextSum.getText().toString());
                }
            });

        }

        return view;
    }

    public void getResult(){

        String result = presenter.getResult(editTextSum.getText().toString());
        textViewResult.setText(result);

    }

    @Override
    public void notifyAdapter() {
        Observable<MainPresenter> observable = Observable.just(presenter);
        observable.map(x -> {List<CurrencyValue> list = x.getListCurrency();
                    Collections.sort(list, CurrencyValue.comparator);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    adapter.clear();
                    adapter.addAll(x);
                    adapter.notifyDataSetChanged();
                });
    }

    public void setEditTextSum(String s) {
        editTextSum.setText("");
        editTextSum.append(s);
    }

    @Override
    public void setSpinner() {
        spinnerFrom.setSelection(adapter.getPosition(presenter.getCurrencyValueFrom()));
        spinnerIn.setSelection(adapter.getPosition(presenter.getCurrencyValueIn ()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnReplace:
                presenter.replaceCurrencyValue();
                setSpinner();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sPref = App.getContext().getSharedPreferences(getResources().getString(R.string.name_preferences),MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("charCodeCurrencyFrom", presenter.getCurrencyValueFrom().getCharCode());
        ed.putString("charCodeCurrencyIn", presenter.getCurrencyValueIn().getCharCode());
        ed.commit();    }

    private class CurrencyAdapter extends ArrayAdapter<CurrencyValue> {

        private LayoutInflater inflater;
        private int layout;
        private List<CurrencyValue> listCurrencyValue;

        public CurrencyAdapter(Context context, int resource, List<CurrencyValue> listCurrencyValue) {
            super(context, resource, listCurrencyValue);
            this.listCurrencyValue = listCurrencyValue;
            this.layout = resource;
            this.inflater = LayoutInflater.from(context);
        }
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(this.layout, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CurrencyValue currencyValue = listCurrencyValue.get(position);

            viewHolder.imageView.setImageResource(flagMap.get(currencyValue.getCharCode()));
            viewHolder.textView.setText(currencyValue.getCharCode() + " (" + currencyValue.getName() + ")");

            return convertView;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(this.layout, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CurrencyValue currencyValue = listCurrencyValue.get(position);

            viewHolder.imageView.setImageResource(flagMap.get(currencyValue.getCharCode()));
            viewHolder.textView.setText(currencyValue.getCharCode() + " (" + currencyValue.getName() + ")");

            return convertView;
        }

        private class ViewHolder {
            final ImageView imageView;
            final TextView textView;
            ViewHolder(View view){

                imageView = (ImageView) view.findViewById(R.id.imageView);
                textView = (TextView) view.findViewById(android.R.id.text1);
            }
        }
    }

}