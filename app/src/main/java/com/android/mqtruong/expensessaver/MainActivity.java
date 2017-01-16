package com.android.mqtruong.expensessaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences attributePreferences;
    SharedPreferences namePreferences;
    ArrayList<Tally> tallyList;
    TallyAdapter adapter;
    private static final String main = "MainActivity";
    public static final String PREFERENCE_NAME = "name_pref";
    public static final String PREFERENCE_ATTRIBUTE = "attr_pref";
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

        namePreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        attributePreferences = getSharedPreferences(PREFERENCE_ATTRIBUTE, MODE_PRIVATE);
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
        Map<String, ?> nameMap = namePreferences.getAll();

        if (nameMap.isEmpty()) {
            Log.d(main, "getTallies(): tallyList is empty or dataMap is empty");
            addTally(new Tally("First Tally", Tally.DEFAULT_VALUE, Tally.DEFAULT_AMOUNT, Tally.DEFAULT_STEP));
        } else {
            Log.d(main, "add all the tallies to tallyList");
            for (Map.Entry<String, ?> entry : nameMap.entrySet()) {
                Log.d(main, "getTallies(): " + entry.getKey());
                String name = entry.getKey();
                int value = attributePreferences.getInt(name + "_value", 0);
                double amount = attributePreferences.getFloat(name + "_amount", 0.0f);
                double step = attributePreferences.getFloat(name + "_step", 0.0f);
                tallyList.add(new Tally(name, value, amount, step));
            }
        }
    }

    public void addTally(Tally tally) {
        Log.d(main, "addTally(): add a single tally");
        tallyList.add(tally);
        adapter.notifyDataSetChanged();
    }

    public void saveTallies() {
        Log.d(main, "saveTallies()");
        SharedPreferences.Editor nameEditor = namePreferences.edit();
        SharedPreferences.Editor attrEditor = attributePreferences.edit();
        nameEditor.clear();
        attrEditor.clear();
        for (Tally tally : tallyList) {
            Log.d(main, "tally: " + tally.name);
            nameEditor.putInt(tally.name, 0);
            attrEditor.putInt(tally.name + "_value", tally.value);
            attrEditor.putFloat(tally.name + "_amount", (float) tally.amount);
            attrEditor.putFloat(tally.name + "_step", (float) tally.steps);
        }
        nameEditor.apply();
        attrEditor.apply();
    }

    public void removeAllTallies() {
        tallyList.clear();
        addTally(new Tally("First Tally", Tally.DEFAULT_VALUE, Tally.DEFAULT_AMOUNT, Tally.DEFAULT_STEP));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(main, "OnResume");
        getTallies();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(main, "onPause()");
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
