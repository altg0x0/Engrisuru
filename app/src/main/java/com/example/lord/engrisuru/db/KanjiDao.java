package com.example.lord.engrisuru.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.lord.engrisuru.japanese.Kanji;

@Dao
public interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertKanji(Kanji kanji);

    @Query("select * from Kanji where grade <= :maxGrade;")
    public Kanji[] getKanjiByMaxGrade(int maxGrade);

    @Query("SELECT COUNT(*) FROM Kanji")
    int getDataCount();
}
