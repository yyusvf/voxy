# Voxy NeoForge 1.21.1 Port - Project Context

## Project Overview

This is a **NeoForge 1.21.1 port** of the Voxy mod (originally Fabric).

**Target Platform**: NeoForge 21.1.x for Minecraft 1.21.1
**Source**: Fabric Voxy mod
**Goal**: Full compatibility with NeoForge modding ecosystem

## Critical Development Rules

### 1. Reference-First Research

**ALWAYS** research cloned GitHub repositories before making changes:

```
.reference/
├── minecraft/1.21.1/decompiled/    # MC 1.21.1 decompiled sources
├── sodium/0.6.13/                   # Sodium bytecode/sources
└── [other reference repos]
```

**Process**:
1. Identify the issue (mixin signature, API change, etc.)
2. Search `.reference/` repositories for actual source code
3. Verify method signatures, class structures, field types
4. Apply fix based on **verified evidence**, not assumptions
5. Document evidence in commit/comments

**NO GUESSING** - Every change must be backed by source code verification.

### 2. Validation Before Changes

Before modifying any file:
1. Read the current implementation
2. Find corresponding reference in `.reference/` repos
3. Verify the proposed change matches the reference
4. Test build after changes

### 3. Dependency Versions

**Current validated versions** (for NeoForge 1.21.1):
- NeoForge: 21.1.217
- Minecraft: 1.21.1
- Embeddium: 1.0.15+mc1.21.1 (**replaces Sodium** - see "Embeddium instead of Sodium" below)
- Lithium: mc1.21.1-0.15.1-neoforge
- Forgified Fabric API: Check Modrinth/CurseForge for correct 1.21.1 version

**IMPORTANT**: Verify all dependency versions against actual releases on Modrinth/CurseForge for NeoForge 1.21.1 specifically.

## Key Files

### Build Configuration
- `build.gradle` - Dependencies, build settings
- `gradle.properties` - Version numbers
- `src/main/resources/META-INF/neoforge.mods.toml` - Mod metadata and dependencies
- `src/main/resources/META-INF/accesstransformer.cfg` - Access transformers

### Mixin Configurations
- `src/main/resources/client.voxy.mixins.json` - Client-side mixins
- `src/main/resources/common.voxy.mixins.json` - Common mixins

## Porting Considerations

### Fabric → NeoForge Differences
1. **Mod metadata**: fabric.mod.json → neoforge.mods.toml
2. **Access wideners**: .accesswidener → accesstransformer.cfg
3. **Dependencies**: Must be explicitly declared in neoforge.mods.toml
4. **Entrypoints**: Fabric entrypoints don't work on NeoForge
5. **Mixin remapping**: Different obfuscation system

### Forgified Fabric API
- Provides Fabric API compatibility layer on NeoForge
- Mod ID is `fabric_api` (not `forgified_fabric_api`)
- Bundles `forgified-fabric-loader` via JarJar
- Version numbers may differ from Fabric releases

## Validation Workflow

1. **Extract reference sources** to `.reference/` directory
2. **Validate mixin signatures** against decompiled MC/mod sources
3. **Validate Access Transformers** against actual class structures
4. **Test build** after every significant change
5. **Document fixes** with evidence from reference sources

## Embeddium instead of Sodium

This port targets **Embeddium** (`org.embeddedt.embeddium.*`), not Sodium
(`net.caffeinemc.mods.sodium.*`), so it can run in packs where Embeddium is mandatory.
Embeddium 1.0.x is a fork of Sodium 0.6 with relocated packages; it does **not** ship any
`net.caffeinemc.mods.sodium.*` classes, so mods compiled against Sodium fail silently there.

Package mapping:

| Sodium 0.6.13 | Embeddium 1.0.15 |
|---|---|
| `net.caffeinemc.mods.sodium.client.*` | `org.embeddedt.embeddium.impl.*` |
| `...client.render.SodiumWorldRenderer` | `...impl.render.EmbeddiumWorldRenderer` |
| `...client.util.color.ColorSRGB` | `...impl.util.color.ColorSRGB` |
| `...client.gui.options.*` (0.5-era API) | `org.embeddedt.embeddium.api.options.*` |
| `...api.config.*` (0.6 config API) | no equivalent - use `OptionGUIConstructionEvent` |

Behavioural differences that required code changes (verified with `javap` against the
Embeddium jar - always re-verify when bumping the Embeddium version):

- `RenderSectionManager`'s `ClientLevel` field is named `world`, not `level`
- `RenderSection.setInfo(BuiltSectionInfo)` returns `void`, not `boolean`
- `RenderRegionManager` has no `uploadResults()` and no chunk fade-in timer
  (`MixinRenderRegionManager` is therefore excluded from compilation)
- Embeddium requires an explicit `OptionIdentifier` on every option and option group

Verification workflow used for this port: extract the Embeddium jar and run
`javap -p` / `javap -c` on each mixin target to confirm class names, field names, method
signatures and injection points before editing.

## Known Issues (NeoForge Port)

Track porting-specific issues here. Pre-existing bugs in the original Fabric mod are out of scope.
