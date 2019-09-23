package com.example.lord.engrisuru.db.kanji;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface KanjiGentleModeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(KanjiGentleModeEntry en);

    @Query("select * from KanjiGentleModeEntry where character = :character")
    KanjiGentleModeEntry getEntryByCharacter(char character);
}

