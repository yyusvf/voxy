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

## mc1.21.1-embeddium

Requires NeoForge 21.1, Embeddium 1.0.x and Forgified Fabric API. Sodium is declared incompatible.
Tested in-game in FTB OceanBlock 2 with Complementary and Photon voxy-support.

Changes over j-shelfwood:
- The terrain integration targets Embeddium (`org.embeddedt.embeddium.impl.*`) instead of Sodium, and the options page uses `OptionGUIConstructionEvent`.
- The Iris integration is backported to MC 1.21.1 and Iris 1.8.12.
- With shaders, the viewport is no longer set up twice per frame, the shadow pass gets its own viewport and no longer writes the chunk-bound buffer, and the uninitialised shadow viewport no longer crashes.
- GL state, including blending, is restored through `GlStateManager`, because 1.21.1 caches it.
- `renderScale` is rounded ([#555](https://github.com/MCRcortex/voxy/issues/555)), the enable toggle applies and persists, and the logo path no longer crashes Embeddium's settings.

Known issues: LODs cast no shadows, and the Photon particle transparency fix is unconfirmed.

Mappings and verification notes are in [CLAUDE.md](CLAUDE.md).
