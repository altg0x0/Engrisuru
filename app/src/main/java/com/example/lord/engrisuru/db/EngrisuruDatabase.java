package com.example.lord.engrisuru.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.lord.engrisuru.japanese.Kanji;

@Database(entities = {Kanji.class}, version = 1)
@TypeConverters(DbTypeConverters.class)
public abstract class EngrisuruDatabase extends RoomDatabase {
    abstract public KanjiDao kanjiDao();
}
