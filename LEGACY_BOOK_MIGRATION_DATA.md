# Legacy Book Files to Patchouli Format Migration Data

## 1. EN_US BOOK FILES (84 files total)

### Directory: `/home/runner/work/NFF-Girls/NFF-Girls/src/main/resources/assets/nffgirls/book/mob_dictionary/en_us/`

All files are `.txt` format. Complete filenames:

**Regular Mob Entries (51 files):**
- alraune.txt
- banshee.txt
- catoblepas.txt
- creeper_girl.txt
- crimson_slaughterer.txt
- cursed_doll.txt
- dodomeki.txt
- dogu.txt
- drowned_girl.txt
- dullahan.txt
- dyssomnia.txt
- ender_executor.txt
- experience.txt
- favorability.txt
- fortress_keeper.txt
- friending.txt
- ghastly_seeker.txt
- ghost.txt
- giant_mummy.txt
- glaryad.txt
- harpy.txt
- hornet.txt
- husk_girl.txt
- imp.txt
- interaction.txt
- jack_frost.txt
- jiangshi.txt
- kasha.txt
- kobold.txt
- lich.txt
- magical_slime.txt
- melty_monster.txt
- monolith.txt
- necrotic_reaper.txt
- nightwalker.txt
- nffgirls_dictionary.txt
- ogre.txt
- redcap.txt
- root.txt
- savagefang.txt
- scorpion.txt
- skeleton_girl.txt
- slime_girl.txt
- snow_canine.txt
- spider_nest.txt
- stray_girl.txt
- swamper.txt
- wither_ghost.txt
- wither_skeleton_girl.txt
- zombie_girl.txt

**HMaG Magazine Entries (33 files):**
- hmag_alraune.txt
- hmag_banshee.txt
- hmag_creeper_girl.txt
- hmag_crimson_slaughterer.txt
- hmag_cursed_doll.txt
- hmag_dodomeki.txt
- hmag_drowned_girl.txt
- hmag_dullahan.txt
- hmag_ender_executor.txt
- hmag_ghastly_seeker.txt
- hmag_glaryad.txt
- hmag_harpy.txt
- hmag_hornet.txt
- hmag_husk_girl.txt
- hmag_imp.txt
- hmag_jack_frost.txt
- hmag_jiangshi.txt
- hmag_kobold.txt
- hmag_melty_monster.txt
- hmag_necrotic_reaper.txt
- hmag_nightwalker.txt
- hmag_redcap.txt
- hmag_skeleton_girl.txt
- hmag_slime_girl.txt
- hmag_snow_canine.txt
- hmag_stray_girl.txt
- hmag_wither_skeleton_girl.txt
- hmag_zombie_girl.txt

**Content saved to:** `EN_US_BOOK_CONTENT.txt` (67 KB)

---

## 2. ZH_CN BOOK FILES (84 files total)

### Directory: `/home/runner/work/NFF-Girls/NFF-Girls/src/main/resources/assets/nffgirls/book/mob_dictionary/zh_cn/`

Same filename structure as en_us, all `.txt` format.

**Content saved to:** `ZH_CN_BOOK_CONTENT.txt` (100 KB)

---

## 3. ROOT JSON FILES (88 files total)

### Directory: `/home/runner/work/NFF-Girls/NFF-Girls/src/main/resources/assets/nffgirls/book/mob_dictionary/`

All files are `.json` format containing mob stats and item drops:

**JSON Files:**
- alraune.json - 435 bytes
- banshee.json - 435 bytes
- catoblepas.json - 498 bytes
- creeper_girl.json - 450 bytes
- crimson_slaughterer.json - 471 bytes
- cursed_doll.json - 447 bytes
- dodomeki.json - 438 bytes
- dogu.json - 480 bytes
- drowned_girl.json - 450 bytes
- dullahan.json - 438 bytes
- dyssomnia.json - 495 bytes
- ender_executor.json - 456 bytes
- experience.json - 298 bytes
- favorability.json - 310 bytes
- fortress_keeper.json - 513 bytes
- friending.json - 304 bytes
- ghastly_seeker.json - 456 bytes
- ghost.json - 483 bytes
- giant_mummy.json - 501 bytes
- glaryad.json - 435 bytes
- harpy.json - 429 bytes
- (+ 33 hmag_*.json files)
- (+ interaction.json, nffgirls_dictionary.json, root.json, tameness.json)

**Content saved to:** `ROOT_JSON_CONTENT.txt` (58 KB)

**Note:** `nffgirls_dictionary.json` (9.3 KB) and `root.json` (12.4 KB) are larger and contain root book information.

---

## 4. EXISTING PATCHOULI ENTRIES

### Directory: `/home/runner/work/NFF-Girls/NFF-Girls/src/main/resources/assets/nffgirls/patchouli_books/mob_dictionary/en_us/entries/`

**Friending Entry Found:**
- `friending/alraune.json` - References: `dict.nffgirls.desc.friending.alraune`

**File Content:**
```json
{
  "category": "nffgirls:mobs",
  "name": "hmag:alraune",
  "icon": "nffgirls:textures/book/icon_alraune.png",
  "pages": [
    {
      "type": "patchouli:entity",
      "entity": "hmag:alraune",
      "scale": 0.75
    },
    {
      "type": "patchouli:text",
      "text": "dict.nffgirls.desc.friending.alraune"
    }
  ]
}
```

**Note:** No mob entries found at `entries/mob/alraune.json` (path does not exist), but friending entry exists showing the Patchouli structure format.

---

## 5. TRANSLATION FILES

### Directory: `/home/runner/work/NFF-Girls/NFF-Girls/src/main/resources/assets/nffgirls/lang/`

**Available translation files:**
- `en_us.json` - English translations (314 lines)
- `zh_cn.json` - Simplified Chinese translations
- `__ja_jp.json` - Japanese translations (prefixed with __)

**Translation Key Pattern:**
- Mob entries: `dict.nffgirls.desc.mob.<mobname>` for descriptions
- Friending entries: `dict.nffgirls.desc.friending.<mobname>` for friending info
- Entity names: `entity.nffgirls.hmag_<mobname>`

**Example from en_us.json:**
```json
{
  "entity.nffgirls.hmag_alraune": "Alraune",
  "entity.nffgirls.hmag_banshee": "Banshee",
  "entity.nffgirls.hmag_creeper_girl": "Creeper Girl",
  ...
}
```

---

## 6. JSON FILE STRUCTURE (Legacy Format)

Each mob JSON file follows this pattern (from root.json):
```json
{
  "parent": "root.json",
  "text": "alraune.txt",
  "title": "entity.hmag.alraune",
  "linked_page_buttons": [],
  "images": [],
  "item_renders": [],
  "recipes": [],
  "tabula_renders": [],
  "entity_renders": [
    {
      "entity": "hmag:alraune",
      "x": 92,
      "y": 102,
      "page": 0,
      "scale": 1.0,
      "rot_x": 30,
      "rot_y": 225,
      "rot_z": 0,
      "follow_cursor": false
    }
  ]
}
```

**Key fields:**
- `parent`: References parent book file (root.json for mobs)
- `text`: Path to txt file with content (format: filename.txt)
- `title`: Translation key for mob title (entity.hmag.<mobname>)
- `entity_renders`: Entity display information
  - x, y: Position on page
  - page: Which page to display on
  - scale: Size of entity render
  - rot_x, rot_y, rot_z: Rotation angles
  - follow_cursor: Whether entity follows mouse

---

## 7. ROOT BOOK STRUCTURE

**root.json (12.4 KB):** Main book definition
**nffgirls_dictionary.json (9.3 KB):** Dictionary metadata

Both contain hierarchy and navigation information for the legacy book system.

---

## 8. CONTENT OVERVIEW

All content files have been extracted and saved:

1. **EN_US_BOOK_CONTENT.txt** - Complete English text for all 84 files
2. **ZH_CN_BOOK_CONTENT.txt** - Complete Chinese text for all 84 files  
3. **ROOT_JSON_CONTENT.txt** - All root JSON files with mob metadata

Each file starts with `=== filename ===` delimiter for easy parsing.

---

## 9. MIGRATION NOTES

**Current Patchouli Structure Found:**
- Friending entries follow pattern: `patchouli_books/mob_dictionary/en_us/entries/friending/<mobname>.json`
- Uses Patchouli text types: `patchouli:entity`, `patchouli:text`
- Translatable content via `lang/*.json` files with `dict.nffgirls.*` keys

**Files to Create for Migration:**
1. `patchouli_books/mob_dictionary/en_us/entries/mob/<mobname>.json` - Main mob entry
2. `patchouli_books/mob_dictionary/zh_cn/entries/mob/<mobname>.json` - Chinese mob entry
3. `patchouli_books/mob_dictionary/zh_cn/entries/friending/<mobname>.json` - Chinese friending entry
4. Update `lang/en_us.json` and `lang/zh_cn.json` with new translation keys

---

## 10. FILE COUNT SUMMARY

- **en_us/ directory:** 84 .txt files
- **zh_cn/ directory:** 84 .txt files
- **Root directory:** 88 .json files
- **Translation files:** 3 .json files (en_us.json, zh_cn.json, __ja_jp.json)
- **Existing Patchouli entries:** At least 1 friending entry per mob type

**Total legacy book files: 259+ files to migrate**

