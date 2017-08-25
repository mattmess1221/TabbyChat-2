TabbyChat 2
-----------
TabbyChat 2 is the successor to [TabbyChat](http://github.com/killjoy1221/tabbychat), which was originally created by RocketMan10404.  The mod is mostly stable, but there may still be some bugs. Report any you find.
 
Stable builds are available on the Minecraft Forums topic and CurseForge, linked below.

# Links
- [Snapshot Builds](https://drone.io/github.com/killjoy1221/TabbyChat-2/files). [![Build Status](https://drone.io/github.com/killjoy1221/TabbyChat-2/status.png)](https://drone.io/github.com/killjoy1221/TabbyChat-2/latest)
- [Minecraft Forums](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2181597-n).
- [CurseForge](https://minecraft.curseforge.com/projects/tabbychat-2)

TabbyChat 2 also has an API. Check the `mnm.mods.tabbychat.api` package for the source references.

# Installing
**Requires LiteLoader**

After installing LiteLoader, copy TabbyChat-2 into your `.minecraft/mods` folder. Don't extract it.

If you wish to use forge on 1.10.x, the minimum version supported is build 2020. A bug was fixed in that version which was causing a crash with mixins (See [#57](../../issues/57)).

# Building
To build, run the following commands. Git is required to be installed.

**Note:** If you are on Linux or Mac, you will need to use `./gradlew` in place of `gradlew`.
```
gradlew setupCIWorkspace
gradlew build
```
TabbyChat will be in the `build/libs` folder
