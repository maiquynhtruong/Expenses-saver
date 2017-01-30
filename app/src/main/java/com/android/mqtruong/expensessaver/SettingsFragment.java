package com.android.mqtruong.expensessaver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "SettingsFragment";
    public static final String KEY_VERSION = "version";
    public static final String KEY_DEVELOPER = "developer";
    public static SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        findPreference(KEY_VERSION).setSummary(getAppVersion());
        findPreference(KEY_DEVELOPER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openDeveloperInfoDialog();
                return true;
            }
        });
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private String getAppVersion() {
        try {
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return getResources().getString(R.string.settings_unknown);
        }
    }

    private void openDeveloperInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings_about_developer_title);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_layout = inflater.inflate(R.layout.dialog_about, null);
        TextView textView_about_name = (TextView) dialog_layout.findViewById(R.id.about_name);
//        textView_about_name.setText(Html.fromHtml(getResources().getString(R.string.settings_about_developer_name)));
        textView_about_name.setText(Html.fromHtml("<b>Developer: </b> Mai Truong"));
        TextView textView_about_email = (TextView) dialog_layout.findViewById(R.id.about_email);
//        textView_about_email.setText(Html.fromHtml(getResources().getString(R.string.settings_about_developer_email)));
        textView_about_email.setText(Html.fromHtml("<b><u>Email: </u></b>maitruong2801@gmail.com"));
        TextView textView_about_github = (TextView) dialog_layout.findViewById(R.id.about_github_link);
//        textView_about_github.setText(Html.fromHtml(getResources().getString(R.string.settings_about_developer_github)));
        textView_about_github.setText(Html.fromHtml("<b><u>Github</u></b>:\n" +
                "        <a href=\"https://github.com/maiquynhtruong/expenses-saver\"> Expenses Saver</a>"));
        builder.setView(dialog_layout);
        builder.setPositiveButton(R.string.settings_about_developer_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
