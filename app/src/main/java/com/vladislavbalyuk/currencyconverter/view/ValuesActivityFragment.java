package com.vladislavbalyuk.currencyconverter.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladislavbalyuk.currencyconverter.R;
import com.vladislavbalyuk.currencyconverter.model.CurrencyValue;
import com.vladislavbalyuk.currencyconverter.presenter.ValuesPresenter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ValuesActivityFragment extends Fragment implements ValuesInterface {

    private View view;

    private ValuesPresenter presenter;

    private Map<String, Integer> flagMap;


    public ValuesActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ValuesPresenter(this);
        flagMap = presenter.getFlagMap();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_values, container, false);

            Observable<ValuesPresenter> observable = Observable.just(presenter);
            observable.map(x -> {List<CurrencyValue> list = x.getListCurrency();
                                Collections.sort(list, CurrencyValue.comparator);
                                return list;
                            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(x -> {
                        RecyclerView listView = (RecyclerView) view.findViewById(R.id.rcValues);
                        ValuesAdapter adapter = new ValuesAdapter(getActivity(), x);
                        listView.setAdapter(adapter);
                    });
        }

        return view;
    }

    public class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private List<CurrencyValue> list;

        public ValuesAdapter(Context context, List<CurrencyValue> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.item_recycler, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CurrencyValue currencyValue = list.get(position);

            holder.imageView.setImageResource(flagMap.get(currencyValue.getCharCode()));
            holder.textViewCharCode.setText(currencyValue.getCharCode());
            holder.textViewName.setText(currencyValue.getName());
            holder.textViewValue.setText(String.valueOf(currencyValue.getValue()
                    .divide(new BigDecimal(currencyValue.getNominal()))));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageView;
            final TextView textViewCharCode;
            final TextView textViewName;
            final TextView textViewValue;
            ViewHolder(View view){
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                textViewCharCode = (TextView) view.findViewById(R.id.textCharCode);
                textViewName = (TextView) view.findViewById(R.id.textName);
                textViewValue = (TextView) view.findViewById(R.id.textValue);
            }
        }
    }
}
