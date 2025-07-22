package content.global.skill.slayer;

import core.cache.def.impl.NPCDefinition;
import core.game.node.entity.player.Player;
import org.rs.consts.NPCs;

import java.util.Arrays;
import java.util.HashMap;

import static core.api.ContentAPIKt.hasRequirement;

/**
 * The enum Tasks.
 */
public enum Tasks {
    /**
     * The Aberrant spectres.
     */
    ABERRANT_SPECTRES(65, new int[]{NPCs.ABERRANT_SPECTRE_1604, NPCs.ABERRANT_SPECTRE_1605, NPCs.ABERRANT_SPECTRE_1606, NPCs.ABERRANT_SPECTRE_1607, NPCs.ABERRANT_SPECTRE_7801, NPCs.ABERRANT_SPECTRE_7802, NPCs.ABERRANT_SPECTRE_7803, NPCs.ABERRANT_SPECTRE_7804}, new String[]{"Aberrant spectres have an extremely potent stench that drains", "stats and life points. A nose peg, protects against the stench."}, 60, true, false),
    /**
     * The Abyssal demons.
     */
    ABYSSAL_DEMONS(85, new int[]{NPCs.ABYSSAL_DEMON_1615, NPCs.ABYSSAL_DEMON_4230}, new String[]{"Abyssal Demons are nasty creatures to deal with, they aren't really part, ", "of this realm, and are able to move very quickly to trap their prey"}, 85, false, false),
    /**
     * The Ankou.
     */
    ANKOU(40, new int[]{NPCs.ANKOU_4381, NPCs.ANKOU_4382, NPCs.ANKOU_4383}, new String[]{"Neither skeleton nor ghost, but a combination of both."}, 1, true, false),
    /**
     * The Aviansies.
     */
    AVIANSIES(60, new int[]{NPCs.AVIANSIE_6245, NPCs.AVIANSIE_6243, NPCs.AVIANSIE_6235, NPCs.AVIANSIE_6232, NPCs.AVIANSIE_6244, NPCs.AVIANSIE_6246, NPCs.AVIANSIE_6233, NPCs.AVIANSIE_6241, NPCs.AVIANSIE_6238, NPCs.AVIANSIE_6237, NPCs.AVIANSIE_6240, NPCs.AVIANSIE_6242, NPCs.AVIANSIE_6239, NPCs.AVIANSIE_6234}, new String[]{"Graceful, bird-like creature."}, 1, false, false),
    /**
     * The Banshee.
     */
    BANSHEE(20, new int[]{NPCs.BANSHEE_1612}, new String[]{"Banshees use a piercing scream to shock their enemies", "you'll need some Earmuffs to protect yourself from them."}, 15, true, false),
    /**
     * The Basilisks.
     */
    BASILISKS(40, new int[]{NPCs.BASILISK_1616, NPCs.BASILISK_1617, NPCs.BASILISK_4228}, new String[]{"A mirror shield is much necessary when hunting", "these mad creatures."}, 40, false, false),
    /**
     * The Bats.
     */
    BATS(5, new int[]{NPCs.BAT_412, NPCs.GIANT_BAT_78, NPCs.GIANT_BAT_1005, NPCs.GIANT_BAT_2482, NPCs.GIANT_BAT_3711}, new String[]{"These little creatures are incredibly quick.", "make sure you keep your eye on them at all times."}, 1, false, false),
    /**
     * The Bears.
     */
    BEARS(13, new int[]{NPCs.BLACK_BEAR_106, NPCs.GRIZZLY_BEAR_105, NPCs.GRIZZLY_BEAR_1195, NPCs.ANGRY_BEAR_3645, NPCs.ANGRY_BEAR_3664, NPCs.BEAR_CUB_1326, NPCs.BEAR_CUB_1327}, new String[]{"A large animal with a crunching punch."}, 1, false, false),
    /**
     * The Birds.
     */
    BIRDS(1, new int[]{1475, 5120, 5121, 5122, 5123, 5133, 1475, 1476, 41, 951, 1017, 1401, 1402, 2313, 2314, 2315, 1016, 1550, 147, 1180, 1754, 1755, 1756, 2252, 4570, 4571, 1911, 6114, 46, 2693, 6113, 6112, 146, 149, 150, 450, 451, 1179, 1322, 1323, 1324, 1325, 1400, 2726, 2727, 3197, 138, 48, 4373, 4374, 4535, 139, 1751, 148, 1181, 6382, 2459, 2460, 2461, 2462, 2707, 2708, 6115, 6116, 3296, 6378, 1996, 3675, 3676, 6792, 6946, 7320, 7322, 7324, 7326, 7328, 1692, 6285, 6286, 6287, 6288, 6289, 6290, 6291, 6292, 6293, 6294, 6295, 6322, 6323, 6324, 6325, 6326, 6327, 6328, 6329, 6330, 6331, 6332, 3476, 1018, 1403,}, new String[]{"Birds aren't the most intelligent of creatures, but watch out for their", "sharp, stabbing beaks."}, 1, false, false),
    /**
     * The Black demons.
     */
    BLACK_DEMONS(80, new int[]{NPCs.BLACK_DEMON_84, NPCs.BLACK_DEMON_677, NPCs.BLACK_DEMON_4702, NPCs.BLACK_DEMON_4703, NPCs.BLACK_DEMON_4704, NPCs.BLACK_DEMON_4705, NPCs.BALFRUG_KREEYATH_6208}, new String[]{"Black Demons are magic creatures that are weak to magic attacks.", "They're the strongest demon and very dangerous."}, 1, false, false),
    /**
     * The Black dragons.
     */
    BLACK_DRAGONS(80, new int[]{NPCs.BLACK_DRAGON_54, NPCs.BLACK_DRAGON_4673, NPCs.BLACK_DRAGON_4674, NPCs.BLACK_DRAGON_4675, NPCs.BLACK_DRAGON_4676, NPCs.BABY_BLACK_DRAGON_3376, NPCs.KING_BLACK_DRAGON_50}, new String[]{"Black dragons are the strongest dragons", "watch out for their fiery breath"}, 1, false, true, 40 | 80 << 16),
    /**
     * The Bloodvelds.
     */
    BLOODVELDS(50, new int[]{NPCs.BLOODVELD_1618, NPCs.BLOODVELD_1619, NPCs.BLOODVELD_6215, NPCs.MUTATED_BLOODVELD_7643, NPCs.MUTATED_BLOODVELD_7642}, new String[]{"Bloodvelds are strange demonic creatures, they use their long rasping tongue", "to feed on just about anything they can find."}, 50, false, false),
    /**
     * The Blue dragons.
     */
    BLUE_DRAGONS(65, new int[]{NPCs.BLUE_DRAGON_55, NPCs.BLUE_DRAGON_4681, NPCs.BLUE_DRAGON_4682, NPCs.BLUE_DRAGON_4683, NPCs.BLUE_DRAGON_4684, NPCs.BLUE_DRAGON_5178, NPCs.BABY_BLUE_DRAGON_52, NPCs.BABY_BLUE_DRAGON_4665, NPCs.BABY_BLUE_DRAGON_4666}, new String[]{"Blue dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their fiery breath."}, 1, false, true),
    /**
     * The Brine rats.
     */
    BRINE_RATS(45, new int[]{NPCs.BRINE_RAT_3707}, new String[]{"Small little creatures they are, yet so very", "powerful."}, 47, false, false),
    /**
     * The Bronze dragons.
     */
    BRONZE_DRAGONS(75, new int[]{NPCs.BRONZE_DRAGON_1590}, new String[]{"Bronze dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their fiery breath."}, 1, false, true, 30 | 60 << 16),
    /**
     * The Catablepons.
     */
    CATABLEPONS(35, new int[]{NPCs.CATABLEPON_4397, NPCs.CATABLEPON_4398, NPCs.CATABLEPON_4399}, new String[]{"They use the magic spell Weaken to drain up to 15% of their", "opponent's maximum Strength level."}, 1, false, false),
    /**
     * The Cave bug.
     */
    CAVE_BUG(1, new int[]{NPCs.CAVE_BUG_1832, NPCs.CAVE_BUG_5750}, new String[]{"It regenerates life points quickly and seems to be a good", "herblore monster."}, 7, false, false),
    /**
     * The Cave crawlers.
     */
    CAVE_CRAWLERS(10, new int[]{NPCs.CAVE_CRAWLER_1600, NPCs.CAVE_CRAWLER_1601, NPCs.CAVE_CRAWLER_1602, NPCs.CAVE_CRAWLER_1603}, new String[]{"The poisonous parts of them are presumably removed."}, 10, false, false),
    /**
     * The Cave horrors.
     */
    CAVE_HORRORS(85, new int[]{NPCs.CAVE_HORROR_4353, NPCs.CAVE_HORROR_4354, NPCs.CAVE_HORROR_4355, NPCs.CAVE_HORROR_4356, NPCs.CAVE_HORROR_4357,}, new String[]{"A Cave horror wears a creepy mask, it is", "preferred to wear a witchwood icon."}, 58, "Cabin Fever"),
    /**
     * The Cave slimes.
     */
    CAVE_SLIMES(15, new int[]{NPCs.CAVE_SLIME_1831}, new String[]{"These are lesser versions of jellies, watch out they can poison you."}, 17, false, false),
    /**
     * The Cockatrices.
     */
    COCKATRICES(25, new int[]{NPCs.COCKATRICE_1620, NPCs.COCKATRICE_1621, NPCs.COCKATRICE_4227,}, new String[]{"A Mirror shield is necessary when", "fighting these monsters."}, 25, false, false),
    /**
     * The Cows.
     */
    COWS(5, new int[]{NPCs.COW_CALF_1766, NPCs.COW_CALF_1768, NPCs.COW_CALF_2310, NPCs.COW_81, NPCs.COW_397, NPCs.COW_955, NPCs.COW_1767, NPCs.COW_3309}, new String[]{"Cow's may seem stupid, however they know more then", "you think. Don't under estimate them."}, 1, false, false),
    /**
     * The Crawling hand.
     */
    CRAWLING_HAND(1, new int[]{NPCs.CRAWLING_HAND_1648, NPCs.CRAWLING_HAND_1649, NPCs.CRAWLING_HAND_1650, NPCs.CRAWLING_HAND_1651, NPCs.CRAWLING_HAND_1652, NPCs.CRAWLING_HAND_1653, NPCs.CRAWLING_HAND_1654, NPCs.CRAWLING_HAND_1655, NPCs.CRAWLING_HAND_1656, NPCs.CRAWLING_HAND_1657, NPCs.CRAWLING_HAND_4226, NPCs.SKELETAL_HAND_7640, NPCs.ZOMBIE_HAND_7641}, new String[]{"Crawling Hands are undead severed hands, fast and", "dexterous they claw their victims."}, 5, true, false),
    /**
     * The Crocodiles.
     */
    CROCODILES(50, new int[]{NPCs.CROCODILE_1993, NPCs.CROCODILE_6779}, new String[]{"Crocodiles can be found near water and marshes in and near the Kharidian Desert."}, 1, false, false),
    /**
     * The Cyclopes.
     */
    CYCLOPES(25, new int[]{NPCs.CYCLOPS_116, NPCs.CYCLOPS_4291, NPCs.CYCLOPS_4292, NPCs.CYCLOPS_6078, NPCs.CYCLOPS_6079, NPCs.CYCLOPS_6080, NPCs.CYCLOPS_6081, NPCs.CYCLOPS_6269, NPCs.CYCLOPS_6270}, new String[]{"Large one eyed creatures who normally wield a", "large mallet."}, 1, false, false),
    /**
     * The Dagannoths.
     */
    DAGANNOTHS(75, new int[]{NPCs.DAGANNOTH_1338, NPCs.DAGANNOTH_MOTHER_1348, NPCs.DAGANNOTH_MOTHER_1349, NPCs.DAGANNOTH_MOTHER_1350, NPCs.DAGANNOTH_MOTHER_1351, NPCs.DAGANNOTH_MOTHER_1352, NPCs.DAGANNOTH_MOTHER_1353, NPCs.DAGANNOTH_MOTHER_1354, NPCs.DAGANNOTH_MOTHER_1355, NPCs.DAGANNOTH_MOTHER_1356, NPCs.DAGANNOTH_1339, NPCs.DAGANNOTH_1340, NPCs.DAGANNOTH_1341, NPCs.DAGANNOTH_1342, NPCs.DAGANNOTH_1343, NPCs.DAGANNOTH_1344, NPCs.DAGANNOTH_1345, NPCs.DAGANNOTH_1346, NPCs.DAGANNOTH_1347, NPCs.DAGANNOTH_SPAWN_2454, NPCs.DAGANNOTH_2455, NPCs.DAGANNOTH_2456, NPCs.DAGANNOTH_SUPREME_2881, NPCs.DAGANNOTH_PRIME_2882, NPCs.DAGANNOTH_REX_2883, NPCs.DAGANNOTH_2887, NPCs.DAGANNOTH_2888, NPCs.DAGANNOTH_3591}, new String[]{"There are many types of Dagannoth, the most powerful being the three Dagannoth Kings."}, 1, false, false),
    /**
     * The Dark beasts.
     */
    DARK_BEASTS(90, new int[]{NPCs.DARK_BEAST_2783}, new String[]{"A dark beast can attack using magic or melee."}, 90, false, false),
    /**
     * The Desert lizards.
     */
    DESERT_LIZARDS(15, new int[]{NPCs.LIZARD_2803, NPCs.DESERT_LIZARD_2804, NPCs.DESERT_LIZARD_2805, NPCs.DESERT_LIZARD_2806, NPCs.SMALL_LIZARD_2807}, new String[]{"Desert lizards are big Slayer monsters found in the Kharidian Desert."}, 22, false, false),
    /**
     * The Dog.
     */
    DOG(15, new int[]{NPCs.GUARD_DOG_99, NPCs.GUARD_DOG_3582, NPCs.GUARD_DOG_6374, NPCs.JACKAL_1994, NPCs.WILD_DOG_1593, NPCs.WILD_DOG_1594, NPCs.GUARD_DOG_3582}, new String[]{"Dogs are much like Wolves, they are", "pack creatures which will hunt in groups."}, 1, false, false),
    /**
     * The Dust devils.
     */
    DUST_DEVILS(70, new int[]{NPCs.DUST_DEVIL_1624}, new String[]{"Dust Devils use clouds of dust, sand, ash and whatever", "else they can inhale to blind and disorientate", "their victims."}, 65, false, false),
    /**
     * The Dwarf.
     */
    DWARF(6, new int[]{118, 120, 121, 382, 3219, 3220, 3221, 3268, 3269, 3270, 3271, 3272, 3273, 3274, 3275, 3294, 3295, 4316, 5880, 5881, 5882, 5883, 5884, 5885, 2130, 2131, 2132, 2133, 3276, 3277, 3278, 3279, 119, 2423}, new String[]{"They are slightly resistant to Magic attacks", "and are not recommended for low levels."}, 1, false, false),
    /**
     * The Earth warriors.
     */
    EARTH_WARRIORS(35, new int[]{NPCs.EARTH_WARRIOR_124}, new String[]{"An Earth warrior is a monster made of earth", " which fights using melee."}, 1, false, false),
    /**
     * The Elves.
     */
    ELVES(70, new int[]{NPCs.ELF_WARRIOR_1183, NPCs.ELF_WARRIOR_1184, NPCs.ELF_WARRIOR_2359, NPCs.ELF_WARRIOR_2360, NPCs.ELF_WARRIOR_2361, NPCs.ELF_WARRIOR_2362, NPCs.ELF_WARRIOR_7438, NPCs.ELF_WARRIOR_7439, NPCs.ELF_WARRIOR_7440, NPCs.ELF_WARRIOR_7441}, new String[]{"Elves are agile creatures."}, 1, false, false),
    /**
     * The Fire giants.
     */
    FIRE_GIANTS(65, new int[]{NPCs.FIRE_GIANT_110, NPCs.FIRE_GIANT_1582, NPCs.FIRE_GIANT_1583, NPCs.FIRE_GIANT_1584, NPCs.FIRE_GIANT_1585, NPCs.FIRE_GIANT_1586, NPCs.FIRE_GIANT_7003, NPCs.FIRE_GIANT_7004}, new String[]{"Like other giants, Fire Giants often wield large weapons", "learn to recognise what kind of weapon it is, and act accordingly."}, 1, false, false),
    /**
     * The Frogs.
     */
    FROGS(1, new int[]{NPCs.FROG_3783, NPCs.BIG_FROG_1829, NPCs.GIANT_FROG_1828, NPCs.FROGEEL_5593, NPCs.PLAGUE_FROG_1997}, new String[]{"Frogs will attack and move faster than the average human. You can find frogs in the Lumbridge swamp and if you venture into the swamp caves you can find giant frogs."}, 1, false, false),
    /**
     * The Flesh crawlers.
     */
    FLESH_CRAWLERS(15, new int[]{NPCs.FLESH_CRAWLER_4389, NPCs.FLESH_CRAWLER_4390, NPCs.FLESH_CRAWLER_4391}, new String[]{"Flesh crawlers are medium level monsters found on", "level 2 of the Stronghold of Security."}, 1, false, false),
    /**
     * The Gargoyles.
     */
    GARGOYLES(80, new int[]{NPCs.GARGOYLE_1610, NPCs.SWARMING_TUROTH_1611, NPCs.GARGOYLE_6389}, new String[]{"Gargoyles are winged creatures of stone. You'll need to fight them to", "near death before breaking them apart with a rock hammer."}, 75, false, false),
    /**
     * The Ghosts.
     */
    GHOSTS(13, new int[]{103, 104, 491, 1541, 1549, 2716, 2931, 4387, 388, 5342, 5343, 5344, 5345, 5346, 5347, 5348, 1698, 5349, 5350, 5351, 5352, 5369, 5370, 5371, 5372, 5373, 5374, 5572, 6094, 6095, 6096, 6097, 6098, 6504, 13645, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481}, new String[]{"A Ghost is an undead monster that is found", "in various places and dungeons. "}, 1, false, false),
    /**
     * The Ghouls.
     */
    GHOULS(25, new int[]{NPCs.GHOUL_1218, NPCs.GHOUL_CHAMPION_3059}, new String[]{"Ghouls are a humanoid race and the descendants of a long-dead society", "that degraded to the point that its people ate their dead."}, 1, false, false),
    /**
     * The Goblins.
     */
    GOBLINS(1, new int[]{100, 101, 102, 444, 445, 489, 1769, 1770, 1771, 1772, 1773, 1774, 1775, 1776, 2274, 2275, 2276, 2277, 2278, 2279, 2280, 2281, 2678, 2679, 2680, 2681, 3060, 3264, 3265, 3266, 3267, 3413, 3414, 3415, 3726, 4261, 4262, 4263, 4264, 4265, 4266, 4267, 4268, 4269, 4270, 4271, 4272, 4273, 4274, 4275, 4276, 4407, 4408, 4409, 4410, 4411, 4412, 4479, 4480, 4481, 4482, 4483, 4484, 4485, 4486, 4487, 4488, 4489, 4490, 4491, 4492, 4499, 4633, 4634, 4635, 4636, 4637, 5786, 5824, 5855, 5856, 6125, 6126, 6132, 6133, 6279, 6280, 6281, 6282, 6283, 6402, 6403, 6404, 6405, 6406, 6407, 6408, 6409, 6410, 6411, 6412, 6413, 6414, 6415, 6416, 6417, 6418, 6419, 6420, 6421, 6422, 6423, 6424, 6425, 6426, 6427, 6428, 6429, 6430, 6431, 6432, 6433, 6434, 6435, 6436, 6437, 6438, 6439, 6440, 6441, 6442, 6443, 6444, 6445, 6446, 6447, 6448, 6449, 6450, 6451, 6452, 6453, 6454, 6455, 6456, 6457, 6458, 6459, 6460, 6461, 6462, 6463, 6464, 6465, 6466, 6467, 6490, 6491, 6492, 6493, 6494, 6495, 6496, 6497}, new String[]{"Goblins are mostly just annoying, but they can be vicious.", "Watch out for the spears they sometimes carry."}, 1, false, false),
    /**
     * The Goraks.
     */
    GORAKS(70, new int[]{NPCs.GORAK_4418, NPCs.GORAK_6218}, new String[]{"Goraks can be tough monsters to fight. Be prepared."}, 1, false, false),
    /**
     * The Greater demons.
     */
    GREATER_DEMONS(75, new int[]{NPCs.GREATER_DEMON_83, NPCs.GREATER_DEMON_4698, NPCs.GREATER_DEMON_4699, NPCs.GREATER_DEMON_4700, NPCs.GREATER_DEMON_4701, NPCs.TSTANON_KARLAK_6204}, new String[]{"Greater Demons are magic creatures so they are weak to magical attacks.", "They're the strongest demon and very dangerous."}, 1, false, false),
    /**
     * The Green dragons.
     */
    GREEN_DRAGONS(52, new int[]{NPCs.GREEN_DRAGON_941, NPCs.GREEN_DRAGON_4677, NPCs.GREEN_DRAGON_4678, NPCs.GREEN_DRAGON_4679, NPCs.GREEN_DRAGON_4680, NPCs.BRUTAL_GREEN_DRAGON_5362, NPCs.ELVARG_742}, new String[]{"Green dragons are very powerful, they have fierce", "fiery breath."}, 1, false, true),
    /**
     * The Harpie bug swarms.
     */
    HARPIE_BUG_SWARMS(45, new int[]{NPCs.HARPIE_BUG_SWARM_3153}, new String[]{"Harpie Bug Swarms are insectoid Slayer monsters."}, 33, false, false),
    /**
     * The Hellhounds.
     */
    HELLHOUNDS(75, new int[]{NPCs.HELLHOUND_49, NPCs.HELLHOUND_3586, NPCs.HELLHOUND_6210}, new String[]{"Hellhounds are mid to high level demons."}, 1, false, false),
    /**
     * The Hill giants.
     */
    HILL_GIANTS(25, new int[]{NPCs.HILL_GIANT_117, NPCs.HILL_GIANT_4689, NPCs.HILL_GIANT_4690, NPCs.GIANT_CHAMPION_3058, NPCs.HILL_GIANT_4691, NPCs.HILL_GIANT_4692, NPCs.HILL_GIANT_4693}, new String[]{"Hill giants can hit up to 19 damage, and they only attack with Melee."}, 1, false, false),
    /**
     * The Hobgoblins.
     */
    HOBGOBLINS(20, new int[]{NPCs.HOBGOBLIN_122, NPCs.HOBGOBLIN_123, NPCs.HOBGOBLIN_2685, NPCs.HOBGOBLIN_2686, NPCs.HOBGOBLIN_CHAMPION_3061, NPCs.REVENANT_HOBGOBLIN_6608, NPCs.REVENANT_HOBGOBLIN_6642, NPCs.REVENANT_HOBGOBLIN_6661, NPCs.REVENANT_HOBGOBLIN_6684, NPCs.REVENANT_HOBGOBLIN_6710,  NPCs.REVENANT_HOBGOBLIN_6722,  NPCs.REVENANT_HOBGOBLIN_6727,  NPCs.HOBGOBLIN_2687,  NPCs.HOBGOBLIN_2688,  NPCs.HOBGOBLIN_3583,  NPCs.HOBGOBLIN_4898, NPCs.HOBGOBLIN_6275}, new String[]{"Mysterious goblin like creatures."}, 1, false, false),
    /**
     * The Ice fiends.
     */
    ICE_FIENDS(20, new int[]{NPCs.ICEFIEND_3406, NPCs.ICEFIEND_6217, NPCs.ICEFIEND_7714, NPCs.ICEFIEND_7715, NPCs.ICEFIEND_7716}, new String[]{"An Icefiend is a monster found on top of Ice Mountain."}, 1, false, false),
    /**
     * The Ice giants.
     */
    ICE_GIANTS(50, new int[]{NPCs.ICE_GIANT_111, NPCs.ICE_GIANT_3072, NPCs.ICE_GIANT_4685, NPCs.ICE_GIANT_4686, NPCs.ICE_GIANT_4687}, new String[]{"Ice Giants often wield large weapons, learn to recognise", "what kind of weapon it is, and act accordingly"}, 1, false, false),
    /**
     * The Ice trolls.
     */
    ICE_TROLLS(60, new int[]{ NPCs.ICE_TROLL_RUNT_5473, NPCs.ICE_TROLL_RUNT_5521, NPCs.ICE_TROLL_RUNT_5525, NPCs.ICE_TROLL_MALE_5474, NPCs.ICE_TROLL_MALE_5522, NPCs.ICE_TROLL_MALE_5526, NPCs.ICE_TROLL_FEMALE_5475, NPCs.ICE_TROLL_FEMALE_5523, NPCs.ICE_TROLL_FEMALE_5527, NPCs.ICE_TROLL_GRUNT_5476, NPCs.ICE_TROLL_GRUNT_5524, NPCs.ICE_TROLL_GRUNT_5528}, new String[]{"Trolls have a crushing attack, it's bets to wear a high crushing defence."}, 1, false, false),
    /**
     * The Ice warrior.
     */
    ICE_WARRIOR(45, new int[]{NPCs.ICE_WARRIOR_125, NPCs.ICE_WARRIOR_145, NPCs.ICE_WARRIOR_3073}, new String[]{"Ice warriors, are cold majestic creatures."}, 1, false, false),
    /**
     * The Infernal mages.
     */
    INFERNAL_MAGES(40, new int[]{NPCs.INFERNAL_MAGE_1643, NPCs.INFERNAL_MAGE_1644, NPCs.INFERNAL_MAGE_1645, NPCs.INFERNAL_MAGE_1646, NPCs.INFERNAL_MAGE_1647}, new String[]{"Infernal Mages are dangerous spell users, beware of their magic", "spells an go properly prepared"}, 45, false, false),
    /**
     * The Iron dragons.
     */
    IRON_DRAGONS(80, new int[]{NPCs.IRON_DRAGON_1591}, new String[]{"Iron dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their fiery breath."}, 1, false, true, 40 | 59 << 16),
    /**
     * The Jellies.
     */
    JELLIES(57, new int[]{NPCs.JELLY_1637, NPCs.JELLY_1638, NPCs.JELLY_1639, NPCs.JELLY_1640, NPCs.JELLY_1641, NPCs.JELLY_1642}, new String[]{"Jellies are nasty cube-like gelatinous creatures which", "absorb everything they come across into themselves."}, 52, false, false),
    /**
     * The Jungle horrors.
     */
    JUNGLE_HORRORS(65, new int[]{NPCs.JUNGLE_HORROR_4348, NPCs.JUNGLE_HORROR_4349, NPCs.JUNGLE_HORROR_4350, NPCs.JUNGLE_HORROR_4351, NPCs.JUNGLE_HORROR_4352}, new String[]{"Jungle Horrors can be found all over Mos Le'Harmless.", "They are strong and aggressive, so watch out!"}, 1, false, false),
    /**
     * The Kalphites.
     */
    KALPHITES(15, new int[]{NPCs.KALPHITE_WORKER_1153, NPCs.KALPHITE_SOLDIER_1154, NPCs.KALPHITE_GUARDIAN_1155, NPCs.KALPHITE_WORKER_1156, NPCs.KALPHITE_GUARDIAN_1157, NPCs.KALPHITE_QUEEN_1159, NPCs.KALPHITE_QUEEN_1160, NPCs.KALPHITE_LARVA_1161}, new String[]{"Kalaphite are large insects which live in great hives under the desert sands."}, 1, false, false),
    /**
     * The Kurasks.
     */
    KURASKS(65, new int[]{NPCs.KURASK_1608, NPCs.KURASK_1609, NPCs.KURASK_4229, NPCs.KURASK_MINION_7805, NPCs.KURASK_OVERLORD_7797}, new String[]{"A kurask is a very quick creature."}, 70, false, false),
    /**
     * The Lesser demons.
     */
    LESSER_DEMONS(60, new int[]{NPCs.LESSER_DEMON_82, NPCs.KRIL_TSUTSAROTH_6203, NPCs.LESSER_DEMON_CHAMPION_3064, NPCs.LESSER_DEMON_4694, NPCs.LESSER_DEMON_4695, NPCs.ZAKLN_GRITCH_6206, NPCs.LESSER_DEMON_CHAMPION_3064, NPCs.LESSER_DEMON_4696, NPCs.LESSER_DEMON_4697, NPCs.LESSER_DEMON_6101}, new String[]{"Lesser Demons are magic creatures so they are weak to magical attacks."}, 1, false, false),
    /**
     * The Mithril dragons.
     */
    MITHRIL_DRAGONS(60, new int[]{NPCs.MITHRIL_DRAGON_5363}, new String[]{"Mithril dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their fiery breath."}, 1, false, true, 5 | 9 << 16),
    /**
     * The Minotaurs.
     */
    MINOTAURS(7, new int[]{NPCs.MINOTAUR_4404, NPCs.MINOTAUR_4405, NPCs.MINOTAUR_4406}, new String[]{"Minotaurs are large manlike creatures but you'll", "want to be careful of their horns."}, 1, false, false),
    /**
     * The Monkeys.
     */
    MONKEYS(1, new int[]{132, 1463, 1464, 2301, 4344, 4363, 6943, 7211, 7213, 7215, 7217, 7219, 7221, 7223, 7225, 7227, 1455, 1459, 1460, 1456, 1457, 1458}, new String[]{"Small agile creatures, watch out they pinch!"}, 1, false, false),
    /**
     * The Moss giants.
     */
    MOSS_GIANTS(40, new int[]{NPCs.MOSS_GIANT_112, NPCs.MOSS_GIANT_1587, NPCs.MOSS_GIANT_1588, NPCs.MOSS_GIANT_1681, NPCs.MOSS_GIANT_4534, NPCs.MOSS_GIANT_4688, NPCs.MOSS_GIANT_4706}, new String[]{"They are known to carry large sticks."}, 1, false, false),
    /**
     * The Nechryaels.
     */
    NECHRYAELS(85, new int[]{NPCs.NECHRYAEL_1613}, new String[]{"Nechryael are demons of decay which summon small winged beings which", "help them fight their victims."}, 80, false, false),
    /**
     * The Ogres.
     */
    OGRES(40, new int[]{115, 374, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2051, 2052, 2053, 2054, 2055, 2056, 2057, NPCs.SLASH_BASH_2060, 2801, 3419, 7078, 7079, 7080, 7081, 7082}, new String[]{"Ogres are brutal creatures, favouring large blunt maces and clubs", "they often attack without warning."}, 1, false, false),
    /**
     * The Otherwordly being.
     */
    OTHERWORDLY_BEING(40, new int[]{NPCs.OTHERWORLDLY_BEING_126}, new String[]{"A creature filled with everlasting power."}, 1, false, false),
    /**
     * The Pyrefiends.
     */
    PYREFIENDS(25, new int[]{1633, 1634, 1635, 1636, 6216, 6631, 6641, 6660, 6668, 6683, 6709, 6721,}, new String[]{"A scorching hot creature, watch out!"}, 30, false, false),
    /**
     * The Rats.
     */
    RATS(1, new int[]{2682, 2980, 2981, 3007, 88, 224, 4928, 4929, 4936, 4937, 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 4396, 4415, 7202, 7204, 7417, 7461, 87, 446, 950, 4395, 4922, 4923, 4924, 4925, 4926, 4927, 4942, 4943, 4944, 4945, 86, 87, 446, 950, 4395, 4922, 4923, 4924, 4925, 4926, 4927, 4942, 4943, 4944, 4945}, new String[]{"Quick little rodents!"}, 1, false, false),
    /**
     * The Rock slugs.
     */
    ROCK_SLUGS(20, new int[]{NPCs.ROCKSLUG_1631, NPCs.ROCKSLUG_1632}, new String[]{"A rock slug can leave behind a trail of his presence.."}, 20, false, false),
    /**
     * The Scorpions.
     */
    SCORPIONS(7, new int[]{NPCs.SCORPION_107, NPCs.SCORPION_1477, NPCs.SCORPION_4402, NPCs.SCORPION_4403, NPCs.KING_SCORPION_144}, new String[]{"A scorpion makes a piercing sound, watch out for", "its long sharp tail."}, 1, false, false),
    /**
     * The Shade.
     */
    SHADE(30, new int[]{NPCs.SHADE_3617, NPCs.LOAR_SHADE_1241, NPCs.RIYL_SHADE_1246, NPCs.ASYN_SHADE_1248, NPCs.FIYR_SHADE_1250, NPCs.SHADE_428, NPCs.LOAR_SHADOW_1240}, new String[]{"Shades are dark and mysterious", "they hide in the shadows so be wary of ambushes."}, 1, true, false),
    /**
     * The Skeletons.
     */
    SKELETONS(15, new int[]{90, 91, 92, 93, 94, 459, 1471, 1575, 1973, 2036, 2037, 2715, 2717, 3065, 3151, 3291, 3581, 3697, 3698, 3699, 3700, 3701, 3702, 3703, 3704, 3705, 3844, 3850, 3851, 4384, 4385, 4386, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341, 5359, 5365, 5366, 5367, 5368, 5381, 5384, 5385, 5386, 5387, 5388, 5389, 5390, 5391, 5392, 5411, 5412, 5422, 6091, 6092, 6093, 6103, 6104, 6105, 6106, 6107, 6764, 6765, 6766, 6767, 6768, 2050, 2056, 2057, 1539, 7640}, new String[]{"Skeletons are undead monsters found in various locations."}, 1, true, false),
    /**
     * The Spiders.
     */
    SPIDERS(1, new int[]{61, 1004, 1221, 1473, 1474, 63, 4401, 2034, 977, 7207, 134, 1009, 59, 60, 4400, 58, 62, 1478, 2491, 2492, 6376, 6377,}, new String[]{"Level 24 spiders are aggressive and can hit up to 60 life points."}, 1, false, false),
    /**
     * The Spirtual mages.
     */
    SPIRTUAL_MAGES(60, new int[]{NPCs.SPIRITUAL_MAGE_6221, NPCs.SPIRITUAL_MAGE_6231, NPCs.SPIRITUAL_MAGE_6257, NPCs.SPIRITUAL_MAGE_6278}, new String[]{"They are dangerous, they hit with mage."}, 83, false, false),
    /**
     * The Spirtual rangers.
     */
    SPIRTUAL_RANGERS(60, new int[]{NPCs.SPIRITUAL_RANGER_6220, NPCs.SPIRITUAL_RANGER_6230, NPCs.SPIRITUAL_RANGER_6256, NPCs.SPIRITUAL_RANGER_6276}, new String[]{"They are dangerous, they hit with range."}, 63, false, false),
    /**
     * The Spirtual warriors.
     */
    SPIRTUAL_WARRIORS(60, new int[]{NPCs.SPIRITUAL_WARRIOR_6219, NPCs.SPIRITUAL_WARRIOR_6229, NPCs.SPIRITUAL_WARRIOR_6255, NPCs.SPIRITUAL_WARRIOR_6277,}, new String[]{"They are dangerous, they hit with melee."}, 68, false, false),
    /**
     * The Steel dragons.
     */
    STEEL_DRAGONS(85, new int[]{NPCs.STEEL_DRAGON_1592, NPCs.STEEL_DRAGON_3590}, new String[]{"Steel dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their fiery breath."}, 1, false, true, 10 | 20 << 16),
    /**
     * The Trolls.
     */
    TROLLS(60, new int[]{72, 3584, 1098, 1096, 1097, 1095, 1101, 1105, 1102, 1103, 1104, 1130, 1131, 1132, 1133, 1134, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1138, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1935, 1936, 1937, 1938, 1939, 1940, 1941, 1942, 3840, 3841, 3842, 3843, 3845, 1933, 1934, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 391, 392, 393, 394, 395, 396}, new String[]{"Trolls have a crushing attack, it's bets to wear a high crushing defence."}, 1, false, false),
    /**
     * The Turoths.
     */
    TUROTHS(60, new int[]{NPCs.TUROTH_1622, NPCs.SWARMING_TUROTH_1611, NPCs.TUROTH_1623, NPCs.TUROTH_1626, NPCs.TUROTH_1627, NPCs.TUROTH_1628, NPCs.TUROTH_1629, NPCs.TUROTH_1630, NPCs.MIGHTIEST_TUROTH_7800}, new String[]{"Turoths are Slayer monsters that require a Slayer level of 55 to kill"}, 55, false, false),
    /**
     * The Tzhaar.
     */
    TZHAAR(45, new int[]{2591, 2592, 2593, 2745, 2594, 2595, 2596, 2597, 2604, 2605, 2606, 2607, 2608, 2609, 7755, 7753, 2598, 2599, 2600, 2601, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2624, 2617, 2618, 2625, 2602, 2603, 7754, 7767, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2624, 2625, 2627, 2628, 2629, 2630, 2631, 2632, 7746, 7747, 7748, 7749, 7750, 7751, 7752, 7753, 7754, 7755, 7756, 7757, 7758, 7759, 7760, 7761, 7762, 7763, 7764, 7765, 7766, 7767, 7768, 7769, 7770, 7771, 7747, 7747, 7748, 7749, 7750, 7751, 7752, 7753, 7757, 7765, 7769, 7768}, new String[]{"Young Tzhaar's of the century are furious with your kind."}, 1, false, false),
    /**
     * The Zygomites.
     */
    ZYGOMITES(10, new int[]{NPCs.ZYGOMITE_3346, NPCs.ZYGOMITE_3347}, new String[]{"Mutated zygomites are hard to destroy and attack with mainly magical damage. " + "They regenerate quickly, so you will need to finish them off with fungicide. " + "We have a bit of a problem with them here in Zanaris."}, 1, true, false),
    /**
     * The Shades.
     */
    SHADES(30, new int[]{NPCs.SHADE_3617, NPCs.LOAR_SHADE_1241, NPCs.RIYL_SHADE_1246, NPCs.ASYN_SHADE_1248, NPCs.FIYR_SHADE_1250, NPCs.SHADE_428, NPCs.LOAR_SHADOW_1240}, new String[]{"Shades are undead - The town of Mort'ton in Morytania is plagued by these creatures, " + "so help if you can. There are some shades in the Stronghold of Security too, but you won't " + "learn much from fighting those; stick to Mort'ton."}, 1, true, false),
    /**
     * The Mogres.
     */
    MOGRES(1, new int[]{NPCs.MOGRE_114}, new String[]{"Mogres are a type of aquatic ogre that is often mistaken for a giant mudskipper. " + "You have to force them out of the water with a fishing explosive. " + "You can find them on the peninsula to the south of Port Sarim."}, 32, false, false),
    /**
     * The Suqahs.
     */
    SUQAHS(65, new int[]{NPCs.SUQAH_4527, NPCs.SUQAH_4528, NPCs.SUQAH_4529, NPCs.SUQAH_4530, NPCs.SUQAH_4531, NPCs.SUQAH_4532, NPCs.SUQAH_4533}, new String[]{"Suquah are big, angry folk that inhabit Lunar Isle."}, 1, "Lunar Diplomacy"),
    /**
     * The Vampires.
     */
    VAMPIRES(35, new int[]{NPCs.VAMPIRE_1023, NPCs.VAMPIRE_1220, NPCs.VAMPIRE_1223, NPCs.VAMPIRE_1225, NPCs.VAMPIRE_6214}, new String[]{"Vampires are equipped with large fangs,", "they can do serious damage."}, 1, false, false),
    /**
     * The Waterfiends.
     */
    WATERFIENDS(75, new int[]{NPCs.WATERFIEND_5361}, new String[]{"A waterfiend takes no damage from fire!"}, 1, false, false),
    /**
     * The Werewolfs.
     */
    WEREWOLFS(60, new int[]{1665, 6006, 6007, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 6015, 6016, 6017, 6018, 6019, 6020, 6021, 6022, 6023, 6024, 6025, 6212, 6213, 6607, 6609, 6614, 6617, 6625, 6632, 6644, 6663, 6675, 6686, 6701, 6712, 6724, 6728,}, new String[]{"There temper is alot more nasty then a regular wolf!"}, 1, false, false),
    /**
     * The Wolves.
     */
    WOLVES(20, new int[]{95, 96, 97, 141, 142, 143, 839, 1198, 1330, 1558, 1559, 1951, 1952, 1953, 1954, 1955, 1956, 4413, 4414, 6046, 6047, 6048, 6049, 6050, 6051, 6052, 6829, 6830, 7005}, new String[]{"Wolves are more agressive then dog's."}, 1, false, false),
    /**
     * The Zombies.
     */
    ZOMBIES(10, new int[]{73, 74, 75, 76, 2714, 2863, 2866, 2869, 2878, 3622, 4392, 4393, 4394, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5312, 5313, 5314, 5315, 5316, 5317, 5318, 5319, 5320, 5321, 5322, 5323, 5324, 5325, 5326, 5327, 5328, 5329, 5330, 5331, 5375, 5376, 5377, 5378, 5379, 5380, 5393, 5394, 5395, 5396, 5397, 5398, 5399, 5400, 5401, 5402, 5403, 5404, 5405, 5406, 5407, 5408, 5409, 5410, 6099, 6100, 6131, 8149, 8150, 8151, 8152, 8153, 8159, 8160, 8161, 8162, 8163, 8164, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2051, 2052, 2053, 2054, 2055, 7641, 1465, 1466, 1467, 2837, 2838, 2839, 2840, 2841, 2842, 5629, 5630, 5631, 5632, 5633, 5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643, 5644, 5645, 5646, 5647, 5648, 5649, 5650, 5651, 5652, 5653, 5654, 5655, 5656, 5657, 5658, 5659, 5660, 5661, 5662, 5663, 5664, 5665, 2843, 2844, 2845, 2846, 2847, 2848}, new String[]{"Zombies are creatures with no brain, they do hit farley", "high though."}, 1, true, false),
    /**
     * The Jad.
     */
    JAD(90, new int[]{NPCs.TZTOK_JAD_2745}, new String[]{"TzTok-Jad is the king of the Fight Caves."}, 1, false, false, 1 | 1 << 16),
    /**
     * The Chaos elemental.
     */
    CHAOS_ELEMENTAL(90, new int[]{NPCs.CHAOS_ELEMENTAL_3200}, new String[]{"The Chaos Elemental is located in the deep Wilderness."}, 1, false, false, 5 | 25 << 16),
    /**
     * The Giant mole.
     */
    GIANT_MOLE(75, new int[]{NPCs.GIANT_MOLE_3340}, new String[]{"Fighting the Giant Mole will require a light source."}, 1, false, false, 5 | 25 << 16),
    /**
     * The King black dragon.
     */
    KING_BLACK_DRAGON(75, new int[]{NPCs.KING_BLACK_DRAGON_50}, new String[]{"The King Black Dragon is located in the deep wilderness."}, 1, false, true, 5 | 25 << 16),
    /**
     * The Commander zilyana.
     */
    COMMANDER_ZILYANA(90, new int[]{NPCs.COMMANDER_ZILYANA_6247}, new String[]{"Commander Zilyana is one of the four Godwars bosses."}, 1, false, false, 5 | 25 << 16),
    /**
     * The General grardoor.
     */
    GENERAL_GRARDOOR(90, new int[]{NPCs.GENERAL_GRAARDOR_6260}, new String[]{"General Grardoor is one of the four Godwars bosses."}, 1, false, false, 5 | 25 << 16),
    /**
     * The Kril tsutsaroth.
     */
    KRIL_TSUTSAROTH(90, new int[]{NPCs.KRIL_TSUTSAROTH_6203}, new String[]{"Kril Tsutsaroth is one of the four Godwars bosses."}, 1, false, false, 5 | 25 << 16),
    /**
     * The Kree arra.
     */
    KREE_ARRA(90, new int[]{NPCs.KREEARRA_6222}, new String[]{"Kree'arra is one of the four Godwars bosses."}, 1, false, false, 5 | 25 << 16),
    /**
     * The Skeletal wyvern.
     */
    SKELETAL_WYVERN(70, new int[]{NPCs.SKELETAL_WYVERN_3068, NPCs.SKELETAL_WYVERN_3069, NPCs.SKELETAL_WYVERN_3070, NPCs.SKELETAL_WYVERN_3071}, new String[]{"A skeletal wyvern requires an elemental, mirror", "or dragonfire shield."}, 72, false, false, 24 | 39 << 16);

    /**
     * The Task map.
     */
    static final HashMap<Integer, Tasks> taskMap = new HashMap<>();

    static {
        Arrays.stream(Tasks.values()).forEach(entry -> Arrays.stream(entry.ids).forEach(id -> taskMap.putIfAbsent(id, entry)));
    }

    /**
     * The Level req.
     */
    public final int levelReq;
    /**
     * The Combat check.
     */
    public final int combatCheck;
    /**
     * The Info.
     */
    public final String[] info;
    /**
     * The Ids.
     */
    public final int[] ids;
    /**
     * The Undead.
     */
    public boolean undead = false;
    /**
     * The Dragon.
     */
    public boolean dragon = false;
    /**
     * The Amt hash.
     */
    public int amtHash;
    /**
     * The Quest req.
     */
    public String questReq = "";

    Tasks(int combatCheck, int[] ids, String[] info, int levelReq, boolean undead, boolean dragon) {
        this.levelReq = levelReq;
        this.ids = ids;
        this.info = info;
        this.undead = undead;
        this.dragon = dragon;
        this.combatCheck = combatCheck;
    }

    Tasks(int combatCheck, int[] ids, String[] info, int levelReq, boolean undead, boolean dragon, int amtHash) {
        this.levelReq = levelReq;
        this.ids = ids;
        this.info = info;
        this.undead = undead;
        this.dragon = dragon;
        this.amtHash = amtHash;
        this.combatCheck = combatCheck;
    }

    Tasks(int combatCheck, int[] ids, String[] info, int levelReq, String questReq) {
        this.combatCheck = combatCheck;
        this.ids = ids;
        this.info = info;
        this.levelReq = levelReq;
        this.questReq = questReq;
    }

    /**
     * Get npcs int [ ].
     *
     * @return the int [ ]
     */
    public int[] getNpcs() {
        return ids;
    }

    /**
     * Get tip string [ ].
     *
     * @return the string [ ]
     */
    public String[] getTip() {
        return info;
    }

    /**
     * Has quest requirements boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasQuestRequirements(Player player) {
        return questReq.equals("") || hasRequirement(player, questReq, false);
    }

    /**
     * For id tasks.
     *
     * @param id the id
     * @return the tasks
     */
    public static Tasks forId(int id) {
        return taskMap.get(id);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return NPCDefinition.forId(ids[0]).getName();
    }

}
