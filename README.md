#TabbyChat 2
TabbyChat 2 is the successor to [TabbyChat](http://github.com/killjoy1221/tabbychat), which was originally created by RocketMan10404.  The mod is mostly stable, but there may still be some bugs. Report any you find.
 
Stable builds are available on the Minecraft Forums topic, linked below.

Latest builds can be found [here](https://drone.io/github.com/killjoy1221/TabbyChat-2/files). [![Build Status](https://drone.io/github.com/killjoy1221/TabbyChat-2/status.png)](https://drone.io/github.com/killjoy1221/TabbyChat-2/latest)

Discussion for this mod can happen on the Minecraft Forum [Thread](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2181597-n).

TabbyChat 2 also has an API, on top of which some optional addons will be built. Check the TabbyChat-API directory for the source references.

##Installing
**Requires LiteLoader**

After installing LiteLoader, copy both TabbyChat-2 and MnM-Utils into your `.minecraft/mods` folder. Don't extract them.

**Note:** At the time of writing, there is a bug in LiteLoader that effects Linux and Mac users. This bug causes TabbyChat's dependency check to fail. To circumvent this, instead of placing TabbyChat in the `mods` folder, put it the `mods/{mcversion}` folder. I.E. `.minecraft/mods/1.8`.

##Building
To build, run the following commands. Git is required to be installed.

**Note:** If you are on Linux or Mac, you will need to use `./gradlew` in place of `gradlew`.
```
git submodule update --init --recursive
gradlew setupCIWorkspace
gradlew build
```
Both TabbyChat and MnmUtils will be in the `build/libs` folder
