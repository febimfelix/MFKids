package com.febi.mfkids;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.febi.mfkids.datamodels.Kids;

import java.util.Calendar;

public class AddKidActivity extends BaseActivity {
    private EditText mKidNameEditText;
    private static TextView mSelectDateTextView;

    private boolean mIsGenderMale           = true;
    private static boolean mIsDateSelected  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        mKidNameEditText            = (EditText) findViewById(R.id.id_kid_name);
        mSelectDateTextView         = (TextView) findViewById(R.id.id_select_date_text);
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.id_gender_radio_group);
        Button addKidButton         = (Button) findViewById(R.id.id_add_kid_button);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.id_gender_male_radio_button) {
                    mIsGenderMale = true;
                } else {
                    mIsGenderMale = false;
                }
            }
        });

        addKidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    saveAndFinish();
                } else {
                    showDismissiveAlerts("Fill fields properly.");
                }
            }
        });

        mSelectDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarWidget();
            }
        });
    }

    private boolean validateFields() {
        if(mKidNameEditText.getText() != null && !mKidNameEditText.getText().toString().trim().isEmpty()) {
            if(!mIsDateSelected) {
                return false;
            }
        }
        return true;
    }

    private void saveAndFinish() {
        String userId   = mKidsDatabaseReference.push().getKey();
        Kids kids       = new Kids(userId, mKidNameEditText.getText().toString().trim(),
                mIsGenderMale ? "male" : "female", mSelectDateTextView.getText().toString());

        mKidsDatabaseReference.child(userId).setValue(kids);

        setResult(RESULT_OK);
        finish();
    }

    private void showCalendarWidget() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year                = calendar.get(Calendar.YEAR);
            int month               = calendar.get(Calendar.MONTH);
            int day                 = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mIsDateSelected = true;
            mSelectDateTextView.setText(year + "/" + (month + 1) + "/" + day);
        }
    }
}
