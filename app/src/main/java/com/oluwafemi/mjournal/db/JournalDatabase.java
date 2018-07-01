package com.oluwafemi.mjournal.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.oluwafemi.mjournal.model.Journal;

@Database(entities = {Journal.class},
        version = 1, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {

    private static JournalDatabase INSTANCE;

    public static JournalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, JournalDatabase.class, "mjournal_db").build();
        }
        return INSTANCE;
    }

    public abstract JournalDao journalDao();
}
