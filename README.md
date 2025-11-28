![Demonstration of different sizes while also being a neat banner :D](https://cdn.modrinth.com/data/6uXW27tQ/images/6ec55b6d75f2bfa966ae144dad1f7fa63d62e1d6.png)
\
![context break](https://cdn.modrinth.com/data/cached_images/a83711ea404ca28bcc5bc82334da3848c952dc27.png)
\
![Mod Info](https://cdn.modrinth.com/data/cached_images/7eee334d8ef5be6adc1414f9129ec3e91b9c9c5b.png)
\
Size Matters is a roleplay-centric mod that adds the unique trait of height to the game.

Upon joining a world for the first time, players will be assigned a random permanent height ranging from 70% to 130% of normal height.

There are also options for having player height be dependent on a player's UUID, causing the player to have the same height no matter what server they join, as long as the option is enabled on that server.

![context break](https://cdn.modrinth.com/data/cached_images/a83711ea404ca28bcc5bc82334da3848c952dc27.png)
\
![Technical Info](https://cdn.modrinth.com/data/cached_images/1c2e3592134456fc6d8bb3f8e60b98d0fdcfa639.png)
When joining a server the mod:
1. Checks to see if the player already has data (if no continue if yes skip to step 4)
2. Generates a random number between 0.3 and -0.3 (clamped to the 2nd decimal)
3. Saves that number in the players unique user data (sizematters.size_data:player_height)
4. Applies the number as a modifier to the base player height

![context break](https://cdn.modrinth.com/data/cached_images/a83711ea404ca28bcc5bc82334da3848c952dc27.png)
\
![Requirements](https://cdn.modrinth.com/data/cached_images/c9927dc908799f8c9ab4ec53940642cb3c9d42de.png)
While this mod isn't required on the client side it is dependant on and requires:
- [Fabric API](https://modrinth.com/mod/fabric-api) (Client and Server)
- [owo-lib](https://modrinth.com/mod/owo-lib) (Server Only)
