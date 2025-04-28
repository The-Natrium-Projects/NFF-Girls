# Change Log

### 0.2.29.2

Modified Mob Dictionary to match 0.2.29 features.

### 0.2.29.1

Fixed "removing equipment on friended" configs not taking effect.

### 0.x.29

Note: this version has trade data format change, and related datapacks will stop working. To port old datapack format to new version, rename the path `nffgirls/vanilla_trade_listings` to `nffgirls/trades`, and use `trade_registry.json` to add new trade collections to mobs (see source for details).

Added mob searching feature for Commanding Wand. Right clicking with a Commanding Wand on a Soul Powder Block repeatedly will show the locations of mobs one by one. Shift+right click on a Soul Powder Block with an Evil Crystal Fragment on the off-hand will summon the latest-shown mob if it's in a loaded chunk, consuming a fragment.

Now Commanding Wand summons mobs around by right-clicking on a vanilla Bell instead of Shift+Right Click without target.

Now waiting mobs will be sitting.

Now Ender Executor will not teleport on friending process within 16 blocks away from player instead of 7 blocks.

Fixed Ender Executor keeping the attacking pose without an attacking target in some permanent-anger modes.

Fixed Nightwalker using wrong friending process.

Fixed Nightwalker and Melty Monster using wrong healing items.

Fixed /give still giving uninitialized items.

Fixed respawners not consuming in the creative mode.

### 0.1.28.4

Fixed HMaG mobs vanishing after reloading the world.

Fixed crash on mob picking up item.

### 0.2.28.4 & 0.1.28.3 & 0.0.28.2

Fixed Soul Cake providing 5 minutes of saturation. (0.25 s expected)

### 0.0.28.1

Fixed Slime Girls being not able to be given items.

Fixed crash on Ghastly Seeker setting attack target.

Fixed skeleton mobs keeping making shooting actions when the arrow runs out.

### 0.2.28.3 & 0.1.28.2

Fixed Slime Girls being not able to be given items.

### 0.2.28.2 & 0.1.28.1

Fixed crash on Ghastly Seeker setting attack target.

Fixed skeleton mobs keeping making shooting actions when the arrow runs out.

### 0.2.28.1

Fixed HMaG mobs vanishing after reloading the world.

### 0.x.28

Note: this update may have respawner format change. Release mobs from respawners and storage pods before update.

Taming process refactor.

​	-Now a mob only allows 1 player in process. If the ongoing player is online, other players are not allowed to enter the process.

​	-Reduced the progress loss from 0.1/s to 0.02/s for mobs with the continuous progress loss mechanics when some conditions are not satisfied.

​	-Reduced the item giving cool-down. (5s -> 2s; 10s -> 3s; 15s -> 5s)

​	-Modified mob anger durations.

Several changes to make the mod more friendly to early-stage players.

​	-Added potions for Undead Affinity and Ender Protection.

​	-Reduced some item's costs.

​	-Enhanced the effects of the Soul Cake, Soul Cake Slice and Ender Pie.

​	-Reduced the costs for getting friendly with mobs.

Slightly modified the mob accessory effects.

Slightly modified the taming items. 

  -Now Ghastly Seeker uses `undead_nether_b` instead of `undead_b`.

  -Now Wither Skeleton Girl uses `undead_nether_c` instead of `undead_c`.

Commanding Wand optimization.

​	-Now Commanding Wand can switch mob's AI from at most 32 blocks away.

​	-Now Shift+Right click can make all mobs follow within 16 blocks.

​	-Added Combat Commanding Wand, allowing to control your mob's attack target.

Added config options to remove mob's equipment on friended.

Now Necrostalker will gain Strength and Speed effects instead of silently boost its ATK during the friending process, and will lose progress after not hitting the player for 30 s instead of 10 s.

Now Necrostalker's interaction uses Commanding Wand like other mobs, and no longer requires necromancer items.

Now Peach-Wood Sword no longer makes percentage damage, but provides Instant Health II (damage for undead), Weakness III (5 s) and Slowness III (5 s).

Fixed Ender Executor crash on attacking.

Fixed mobs not avoiding Enderberry Bush.

### 0.x.27.1

Fixed friending processes not consuming items.

### 0.x.27

Added mob dictionary (Requires Citadel as dependency). Special thanks to Koishi for his significant help in making the dictionary!

Added data pack support for mob healing items, taming items and trade list. For usage, see data pack reference on GitHub (ongoing).

Added Enderberry Bush. Now Enderberry is growable and needs friendly Ender Executor to help growing.

Updated trade list. Now mobs below have trades: all Vanilla mob girls, Ender Executor, Kobold, Necrostalker (Necrotic Reaper).

Added configs:

​	-`enableFriendlyDamage`: If false, players and owned NFF-Girls mobs will not be able to damage each other.

Now friendly Ender Executor will not do random teleport unless hurt by water.

Now Nightwalker's magic ball can convert blocks in larger area.

Now friendly Nightwalker's magic ball can always convert blocks, and will convert blocks of larger range when using the Ancient Stone.

Fixed friendly Ender Executor not performing beam attack in the easy mode.

Fixed `/give` command giving invalid Respawners or Magical Slime Bottles which may cause data break.

Fixed Nightwalkers only converting Pink Glazed Terracotta.

Fixed Soul Carpet recipe error in 1.18.2.



 

### 0.0.26.1

Fixed pickaxe unable to dig blocks caused by a broken tag.

### 0.x.26

WARNING: This update has massive save data format changes. Back up before update!

Renamed project name from *Days with Monster Girls* to *NFF: Girls*.

Renamed mod ID from `dwmg` to `nffgirls`.

Fixed mobs not moving on wandering mode.

Renamed tag:

​	- `dwmg:can_befriend_nightwalker_on` -> `nffgirls:can_tame_nightwalker_on`

### 0.x.25

WARNING: This update has save data format changes. Back up before update!

Fixed Dullahan missing variant.

Fixed Melty Monster GUI attribute display error.

### 0.x.24

Config changes:

​	-Added configs for max HP and ATK increase on level upgrade.

​	-Added configs for scaling some bauble effects.

​	-Renamed some configs:

​		-`maxHealthBoostByLevel` => `maxHealthIncreaseByLevel`

​		-`maxAtkIncreaseByLevel` => `maxAtkIncreaseByLevel`

Bauble changes:

​	-Added Soul Amulet III and IV.

​	-Reduced the costs of baubles.

​	-Modified the effect of Aqua Jade.

​	-Now the off-hand of Necrostalker (previouly Necrotic Reaper) can equip hoes and increase ATK.

Added trades for Creeper Girl and Necrostalker.

Fixed Necrostalker unable to keep the attack-boosting effect after restarting the game during befriending process.

Fixed Nightwalker armor increase by level-up not taking effect.



### 0.2.23.1

Fixed dedicated server crash.

### 0.1.23.2

Fixed dedicated server crash.

### 0.0.23.1

Fixed dedicated server crash.

Fixed wrong BefriendMobs Framework version.

### 0.1.23.1

Fixed wrong point requirement for introduction letter.

### 0.x.23

Updated HMaG versions to 5.1.26/6.2.19/9.0.20.

Added trades to Zombie/Skeleton-derived mobs (6 types overall). Interact with empty hand or Evil Gem to open the trade window. 

Now Creeper Girl charging & uncharging requires Shift+Right Click.

Rearranged the info of Slime Girls in the GUI to prevent from getting out of the box.

Increased the durability of Undead Affinity from Soul Carpet from 0.15 s to 1.00 s, so that jumping will not interrupt the effect.

Fixed some mobs getting silent when the configured ambient sound chance is 1.0. Now for all mobs 0.0 is complete silence and 1.0 is normal.

### 0.x.22

Since this version until the final release, BefriendMobs Framework will be embedded in the dwmg jar. Now there's no need to manually add BefriendMobs into the modpack.

Updated HMaG version to 5.1.23/6.2.16/9.0.17.

Modified Drowned Girl trident damage and related behaviors.

Now skeletons can use non-vanilla bows, tipped arrows and spectral arrows.

Now Ender Executor can equip blocks.

Now Melty Monster will attempt to return to lava only when heat is <20% in follow mode.

Some compatibility issues may be solved that befriended mobs not recognized as allies since `IBefriendedMob` in BefriendMobs Framework now implements vanilla `OwnableEntity` interface.

Fixed some items not working on using to Wither Skeleton Girls.

Fixed Redcap providing Haste even if the owner is over 8 blocks away or cannot be seen.

Fixed wrong bauble slots of Dullahan and Dodomeki.

Fixed Cursed Doll not sun-immune with corresponding baubles.

Fixed all sun-sensitive mobs not sun-immune with corresponding baubles. (1.20.1)

Fixed Jack o' Frost not immune to hot biomes wearing Amulet of Resistance. (1.18.2/1.20.1)

Fixed Melty Monster unable to switch whether to set fire with Water Bucket / Flint and Steel. (1.18.2/1.20.1)

Fixed Harpy and flyable befriended mobs unexpectedly taking falling damage. (1.20.1)

Fixed crash on Harpy and Slime Girl befriended. (1.20.1)



### 0.x.21

Because the Bauble System in BefriendMobs Framework is fully remade, the current Bauble System is possibly **unstable**. Ensure to keep your data backed up before updating to this version!!!

Now Bauble items are stackable.

Now Amulet of Resistance will add extra Max HP.

Fixed an issue that missing BefriendMobs dependency causes a pre-launch crash without information. Now it will show an error screen like other mods.

Fixed some features of Jack o' Frost and Melty Monster not working due to wrong mixin setup. (1.18.2 & 1.20.1)

Fixed Nightwalkers causing crash. (1.20.1)

Now DWMG will not provide -slim files. To fix possible compat issues, follow the instruction on Curseforge or Github.

Added tag: `befriendmobs:neutral_to_bm_mobs`. Mobs with this tag will not proactively attack befriended mobs unless being attacked.

# Earlier versions

## 1.19.2

### 0.1.20

Added Nightwalker support.

Added Luminous Terracotta and Enhanced Luminous Terracotta.

Now damage of Necromancer's Hat will not bypass Absorption effect.

Now EXP Modifier's max amount is 2^20 instead of 2^10.

Fixed Necromancer's Hat still damaging creative and spectator players.

Fixed Skeleton Girls converting to Stray Girls immediately on befriended.

Fixed Crimson Slaughterers restricting sun.

Fixed Alraune and Glaryad Bauble slots wrong display.

### 0.1.19

Added Mixin-Extras dependency. (There's no extra steps to install, but dependents need this in `build.gradle`.)

Added Melty Monster support.

Added configs about zombies and skeletons inter-variant conversion.

Now befriended mobs will not be knocked back when getting sweep-attacked by the owner.

Now befriended mobs' attacking targets expire after 30 s.

Fixed undead mobs with sun-immune baubles but not wearing helmets still avoiding sun.

Fixed only the last bauble of Jiangshi working.

Fixed crash on getting attacked when befriending Cursed Dolls.

Some internal implementation changes.

### 0.1.18

- Added some configs:
  - Ghast sounds
  - Enable/disable friendly damage of projectiles from befriended mobs (Disabled by default)
  - Max health & atk boost from mob level
- Now Ghastly Seekers accept Gunpowder as healing item.
- Now explosions from befriended mobs will not destroy dropped items (but will blow them away).
- Now fireballs of Ghastly Seekers don't hurt allies, and the direct hit damage is scaled like the explosion.
- Fixed incompatibility with Target Dummy.
- Fixed the last bauble of Jack o' Frost not working.

### 0.1.17.1

Fixed Jack o' Frost (HMaG) not spawning.

Fixed Banshee, Cursed Doll and Jiangshi not maintaining the color variant on befriended.

Fixed Redcap and Jack o' Frost missing name.

### 0.1.17

Updated HMaG dependency to 6.2.5.

Added Redcap and Jack o' Frost support.

Added some configs for mob sound.

​	-Frequency of the ambient sound can be reduced.

​	-Zombie and Skeleton sound can be replaced with HMaG girl mob sound.

​	-Zombie and Skeleton ambient sound can be blocked.

Added EXP Modifier (debug only).

Now mobs equipping Courage Amulet II will proactively attack enemies hostile to the owner. 

Now off-hand item of Kobold and Imp can be stacked only to 1.

Now Curse of Necromancy (curse effect of Necromancer's Hat) deals 1 damage per 4 seconds instead of 2 and don't cause knockback, but bypassing enchantments.

Fixed Banshee not synched between server and client (i.e. display error).

Fixed Kobold not accepting Golden Pickaxe.

### 0.1.16.3

Fixed entity tags missing.

### 0.1.16.2

Now Phantoms and Dyssomnias don't proactively attack befriended mobs.

Now Husk Girls don't burn under sun.

Changed tag names:

​	-`ignore_undead_affinity` => `ignores_undead_affinity`

​	-`ignore_magical_gel_slowdown` => `ignores_magical_gel_slowness`

​	-`use_fortune_as_looting` => `uses_fortune_as_looting`

Fixed some mobs displaying a barrier label on the head when equipping baubles provide sun-immunity.

Fixed crash on Zombie Girl, Husk Girl and Drowned Girl hurt.

### 0.1.16.1

Added Befriending Progress Probe (debug only).

Fixed Harpy and Snow Canine not attempting to pick items.

### 0.1.16

Added Crimson Slaughterer and Cursed Doll support.

Enhanced Poisonous Thorn effect:

​	- Now the effect is repeatable.

​	- Each bauble increases the poison level by 1 and duration by 5 s.

​	- Crimson Slaughterer can equip it, enhancing the slowness effect and adding poison effect.

Now Arrows shot by Skeleton Girl and derivatives can be picked up, unless with Infinite bow.

Adjusted some recipes to reduce the cost.

Some internal registry changes (not affecting data).

### 0.1.15

Added Alraune and Glaryad support.

Added Reinforced Fishing Rod.

Adjusted some recipes to reduce Evil Crystal cost.

Now the mob's custom name will be removed on befriended.

### 0.1.14.1

Fixed Jiangshi, Dullahan and Dodomeki missing translation.

Fixed wrong version display in the mod list.

### 0.1.14

Added Jiangshi, Dullahan and Dodomeki support.

Added Taoist Talisman and Peach-Wood Sword.

### 0.1.13.2

Temporarily fixed magical gel bottle vanishing on usage. Now the bottle will always drop on usage, usually being picked up immediately, but if player is overlapping with other item entities it may drop as entity.

### 0.1.13.1

Now on being attacked by owner, befriended mobs will drop damage depending on the actual taken damage instead of the initial damage before absorption by armor, enchantment, etc.

Fixed befriended mobs still dropping favorability on sweeping-attacked by the owner, although not taking damage. 

### 0.1.13

Added Slime Girl support.

Added Magical Gel Bottle and Magical Gel Ball.

Now befriending process will not be interrupted (progress returning zero) if player is attacked by the mob, or making a non-damaging attack (e.g. Snowball hit).

Now the favorability change on applying healing item depends on the actual healing amount instead of the expected amount.

Now Skeleton, Stray and Wither Skeleton Girls' shooting damages will increase on their ATK increase.

Now undead mobs will not try avoiding the sun when having Fire Resistance effect.

Now if a "mob missing owner" crash occurs, you can interact with an unwritten Transferring Tag to own it (not consuming the item) without crash.

Fixed Imp, Harpy and Snow Canine opening GUI without Commanding Wand.

Fixed Snow Canine summonable.

Fixed Harpy and Snow Canine missing translation.

Fixed crash on Harpy and Snow Canine befriended.

Fixed crash on Ender Executor attacked.

Fixed Skeleton, Stray and Wither Skeleton Girls making repeated armor sound when wearing a helmet and being sun-immune.

Fixed invalid respawners (usually generated with /give) crashing on mouse hovering. Now mouse hovering on it doesn't cause crash, but trying using it still does. Please do NEVER use /give for Mob Respawners (including non-empty Mob Storage Pods)!

### 0.1.12

Added Harpy and Snow Canine support.

Fixed flying mobs (like Hornets) cannot effectively hit targets.

Fixed Slimes not damaging befriended mobs.

### 0.1.11

Added Transferring Tag. 

Now befriended mobs are immune to sweeping attack of the owner.

Fixed Mob Storage Pod being able to store all mobs. Now Mob Storage Pod can only store the user's own befriended mobs.

Fixed Imp switching AI without Commanding Wand.

Fixed ground mobs like Zombie Girls cannot effectively hit flying mobs like Phantoms.

### 0.1.10

Added Mob Storage Pod.

Fixed some AI adjustment not working.

Fixed Ender Executor generating angry particles on being attacked even if not in befriending process.

Fixed Kobold and Imp not generating heart particles on befriending.

### 0.1.9

WARNING: Due to item change, Mob Respawners in earlier versions may be incompatible to this version and above. They will possibly **VANISH** on updating to this version! Please make sure to have released all mobs from Respawners before updating!!

Added Kobold and Imp support.

Added weapon: Netherite Fork.

Fixed Soul Amulet II not working.

Fixed server crash on player taking damage when having Ender Protection effect.

Fixed Ender Executors stop attacking unexpectedly.

### 0.1.8.1

Reduced the cost of some Baubles.

Fixed Soul Amulet II not working.

Fixed Amulet of Resistance II not providing sun immunity.

### 0.1.8

Added Baubles: Amulet of Courage, Life Jade

Added upgrade to some Baubles: Soul Amulet, Amulet of Resistance, Amulet of Courage, Life Jade. 

Added Bauble effect description.

Added Evil Magnet, a tool for finding respawners in the level.

Reduced the cost of some recipes.

Adjusted Necromancer's Wand: 

 -- Changed bullet speed from 4 to 16.

 -- Reduced usage cooldown from 5 s to 2.5 s.

 -- Now the bullet will blast on hit something, and give previous effects to all living entities in the  3x3x3 area around. If a living entity is directly hit, a stronger effect will be given to it, either negative or positive.

 -- Increased Wither effect duration from 5 s to 10 s.

 -- Increased  Strength effect duration from 5 s to 10 s.

 -- If directly hit: the level of Wither will increase by 1; the duration of Strength will be 15 s; healing amount will be doubled. 

Reduced the forgiving time of undead mobs for Undead Affinity effect from 15 min to 5 min.

Changed the hatred duration of most befriendable mob. Now the hatred durations are listed in `wiki/hatred-durations.txt` in the repo.

Fixed Zombies and variants GUI Bauble slot display error.

Fixed crash caused by null damage source on mob death, described in issue #7

Fixed some items in other mods may make Ender Executor and Necrotic Reaper non-hostile even during befriending process.

Fixed Mob Respawner item entities vanishing in fire and lava.

### 0.1.7

Fixed Creeper Girl not being interactable.

Fixed Creeper Girl and Ender Executor still be waiting instead of following right after befriending.

### 0.1.6

Added Ghastly Seeker support.

Added Commanding Wand.

Adjusted mob interaction method:

 --To switch mob's AI state, player must have Commanding Wand on either hand, and right click the mob.

 --To open mob's inventory GUI, player must have Commanding Wand on either hand, and press shift+right click.

 --For Necrotic Reapers, use Necromancer's Wand instead.

Optimized the AI of flying befriended mobs.

Adjusted the AI of Ghastly Seeker (non-befriended, HMaG) out of the Nether. Now if it's not in Nether, it will fly at the height no more than 32 blocks above the ground. Also it won't move too far from players. This change makes effect only if DWMG is loaded. 

Now Wither Skeleton Girls will not catch Curse of Necromancy effect.

Now mob will drop player-kill loot and affected by Looting enchantment if killed by befriended mobs.  

Now befriended mob's AI state will be Following instead of Waiting right after befriended.

Fixed monster not being hostile to befriended mobs.

Fixed Wither Skeleton Girls not accepting Nether Stars.

Fixed Skeletons not consuming arrow on shooting.

Fixed crash and helmet becoming Dummy Item on skeletons killing mobs.

Fixed Stray Girl server crash.

Fixed Sunhat not preventing Skeleton/Stray Girl from avoiding the sun.

Fixed Soul Carpet not dropping on broken.

Fixed armor losing durability on taking bypass-armor damages on befriended mobs.

Fixed GUI persisting when mob is removed from level or moving too far away.

Reduced random stroll radius from 64 to 16.

Removed and fixed some deprecated methods in code.

### 0.1.5.1

Removed verbose debug output.

### 0.1.5

Added Banshee support.

Adjusted Hornet befriending condition. Now it won't be immediately interrupted if there're no enough Honey Blocks, but the progress will drop by 0.1 per second while the mob releases massive smoke particles.

Added Curse of Necromancy effect in place of Wither effect for Necromancer's Hat. The damaging effect is same to Wither I, but not causing the HP slot turning black.

Slightly adjusted the recipes to reduce the Evil Crystal cost.

Fixed server crashing on befriended mobs' conversion.

Fixed some compatibility issues:

--Solved mob interaction conflict with Quantum Catcher (*Forbidden and Arcanus*), fixed GitHub issue #6.

--Fixed undead mob still adding hatred when player got undead-neutral by other ways e.g.  Unsettling Kimono (*Alex's Mobs*).

### 0.1.4

WARNING: Mob Respawner save dat format changed in this version, making it incompatible with versions below. To solve this, load 0.1.3 (or 0.1.2 for multiplayer), respawn all mobs in the respawners, and then update to 0.1.4.

Fixed GUI not working in the multiplayer game.

Fixed 0.1.3 crashing the multiplayer server.

Fixed Respawners spawning pigs in the multiplayer game.

Fixed Slimes not damaging befriended mobs.

Fixed Creeper Girls and Ender Executors missing texture.

Set that monsters are hostile to certain types of befriended mobs:

--Zombies and derivatives (not girls, including Zombified Piglins and Zoglins) attacks Skeleton/Stray/Wither Skeleton Girls.

Skeletons and derivatives (not girls) attacks Zombie/Husk/Drowned Girls.

--Slimes (not girls), Magma Cubes, Illagers, Phantoms (including Dyssomnias), Piglin Brutes and Hoglins attack all befriended mobs.

--Ghasts attack non-undead mobs.

--Blazes attack Skeleton/Stray Girls and flying befriended mobs.

--Spiders attack non-arthropod befriended mobs.

### 0.1.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.1.2-alpha, interact with all your mobs and save.

Added Favorability System.

Added Exp & Level System. 

Added armor durability drop on mob hurt.

Now Soul Amulet and Resistance Amulet can provide undead mobs sun immunity.

Some numerical changes:

- Reduced the atk enhancement from Efficiency enchantment for Necrotic Reapers, from 20% to 10% hoe basic atk each level. 
- Reduced the speed-down effect of Resistance Amulet from 20% to 10% each.
- Increased Undead Affinity effect duration from Soul Cake from 30 s to 1.5 min each slice; from 1.5 min to 5 min for Each Soul Cake Slice.
- Increased Ender Protection effect duration from Ender Pie, from 30 s to 3 min.

Fixed befriended mob not dropping inventory on death.

Fixed mob main-hand weapons not breaking on durability ran out.

### 0.1.2-alpha

Added Necrotic Reaper support.

Added *Necromancer's Wand*.

Added Sharpness enchantment effect and durability drop features on befriended mob attacking with weapons.

Increased *Death Crystal Powder* production from 4 to 9 each *Crystal of Death*.

Befriended mobs now don't attack their hostile/neutral relatives, and *vice versa*. 

### 0.1.1-alpha

Extracted BefriendMobs API out as a lib. Now this mod needs BefriendMobs API as dependency.

(As BefriendMobs API hasn't been approved in CurseForge, the .jar files are also available in Github: https://github.com/SodiumZH/Days-with-Monster-Girls/tree/1.18.2/files)

### 0.1.0-snapshot.11

Fixed Ender Executor befriending interruption on the mob attacking the player.

Now Ender Executor befriending will interrupt if it failed to attack the player (no matter blocked with Shield or not) within 10 seconds.

### 0.1.0-snapshot.10

Remade the Bauble System and changed the accepted items.

Added Baubles: Soul Amulet, Amulet of Resistance, Healing Jade, Aqua Jade, Poisonous Thorn.

Added Equipment: Necromancer's Hat, Sunhat.

### 0.0.0-snapshot.9

**WARNING: It is known that mobs LOSE THE OWNER INFORMATION on updating to this version. Now ownerless mob from this bug will be owned by the first player interacted with it.**

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Hornet support.

Fixed Creeper Girl befriending still succeeding when player is killed by the final 12-ranged explosion.

Fixed befriendable mob not forgiving player after 15 min.

### 0.1.0-snapshot.8

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Drowned Girl support.

Added Mob Respawner.

Fixed Creeper Girl wrong AI behaviors.

### 0.1.0-snapshot.7

Fixed Golems being hostile to befriended mobs. Now they're neutral.

Fixed befriended mobs starting to attack immediately after switching AI state.

### 0.1.0-snapshot.6

Added Bauble system (see wiki for details).

Fixed world timers ticking twice each frame (i.e. timers running 2x faster).

Disabled /summon command for raw befriended mobs which may cause unknown errors.

Added 15 min forgiving cooldown to undead mobs. After forgiving, they will not keep hostile when player has Undead Affinity effect. 

### 0.1.0-snapshot.5

Fixed Soul Carpet recipe missing tag.

### 0.1.0-snapshot.4

Added Stray Girl support.

Added Wither Skeleton Girl support.

Added 2s cooldown for giving healing items.

Added healing items to Creeper Girl and Ender Executor.

Rollbacked HMaG dependency to 6.1.0.

### 0.1.0-snapshot.3

Ported from 1.18.2 - 0.0.0-snapshot.3.

## 1.20.1

### 0.2.17

Updated HMaG dependency version to 9.0.2.

All other changes in 1.19.2-0.1.17.

Fixed possible compatibility issues of the feature that Jiangshi, Necrotic Reaper and Ender Executor keeping hostile during befriending process.

Fixed Banshee, Cursed Doll and Jiangshi not maintaining the color variant on befriended.

Fixed some random crashes on successfully befriending mobs using Item-Dropping Process.

### 0.2.16.2

Identical to 1.19.2-0.1.16.3.

### 0.2.16.1

Identical to 1.19.2-0.1.16.2.

### 0.2.16

Identical to 1.19.2-0.1.16 & 0.1.16.1.

### 0.2.15

Ported from 1.19.2-0.1.15.

Temporarily removed the special death message of Necromancer's Hat curse due to an unfixed bug.

Now blocks destroyed by the explosions of Creeper Girl and Ghastly Seeker will always drop corresponding items.

## 1.18.2

### 0.0.17

Updated HMaG dependency to 5.1.15.

All changes in 1.19.2-0.1.17.

Fixed crash on mob changing target.

### 0.0.16.3

Identical to 1.19.2-0.1.16.3.

### 0.0.16.2

Identical to 1.19.2-0.1.16.2.

### 0.0.16.1

Identical to 1.19.2-0.1.16.1.

### 0.0.16

Identical to 1.19.2-0.1.16.

### 0.0.15

Identical to 1.19.2-0.1.15.

### 0.0.14

Added Dullahan and Dodomeki support.

### 0.0.13.1

Identical to 1.19.2-0.1.13.2.

### 0.0.13

Identical to 1.19.2-0.1.13 and 0.1.13.1.

### 0.0.12

Identical to 1.19.2-0.1.12.

### 0.0.11

Identical to 1.19.2-0.1.11.

### 0.0.10

Added Mob Storage Pod.

Fixed some AI adjustment not working.

Fixed Ender Executor generating angry particles on being attacked even if not in befriending process.

Fixed Kobold and Imp not generating heart particles on befriending.

### 0.0.9

WARNING: Due to item change, Mob Respawners in earlier versions may be incompatible to this version and above. They will possibly **VANISH** on updating to this version! Please make sure to have released all mobs from Respawners before updating!!

Added Kobold and Imp support.

Added weapon: Netherite Fork.

Fixed Soul Amulet II not working.

Fixed server crash on player taking damage when having Ender Protection effect.

Fixed Ender Executors stop attacking unexpectedly.

### 0.0.8

Added Baubles: Amulet of Courage, Life Jade

Added upgrade to some Baubles: Soul Amulet, Amulet of Resistance, Amulet of Courage, Life Jade. 

Added Bauble effect description.

Added Evil Magnet, a tool for finding respawners in the level.

Reduced the cost of some recipes.

Adjusted Necromancer's Wand: 

 -- Changed bullet speed from 4 to 16.

 -- Reduced usage cooldown from 5 s to 2.5 s.

 -- Now the bullet will blast on hit something, and give previous effects to all living entities in the  3x3x3 area around. If a living entity is directly hit, a stronger effect will be given to it, either negative or positive.

 -- Increased Wither effect duration from 5 s to 10 s.

 -- Increased  Strength effect duration from 5 s to 10 s.

 -- If directly hit: the level of Wither will increase by 1; the duration of Strength will be 15 s; healing amount will be doubled. 

Reduced the forgiving time of undead mobs for Undead Affinity effect from 15 min to 5 min.

Changed the hatred duration of most befriendable mob. Now the hatred durations are listed in `wiki/hatred-durations.txt` in the repo.

Fixed Zombies and variants GUI Bauble slot display error.

Fixed crash caused by null damage source on mob death, described in issue #7

Fixed some items in other mods may make Ender Executor and Necrotic Reaper non-hostile even during befriending process.

Fixed Mob Respawner item entities vanishing in fire and lava.

Fixed Sunhat not working for Skeleton and Stray Girls.

Fixed crash related to sun immunity of befriended Skeleton and related mobs.

### 0.0.7

Fixed Creeper Girl not being interactable.

Fixed Creeper Girl and Ender Executor still be waiting instead of following right after befriending.

### 0.0.6

Added Ghastly Seeker support.

Added Commanding Wand.

Adjusted mob interaction method:

 --To switch mob's AI state, player must have Commanding Wand on either hand, and right click the mob.

 --To open mob's inventory GUI, player must have Commanding Wand on either hand, and press shift+right click.

 --For Necrotic Reapers, use Necromancer's Wand instead.

Optimized the AI of flying befriended mobs.

Adjusted the AI of Ghastly Seeker (non-befriended, HMaG) out of the Nether. Now if it's not in Nether, it will fly at the height no more than 32 blocks above the ground. Also it won't move too far from players. This change makes effect only if DWMG is loaded. 

Now Wither Skeleton Girls will not catch Curse of Necromancy effect.

Now mob will drop player-kill loot and affected by Looting enchantment if killed by befriended mobs.  

Now befriended mob's AI state will be Following instead of Waiting right after befriended.

Fixed monster not being hostile to befriended mobs.

Fixed Wither Skeleton Girls not accepting Nether Stars.

Fixed Skeletons not consuming arrow on shooting.

Fixed crash and helmet becoming Dummy Item on skeletons killing mobs.

Fixed Stray Girl server crash.

Fixed Soul Carpet not dropping on broken.

Fixed armor losing durability on taking bypass-armor damages on befriended mobs.

Fixed GUI persisting when mob is removed from level or moving too far away.

Reduced random stroll radius from 64 to 16.

Removed and fixed some deprecated methods in code.

### 0.0.5.1

Removed verbose debug output.

### 0.0.5

Added Banshee support.

Adjusted Hornet befriending condition. Now it won't be immediately interrupted if there're no enough Honey Blocks, but the progress will drop by 0.1 per second while the mob releases massive smoke particles.

Added Curse of Necromancy effect in place of Wither effect for Necromancer's Hat. The damaging effect is same to Wither I, but not causing the HP slot turning black.

Slightly adjusted the recipes to reduce the Evil Crystal cost.

Fixed server crashing on befriended mobs' conversion.

Fixed some compatibility issues:

--Solved mob interaction conflict with Quantum Catcher (*Forbidden and Arcanus*), fixed GitHub issue #6.

--Fixed undead mob still adding hatred when player got undead-neutral by other ways.

### 0.0.4

WARNING: Mob Respawner save data format changed in this version, making it incompatible with versions below. To solve this, load 0.0.3 (or 0.0.2 for multiplayer), respawn all mobs in the respawners, and then update to 0.0.4.

Fixed GUI not working in the multiplayer game.

Fixed 0.0.3 crashing the multiplayer server.

Fixed Respawners spawning pigs in the multiplayer game.

Fixed Slimes not damaging befriended mobs.

Set that monsters are hostile to certain types of befriended mobs:

- Zombies and derivatives (not girls, including Zombified Piglins and Zoglins) attacks Skeleton/Stray/Wither Skeleton Girls.
- Skeletons and derivatives (not girls) attacks Zombie/Husk/Drowned Girls.
- Slimes (not girls), Magma Cubes, Illagers, Phantoms (including Dyssomnias), Piglin Brutes and Hoglins attack all befriended mobs.
- Ghasts attack non-undead mobs.
- Blazes attack Skeleton/Stray Girls and flying befriended mobs.
- Spiders attack non-arthropod befriended mobs.

### 0.0.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.0.2-alpha, interact with all your mobs and save.

Added Favorability System.

Added Exp & Level System. 

Added armor durability drop on mob hurt.

Now Soul Amulet and Resistance Amulet can provide undead mobs sun immunity.

Some numerical changes:

- Reduced the atk enhancement from Efficiency enchantment for Necrotic Reapers, from 20% to 10% hoe basic atk each level. 
- Reduced the speed-down effect of Resistance Amulet from 20% to 10% each.
- Increased Undead Affinity effect duration from Soul Cake from 30 s to 1.5 min each slice; from 1.5 min to 5 min for Each Soul Cake Slice.
- Increased Ender Protection effect duration from Ender Pie, from 30 s to 3 min.

Fixed befriended mob not dropping inventory on death.

Fixed mob main-hand weapons not breaking on durability ran out.

### 0.0.2-alpha

Added Necrotic Reaper support.

Added *Necromancer's Wand*.

Added Sharpness enchantment effect and durability drop features on befriended mob attacking with weapons.

Increased *Death Crystal Powder* production from 4 to 9 each *Crystal of Death*.

Befriended mobs now don't attack their hostile/neutral relatives, and *vice versa*. 

### 0.0.1-alpha

Extracted BefriendMobs API out as a lib. Now this mod needs BefriendMobs API as dependency.

(As BefriendMobs API hasn't been approved in CurseForge, the .jar files are also available in Github: https://github.com/SodiumZH/Days-with-Monster-Girls/tree/1.18.2/files)

### 0.0.0-snapshot.10

Fixed Ender Executor befriending interruption on the mob attacking the player.

Now Ender Executor befriending will interrupt if it failed to attack the player (no matter blocked with Shield or not) within 10 seconds.

### 0.0.0-snapshot.9

Remade the Bauble System and changed the accepted items.

Added Baubles: Soul Amulet, Amulet of Resistance, Healing Jade, Aqua Jade, Poisonous Thorn.

Added Equipment: Necromancer's Hat, Sunhat.

### 0.0.0-snapshot.8

**WARNING: It is known that mobs LOSE THE OWNER INFORMATION on updating to this version. Now ownerless mob from this bug will be owned by the first player interacted with it.**

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Hornet support.

Fixed Creeper Girl befriending still succeeding when player is killed by the final 12-ranged explosion.

Fixed befriendable mob not forgiving player after 15 min.

### 0.0.0-snapshot.7

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Drowned Girl support.

Added Mob Respawner.

Fixed Creeper Girl wrong AI behaviors.

Fixed befriended mobs starting to attack immediately after switching AI state.

### 0.0.0-snapshot.6

Fixed Golems being hostile to befriended mobs. Now they're neutral.

### 0.0.0-snapshot.5

Added Bauble system (see wiki for details).

Fixed world timers ticking twice each frame (i.e. timers running 2x faster).

Disabled /summon command for raw befriended mobs which may cause unknown errors.

Added 15 min forgiving cooldown to undead mobs. After forgiving, they will not keep hostile when player has Undead Affinity effect. 

### 0.0.0-snapshot.4

Added Stray Girl support.

Added Wither Skeleton Girl support.

Added 2s cooldown for giving healing items.

Added healing items to Creeper Girl and Ender Executor.

### 0.0.0-snapshot.3

Changed the mechanism for befriending of all supported mobs. Now the mobs don't require a certain amount of items, but have a progress value with maximum 1. Each support item adds a random value to the mob, and when the value reaches 1 it's done. 

Added Ender Executor support.

Added effects: Undead Affinity, Ender Protection.

Added foods: Soul Cake, Soul Cake Slice, Enderberry, Ender Pie.

Added blocks: Soul Carpet.

Updated HMaG dependency to 5.1.9.

Removed GoG4 dependency.

Fixed wrong mob attributes.

### 0.0.0-snapshot.2

Added Creeper Girl support.

### 0.0.0-snapshot.1

Added befriending Zombie Girl, Husk Girl and Skeleton Girl support.