package com.android.mqtruong.expensessaver;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnCreateContextMenuListener{
    static SharedPreferences attributePreferences;
    static SharedPreferences namePreferences;
    ArrayList<Tally> tallyList;
    TallyAdapter adapter;
    private static final String main = "MainActivity";
    public static final String PREFERENCE_NAME = "name_pref";
    public static final String PREFERENCE_ATTRIBUTE = "attr_pref";
    private static final String BUNDLE_ARGUMENTS_INDEX = "index";
    public static FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        namePreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        attributePreferences = getSharedPreferences(PREFERENCE_ATTRIBUTE, MODE_PRIVATE);
        tallyList = new ArrayList<>();

        ListView tallyView = (ListView) findViewById(R.id.tally_list_view);
        adapter = new TallyAdapter(this, tallyList);
        tallyView.setAdapter(adapter);
        registerForContextMenu(tallyView);
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
        Log.d(main, "onCrateOptionMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_reset_all:
                showResetAllDialog();
                return true;
            case R.id.action_remove_all:
                showRemoveAllDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d(main, "onCrateContextMenu()");
        if (v.getId() == R.id.tally_list_view) {
            Snackbar.make(findViewById(R.id.coordinator_layout), "long click", Snackbar.LENGTH_LONG);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(R.string.tally_options_title);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_item, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(main, "onCrateContextItemSelected()");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteDialog(info.position);
                return true;
            case R.id.action_edit:
                showEditDialog(info.position);
                return true;
            case R.id.action_reset:
                showResetDialog(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAddDialog(){
        AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), AddDialog.TAG);
    }

    private void showSettings() {
        getFragmentManager().beginTransaction().replace(R.id.preference_layout, new SettingsFragment())
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            fab.show();
        }
    }

    private void showDeleteDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_message_confirm)
                .setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTally(index);
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.delete_done, Snackbar.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.alert_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void showEditDialog(int index) {
        EditDialog editDialog = new EditDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ARGUMENTS_INDEX, index);
        editDialog.setArguments(bundle);
        editDialog.show(getSupportFragmentManager(), EditDialog.TAG);
    }

    private void showResetDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_dialog_title)
                .setMessage(R.string.reset_dialog_confirm)
                .setPositiveButton(R.string.reset_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetTally(index);
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.reset_done, Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void showResetAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_dialog_title)
                .setMessage(R.string.reset_dialog_confirm)
                .setPositiveButton(R.string.reset_all_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetAllTallies();
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.reset_all_done, Snackbar.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.alert_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void showRemoveAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_dialog_title)
                .setMessage(R.string.reset_dialog_confirm)
                .setPositiveButton(R.string.reset_all_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeAllTallies();
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.reset_all_done, Snackbar.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.alert_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    public void addTally(Tally tally) {
        Log.d(main, "addTally(): add a single tally");
        tallyList.add(tally);
        adapter.notifyDataSetChanged();
    }

    public void updateTally(int index, Tally newTally) {
        Tally toBeEdited = tallyList.get(index);
        toBeEdited.name = newTally.name;
        toBeEdited.value = newTally.value;
        toBeEdited.amount = newTally.amount;
        toBeEdited.steps = newTally.steps;
        adapter.notifyDataSetChanged();
    }
    public void removeTally(int index) {
        Log.d(main, "removeTally(): remove a single tally at index " + index);
        tallyList.remove(index);
        adapter.notifyDataSetChanged();
    }

    public void resetTally(int index) {
        Tally tally = tallyList.get(index);
        tally.value = Tally.DEFAULT_VALUE;
        tally.amount = Tally.DEFAULT_AMOUNT;
        adapter.notifyDataSetChanged();
    }

    public void resetAllTallies() {
        for (int i = 0; i < tallyList.size(); i++) {
            Tally tally = tallyList.get(i);
            tally.value = Tally.DEFAULT_VALUE;
            tally.amount = Tally.DEFAULT_AMOUNT;
        }
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
}
