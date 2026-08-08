# Voxy NeoForge 1.21.1

> **Unofficial NeoForge port** of the Voxy mod

## Special Thanks

**All credit for Voxy goes to [MCRcortex](https://github.com/MCRcortex)**, the original author and creator of this incredible LOD rendering mod.

- **Original Repository:** [MCRcortex/voxy](https://github.com/MCRcortex/voxy)
- **Original Author:** [MCRcortex](https://github.com/MCRcortex)

This repository is a community port to NeoForge 1.21.1, created because the original author has indicated they will not be backporting to this version. We are deeply grateful for MCRcortex's work on Voxy.

## License Notice

The original Voxy mod is licensed under **All Rights Reserved** by MCRcortex. This port is provided for personal use. Please respect the original author's licensing terms.

---

## About

**Voxy** is a Level-of-Detail (LOD) rendering mod for Minecraft that extends your view distance far beyond vanilla limits by rendering distant terrain at lower detail levels.

## Why This Port?

You might wonder: "Why not just use the Fabric version with [Sinytra Connector](https://github.com/Sinytra/Connector)?"

| Aspect | Native NeoForge Port (this repo) | Sinytra Connector |
|--------|----------------------------------|-------------------|
| **Performance** | No translation overhead | Runtime translation layer |
| **Mod Integration** | Native NeoForge API calls | Fabric API emulation via FFAPI |
| **Maintenance** | Must track upstream Voxy changes | Just drop in Fabric jar |
| **Stability** | Tested against NeoForge directly | May have edge cases from translation |
| **Dependencies** | Forgified Fabric API | Connector + Forgified Fabric API |

**Bottom line:** For a performance-critical LOD mod like Voxy, eliminating the translation layer overhead is worthwhile. If you prioritize simplicity and don't mind potential overhead, Sinytra Connector is a valid alternative.

## Status

**Alpha** - Functional with known limitations.

### Working Features
- LOD terrain rendering beyond vanilla render distance
- Smooth transitions between LOD and vanilla chunks
- Fog integration (disabled at LOD boundaries)
- Block model baking for all render types (solid, cutout, cutout_mipped, translucent)
- Delayed chunk unloading to prevent pop-out effects

### Current Limitations
- Requires Embeddium 1.0.x (NeoForge). This build integrates with Embeddium instead of Sodium,
  so it can be used in packs that mandate Embeddium. Sodium is declared incompatible.
- Some optional integrations not yet ported (Iris, Nvidium, Vivecraft)
- Debug screen integration disabled (MC 1.21.1 API changes)

## Requirements

### Required Dependencies

| Dependency | Version | Link |
|------------|---------|------|
| Minecraft | 1.21.1 | - |
| NeoForge | 21.1.x | [NeoForge](https://neoforged.net/) |
| Embeddium | 1.0.15+mc1.21.1 (1.0.x) | [Modrinth](https://modrinth.com/mod/embeddium/version/1.0.15+mc1.21.1) |
| Forgified Fabric API | 0.116.7+2.2.0+1.21.1 | [Modrinth](https://modrinth.com/mod/forgified-fabric-api/version/0.116.7+2.2.0+1.21.1) |

Sodium must **not** be installed; Embeddium and Sodium are mutually exclusive.
Voxy's settings appear as a "Voxy" page inside Embeddium's Video Settings screen.

### Recommended Dependencies

| Dependency | Purpose | Link |
|------------|---------|------|
| Lithium | General performance improvements | [Modrinth](https://modrinth.com/mod/lithium) |

## Installation

> **Note:** Due to Voxy's ARR (All Rights Reserved) license, compiled JARs are not distributed. You must build from source.

1. Install NeoForge for Minecraft 1.21.1
2. Install required dependencies (see above)
3. Build Voxy from source (see below)
4. Place the built JAR in your `mods` folder

## Building from Source

```bash
git clone https://github.com/j-shelfwood/voxy-neoforge.git
cd voxy-neoforge
./gradlew build
```

The built JAR will be in `build/libs/`.

## Contributing

For development guidelines, see [CLAUDE.md](CLAUDE.md).

### Validation Scripts

The `scripts/` directory contains build validation tools used in CI.

## Links

- **Original Voxy:** [github.com/MCRcortex/voxy](https://github.com/MCRcortex/voxy)
- **This Port:** [github.com/j-shelfwood/voxy-neoforge](https://github.com/j-shelfwood/voxy-neoforge)
- **Sinytra Connector (alternative):** [github.com/Sinytra/Connector](https://github.com/Sinytra/Connector)
