package com.example.lord.engrisuru.db;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

final class DbMigrations {
    static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `KanjiGentleModeEntry` (`character` TEXT NOT NULL, `learnt` INTEGER NOT NULL, PRIMARY KEY(`character`))");
        }
    };
}
