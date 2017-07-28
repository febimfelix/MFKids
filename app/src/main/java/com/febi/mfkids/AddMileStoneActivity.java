package com.febi.mfkids;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.febi.mfkids.datamodels.KidsMilestones;

import java.util.Calendar;

/**
 * Created by flock on 28/7/17.
 */

public class AddMileStoneActivity extends BaseActivity {
    private EditText mMileStoneEditText;
    private static TextView mSelectDateTextView;

    private static boolean mIsDateSelected  = false;

    private String mSelectedKidKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_milestone);

        mMileStoneEditText          = (EditText) findViewById(R.id.id_kid_milestone);
        mSelectDateTextView         = (TextView) findViewById(R.id.id_select_milestone_date_text);
        Button addMilestoneButton   = (Button) findViewById(R.id.id_add_milestone_button);

        addMilestoneButton.setOnClickListener(new View.OnClickListener() {
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

        mSelectedKidKey = getIntent().getStringExtra("kid_key");
    }

    private boolean validateFields() {
        if(mMileStoneEditText.getText() != null && !mMileStoneEditText.getText().toString().trim().isEmpty()) {
            if(!mIsDateSelected) {
                return false;
            }
        }
        return true;
    }

    private void saveAndFinish() {
        String milestoneId          = mMilestoneDatabaseReference.push().getKey();
        KidsMilestones milestone         = new KidsMilestones(milestoneId, mSelectedKidKey,
                mMileStoneEditText.getText().toString().trim(), mSelectDateTextView.getText().toString());

        mMilestoneDatabaseReference.child(milestoneId).setValue(milestone);

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
