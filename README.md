# voxy 1.21.1 backport

Unofficial backport of [MCRcortex/voxy](https://github.com/MCRcortex/voxy) to Minecraft 1.21.1.
It is based on [j-shelfwood/voxy-neoforge](https://github.com/j-shelfwood/voxy-neoforge). This fork is not affiliated with upstream, so please don't report issues there.

**Source only.** Voxy is All Rights Reserved ([LICENSE.md](LICENSE.md)), so no releases or CI artifacts are published.

| Branch | Loader | Renderer | Shaders |
|---|---|---|---|
| [`mc1.21.1-embeddium`](../../tree/mc1.21.1-embeddium) | NeoForge 21.1 | Embeddium 1.0.x | Iris 1.8.12 + Monocle |
| [`mc1.21.1-fabric`](../../tree/mc1.21.1-fabric) | Fabric 0.16+ | Sodium 0.8.x | Iris 1.8.x |

## Build

You need Git and JDK 21. The first build downloads Minecraft and all dependencies, so it takes a few minutes. Later builds are much faster.

### 1. Install JDK 21

Windows (PowerShell):
```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

macOS:
```bash
brew install --cask temurin@21
```

Linux (Debian/Ubuntu):
```bash
sudo apt install openjdk-21-jdk
```

### 2. Get the source

```bash
git clone --depth 1 -b mc1.21.1-embeddium https://github.com/yyusvf/voxy.git
cd voxy
```

Without Git, click *Code* on this branch, then *Download ZIP*, unpack it and open a terminal in the extracted folder.
On Windows, keep the folder path short (for example `C:\dev\voxy`), because some source paths are deep enough to hit the Windows path length limit.

### 3. Build

Windows (PowerShell). The first line points the build at JDK 21, even if an older Java comes first on your `PATH`:
```powershell
$env:JAVA_HOME = (Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory -Filter "jdk-21*" | Select-Object -First 1).FullName
.\gradlew.bat build
```

macOS and Linux (`chmod` is only needed after a ZIP download and is harmless otherwise):
```bash
chmod +x gradlew
./gradlew build
```

For the shader variant, add `-Pshaders`, for example `.\gradlew.bat build -Pshaders` or `./gradlew build -Pshaders`.

### 4. Install

The jar ends up in `build/libs/`. A plain `build` produces `voxy-0.2.9-alpha.jar`, and `build -Pshaders` produces `voxy-shaders-0.2.9-alpha.jar`.
Copy one of them into your instance's `mods` folder and remove any other Voxy jar, because both use the same mod id.
The shader variant only changes anything with packs that ship a `voxy.json`, such as Photon's voxy-support build.

### Troubleshooting

- **"Gradle requires JVM 17 or later"** or **"Unsupported class file major version"** means the build is running on an older Java. On Windows, run the `$env:JAVA_HOME` line again in the same window. On macOS, run `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`. On Linux, run `export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64`.
- **"Filename too long"** on Windows means the folder path is too deep. Move it somewhere shorter, or run `git config --global core.longpaths true` and clone again.

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
