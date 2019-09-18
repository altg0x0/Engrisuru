package com.example.lord.engrisuru.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.lord.engrisuru.MainActivity;
import com.example.lord.engrisuru.Utils;
import com.example.lord.engrisuru.japanese.Kanji;

import java.io.File;

@Database(entities = {Kanji.class}, version = 1)
@TypeConverters(DbTypeConverters.class)
public abstract class EngrisuruDatabase extends RoomDatabase {
    // https://stackoverflow.com/questions/50103232/using-singleton-within-the-android-room-library
    private static final String DB_NAME = "engrisurudb.db";
    private static volatile EngrisuruDatabase instance;

    public static synchronized EngrisuruDatabase getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }
    public static void init() {
        instance = Room.databaseBuilder(MainActivity.getAppContext(), EngrisuruDatabase.class, "engrisurudb.sqlite").build();
    }

    public static boolean exportDatabase() {
        getInstance().close();
        final File dbFile = MainActivity.getAppContext().getDatabasePath("engrisurudb.sqlite");
        boolean ret = Utils.FS.copyFileToExternalStorage(dbFile, "engrisurudb.sqlite");
        EngrisuruDatabase.init();
        return ret;
    }

    public static boolean importDatabase() {
        getInstance().close();
        final File dbFile = MainActivity.getAppContext().getDatabasePath("engrisurudb.sqlite");
        boolean ret = Utils.FS.copyFileFromExternalStorage("engrisurudb.sqlite", dbFile);
        EngrisuruDatabase.init();
        return ret;
    }




    abstract public KanjiDao kanjiDao();
}
