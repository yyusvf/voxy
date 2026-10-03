# voxy — 1.21.1 backport

Unofficial backport of [MCRcortex/voxy](https://github.com/MCRcortex/voxy) to Minecraft 1.21.1.
Based on [j-shelfwood/voxy-neoforge](https://github.com/j-shelfwood/voxy-neoforge). Not affiliated with upstream — don't report issues there.

**Source only.** Voxy is All Rights Reserved ([LICENSE.md](LICENSE.md)); no releases or CI artifacts are published.

| Branch | Loader | Renderer | Shaders |
|---|---|---|---|
| [`mc1.21.1-embeddium`](../../tree/mc1.21.1-embeddium) | NeoForge 21.1 | Embeddium 1.0.x | Iris 1.8.12 + Monocle |
| [`mc1.21.1-fabric`](../../tree/mc1.21.1-fabric) | Fabric 0.16+ | Sodium 0.8.x | Iris 1.8.x |

## Build

JDK 21 (`JAVA_HOME`, or `org.gradle.java.home` in `~/.gradle/gradle.properties`).

```
./gradlew build             # voxy-<ver>.jar
./gradlew build -Pshaders   # voxy-shaders-<ver>.jar, Iris integration registered
```

Same mod id — install one. The Iris pipeline only engages for packs shipping `voxy.json`.

## mc1.21.1-fabric

Requires Fabric Loader 0.16+, Fabric API, Sodium 0.8.x. Converted from the NeoForge branch; builds, **not tested in-game**.

- Loom on Mojang mappings
- Sodium 0.8.12 deltas: `SortBehavior` ctor param, extra `boolean` on `DefaultChunkRenderer.render`, `ParsedShader.src()`
- Sodium config API settings page; FREX flawless frames
- Access widener validated (3 dead entries removed)

Known: LODs cast no shadows.

Mappings and verification notes: [CLAUDE.md](CLAUDE.md).
