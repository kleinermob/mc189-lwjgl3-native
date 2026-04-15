# Minecraft 1.8.9 — Native LWJGL3 Port

A native port of Minecraft 1.8.9 (MCP) from LWJGL2 to LWJGL3, built with Maven and Java 17.
LWJGL2 compatibility shims (`org.lwjgl.input.Mouse`, `Keyboard`, etc.) are included in-tree so the original MC code runs on modern LWJGL3 + GLFW.

## Prerequisites

- **Java 17+** (e.g. [Eclipse Temurin](https://adoptium.net/))
- **Python 3.6+** (for asset download script only)

Maven is **not** required — the included Maven Wrapper (`mvnw`) handles it automatically.

## Quick Start

```bash
# 1. Clone
git clone <repo-url> && cd mc189-lwjgl3-native

# 2. Download Minecraft 1.8 assets (~200 MB, one-time)
python scripts/download_assets.py

# 3. Compile and run
./mvnw compile exec:java        # Linux / macOS
mvnw.cmd compile exec:java      # Windows
```

The game's working directory is `test_run/`. Saves, resource packs, and configs go there.

## Building a JAR

```bash
./mvnw clean package
```

Output in `target/`. The fat JAR (`*-jar-with-dependencies.jar`) includes all dependencies.

## Project Structure

```
src/main/java/
├── net/minecraft/        # Decompiled MC 1.8.9 source
├── org/lwjgl/input/      # LWJGL2 compat shims (Mouse, Keyboard)
└── ...
src/main/resources/       # Assets, shaders, fonts
test_run/                 # Game working directory
scripts/                  # Utility scripts
.mvn/                     # Maven wrapper config + JVM flags
```

## Cross-Platform

LWJGL3 native libraries are resolved automatically via Maven profiles:
- **Windows** — auto-detected
- **Linux** — auto-detected
- **macOS** — auto-detected (includes ARM64/Apple Silicon)

## Known Limitations

- **Sound** — `SoundManager` is stubbed (no audio). The original paulscode dependency is not available.
- **Twitch/Realms** — Stream and Realms integrations are non-functional stubs.
- **macOS** — Untested. May need `-XstartOnFirstThread` JVM flag.

## JVM Flags

The required `--add-opens` flags for Java 17 are configured in `.mvn/jvm.config` and applied automatically when using `mvnw`.

If running manually:
```bash
java --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.io=ALL-UNNAMED \
     --add-opens java.base/java.nio=ALL-UNNAMED \
     --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
     --add-opens java.base/java.net=ALL-UNNAMED \
     -cp <classpath> net.minecraft.client.main.Main \
     --username TestUser --version MavenMCP --accessToken 0 \
     --assetsDir assets --assetIndex 1.8 --userProperties {}
```

## Credits

Based on [MavenMCP 1.8.9](https://github.com/nicholasc120/MavenMCP-1.8.9) by marCloud.