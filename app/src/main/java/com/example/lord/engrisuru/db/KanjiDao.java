package com.example.lord.engrisuru.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lord.engrisuru.japanese.Kanji;

@Dao
public interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertKanji(Kanji kanji);

    @Query("select * from Kanji where grade <= :maxGrade;")
    Kanji[] getKanjiByMaxGrade(int maxGrade);

    @Query("select * from Kanji where grade between :minGrade and :maxGrade;")
    Kanji[] getKanjiByMinMaxGrade(int minGrade, int maxGrade);

    // 2 ** 31 == 2147483648
    // This query does not, in fact, return true weight-based distribution. However, for > 50 entries
    // the result should be good enough if weights of different entries are not too far apart.
    // For better results http://utopia.duth.gr/~pefraimi/research/data/2007EncOfAlg.pdf algorithm is recommended.
    // It cannot be implemented here because android sqlite3 has neither log nor power functions nor any option to implement them
    // without using android NDK.
    @Query("select * from Kanji where grade between :minGrade and :maxGrade order by abs(random() / 2147483648) / weight limit :n;")
    Kanji[] getKanjiByMinMaxGrade(int minGrade, int maxGrade, int n);

    // ROADMAP: change hardcoded constants
    @Query("with true_delim(maxweight, maxrowid) as " +
           "    (select weight, kanji.rowid from kanji where grade between :minGrade and :maxGrade and " +
           "    weight > 1 order by weight, rowid limit 1 offset :n)," +
           "delim_or_default(maxweight, maxrowid) as " +
           "    (select * from (select * from true_delim union values(1.e308, 2147483648)) order by maxweight limit 1)" +
           "select character, grade, onyomiReadings, kunyomiReadings, englishMeanings, weight from kanji join delim_or_default " +
           "where grade between :minGrade and :maxGrade and " +
           "    weight <= ifnull(maxweight, 1) or (weight = ifnull(maxweight, 1) and kanji.rowid < ifnull(maxrowid, 2147483648))" +
           "order by abs(random() / 2147483648) / weight " +
           "limit :n;")
    Kanji[] getKanjiByMinMaxGradeGentleMode(int minGrade, int maxGrade, int n);

    @Query("SELECT COUNT(*) FROM Kanji")
    int getDataCount();

    @Query("update Kanji set weight = :newWeight where character = :character")
    int updateWeight(char character, double newWeight);

    @Update
    int updateWeight(Kanji kanji);
}
