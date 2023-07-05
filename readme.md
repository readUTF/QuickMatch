## QuickMatch-Spigot

This plugin is seperated into two modes, 'minigame' and 'hub'

Hub mode registers all servers as a hub server, using the 'serverType' as a individual type of hub.
If you were to set the serverType to 'Main' this will function as a seperate hub to 'Bedwars'.
When a player joins that will be connected to a server with the same hub type.
This also enables the following commands:
* /hub


Minigame mode registers an individual minigame server, using the 'serverType' as the type of minigame.
Your own companion plugin should supply a 'MatchSupplier' implementation, which can be set within the 'MatchClientPlugin'

The velocity plugin will handle the distribution of players on join.

## QuickMatch-Velocity
The velocity plugin is a listen only plugin, it will listen to instructions from the main server.
Its main functionality is to distribute players to the correct server on join and on queue tick.
The only time this plugin communicates with the main server is when a player joins.