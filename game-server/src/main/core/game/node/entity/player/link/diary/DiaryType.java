package core.game.node.entity.player.link.diary;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The Diary type.
 */
public enum DiaryType {
    /**
     * The Karamja.
     */
    KARAMJA("Karamja", 11,
            new String[]{"Easy", "Medium", "Hard"},
            new String[][]{
                    {
                            "Pick 5 bananas from the plantation located east of the <br><br>volcano",
                            "Use the rope swing to travel to the small island north-west<br><br>of Karamja, where the moss giants are",
                            "Mine some gold from the rocks on the north-west<br><br>peninsula of Karamja",
                            "Travel to Port Sarim via the dock, east of Musa Point",
                            "Travel to Ardougne via the port near Brimhaven",
                            "Explore Cairn Island to the west of Karamja",
                            "Use the Fishing spots north of the banana plantation",
                            "Collect 5 seaweed from anywhere on Karamja",
                            "Attempt the TzHaar Fight Pits or Fight Cave",
                            "Kill a Jogre in the Pothole dungeon"
                    },
                    {
                            "Claim a ticket from the Agility arena in Brimhaven",
                            "Discover hidden wall in the dungeon below the volcano",
                            "Visit the Isle of Crandor via the dungeon below the volcano",
                            "Use Vigroy and Hajedy's cart service",
                            "Earn 100% favour in the village of Tai Bwo Wannai Cleanup",
                            "Cook a spider on stick",
                            "Charter the Lady of the Waves from Cairn Isle to Port Khazard",
                            "Cut a log from a teak tree",
                            "Cut a log from a mahogany tree",
                            "Catch a karambwan",
                            "Exchange gems, a gout tuber, trading sticks for a machete",
                            "Use the gnome glider to travel to Karamja",
                            "Grow a healthy fruit tree in the patch near Brimhaven",
                            "Trap a Horned Graahk",
                            "Chop the vines to gain deeper access to Brimhaven Dungeon",
                            "Cross the lava using the stepping stones within Brimhaven<br><br>Dungeon",
                            "Climb the stairs within Brimhaven Dungeon",
                            "Charter a ship from the shipyard in the far east of Karamja",
                            "Mine a red topaz from a gem rock"
                    },
                    {
                            "Become the Champion of the Fight Pits",
                            "Successfully kill a Ket-Zek in the Fight Caves",
                            "Eat an oomlie wrap",
                            "Craft some nature runes",
                            "Cook a karambwan thoroughly",
                            "Kill a deathwing in the dungeon under the Kharazi Jungle",
                            "Use the crossbow shortcut south of the volcano",
                            "Collect five palm leaves",
                            "Be assigned a Slayer task by Duradel or Lapalok in Shilo<br><br>Village.",
                            "Kill a metal dragon in Brimhaven Dungeon."
                    }
            },
            new Item[][]{
                    {new Item(Items.KARAMJA_GLOVES_1_11136), new Item(Items.ANTIQUE_LAMP_11137)},
                    {new Item(Items.KARAMJA_GLOVES_2_11138), new Item(Items.ANTIQUE_LAMP_11139)},
                    {new Item(Items.KARAMJA_GLOVES_3_11140), new Item(Items.ANTIQUE_LAMP_11141)}
            },
            "To start marking off tasks in your journal, speak to Pirate<br><br>Jackie the Fruit in Brimhaven, Kaleb Paramay in Shilo<br><br>Village or one of the Jungle Foresters north of the<br><br>Kharazi Jungle.",
            new int[]{NPCs.PIRATE_JACKIE_THE_FRUIT_1055, NPCs.KALEB_PARAMAYA_512, NPCs.JUNGLE_FORESTER_401}

    ),

    /**
     * The Varrock.
     */
    VARROCK("Varrock", 15,
            new String[]{"Easy", "Medium", "Hard"},
            new String[][]{
                    {
                            "Have Thessalia show you what outfits you can wear",
                            "Have Aubury teleport you to the essence mine",
                            "Mine some Iron in the south east mining patch near Varrock",
                            "Make a plank at the sawmill",
                            "Enter the second level of the Stronghold of Security",
                            "Jump over the fence south of Varrock",
                            "Chop down a dying tree in the Lumber Yard",
                            "Buy a copy of the Varrock Herald",
                            "Give a stray dog a bone",
                            "Spin a bowl on the pottery wheel and fire it in the oven in<br><br>Barbarian Village",
                            "Enter Edgeville Dungeon using the entrance to the south of<br><br>Edgeville",
                            "Move your player-owned house portal to a different location<br><br>using the Varrock estate agent",
                            "Speak to Haig Halen after obtaining at least 50 quest points",
                            "Enter the Earth Altar using an earth tiara or talisman",
                            "Have Elsie tell you a story",
                            "Mine some limestone near Paterdomus, the temple to the east<br><br>of Varrock",
                            "Catch a trout in the river to the east of Barbarian Village.",
                            "Venture through the cobwebbed corridor in Varrock Sewers",
                            "Find the highest point in Varrock"
                    },
                    {
                            "Have the Apothecary in Varrock make you a strength potion.",
                            "Enter the Champions Guild.",
                            "Take the Dagon'Hai shortcut to the chaos portal",
                            "Get a full complement of rats on your rat pole",
                            "Escape from the spider lair in Varrock Sewers with some red<br><br>spiders eggs",
                            "Use the spirit tree north of Varrock",
                            "Perform the 4 emotes from the Stronghold of Security.",
                            "Select a colour for a new kitten",
                            "Use the shortcut under the wall, north-west of the Grand<br><br>Exchange",
                            "Enter the A Soul's Bane rift",
                            "Teleport to the Digsite using a Digsite pendant",
                            "Craft an earth tiara on the Earth Altar",
                            "Pickpocket a guard in the Varrock Palace courtyard",
                            "Use the teleport to Varrock spell",
                            "Get a Slayer task from Vannaka",
                            "Buy twenty mahogany planks from the Sawmill Operator in one<br><br>transaction",
                            "Pick a fruit from the White Tree",
                            "Use the hot air balloon to travel from Varrock to somewhere<br><br>else",
                            "Get a cat training medal from Gertrude",
                            "Dial to the fairy ring west of Varrock",
                            "Browse through Oziach's Armour Shop"
                    },
                    {
                            "Pick poison ivy from your bush Farming patch in Varrock<br><br>(west of Champions' Guild)",
                            "Use the pipe shortcut in Varrock Sewers, near the moss<br><br>giants",
                            "Trade furs with the Fancy Dress Seller for a spottier cape",
                            "Smith an adamantite medium helm on the south-east anvil in<br><br>Varrock, next to Aubury's Rune Shop",
                            "Speak to Orlando Smith when you have achieved 153 kudos",
                            "Talk to Romily Weaklax and give him a wild pie",
                            "Craft an air battlestaff",
                            "Give your player-owned house a tropical wood or fancy stone<br><br>finish at the Varrock estate agent's",
                            "Make a Varrock teleport tablet on a mahogany lectern",
                            "Obtain a new set of Family Crest gauntlets from Dimintheis",
                            "Make a Waka Canoe near Edgeville",
                            "Use the Home Teleport spell in the Ancient Magicks spellbook<br><br>to teleport to Edgeville",
                            "Use the skull sceptre to teleport to Barbarian Village"
                    }
            },
            new Item[][]{
                    {new Item(Items.VARROCK_ARMOUR_1_11756), new Item(Items.ANTIQUE_LAMP_11753)},
                    {new Item(Items.VARROCK_ARMOUR_2_11757), new Item(Items.ANTIQUE_LAMP_11754)},
                    {new Item(Items.VARROCK_ARMOUR_3_11758), new Item(Items.ANTIQUE_LAMP_11755)}
            },
            "To start marking off tasks in your journal, speak to<br><br>Rat Burgiss south of the city, Reldo in the palace<br><br>library, or Vannaka in the sewers.",
            new int[]{NPCs.RAT_BURGISS_5833, NPCs.RELDO_2660, NPCs.VANNAKA_1597}

    ),

    /**
     * The Lumbridge.
     */
    LUMBRIDGE("Lumbridge", 2,
            new String[]{"Beginner", "Easy", "Medium"},
            new String[][]{
                    {
                            "Climb to the highest point in Lumbridge",
                            "Raise the flag on the roof of the Lumbridge Bank",
                            "Speak to the Duke of Lumbridge",
                            "Speak with the Doomsayer about the Warning System",
                            "Pass through the Al Kharid gate",
                            "Mine some clay in the Mining patch north of the Champions'<br><br>Guild",
                            "Make some soft clay in the Barbarian Village",
                            "Fire a pot in the kiln in the Barbarian Village potter's<br><br>house",
                            "Enter the courtyard of the spooky mansion in Draynor Village",
                            "Visit the Draynor Village market",
                            "Find out about the Rules of Conduct from the Draynor<br><br>Town Crier",
                            "Climb to the top of the Wizards' Tower",
                            "Mine some copper in the Mining spot to the south-east of<br><br>Lumbridge Swamp",
                            "Catch some shrimp in the Fishing spot to the east of<br><br>Lumbridge Swamp",
                            "Have the Fishing Tutor send you on an errand",
                            "Look through Father Aereck's selection of gravestones",
                            "Play the organ in the Lumbridge Church",
                            "Find out about the Stronghold of Security from the Lumbridge<br><br>Guide",
                            "Browse the Lumbridge General Store",
                            "Visit Fred the Farmer's chicken and sheep farm",
                            "Grind some flour in the windmill north of Lumbridge"
                    },
                    {
                            "Mine some iron ore from the Al Kharid Mining spot",
                            "Obtain a cow-hide from a cow in the field north-east of<br><br>Lumbridge",
                            "Have Ellis tan your cow-hide to make soft leather at his<br><br>shop in Al Kharid",
                            "Craft a pair of soft leather gloves",
                            "Catch a pike in the river to the east of Lumbridge Castle",
                            "Smelt a steel bar in the Lumbridge furnace",
                            "Search the shed in Lumbridge Swamp",
                            "Kill a giant rat in Lumbridge Swamp",
                            "Cut down a dead tree in Lumbridge Swamp",
                            "Light a campfire from normal logs in Lumbridge Swamp",
                            "Cook some rat meat on a campfire in Lumbridge Swamp",
                            "Craft a water rune at the Water Altar",
                            "Get a replacement Ghostspeak Amulet from Father Urhney",
                            "Taunt the demon at the top of the Wizard's Tower",
                            "Have Sedridor teleport you to the Essence Mine",
                            "Access the bank in Draynor Village",
                            "Have the Wise Old man check your bank for unnecessary<br><br>quest-related items",
                            "Discover what the Wise Old man is watching through his<br><br>telescope",
                            "Defeat a zombie in the sewers under the jail"
                    },
                    {
                            "Smith a steel longsword on the anvil in the jailhouse<br><br>sewers",
                            "Take a ride on Gnomecopter",
                            "Use the teleport Lumbridge spell",
                            "Light a willow log fire in Lumbridge Castle courtyard",
                            "Cook a lobster on the range in Lumbridge Castle kitchen",
                            "Obtain an Anti-dragonbreath shield from Duke Horacio",
                            "Cut a willow tree, east of Lumbridge Castle",
                            "Smelt a silver bar in the Lumbridge furnace",
                            "Craft a holy symbol in the Lumbridge furnace",
                            "Catch a salmon in the river to the east of Lumbridge Castle",
                            "Mine some silver from the mining spot north of Al Kharid",
                            "Mine some coal in the Mining spot south-west of Lumbridge<br><br>Swamp"
                    }
            },
            new Item[][]{
                    {new Item(Items.EXPLORERS_RING_1_13560), new Item(Items.ANTIQUE_LAMP_11185)},
                    {new Item(Items.EXPLORERS_RING_2_13561), new Item(Items.ANTIQUE_LAMP_11186)},
                    {new Item(Items.EXPLORERS_RING_3_13562), new Item(Items.ANTIQUE_LAMP_11187)}},
            "To start marking off tasks in your journal, speak to<br><br>Explorer Jack near the Lumbridge General Store, Bob<br><br>in his axe store or Ned in his house in Draynor Village.",
            new int[]{NPCs.EXPLORER_JACK_7969, NPCs.BOB_519, NPCs.NED_743}

    ),

    /**
     * The Falador.
     */
    FALADOR("Falador", 23,
            new String[]{"Easy", "Medium", "Hard"},
            new String[][]{
                    {
                            "Buy a Farming amulet from Sarah on the farm north of Port<br><br>Sarim",
                            "Buy a stat-boosting beer from a waitress in the Rising Sun<br><br>Tavern",
                            "Buy a black chainbody from Wayne's Chains, and try it on in<br><br>the shop",
                            "Climb to the top of the White Knights' Castle",
                            "Discover your family crest from Sir Renitee",
                            "Enter the mole's lair under Falador Park",
                            "Feed Ridgeley, the hairdresser's pet",
                            "Fill a bucket from the pump north of the west Falador bank",
                            "Heal an elemental wizard by casting an appropriate elemental<br><br>spell on him (Air, Water, Earth, Fire)",
                            "Kill a duck in Falador Park",
                            "Kill a highwayman on the road south of Falador",
                            "Make an air tiara",
                            "Pop a party balloon",
                            "Recharge your Prayer points at the altar south-west of Port<br><br>Sarim",
                            "Take the boat to Entrana"
                    },
                    {
                            "Craft a Fruit basket using the loom at the farm north of<br><br>Port Sarim",
                            "Crawl under Falador's south wall",
                            "Grapple up, and then jump off the north Falador wall",
                            "Increase your reputation with the White Knights by killing<br><br>a black knight",
                            "Kill an ice giant in the Asgarnian Ice Dungeon",
                            "Light a bullseye lantern in the chemist's",
                            "Pickpocket a Falador guard",
                            "Place a scarecrow to protect your sweetcorn as it grows in<br><br>the patch north of Port Sarim",
                            "Salute Sir Tiffy Cashien while wearing full initiate armour",
                            "Smith blurite crossbow limbs on Thurgo's anvil",
                            "Travel from Port Sarim to Musa Point for free (with a little<br><br>help from Charos)",
                            "Visit the Port Sarim rat pits"
                    },
                    {
                            "Ascend the Dark Wizards' Tower while wearing full proselyte<br><br>armour",
                            "Change your family crest to the Saradomin symbol",
                            "Craft 196 or more air runes simultaneously",
                            "Cut down a Yew tree or Magic tree that you grew in Falador<br><br>Park",
                            "Dial to the fairy ring on Mudskipper Point",
                            "Dye a cape pink with Pink dye from Betty in Port Sarim",
                            "Enter the Mining Guild",
                            "Kill a mogre at Mudskipper Point",
                            "Kill a skeletal wyvern in the Asgarnian Ice Dungeon",
                            "Summon an Ibis in the Port Sarim fish store"
                    }
            },
            new Item[][]{
                    {new Item(Items.FALADOR_SHIELD_1_14577), new Item(Items.ANTIQUE_LAMP_14580)},
                    {new Item(Items.FALADOR_SHIELD_2_14578), new Item(Items.ANTIQUE_LAMP_14581)},
                    {new Item(Items.FALADOR_SHIELD_3_14579), new Item(Items.ANTIQUE_LAMP_14582)}
            },
            "To start marking off tasks in your journal, speak to<br><br>Redbeard Frank outside the bar in Port Sarim, The<br><br>Chemist west of Rimmington, or Sir Vyvin's Squire in the<br><br>Falador Castle courtyard.",
            new int[]{NPCs.REDBEARD_FRANK_375, NPCs.CHEMIST_367, NPCs.SQUIRE_606}

    ),

    /**
     * The Fremennik.
     */
    FREMENNIK("Fremennik", 19,
            new String[]{"Easy", "Medium", "Hard"},
            new String[][]{
                    {
                            "Kill a cave crawler in the Fremennik Slayer Dungeon",
                            "Kill five rock crabs on the shore near Rellekka or on<br><br>Waterbirth Island",
                            "Find the highest tree on the Fremennik mainland",
                            "View the rewards in the Barbarian Assault tutorial",
                            "Speak to Otto Godblessed about barbarian training",
                            "Collect three seaweed from the shore north-east of Rellekka",
                            "Find the Hunting Expert on the northern ice plains",
                            "Catch a fish off one of Rellekka's piers",
                            "Recharge your Summoning points near Rellekka's gate",
                            "Kill an adult black unicorn"
                    },
                    {
                            "Learn of the history of the Fremennik and the Outerlanders<br><br>from Chieftain Brundt",
                            "Watch a shouting match between Fremennik Isles tower guards<br><br>(the guards can be found between Jatizso and Neitiznot in<br><br>one of the towers)",
                            "Interact with a Pet rock",
                            "Make three vials in the furnace building at Rellekka",
                            "Charm the Fossegrimen into accepting a raw bass",
                            "Wear yak-hide armour and kill an ice troll",
                            "Make cheese in the dairy churn in Rellekka",
                            "Use a fairy ring to appear on a mountaintop, near the<br><br>windswept tree",
                            "Look at Yrsa's options for recolouring your boots in her<br><br>clothes shop in Rellekka",
                            "Successfully hunt a sabre-toothed kyatt",
                            "Steal a fish from the fishing stall in the Rellekka<br><br>marketplace"
                    },
                    {
                            "Kill three dagannoths in the first layer of the Waterbirth<br><br>Island Dungeon",
                            "Wear rockshell, spined or skeletal armour and have the<br><br>locals use an honorific with your Fremennik name",
                            "Complete the Barbarian Outpost Agility Course",
                            "Mine pure essence on Lunar Isle",
                            "Make a barbarian pyre ship from arctic pine",
                            "Catch a tuna without a harpoon",
                            "Bake a pie using Magic",
                            "Kill a Mithril dragon",
                            "Get mahogany from your Etceterian subjects"
                    }
            },
            new Item[][]{
                    {new Item(Items.FREMENNIK_SEA_BOOTS_1_14571), new Item(Items.ANTIQUE_LAMP_14574)},
                    {new Item(Items.FREMENNIK_SEA_BOOTS_2_14572), new Item(Items.ANTIQUE_LAMP_14575)},
                    {new Item(Items.FREMENNIK_SEA_BOOTS_3_14573), new Item(Items.ANTIQUE_LAMP_14576)}
            },
            "To start marking off tasks in your journal, speak to<br><br>Council Workman on the bridge between the Seers' Village<br><br>and Rellekka, Yrsa in Rellekka or Advisor Ghrim on the<br><br>Miscellania.",
            new int[]{NPCs.COUNCIL_WORKMAN_1287, NPCs.YRSA_1301, NPCs.ADVISOR_GHRIM_1375}
    ),
    /**
     * The Seers village.
     */
    SEERS_VILLAGE("Seers' Village", 27,
            new String[]{"Easy", "Medium", "Hard"},
            new String[][]{
                    {
                            "Pick five flax from the flax field",
                            "Walk clockwise around the big mysterious statue",
                            "Have Sir Galahad make you a cup of tea",
                            "Take the poison chalice to King Arthur",
                            "Spin five bow strings",
                            "Fill five pots with flour from the Sinclair Mansion",
                            "Give five locals a glass of cider in the Forester's Arms",
                            "Plant some jute",
                            "Use the churn in the Sinclair Mansion garden",
                            "Buy a candle from the candle-maker",
                            "Pray at the Seers' Village altar",
                            "Catch a mackerel"
                    },
                    {
                            "Use the Sinclair Mansion to Fremennik agility shortcut",
                            "Talk to Thormac the Sorcerer about making mystic staves",
                            "Transport a full load of coal to Seers' Village",
                            "Find the highest point in Seers' Village",
                            "Defeat each type of elemental in the Elemental Workshop",
                            "Teleport to Camelot",
                            "Kill one guard on each tower of the Ranging Guild using a longbow",
                            "Have the Ranging Guild competition judge congratulate you<br><br>for acquiring over 1,000 Archery tickets",
                            "Buy something from the ticket exchange in the Ranging Guild",
                            "Use a familiar to make a fire from maple logs within Seers'<br><br>Village",
                            "Get a pet fish from Harry",
                            "Catch and cook a bass in Catherby"
                    },
                    {
                            "Teleport to the Ranging Guild",
                            "Cut five sets of yew logs",
                            "Fletch a magic shortbow in the Seers' Village Bank",
                            "Enter the Seers' Village courthouse with your Piety prayer<br><br>turned on",
                            "Use the fairy ring in McGrubor's Wood",
                            "Burn a magic log in Seers' Village",
                            "High Alch a magic shortbow in the Seers' Village bank",
                            "Catch five sharks in Catherby",
                            "Cook 5 sharks in Catherby using the Cooking gauntlets",
                            "Charge five water orbs in one go",
                            "Use the grapple shortcut to get from the water obelisk<br><br>island to Catherby shore"
                    }
            },
            new Item[][]{
                    {new Item(Items.SEERS_HEADBAND_1_14631), new Item(Items.ANTIQUE_LAMP_14633)},
                    {new Item(Items.SEERS_HEADBAND_2_14640), new Item(Items.ANTIQUE_LAMP_14634)},
                    {new Item(Items.SEERS_HEADBAND_3_14641), new Item(Items.ANTIQUE_LAMP_14635)}
            }, "To start marking off tasks in your journal, speak to<br><br>any seer in Seers' Village, Stankers by the coal<br><br>trucks or Sir Kay in Camelot.",
            new int[]{NPCs.SEER_388, NPCs.STANKERS_383, NPCs.SIR_KAY_241}

    );

    private final String name;

    private final int child;

    private final String[] levelNames;

    private final String[][] achievements;

    private final Item[][] rewards;

    private final String info;

    private final int[] npcs;

    DiaryType(String name, int child, String[] levelNames, String[][] achievements, Item[][] rewards, String info, int[] npcs) {
        this.name = name;
        this.child = child;
        this.levelNames = levelNames;
        this.achievements = achievements;
        this.rewards = rewards;
        this.info = info;
        this.npcs = npcs;
    }

    /**
     * For child diary type.
     *
     * @param child the child
     * @return the diary type
     */
    public static DiaryType forChild(int child) {
        for (DiaryType type : values()) {
            for (int i = 0; i < 4; i++) {
                if (child == type.getChild() + i) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * Gets npc.
     *
     * @param level the level
     * @return the npc
     */
    public int getNpc(int level) {
        return npcs[level];
    }

    /**
     * Has reward equipment boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasRewardEquipment(Player player) {
        for (Item[] tier : getRewards()) {
            for (Item item : tier) {
                if (!item.getName().toLowerCase().contains("lamp") && player.hasItem(item)) return true;
            }
        }
        return false;
    }

    /**
     * Get rewards item.
     *
     * @param level the level
     * @return the item
     */
    public Item[] getRewards(int level) {
        return rewards[level];
    }

    /**
     * Get achievements string.
     *
     * @param level the level
     * @return the string
     */
    public String[] getAchievements(int level) {
        return achievements[level];
    }

    /**
     * Get achievements string.
     *
     * @return the string
     */
    public String[][] getAchievements() {
        return achievements;
    }

    /**
     * Get rewards item.
     *
     * @return the item
     */
    public Item[][] getRewards() {
        return rewards;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get level names string.
     *
     * @return the string
     */
    public String[] getLevelNames() {
        return levelNames;
    }

    /**
     * Gets info.
     *
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * Gets child.
     *
     * @return the child
     */
    public int getChild() {
        return child;
    }

    /**
     * Get npcs int.
     *
     * @return the int
     */
    public int[] getNpcs() {
        return npcs;
    }
}