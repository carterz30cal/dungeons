# dungeons
dungeons is a custom plugin made for my server. it features custom mobs, items and crafting systems.

all the item and mob data is bundled into the .jar file. 

### prerequisites for use
- minecraft 1.16.5
- latest version of ProtocolLib 
- spigot/paper server running paper-312 or equivalent.

### setup
1. install prerequisities from their respective websites 
(www.spigotmc.org/resources/protocollib.1997 & https://papermc.io/downloads)
2. create a file called 'eula.txt' and write eula=true inside.
3a. run the server jar without any plugins to create the files
3b. delete the 'world' and nether/end world folders.
4. in 'server.properties', change level-name to 'hub' and pvp to 'true'. force-gamemode should be 'true' and gamemode should be 'survival'. 'allow-nether' should be false and in the bukkit.yml file, 'allow-end' should be false
5. put both ProtocolLib and Dungeons into the Plugins folder.
6. run the server jar again.
7. log into 'localhost'
8. you will spawn at 0, 100, 21000. you'll probably fall onto the regular world or get stuck in a mountain.
9. /op yourself and you're done.
EXTRA: you'll probably want to turn off regular mob spawning.

### what you can do
- spawn mobs using /d spawn <mobname> (/d spawn list will show a complete list of ids)
- use the menu emerald, click on the paper and then there is a complete list of items that can be obtained.
- /d level <levelnumber> to set your level.
- /d coins <coins> to add coins to your balance
- /d pvp to toggle pvp.
- go in creative and place green terracotta then mine it with a pickaxe in survival. (custom mining system)
- to see the full experience: mcexperiment.ddns.net

