package com.oluwafemi.mjournal.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.oluwafemi.mjournal.JournalViewModel;
import com.oluwafemi.mjournal.R;
import com.oluwafemi.mjournal.adapter.JournalRecyclerAdapter;
import com.oluwafemi.mjournal.databinding.ActivityDashboardBinding;
import com.oluwafemi.mjournal.helper.FabVisibilityListener;
import com.oluwafemi.mjournal.helper.PrefUtils;
import com.oluwafemi.mjournal.model.Journal;

import java.util.List;

import static com.oluwafemi.mjournal.helper.Constants.DB_JOURNALS;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    ActivityDashboardBinding binding;

    LinearLayoutManager layoutManager;
    FirebaseDatabase mDatabase;
    DatabaseReference journalRef;
    JournalViewModel journalViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // init viewModel
        journalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);

        // init firebase listeners
        setUpInsertJournalListener();

        fetchJournalList();

        binding.fabAddJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddJournalActivity.class));
            }
        });
    }

    private void fetchJournalList() {
        journalViewModel.getJournalLiveList().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {
                if (journals != null && journals.size() > 0) {
                    addListToAdapter(journals);
                }
            }
        });
    }

    private void addListToAdapter(List<Journal> journals) {
        binding.rvAllJournal.setLayoutManager(new LinearLayoutManager(this));
        JournalRecyclerAdapter adapter = new JournalRecyclerAdapter(journals, this);
        binding.rvAllJournal.setAdapter(adapter);

        // listener to hide fab if recyclerview is scrolled to the last item
        FabVisibilityListener fabVisibilityListener = new FabVisibilityListener(binding.fabAddJournal);
        binding.rvAllJournal.addOnScrollListener(fabVisibilityListener);
    }


    private void setUpInsertJournalListener() {

        // init firebase database
        mDatabase = FirebaseDatabase.getInstance();
        journalRef = mDatabase.getReference(DB_JOURNALS);

        String userId = PrefUtils.getLoggedUserId(this);

        Query journalQuery = journalRef.orderByChild("creatorId").equalTo(userId);
        Log.e(TAG, "setUpInsertJournalListener: user id == " + userId );

        journalQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Journal journal = dataSnapshot.getValue(Journal.class);
                        journalViewModel.insertJournal(journal);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Journal journal = dataSnapshot.getValue(Journal.class);
                journalViewModel.insertJournal(journal);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Journal journal = dataSnapshot.getValue(Journal.class);
                journalViewModel.removeJournal(journal);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
