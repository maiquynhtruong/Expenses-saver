package io.github.maiquynhtruong.expensessaver;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class TallyAdapter extends BaseAdapter{

    String name;
    LinearLayout root_view;
    LayoutInflater inflater;
    ArrayList<Tally> tallyList;
    private Vibrator vibrator;
    private static final int DEFAULT_VIBRATION_DURATION = 30; // milliseconds
    MainActivity activity;

    public TallyAdapter(Context c, ArrayList<Tally> tallyList, MainActivity activity) {
        inflater = LayoutInflater.from(c);
        this.tallyList = tallyList;
        this.activity = activity;
    }

    @Override
    public Tally getItem(int position) {
        return (Tally) tallyList.get(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        root_view = (LinearLayout) inflater.inflate(R.layout.list_item, null);
        final Button increment = (Button) root_view.findViewById(R.id.increment_btn);
        final Button decrement = (Button) root_view.findViewById(R.id.decrement_btn);
        final TextView item_name = (TextView) root_view.findViewById(R.id.tally_name);
        final TextView item_value = (TextView) root_view.findViewById(R.id.tally_value);
        final Tally tally = tallyList.get(position);
        item_name.setText(tally.name + ": " + tally.value);
        String amount = String.format(Locale.US, "$%(,.2f", tally.amount);
        item_value.setText(amount);

        checkButtons(tally.value, increment, decrement);
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name.setText(tally.name + ": " + ++tally.value);
                tally.amount += tally.steps;
                String amount = String.format(Locale.US, "$%(,.2f", tally.amount);
                item_value.setText(amount);
                activity.vibrate(DEFAULT_VIBRATION_DURATION);

                /** check the state of the buttons */
                checkButtons(tally.value, increment, decrement);
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name.setText(tally.name + ": " + --tally.value);
                tally.amount -= tally.steps;
                String amount = String.format(Locale.US, "$%(,.2f", tally.amount);
                item_value.setText(amount);
                activity.vibrate(DEFAULT_VIBRATION_DURATION + 10);

                /** check the state of the buttons */
                checkButtons(tally.value, increment, decrement);
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

    private void checkButtons(int value, Button increment, Button decrement) {
        if (value >= Tally.MAX_VALUE) increment.setEnabled(false);
        else increment.setEnabled(true);
        if (value <= Tally.MIN_VALUE) decrement.setEnabled(false);
        else decrement.setEnabled(true);
    }
}
