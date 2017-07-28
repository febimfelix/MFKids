package com.febi.mfkids;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.febi.mfkids.datamodels.KidsMilestones;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by flock on 28/7/17.
 */

public class KidMileStonesActivity extends BaseActivity {

    private LinearLayout mCardViewParentLayout;
    private ArrayList<KidsMilestones> mKidsMilestonesList = new ArrayList<>();

    private String mSelectedKidKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kid_milestone_layout);

        mCardViewParentLayout = (LinearLayout) findViewById(R.id.id_card_view_milestone_parent_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.milestone_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAddMilestoneScreen();
            }
        });

        mSelectedKidKey = getIntent().getStringExtra("kid_key");

        readDataFromFirebase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 101) {
            readDataFromFirebase();
        }
    }

    private void readDataFromFirebase() {
        mKidsMilestonesList.clear();
        mCardViewParentLayout.removeAllViews();
        showProgressDialog();
        mMilestoneDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    KidsMilestones milestones = children.getValue(KidsMilestones.class);
                    if(milestones.getKidKey().equals(mSelectedKidKey)) {
                        mKidsMilestonesList.add(milestones);
                    }
                }

                addViewsToLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void addViewsToLayout() {
        hideProgressDialog();
        if(mKidsMilestonesList.size() > 0) {
            for(int i = 0; i < mKidsMilestonesList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.milestone_card_layout, null);

                TextView kidNameText = (TextView) view.findViewById(R.id.id_milestone_name_text);
                kidNameText.setText("Milestone : " + mKidsMilestonesList.get(i).getMilestone());
                kidNameText.setTextColor(Color.BLACK);

                TextView kidDobText = (TextView) view.findViewById(R.id.id_milestone_dob_text);
                kidDobText.setText("Dob : " + mKidsMilestonesList.get(i).getDateOfMilestone());
                kidDobText.setTextColor(Color.BLACK);

                mCardViewParentLayout.addView(view);
            }
        }
    }

    private void moveToAddMilestoneScreen() {
        Intent intent = new Intent(KidMileStonesActivity.this, AddMileStoneActivity.class);
        intent.putExtra("kid_key", mSelectedKidKey);
        startActivityForResult(intent, 101);
    }
}

