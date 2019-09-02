# coding=utf-8
import sqlite3
try:
    import lxml.etree as etree
except ImportError:
    print("lxml not found. Using ElementTree, errors are possible.")
    import xml.etree.ElementTree as etree
from pathlib import Path


def firstOrDefaultText(xpath, default):
    return xpath[0].text if xpath else default


def stringsByNodes(nodesList):
    return list(map(lambda x: x.text, nodesList))


if not Path("kanjidic2.xml").is_file():
    print("Kanjidic xml file not found in the working directory.")
    exit(2)
with open("kanjidic2.xml") as f:
    xmlRoot = etree.parse(f)
    print("Kanjidic file parsed successfully!")
try:
    conn = sqlite3.connect("kanji.db")
    cur = conn.cursor()
    # cur.execute(
    #     """CREATE TABLE if not exists kanji ( \
    #     character text primary key, grade integer, onyomiReadings text, kunyomiReadings text, englishMeanings text); \
    #     """)
    cur.execute(
        """CREATE TABLE IF NOT EXISTS Kanji (`character` TEXT NOT NULL, `grade` INTEGER NOT NULL, \
          `onyomiReadings` TEXT, `kunyomiReadings` TEXT, `englishMeanings` TEXT, `weight` REAL NOT NULL, PRIMARY KEY(`character`))""")
    # cur.execute("insert into kanji values ('1', 2, '1', '2', '3');")
except sqlite3.Error:
    print("Problems with sqlite3 database detected.")
    exit(3)
charactersNodes = xmlRoot.xpath("//character")
inserts = []
for i in charactersNodes:
    character = i.xpath(".//literal")[0].text  # literal is always present
    grade = int(firstOrDefaultText(i.xpath(".//grade"), 11))
    onyomiReadingsList = stringsByNodes(i.xpath(".//reading[@r_type='ja_on']")) or ["ソンザイシテイナイ"]
    kunyomiReadingsList = stringsByNodes(i.xpath(".//reading[@r_type='ja_kun']")) or ["そんざいしていない"]
    englishMeaningsList = stringsByNodes(i.xpath(".//meaning[not(@*)]"))
    inserts.append(
        (character,
         grade,
         ','.join(onyomiReadingsList),
         ','.join(kunyomiReadingsList),
         ','.join(englishMeaningsList),))
# print(inserts[10:20])
cur.executemany('insert into kanji values (?, ?, ?, ?, ?, 1.0);', inserts)
conn.commit()
cur.close()
