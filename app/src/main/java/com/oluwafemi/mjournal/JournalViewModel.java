package com.oluwafemi.mjournal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.oluwafemi.mjournal.db.JournalDatabase;
import com.oluwafemi.mjournal.model.Journal;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class JournalViewModel extends AndroidViewModel {

    private static final String TAG = "JournalViewModel";
    private JournalDatabase journalDatabase;
    private LiveData<List<Journal>> journalLiveList;

    public JournalViewModel(@NonNull Application application) {
        super(application);

        journalDatabase = JournalDatabase.getDatabase(application);
        journalLiveList = journalDatabase.journalDao().getAllJournals();
    }

    public LiveData<List<Journal>> getJournalLiveList() {
        return journalLiveList;
    }

    public void insertJournal(final Journal journal) {
        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                journalDatabase.journalDao().insertJournal(journal);
            }
        });
    }

    public void removeJournal(final Journal journal) {
        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                journalDatabase.journalDao().deleteJournal(journal);
            }
        });
    }

    /*class InsertJournalAsyncTask extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }*/

}
