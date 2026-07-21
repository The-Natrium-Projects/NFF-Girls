# NFF-Girls — AI Agent Guide

> Machine-readable orientation document for AI coding agents. Describes what this
> repository is, how it is built, and how it relates to the other Natrium projects.
> **Active branch: `1.20.1-0.2.33-de-legacy`** (study this, not the default `1.20.1`).

## 1. Identity

| Field | Value |
|-------|-------|
| Name | NFF: Girls ("Enjoy your Minecraft days with monster girls!") |
| Repo | `The-Natrium-Projects/NFF-Girls` |
| Mod ID | `nffgirls` |
| Root package | `net.sodiumzh.nff.girls` |
| Maven group | `net.sodiumzh.nff.girls` |
| Language | Java 100% (Java 17 toolchain) |
| Version | `0.2.33` (`filename_version = 0.2.33-dev`) |
| License | LGPL-3.0 |
| Author | SodiumZH |

## 2. Purpose

NFF-Girls is the **content / end-user mod** of the Natrium family. It adds
monster-girl companions and related gameplay content, built on top of the
NFF-Services befriending framework and the NFU-Library utilities. This is the
top-level mod players actually install; it is the only one of the three that
ships a full, distributable, jar-in-jar bundle of its dependencies.

## 3. Dependency Position

```
NFU-Library  (base utilities)
    ^
    |
NFF-Services (befriending framework — depends on NFU-Library)
    ^
    |
NFF-Girls    (THIS repo — depends on NFU-Library + NFF-Services)
```

NFF-Girls sits at the **top of the stack** and depends on **both** lower repos.

### How dependencies are wired (`build.gradle`)

```gradle
flatDir {
    dir 'libs'
    dir '../NFU-Library/build/libs'      // sibling checkout
    dir '../NFF-Services/build/libs'     // sibling checkout
}
...
implementation fg.deobf("blank:nfulib-${minecraft_version}:${nfu_version}")
implementation fg.deobf("blank:nffservices-${minecraft_version}:${services_version}")

// bundled into the shipped jar via JarJar:
jarJar(group: 'net.sodiumzh', name: 'nffservices', version: "[...]") { ... }
jarJar(group: 'net.sodiumzh', name: 'nfulib',      version: "[...]") { ... }
```

`gradle.properties`:
```
nfu_version=0.2.33-dev            nfu_version_range=[0.2.33, 0.2.34)
services_version=0.2.33-dev       services_version_range=[0.2.33, 0.2.34)
```

**Build order:** NFU-Library → NFF-Services → NFF-Girls, each checked out as a
sibling directory so the flat-dir paths resolve.

## 4. Tech Stack

- **Loader:** Minecraft Forge `47.1.44` for **Minecraft 1.20.1**.
- **Build:** Gradle + ForgeGradle `[6.0,6.2)`, ParchmentMC mappings
  (`2023.06.26-1.20.1`), Java 17.
- **Mixins:** SpongePowered Mixin + MixinExtras `0.3.1`
  (refmap `nffgirls.refmap.json`, config `nffgirls.mixins.json`).
- **JarJar:** bundles `nfulib` and `nffservices` into the final jar.
- **Third-party integrations** (via CurseMaven / deobf):
  JEI, Jade, JER (Just Enough Resources), Patchouli (`84.1`, guidebook),
  HMAG (`[9.0.20,10.0)`), Twilight Forest, Citadel, Ice and Fire, Touhou Little Maid.
- Publishes via `maven-publish` to a local `mcmodsrepo`.
- NOTE: `gradle.properties` sets an HTTP proxy (`127.0.0.1:7897`) — remove/adjust
  in CI environments without that proxy.

## 5. Source Layout

Root package `src/main/java/net/sodiumzh/nff/girls/`:

| Package | Responsibility |
|---------|----------------|
| `NFFGirls.java` | Mod entry point (`@Mod("nffgirls")`). |
| `block` | Custom blocks. |
| `client` | Client rendering, models, screens. |
| `compat` | Integrations with the third-party mods above. |
| `data` | Data generation / datapack content. |
| `effect` | Custom mob effects. |
| `entity` | Monster-girl entities (built on NFF-Services taming framework). |
| `eventlistener` | Forge event listeners. |
| `inventory` | Companion inventories/menus. |
| `item` | Custom items. |
| `jei` | JEI plugin integration. |
| `mixin` | Mixins (`nffgirls.mixins.json`). |
| `network` | Packets. |
| `recipe` | Custom recipes/recipe types. |
| `registry` | Deferred registration of all content. |
| `sound` | Sound events. |
| `util` | Utilities. |

## 6. Notes for Agents

- This mod **consumes** the APIs of NFU-Library and NFF-Services; when those
  APIs change, update the corresponding usages here and bump the version ranges
  in `gradle.properties`.
- New content should be registered through the `registry` package following the
  Forge DeferredRegister pattern.
- Version/proxy/dependency-range settings live in `gradle.properties`; prefer
  editing properties over hardcoded values (they feed `processResources`).
- The default GitHub branch is `1.20.1`; active development is on
  `1.20.1-0.2.33-de-legacy`. Always target the active branch.
