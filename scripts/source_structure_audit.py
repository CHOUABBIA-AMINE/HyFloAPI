#!/usr/bin/env python3
"""Read-only, metadata-only structural audit for the checked-out HyFloAPI tree."""
from __future__ import annotations
import hashlib
import json
import re
from pathlib import Path
from zipfile import ZipFile, BadZipFile
from xml.etree import ElementTree as ET

ROOT = Path("src/main/resources")
NS = {"m":"http://schemas.openxmlformats.org/spreadsheetml/2006/main",
      "r":"http://schemas.openxmlformats.org/officeDocument/2006/relationships",
      "p":"http://schemas.openxmlformats.org/package/2006/relationships"}

def workbook_meta(path: Path) -> dict:
    # Never load workbook cell values or sharedStrings.xml; read only workbook.xml,
    # relationships and worksheet structural dimensions. Worksheet titles omitted.
    with ZipFile(path) as z:
        w = ET.fromstring(z.read("xl/workbook.xml"))
        sheets = w.findall("m:sheets/m:sheet", NS)
        relmap = {}
        relpath = "xl/_rels/workbook.xml.rels"
        if relpath in z.namelist():
            rels = ET.fromstring(z.read(relpath))
            for rel in rels:
                relmap[rel.attrib.get("Id")] = rel.attrib.get("Target", "")
        result = []
        for sh in sheets:
            rid = sh.attrib.get("{"+NS["r"]+"}id")
            target = relmap.get(rid, "")
            # Only known XLSX worksheet part paths inside xl/ are inspected.
            if target.startswith("/xl/"):
                member = target.lstrip("/")
            elif target.startswith("xl/"):
                member = target
            else:
                member = "xl/" + target.lstrip("/")
            if ".." in Path(member).parts or not member.startswith("xl/worksheets/") or member not in z.namelist():
                result.append({"sheet_index":len(result)+1,"state":sh.attrib.get("state","visible"),"dimension":None,"row_elements":None,"note":"worksheet part unavailable"})
                continue
            with z.open(member) as source:
                # Count worksheet row elements, not claimed nonempty data records.
                count = 0
                dimension = None
                for event, element in ET.iterparse(source, events=("start","end")):
                    if event == "start" and element.tag == "{"+NS["m"]+"}dimension":
                        dimension = element.attrib.get("ref")
                    if event == "end" and element.tag == "{"+NS["m"]+"}row":
                        count += 1
                    element.clear()
            result.append({"sheet_index":len(result)+1,"state":sh.attrib.get("state","visible"),
                           "dimension":dimension,"row_elements":count})
        return {"sheet_count":len(sheets),"sheets":result}

def sql_meta(path: Path) -> dict:
    # No SQL execution; collect structural counts only, never identifiers or data.
    creates, inserts = set(), set()
    create_re = re.compile(r"\bCREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?(?:`[^`]+`\.)?`?([A-Za-z0-9_]+)`?",re.I)
    insert_re = re.compile(r"\b(?:INSERT|REPLACE)\s+INTO\s+(?:`[^`]+`\.)?`?([A-Za-z0-9_]+)`?",re.I)
    statements = 0
    with path.open("r",encoding="utf-8",errors="replace") as source:
        for line in source:
            # Avoid printing raw SQL, names, comments, or values.
            for m in create_re.finditer(line):
                creates.add(m.group(1))
            for m in insert_re.finditer(line):
                inserts.add(m.group(1))
                statements += 1
    return {"create_table_identifier_count":len(creates),
            "insert_target_identifier_count":len(inserts),
            "insert_line_count_not_row_count":statements}

def main() -> None:
    entries = []
    for directory in ("data","extras"):
        for path in sorted((ROOT/directory).rglob("*")):
            if path.suffix.lower() not in (".xlsx",".sql") or not path.is_file():
                continue
            rel = path.relative_to(ROOT).as_posix()
            # Skip all files in credential-bearing LPL subtree.
            if rel.startswith("data/LPL/"):
                continue
            record = {"path":rel,"bytes":path.stat().st_size}
            try:
                if path.suffix.lower()==".xlsx":
                    record["format"]="xlsx"
                    record.update(workbook_meta(path))
                else:
                    record["format"]="sql"
                    record.update(sql_meta(path))
                record["status"]="inspected_structure_only"
            except (OSError,KeyError,BadZipFile,ET.ParseError,ValueError) as exc:
                record["status"]="unreadable"
                record["error_type"]=type(exc).__name__
            entries.append(record)
    print("SOURCE_STRUCTURE_AUDIT_BEGIN")
    print(json.dumps({"report_version":1,"source":"HyFloAPI working tree",
                      "policy":"metadata only; no sheet names, cell contents, SQL table names, SQL values, credentials or payloads",
                      "files":entries},ensure_ascii=True,separators=(",",":")))
    print("SOURCE_STRUCTURE_AUDIT_END")

if __name__=="__main__":
    main()
