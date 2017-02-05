package io.github.maiquynhtruong.expensessaver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "SettingsFragment";
    public static final String KEY_VERSION = "version";
    public static final String KEY_DEVELOPER = "developer";
    public static final String KEY_VIBRATION = "vibrationOn";
    public static final String KEY_RATE_APP = "rateApp";
    public static SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.fab.hide();
        addPreferencesFromResource(R.xml.settings);
        findPreference(KEY_VERSION).setSummary(getAppVersion());
        findPreference(KEY_DEVELOPER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openDeveloperInfoDialog();
                return true;
            }
        });
        findPreference(KEY_RATE_APP).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences prefs = getActivity().getSharedPreferences("apprater", 0);
                SharedPreferences.Editor editor = prefs.edit();
                AppRater.showRateDialog(getActivity(), editor);
                return true;
            }
        });
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        textView_about_name.setText(Html.fromHtml(getActivity().getResources().getString(R.string.settings_about_developer_name)));
        TextView textView_about_email = (TextView) dialog_layout.findViewById(R.id.about_email);
        textView_about_email.setText(Html.fromHtml(getActivity().getResources().getString(R.string.settings_about_developer_email)));
        TextView textView_about_github = (TextView) dialog_layout.findViewById(R.id.about_github_link);
        textView_about_github.setText(Html.fromHtml(getActivity().getResources().getString(R.string.settings_about_developer_github)));
        textView_about_github.setMovementMethod(LinkMovementMethod.getInstance());
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
