package com.vladislavbalyuk.currencyconverter.V;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vladislavbalyuk.currencyconverter.M.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.P.Presenter;
import com.vladislavbalyuk.currencyconverter.R;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ViewInterface {

    private View view;

    private Presenter presenter;

    TextInputEditText textSum;
    TextView textResult;
    Spinner spinnerFrom, spinnerIn;

    CurrencyAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
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

            textSum = (TextInputEditText)view.findViewById(R.id.TextSumFrom);
            textResult = (TextView)view.findViewById(R.id.TextResult);

            spinnerFrom = (Spinner) view.findViewById(R.id.SpinnerFrom);
            adapter = new CurrencyAdapter(getActivity(), R.layout.item_spinner, presenter.getlistCurrency());
            adapter.setDropDownViewResource(R.layout.item_spinner);
            spinnerFrom.setAdapter(adapter);

            spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    presenter.setCurrencyFrom((CurrencyValue) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerIn = (Spinner) view.findViewById(R.id.SpinnerIn);
            adapter.setDropDownViewResource(R.layout.item_spinner);
            spinnerIn.setAdapter(adapter);

            spinnerIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    presenter.setCurrencyIn((CurrencyValue) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        return view;
    }

    public void getResult(){

        String result = presenter.getResult(textSum.getText().toString());
        textResult.setText(result);

    }

    @Override
    public void notifyAdapter() {
        adapter = new CurrencyAdapter(getActivity(), R.layout.item_spinner, presenter.getlistCurrency());
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerFrom.setAdapter(adapter);
        spinnerIn.setAdapter(adapter);
    }

    public class CurrencyAdapter extends ArrayAdapter<CurrencyValue> {

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

            viewHolder.textView.setText(currencyValue.getCharCode() + " (" + currencyValue.getName() + ")");

            return convertView;
        }

        private class ViewHolder {
            final TextView textView;
            ViewHolder(View view){
                textView = (TextView) view.findViewById(android.R.id.text1);
            }
        }
    }

}
