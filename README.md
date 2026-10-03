# voxy 1.21.1 backport

Unofficial backport of [MCRcortex/voxy](https://github.com/MCRcortex/voxy) to Minecraft 1.21.1.
It is based on [j-shelfwood/voxy-neoforge](https://github.com/j-shelfwood/voxy-neoforge). This fork is not affiliated with upstream, so please don't report issues there.

**Source only.** Voxy is All Rights Reserved ([LICENSE.md](LICENSE.md)), so no releases or CI artifacts are published.

| Branch | Loader | Renderer | Shaders |
|---|---|---|---|
| [`mc1.21.1-embeddium`](../../tree/mc1.21.1-embeddium) | NeoForge 21.1 | Embeddium 1.0.x | Iris 1.8.12 + Monocle |
| [`mc1.21.1-fabric`](../../tree/mc1.21.1-fabric) | Fabric 0.16+ | Sodium 0.8.x | Iris 1.8.x |

## Build

Building needs JDK 21, set via `JAVA_HOME` or `org.gradle.java.home` in `~/.gradle/gradle.properties`.

```
./gradlew build             # voxy-<ver>.jar
./gradlew build -Pshaders   # voxy-shaders-<ver>.jar, Iris integration registered
```

Both jars share the mod id, so install only one. The Iris pipeline only engages for packs that ship a `voxy.json`.

## mc1.21.1-fabric

Requires Fabric Loader 0.16+, Fabric API and Sodium 0.8.x. It was converted from the NeoForge branch and builds, but has **not been tested in-game**.

- The build uses Loom on Mojang mappings.
- Sodium 0.8.12 adds a `SortBehavior` constructor parameter and a trailing `boolean` on `DefaultChunkRenderer.render`, and `parseShader` now returns a `ParsedShader`.
- Settings live on Sodium's config API page, and FREX flawless frames works.
- Loom validates the access widener, which removed 3 dead entries.

Known issue: LODs cast no shadows.

Mappings and verification notes are in [CLAUDE.md](CLAUDE.md).
