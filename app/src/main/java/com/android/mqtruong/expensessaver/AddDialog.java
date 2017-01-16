package com.android.mqtruong.expensessaver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class AddDialog extends DialogFragment {
    public static final String TAG = "AddDialog";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_add_dialog, null);

        final EditText name = (EditText) view.findViewById(R.id.add_dialog_edit_title);
        final EditText value = (EditText) view.findViewById(R.id.add_dialog_edit_value);
        final EditText amount = (EditText) view.findViewById(R.id.add_dialog_edit_amount);
        final EditText steps = (EditText) view.findViewById(R.id.add_dialog_edit_steps);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(R.string.add_dialog_message)
                .setPositiveButton(R.string.add_dialog_positve, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tallyName = name.getText().toString();
                        if (tallyName.equals("")) {
                            Toast.makeText(activity, R.string.toast_no_name, Toast.LENGTH_LONG).show();
                        } else {
                            int tallyValue = 0;
                            String tallyValueString = value.getText().toString();
                            if (!tallyValueString.equals("")) {
                                tallyValue = Integer.valueOf(tallyValueString);
                            }
                            double tallyAmount = 0.0;
                            String tallyAmountString = amount.getText().toString();
                            if (!tallyAmountString.equals("")) {
                                tallyAmount = Double.valueOf(tallyAmountString);
                            }
                            double tallyStep = 0.0;
                            String tallyStepString = steps.getText().toString();
                            if (!tallyAmountString.equals("")) {
                                tallyStep = Double.valueOf(tallyStepString);
                            }
                            activity.addTally(new Tally(tallyName, tallyValue, tallyAmount, tallyStep));
                        }
                    }
                })
                .setNegativeButton(R.string.add_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

}
