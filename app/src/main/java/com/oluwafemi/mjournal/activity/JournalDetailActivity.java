package com.oluwafemi.mjournal.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.oluwafemi.mjournal.R;
import com.oluwafemi.mjournal.databinding.ActivityJournalDetailsBinding;
import com.oluwafemi.mjournal.helper.UIUtils;
import com.oluwafemi.mjournal.model.Journal;

/**
 * Created by femi on 7/1/18.
 */

public class JournalDetailActivity extends AppCompatActivity {

    private static final String TAG = "JournalDetailActivity";
    public static final String JOURNAL_KEY = "journal_bundle";
    private Journal mJournal;

    ActivityJournalDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_details);


        Bundle intentExtra = getIntent().getExtras();
        if (intentExtra != null) {
            String journalStr = intentExtra.getString(JOURNAL_KEY);
            Gson gson = new Gson();
            mJournal = gson.fromJson(journalStr, Journal.class);
            initViews(mJournal);
        }

    }

    private void initViews(Journal journal) {
        // init toolbar
        UIUtils.setupToolbar(this, UIUtils.formatDateNoTime(journal.getTime()));

        binding.tvJournalTitle.setText(journal.getTitle());
        binding.tvDate.setText(UIUtils.formatDateNoTime(journal.getTime()));
        binding.tvTime.setText(UIUtils.formatTimeFromLong(journal.getTime()));
        binding.tvJornalText.setText(journal.getText());
        binding.tvLocation.setText(journal.getLocationText());
    }
}
