# Voxy for Minecraft 1.21.1 — NeoForge (Embeddium) & Fabric

> **Unofficial, community-maintained fork** of [MCRcortex/voxy](https://github.com/MCRcortex/voxy).
> Not affiliated with or endorsed by MCRcortex. Please do **not** report issues from this fork upstream.

Voxy is a level-of-detail (LOD) renderer that draws terrain far beyond the vanilla render distance.
Upstream Voxy jumps from Minecraft 1.20.4 straight to 1.21.6 and never supported 1.21.1 — this fork
is a backport for 1.21.1, on both loaders, including shader-pack support.

## No binaries

Voxy is **All Rights Reserved** by MCRcortex (see [LICENSE.md](LICENSE.md): *"Do not redistribute."*).
This repository therefore publishes **source only** — no releases, no CI artifacts. Build it yourself
(see below). Rights-holder concerns: please open an issue.

## Credits

| | |
|---|---|
| **[MCRcortex](https://github.com/MCRcortex)** | Author of Voxy. All credit for the mod itself goes here. |
| **[Joris Schelfhout](https://github.com/j-shelfwood)** | The NeoForge 1.21.1 port this fork builds on — [j-shelfwood/voxy-neoforge](https://github.com/j-shelfwood/voxy-neoforge). His commits are preserved in this history. |
| **this fork** | Embeddium retarget, Iris shader backport for 1.21.1, the Fabric port, and the fixes listed below. |

## Branches

| Branch | Loader | Terrain renderer | Shaders (optional) |
|---|---|---|---|
| [`mc1.21.1-embeddium`](../../tree/mc1.21.1-embeddium) | NeoForge 21.1 | **Embeddium** 1.0.x | Iris 1.8.12 via Monocle |
| [`mc1.21.1-fabric`](../../tree/mc1.21.1-fabric) | Fabric Loader 0.16+ | **Sodium** 0.8.x | Iris 1.8.x |

## Building

Requires a **JDK 21**. Point Gradle at it with `JAVA_HOME`, or — if another Java is first on your
`PATH` — add `org.gradle.java.home=/path/to/jdk-21` to `~/.gradle/gradle.properties` (keep machine
paths out of the repository's own `gradle.properties`).

```
./gradlew build             # build/libs/voxy-<version>.jar
./gradlew build -Pshaders   # build/libs/voxy-shaders-<version>.jar  (Iris integration enabled)
```

Both jars use the mod id `voxy` — put **only one** of them in your mods folder.
The shader integration only activates with packs that ship Voxy support (`voxy.json`, e.g. Photon's
voxy-support build). With any other pack Voxy falls back to its normal pipeline.

## This branch: Fabric + Sodium

**Requires:** Fabric Loader 0.16+ · Fabric API · **Sodium 0.8.x** (0.8 is the API generation upstream
Voxy targets, so the Sodium side needs almost no backporting).
Settings: Sodium *Video Settings → Voxy* (Sodium 0.8's config API).

**Status:** compiles and builds both variants; **not yet tested in-game.** Reports welcome.

### How it was made
Converted from the NeoForge branch rather than re-backported from upstream: the expensive part is the
MC 1.21.11 → 1.21.1 backport, which is shared — only ~6% of files are loader-specific.
- Loom build on **Mojang mappings** (the tree uses `LevelRenderer`/`ClientLevel`, not Yarn).
- Sodium 0.8.12: `RenderSectionManager` gained `SortBehavior`, `DefaultChunkRenderer.render` a
  trailing `boolean`, `ShaderParser.parseShader` returns `ParsedShader`.
- Fabric entrypoints, client commands on `FabricClientCommandSource`, fog via `MixinFogRenderer`.
- FREX flawless frames works again (needs Fabric's entrypoint mechanism).
- Access widener validated by Loom; three dead entries removed that the NeoForge access transformer
  had carried unnoticed.

### Known limitations
- LODs do not cast shadows in shader packs (there is no shadow-pass viewport setup).

## Development notes

[CLAUDE.md](CLAUDE.md) documents the API mappings and the verification workflow: every mixin target is
checked with `javap` against the actual dependency jar before editing. The build also runs a
*port stub inventory* (`scripts/validate_port_stubs.py`) that flags newly introduced `if (false)` /
`&& !false` branches — several bugs here came from upstream code paths silently stubbed out while
porting.
