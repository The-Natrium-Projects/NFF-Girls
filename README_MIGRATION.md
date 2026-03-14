# Legacy Book Files to Patchouli Format - Complete Migration Package

## Overview

This package contains **all extracted content from the legacy NFF-Girls book system**, ready for migration to the Patchouli mod format. All 84 mobs with both English and Chinese translations, plus comprehensive migration guides.

## What's Included

### 1. Content Files (Complete Text Extraction)

| File | Size | Contains |
|------|------|----------|
| **EN_US_BOOK_CONTENT.txt** | 67 KB | All 84 English text files from `book/mob_dictionary/en_us/` |
| **ZH_CN_BOOK_CONTENT.txt** | 100 KB | All 84 Chinese text files from `book/mob_dictionary/zh_cn/` |
| **ROOT_JSON_CONTENT.txt** | 58 KB | All 88 JSON metadata files with entity rendering info |

**Total Content: 225 KB of extracted legacy book material**

### 2. Documentation Files

| File | Size | Purpose |
|------|------|---------|
| **MIGRATION_SUMMARY.txt** | 15 KB | Executive overview, quick reference, checklist |
| **LEGACY_BOOK_MIGRATION_DATA.md** | 7.3 KB | Structured migration guide with file lists |
| **FILE_EXTRACTION_GUIDE.txt** | 13 KB | Detailed extraction commands and usage examples |
| **README_MIGRATION.md** | This file | Quick start and index |

## Quick Start

### Step 1: Understand the Structure
```
LEGACY SYSTEM:
  book/mob_dictionary/
  ├── en_us/          (84 .txt files)
  ├── zh_cn/          (84 .txt files)
  └── *.json          (88 metadata files)

TARGET SYSTEM (Patchouli):
  patchouli_books/mob_dictionary/
  └── en_us/entries/
      ├── mob/       (create: 84 .json files)
      └── friending/ (create: 84 .json files, partially exists)
  
  lang/
  ├── en_us.json     (add translation keys)
  └── zh_cn.json     (add translation keys)
```

### Step 2: Extract Content
Use any of the content files:
- **EN_US_BOOK_CONTENT.txt** - Extract English descriptions
- **ZH_CN_BOOK_CONTENT.txt** - Extract Chinese translations
- **ROOT_JSON_CONTENT.txt** - Extract metadata and entity rendering info

Each file uses `=== filename ===` as delimiters between entries.

### Step 3: Create Patchouli Entries
For each of 84 mobs:

```json
{
  "category": "nffgirls:mobs",
  "name": "hmag:mobname",
  "icon": "nffgirls:textures/book/icon_mobname.png",
  "pages": [
    {
      "type": "patchouli:entity",
      "entity": "hmag:mobname",
      "scale": 1.0
    },
    {
      "type": "patchouli:text",
      "text": "dict.nffgirls.desc.mob.mobname"
    }
  ]
}
```

See **FILE: patchouli_books/mob_dictionary/en_us/entries/friending/alraune.json** for reference implementation.

### Step 4: Add Translation Keys
Update `lang/en_us.json` and `lang/zh_cn.json`:

```json
{
  "dict.nffgirls.desc.mob.alraune": "[content from alraune.txt]",
  "dict.nffgirls.desc.friending.alraune": "[content from hmag_alraune.txt]"
}
```

## File Manifest

### Content Files (Delimited Format)

All content files use consistent delimiters for easy parsing:
```
=== alraune.txt ===
[file content]
=== banshee.txt ===
[file content]
```

**EN_US_BOOK_CONTENT.txt includes:**
- 51 regular mob descriptions
- 33 HMaG magazine entries (detailed descriptions)
- 13 system pages (root, experience, favorability, friending, etc.)
- Total: 84 entries

**ZH_CN_BOOK_CONTENT.txt includes:**
- Same 84 entries with Chinese translations
- 100 KB (larger due to Chinese character encoding)

**ROOT_JSON_CONTENT.txt includes:**
- 88 JSON metadata files
- Entity rendering parameters (position, scale, rotation)
- Parent/child relationships

### Documentation Files

1. **MIGRATION_SUMMARY.txt** (15 KB)
   - Complete overview of migration process
   - Translation key mapping
   - Migration checklist
   - File statistics
   - Important notes and success criteria

2. **LEGACY_BOOK_MIGRATION_DATA.md** (7.3 KB)
   - Structured file listing
   - Complete mob names
   - JSON structure explanation
   - Translation patterns
   - Migration strategy

3. **FILE_EXTRACTION_GUIDE.txt** (13 KB)
   - Detailed extraction instructions
   - Unix/Linux and Python examples
   - Quick reference commands
   - Content file manifest

## Migration Statistics

- **Total Mobs:** 84 unique
- **Languages:** English (en_us) + Chinese (zh_cn)
- **Entry Types:** Mob description + Friending guide
- **Total Patchouli Entries to Create:** 336 (84 × 2 languages × 2 types)
- **Translation Keys to Add:** 336
- **Content Volume:** 245 KB

## Key Information

### Entity ID Format
- Legacy: `entity.hmag.mobname` or `entity.nffgirls.hmag_mobname`
- Patchouli: `hmag:mobname`
- Translation key: `entity.nffgirls.hmag_mobname`

### Standard Rotation Parameters (from JSON files)
- Default scale: 1.0 (range 0.5-1.5)
- Default position: x=92, y=102
- Default rotation: rot_x=30, rot_y=225, rot_z=0

### Translation Key Patterns
- Mob descriptions: `dict.nffgirls.desc.mob.<mobname>`
- Friending guides: `dict.nffgirls.desc.friending.<mobname>`
- Entity names: `entity.nffgirls.hmag_<mobname>` (already in lang files)

## Mob List (Complete 84 Entries)

**Mobs (51):**
Alraune, Banshee, Catoblepas, Creeper Girl, Crimson Slaughterer, Cursed Doll, Dodomeki, Dogu, Drowned Girl, Dullahan, Dyssomnia, Ender Executor, Ghastly Seeker, Ghost, Giant Mummy, Glaryad, Harpy, Hornet, Husk Girl, Imp, Jack Frost, Jiangshi, Kasha, Kobold, Lich, Magical Slime, Melty Monster, Monolith, Necrotic Reaper, Nightwalker, Ogre, Redcap, Savagefang, Scorpion, Skeleton Girl, Slime Girl, Snow Canine, Spider Nest, Stray Girl, Swamper, Wither Ghost, Wither Skeleton Girl, Zombie Girl

**Magazine Entries (33):** Same mobs prefixed with "hmag_" for detailed descriptions

**System Entries (13):** root, nffgirls_dictionary, experience, favorability, friending, interaction, tameness, and others

## Extraction Examples

### Extract Individual Mob Description
```bash
# From EN_US_BOOK_CONTENT.txt
sed -n '/^=== alraune\.txt ===/,/^=== [a-z_]*\.txt ===/p' EN_US_BOOK_CONTENT.txt | head -n -1

# From ZH_CN_BOOK_CONTENT.txt
sed -n '/^=== alraune\.txt ===/,/^=== [a-z_]*\.txt ===/p' ZH_CN_BOOK_CONTENT.txt | head -n -1
```

### Find All Entity Rendering Parameters
```bash
# From ROOT_JSON_CONTENT.txt
sed -n '/^=== alraune\.json ===/,/^=== [a-z_]*\.json ===/p' ROOT_JSON_CONTENT.txt
```

### Count Total Entries
```bash
grep "^===" EN_US_BOOK_CONTENT.txt | wc -l  # Should be 84
grep "^===" ZH_CN_BOOK_CONTENT.txt | wc -l  # Should be 84
grep "^===" ROOT_JSON_CONTENT.txt | wc -l   # Should be 88
```

## Reference Implementation

**Existing Patchouli Entry:**
```
patchouli_books/mob_dictionary/en_us/entries/friending/alraune.json
```

This file shows the correct Patchouli format and should be used as a template for all new entries.

## Migration Checklist

- [ ] Extract EN_US_BOOK_CONTENT.txt entries
- [ ] Extract ZH_CN_BOOK_CONTENT.txt entries
- [ ] Extract entity parameters from ROOT_JSON_CONTENT.txt
- [ ] Create mob entries: `patchouli_books/mob_dictionary/en_us/entries/mob/`
- [ ] Add English translation keys to `lang/en_us.json`
- [ ] Add Chinese translation keys to `lang/zh_cn.json`
- [ ] Create Chinese mob entries: `patchouli_books/mob_dictionary/zh_cn/entries/mob/`
- [ ] Create Chinese friending entries: `patchouli_books/mob_dictionary/zh_cn/entries/friending/`
- [ ] Test all entries in-game
- [ ] Verify navigation and links

## Success Criteria

✓ All 84 mobs have mob entries in Patchouli format  
✓ All 84 mobs have friending entries in Patchouli format  
✓ English and Chinese versions exist  
✓ All content properly translated  
✓ Entity rendering parameters preserved  
✓ No content loss or truncation  
✓ Book structure maintained  

## Support Files

For detailed information, see:
- **MIGRATION_SUMMARY.txt** - Full overview and checklist
- **LEGACY_BOOK_MIGRATION_DATA.md** - Structured guide
- **FILE_EXTRACTION_GUIDE.txt** - Extraction techniques

## File Sizes Summary

```
EN_US_BOOK_CONTENT.txt          67 KB
ZH_CN_BOOK_CONTENT.txt         100 KB
ROOT_JSON_CONTENT.txt           58 KB
MIGRATION_SUMMARY.txt           15 KB
FILE_EXTRACTION_GUIDE.txt       13 KB
LEGACY_BOOK_MIGRATION_DATA.md  7.3 KB
README_MIGRATION.md           (this file)
────────────────────────────────────
TOTAL                         ~260 KB
```

## Important Notes

1. **Entity ID Format**: Ensure consistency between legacy format (`entity.hmag.*`) and lang files (`entity.nffgirls.hmag_*`)

2. **Scale Adjustment**: Most mobs use scale 1.0; friending entries use 0.75. Adjust per mob as needed.

3. **Rotation Angles**: Standard rotation is rot_x=30, rot_y=225, rot_z=0. Some mobs may vary; check ROOT_JSON_CONTENT.txt.

4. **System Entries**: Items like "experience", "favorability", "interaction" are system guides and should be organized separately from mob entries.

5. **Translation Completeness**: All 84 mobs have English and Chinese translations ready for migration.

## Next Steps

1. Review MIGRATION_SUMMARY.txt for complete overview
2. Use FILE_EXTRACTION_GUIDE.txt for step-by-step extraction
3. Reference existing friending/alraune.json for Patchouli format
4. Begin migration with one mob to establish workflow
5. Use extraction guides to batch-process remaining mobs

---

**Extraction Date:** 2024-03-14  
**Legacy System:** NFF-Girls Custom Book (Citadel-based)  
**Target System:** Patchouli  
**Total Files:** 259+ files  
**Total Content:** 245 KB  

All content is ready for immediate migration. See accompanying documentation files for detailed instructions.
