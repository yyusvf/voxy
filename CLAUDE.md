# Voxy Fabric 1.21.1 Port - Project Context

## Project Overview

This is a **NeoForge 1.21.1 port** of the Voxy mod (originally Fabric).

**Target Platform**: Fabric for Minecraft 1.21.1
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

## Relationship to the NeoForge/Embeddium branch

This tree was converted from the NeoForge 1.21.1 + Embeddium port, not backported from upstream
again: the expensive part is the MC 1.21.11 -> 1.21.1 backport (renderLevel signature, fog system,
GlStateManager state cache, Sodium 0.8 -> 0.6 API), and that was already done and field-tested
there. Only ~6% of files were loader-specific.

Differences from that branch:

| NeoForge branch | here |
|---|---|
| Embeddium 1.0.15 (`org.embeddedt.embeddium.impl.*`) | Sodium 0.6.13 (`net.caffeinemc.mods.sodium.client.*`) |
| `EmbeddiumWorldRenderer` | `SodiumWorldRenderer` |
| `RenderSection.setInfo` returns void | returns **boolean**, and updateSectionInfo branches on it |
| `RenderSectionManager` field `world` | field `level` |
| `ModList` / `LoadingModList` / `FMLPaths` | `FabricLoader` |
| `@Mod` class + NeoForge config screen | `ClientModInitializer` / `ModInitializer` entrypoints |
| `ViewportEvent.RenderFog` (VoxyClientEvents) | `MixinFogRenderer` writing RenderSystem shader fog |
| `RegisterClientCommandsEvent`, `CommandSourceStack` | `ClientCommandRegistrationCallback`, `FabricClientCommandSource` |
| accesstransformer.cfg | voxy.accesswidener |
| FREX flawless frames unavailable | available (Fabric entrypoint mechanism) |

Sodium version note: 0.6.13 is what this tree's API generation targets. Sodium 0.8.x also exists
for MC 1.21.1, but has a different internal API (and is what upstream 0.2.9 targets) - moving to it
would mean undoing the Sodium backport rather than a version bump.

**Sodium 0.6.13 has no config API** (`net.caffeinemc.mods.sodium.api.config`, added in 0.7), so
`SodiumConfigBuilder`, `VoxyConfigMenu`, `IConfigPageSetter` and `MixinVideoSettingsScreen` stay
excluded and there is currently no in-game settings GUI.

## Build

- `./gradlew build` - normal jar
- `./gradlew build -Pshaders` - separate `voxy-shaders-*.jar` with the Iris integration registered

Loom validates the access widener strictly. That caught three dead entries the NeoForge
accesstransformer carried unnoticed (its validation is switched off): `SpriteContents.mipmapStrategy`
and `TextureAtlas.maxMipLevel` do not exist on 1.21.1, and `GameRenderer.getFov` returns double
here, not float. All three were unused.

## Known Issues (NeoForge Port)

Track porting-specific issues here. Pre-existing bugs in the original Fabric mod are out of scope.
