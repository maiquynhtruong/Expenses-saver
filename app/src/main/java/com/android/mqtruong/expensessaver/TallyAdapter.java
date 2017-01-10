package com.android.mqtruong.expensessaver;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;


public class TallyAdapter extends BaseAdapter{

    String name;
    LinearLayout root_view;
    LayoutInflater inflater;
    ArrayList<Tally> tallyList;

    public TallyAdapter(Context c, ArrayList<Tally> tallyList) {
        inflater = LayoutInflater.from(c);
        this.tallyList = tallyList;
    }

    @Override
    public Tally getItem(int position) {
        return (Tally) tallyList.get(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        root_view = (LinearLayout) inflater.inflate(R.layout.list_item, null);
        Button increment = (Button) root_view.findViewById(R.id.increment_btn);
        Button decrement = (Button) root_view.findViewById(R.id.decrement_btn);
        final TextView item_name = (TextView) root_view.findViewById(R.id.tally_name);
        final TextView item_value = (TextView) root_view.findViewById(R.id.tally_value);
        final Tally tally = tallyList.get(position);
        item_name.setText(tally.name + ": " + tally.value);
        item_value.setText("$" + tally.amount);
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name.setText(tally.name + ": " + ++tally.value);
                tally.amount += tally.steps;
                String amount = String.format(Locale.US, "$%(,.2f", tally.amount);
                item_value.setText(amount);
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name.setText(tally.name + ": " + --tally.value);
                tally.amount -= tally.steps;
                String amount = String.format(Locale.US, "$%(,.2f", tally.amount);
                item_value.setText(amount);
            }
        });
        return root_view;
    }

    @Override
    public int getCount() {
        return tallyList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
