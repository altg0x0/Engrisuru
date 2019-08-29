try:
    import lxml.etree as etree
except ImportError:
    print("lxml not found. Using ElementTree, errors are possible.")
    import xml.etree.ElementTree as etree
from pathlib import Path

if not Path("kanjidic2.xml").is_file():
    print("Kanjidic xml file not found in the working directory.")
    exit(2)

with open("kanjidic2.xml") as f:
    xmlRoot = etree.parse(f)
    print("Kanjidic file parsed successfully!")