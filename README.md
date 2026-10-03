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

## mc1.21.1-embeddium

Requires NeoForge 21.1, Embeddium 1.0.x, Forgified Fabric API. Sodium is declared incompatible.
Tested in-game (FTB OceanBlock 2; Complementary, Photon voxy-support).

Changes over j-shelfwood:
- Sodium → Embeddium (`org.embeddedt.embeddium.impl.*`); options page via `OptionGUIConstructionEvent`
- Iris integration backported to MC 1.21.1 / Iris 1.8.12
- Shader-path fixes: double viewport setup per frame, shadow pass sharing the main viewport and
  writing the chunk-bound buffer, uninitialised shadow viewport crash
- GL state (incl. blend) restored through `GlStateManager` — 1.21.1 caches it
- `renderScale` rounding ([#555](https://github.com/MCRcortex/voxy/issues/555)), enable toggle applies/persists, logo path crash

Known: LODs cast no shadows; Photon particle transparency fix unconfirmed.

Mappings and verification notes: [CLAUDE.md](CLAUDE.md).
