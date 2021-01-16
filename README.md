TabbyChat 2
-----------
TabbyChat 2 is the successor to [TabbyChat](http://github.com/killjoy1221/tabbychat), which was originally created by RocketMan10404.  The mod is mostly stable, but there may still be some bugs. Report any you find.
 

[comment]: <> (- [Snapshot Builds]&#40;https://drone.io/github.com/killjoy1221/TabbyChat-2/files&#41;. [![Build Status]&#40;https://drone.io/github.com/killjoy1221/TabbyChat-2/status.png&#41;]&#40;https://drone.io/github.com/killjoy1221/TabbyChat-2/latest&#41;)
[comment]: <> (- [Minecraft Forums]&#40;http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2181597-n&#41;.)
[CurseForge](https://minecraft.curseforge.com/projects/tabbychat-2)

# Installing

|  MC Versions  |   Loader   | Dependencies
| ------------- | ---------- | ------------
| 1.15 - latest | Forge      | [KotlinForForge](https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge)
| 1.12          | LiteLoader
| 1.8 - 1.11    | LiteLoader | [MnMUtil](https://www.curseforge.com/minecraft/mc-mods/mnmutils)


After installing the required loader, copy the mod jar into your `.minecraft/mods` folder. Don't extract it.

If you wish to use forge on 1.10.x, the minimum version supported is build 2020. A bug was fixed in that version which was causing a crash with mixins (See [#57](/killjoy1221/TabbhChat-2/issues/57)).

# Building
To build, run the following command. Git is required to be installed.

**Note:** If you are using Bash, you will need to use `./gradlew` in place of `gradlew`.
```
gradlew build
```
TabbyChat will be in the `build/libs` folder
