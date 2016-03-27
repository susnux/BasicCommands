## BasicCommands - A SpigotMC plugin

BasicCommands provides some essential functions:

 * Teleport to other users
   - Teleports player to other player, which have to allow it
   - Teleporting costs XP so it is not too over powered
 * Kill statistics
   - Show current strike and total kills.
 * Providing home functionality
 * Reward interface

### Commands:
  - ```tp PLAYER```
    * Teleports user to ```PLAYER``` if ```PLAYER``` uses ```/tpaccept```
  - ```tpaccept```
    * Accept first teleport request (allow other user to teleport to you)
  - ```tpa```
    * Alias for ```tpaccept```
  - ```tpdeny```
    * First Teleport request got declined
  - ```tpd```
    * Alias for ```tpdeny```
  - ```tpinfo```
    * Shows information about teleportation like current price (XP) and cooldown time (time until prices reduces).
  - ```sethome```
    * Set position of home to current location
  - ```home```
    * Teleports you to your home
  - ```ret```
    * Return to position where you last issued ```/home``` or ```/tp```
  - ```reward```
    * Get a reward for your work (item for exp).
  - ```kills [PLAYER]```
    * Shows number of kills in this session and total. If no PLAYER is specified your statistics are shown.

Note: Teleportation costs are increased with each teleport, but costs reduce after some time.

### Config:
Example config.yml:

    ---
    teleport:
      return:
        costs: true
        factor: 0.5
      startPrice: 30
      factor: 2
      decreaseTime: 12000
    reward:
      items:
        -
         EXP_BOTTLE: 11
        -
         SADDLE: 315
        -
         SLIME_BLOCK: 550
    commands: ["tp", "home", "kills"]

- ```startPrice``` is the price of one teleport (in XP).
- ```decreaseTime``` is the time until prices are reduces (after latest teleport) 12000 = 12 ingame hours.
- ```factor```: Factor how much prices increase per teleport.

### Permissions:

## Build

Execute:

    mvn clean install

JAR gets generated in ```./target```.
