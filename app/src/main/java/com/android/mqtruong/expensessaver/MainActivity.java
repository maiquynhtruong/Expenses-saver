package com.android.mqtruong.expensessaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<Tally> tallyList;
    TallyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tallyList = new ArrayList<>();

        ListView tallyView = (ListView) findViewById(R.id.tally_list_view);
        adapter = new TallyAdapter(this, tallyList);
        tallyView.setAdapter(adapter);
    }

    private void createDialog(){
        AddDialog dialog = new AddDialog();
        // show the dialog
        dialog.show(getSupportFragmentManager(), AddDialog.TAG);
    }

    public void getTallies() {
        Map<String, ?> dataMap = sharedPreferences.getAll();

        if (tallyList.isEmpty() || dataMap.isEmpty()) {
            addTally(new Tally("First Tally", Tally.DEFAULT_VALUE, Tally.DEFAULT_AMOUNT, Tally.DEFAULT_STEP));
        } else {
            for (String name : dataMap.keySet()) {
                int value = (Integer) dataMap.get(name + "_value");
                double amount = (Double) dataMap.get(name + "_amount");
                double step = (Double) dataMap.get(name + "_step");
                tallyList.add(new Tally(name, value, amount, step));
            }
        }
    }

    public void addTally(Tally tally) {
        tallyList.add(tally);
        adapter.notifyDataSetChanged();
    }

    public void saveTallies() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        for (Tally tally : tallyList) {
            editor.putInt(tally.name + "_value", tally.value);
            editor.putFloat(tally.name + "_amount", (float) tally.amount);
            editor.putFloat(tally.name + "_step", (float) tally.steps);
        }
        editor.commit();
    }

    public void removeAllTallies() {
        tallyList.clear();
        addTally(new Tally("First Tally", Tally.DEFAULT_VALUE, Tally.DEFAULT_AMOUNT, Tally.DEFAULT_STEP));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTallies();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTallies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
