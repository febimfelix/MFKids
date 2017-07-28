package com.febi.mfkids;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.febi.mfkids.datamodels.Kids;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by flock on 28/7/17.
 */

public class HomeActivity extends BaseActivity {

    private LinearLayout mCardViewParentLayout;
    private ArrayList<Kids> mKidsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        mCardViewParentLayout = (LinearLayout) findViewById(R.id.id_card_view_parent_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAddKidScreen();
            }
        });

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
        mKidsList.clear();
        mCardViewParentLayout.removeAllViews();
        showProgressDialog();
        mKidsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    Kids kids = children.getValue(Kids.class);

                    mKidsList.add(kids);
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
        if(mKidsList.size() > 0) {
            for(int i = 0; i < mKidsList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.home_card_layout, null);

                TextView kidNameText = (TextView) view.findViewById(R.id.id_kid_name_text);
                kidNameText.setText("Name : " + mKidsList.get(i).getName());
                kidNameText.setTextColor(Color.BLACK);

                TextView kidGenderText = (TextView) view.findViewById(R.id.id_kid_gender_text);
                kidGenderText.setText("Gender : " + mKidsList.get(i).getGender());
                kidGenderText.setTextColor(Color.BLACK);

                TextView kidDobText = (TextView) view.findViewById(R.id.id_kid_dob_text);
                kidDobText.setText("Dob : " + mKidsList.get(i).getDob());
                kidDobText.setTextColor(Color.BLACK);

                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveToKidDetailsScreen(mKidsList.get(finalI).getKey());
                    }
                });

                mCardViewParentLayout.addView(view);
            }
        }
    }

    private void moveToKidDetailsScreen(String key) {
        Intent intent = new Intent(HomeActivity.this, KidMileStonesActivity.class);
        intent.putExtra("kid_key", key);
        startActivityForResult(intent, 101);
    }

    private void moveToAddKidScreen() {
        Intent intent = new Intent(HomeActivity.this, AddKidActivity.class);
        startActivityForResult(intent, 101);
    }
}
