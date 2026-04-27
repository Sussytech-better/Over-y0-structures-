# OverY0Structures

A small Paper plugin that moves structure spawn locations from below `y=0` up to `y=0`.

## Build

Run from the project root:

```bash
./gradlew build
```

The resulting plugin jar will be available in `build/libs/`.

## Notes

- This plugin uses runtime detection for Paper's `StructureSpawnEvent`.
- On Spigot without Paper, the hook will not be active.
