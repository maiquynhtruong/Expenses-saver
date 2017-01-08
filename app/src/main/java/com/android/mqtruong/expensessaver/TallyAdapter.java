package com.android.mqtruong.expensessaver;

import android.content.Context;
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
import java.util.LinkedHashMap;


public class TallyAdapter extends BaseAdapter{

    String name;
    Button increment, decrement;
    TextView item_name, item_value;
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
        increment = (Button) root_view.findViewById(R.id.increment_btn);
        decrement = (Button) root_view.findViewById(R.id.decrement_btn);
        item_name = (TextView) root_view.findViewById(R.id.tally_name);
        item_value = (TextView) root_view.findViewById(R.id.tally_value);
        Tally curTa = tallyList.get(position);
        Log.i("TallyAdpater", "name= " + curTa.name + ", value= " + curTa.value);
        item_name.setText(curTa.name);
        item_value.setText(String.valueOf(curTa.value));
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



    //    increment.setOnClickListener(new TallyAdapter() {
//        @Override
//        public void onClick(View v) {
//            incrementValue();
//        }
//    });
//
//        decrement.setOnClickListener(new TallyAdapter() {
//            @Override
//            public void onClick(View v) {
//                decrementValue();
//            }
//        });
//    }

    private void setValue(int newValue) {
        if (newValue < Integer.MIN_VALUE) {
            newValue = Integer.MIN_VALUE;
        } else if (newValue > Integer.MAX_VALUE) {
            newValue = Integer.MAX_VALUE;
        }
        item_value.setText(newValue);
    }
    private void setName(String newName) {
        name = newName;
        item_name.setText(newName);
    }

    private void incrementValue() {
        int currentValue = Integer.parseInt(item_value.getText().toString());
        if (currentValue > Integer.MAX_VALUE) {
            currentValue = Integer.MAX_VALUE;
        } else {
            currentValue++;
        }
        item_value.setText(String.valueOf(currentValue));
    }

    private void decrementValue() {
        int currentValue = Integer.parseInt(item_value.getText().toString());
        if (currentValue < Integer.MIN_VALUE) {
            currentValue = Integer.MIN_VALUE;
        } else {
            currentValue--;
        }
        item_value.setText(String.valueOf(currentValue));
    }
}
