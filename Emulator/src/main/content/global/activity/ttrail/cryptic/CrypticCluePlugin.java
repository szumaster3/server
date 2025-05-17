package content.global.activity.ttrail.cryptic;

import content.global.activity.ttrail.ClueLevel;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Plugin;
import org.rs.consts.Items;

/**
 * The Cryptic clue plugin.
 */
public final class CrypticCluePlugin extends CrypticClueScroll {

    /**
     * Constructs a new Cryptic clue plugin.
     */
    public CrypticCluePlugin() {
        this(null, -1, null, null, null);
    }

    /**
     * Constructs a cryptic clue with basic parameters.
     *
     * @param name     the internal name of the clue scroll
     * @param clueId   the item id of the clue scroll
     * @param level    the difficulty level of the clue
     * @param clueText the text/riddle of the clue
     * @param location the in-game location to solve the clue
     */
    public CrypticCluePlugin(String name, int clueId, ClueLevel level, String clueText, Location location) {
        super(name, clueId, level, clueText, location);
    }

    /**
     * Constructs a cryptic clue with additional object and zone data.
     *
     * @param name     the internal name of the clue scroll
     * @param clueId   the item id of the clue scroll
     * @param level    the difficulty level of the clue
     * @param clueText the text/riddle of the clue
     * @param location the in-game location to solve the clue
     * @param object   the object id to interact with (if applicable)
     * @param borders  the zone borders that define the clue's area
     */
    public CrypticCluePlugin(String name, int clueId, ClueLevel level, String clueText, Location location, int object, ZoneBorders... borders) {
        super(name, clueId, level, clueText, location, object, borders);
    }

    /**
     * Registers all known cryptic clue scrolls to the game when the plugin is loaded.
     *
     * @param arg unused initialization argument
     * @return the current plugin instance
     * @throws Throwable if an error occurs during registration
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        register(new CrypticCluePlugin("etceteria-evergreen", Items.CLUE_SCROLL_10190, ClueLevel.HARD, "And so on, and so on, and so on.<br>Walking from the land of many<br>unimportant things leads to a choice<br>of<br>paths.", Location.create(2591, 3879, 0)));
        register(new CrypticCluePlugin("elemental-workshop-crates", Items.CLUE_SCROLL_10192, ClueLevel.HARD, "You have all of the elements<br>available to solve this clue.<br>Fortunately you do not have to<br>go so far as to stand in a<br>draft.", Location.create(2723, 9891, 0), 18506, new ZoneBorders(2722, 9890, 2724, 9892)));
        register(new CrypticCluePlugin("east-ardougne-crate", Items.CLUE_SCROLL_10194, ClueLevel.EASY, "<br><br><br>A crate found in the<br>tower of a church is<br>your next stop.", Location.create(2612, 3306, 1), 34585, new ZoneBorders(2609, 3304,2621, 3311, 1)));
        register(new CrypticCluePlugin("aggie-house-draynor", Items.CLUE_SCROLL_10196, ClueLevel.HARD, "Aggie I see, Lonely and southern I<br>feel, I am neither inside nor outside the<br>house,<br>yet no house would be complete without<br>me. The treasure lies beneath me!", Location.create(3085, 3255, 0)));
        register(new CrypticCluePlugin("wilderness-sapphire", Items.CLUE_SCROLL_10198, ClueLevel.HARD, "46 is my number,<br>my body is the colour of burnt orange<br>and crawls among those with eight.<br>Three mouths I have, yet I cannot<br>eat. My blinking blue eye hides my grave.", Location.create(3169, 3885, 0)));
        register(new CrypticCluePlugin("almera-house", Items.CLUE_SCROLL_10200, ClueLevel.HARD, "A great view - watch<br>the rapidly drying<br>hides get splashed.<br>Check the box you are sitting on.", Location.create(2523, 3493, 1), 359, new ZoneBorders(2522, 3492,2524, 3494, 1)));
        register(new CrypticCluePlugin("canifis-clothes-shop", Items.CLUE_SCROLL_10202, ClueLevel.MEDIUM, "A town with a different sort of<br>night-life is your destination.<br>Search for some crates<br>in one of the houses.", Location.create(3498, 3507, 0), 24911, new ZoneBorders(3497, 3506,3500, 3508)));
        register(new CrypticCluePlugin("nardah-flying-rug", Items.CLUE_SCROLL_10204, ClueLevel.HARD, "As you desert this town,<br>keep an eye out for a set of spines<br>that could ruin nearby rugs:<br>dig carefully around the greenery.", Location.create(3397, 2917, 0)));
        register(new CrypticCluePlugin("west-ardougne-prison", Items.CLUE_SCROLL_10206, ClueLevel.MEDIUM, "Being this far north has meant<br>that these crates have escaped<br>being battled over.", Location.create(2519, 3259, 0), 40495, new ZoneBorders(2517, 3258,2521, 3260)));
        register(new CrypticCluePlugin("sandstone-granite-quarry", Items.CLUE_SCROLL_10208, ClueLevel.MEDIUM, "Brush off the sand and dig in the<br>quarry. There is a wheely handy barrow to<br>the east. Don't worry, it's coal to dig there—in<br>>fact, it's all oclay.", Location.create(3175,2915, 0)));
        register(new CrypticCluePlugin("port-phasmatys-tree", Items.CLUE_SCROLL_10210, ClueLevel.MEDIUM, "By the town of the dead,<br>walk south down a rickety bridge,<br>then dig near the slime-covered<br>tree.", Location.create(3644, 3494, 0)));
        register(new CrypticCluePlugin("edgeville-yew-tree", Items.CLUE_SCROLL_10212, ClueLevel.HARD, "Come to the evil ledge,<br>Yew know yew want to.<br>And try not to get stung.", Location.create(3088, 3468, 0)));
        register(new CrypticCluePlugin("mortton-circle-shadows", Items.CLUE_SCROLL_10214, ClueLevel.HARD, "Covered in shadows, the centre<br>of the circle is where you will<br>find the answer.", Location.create(3488, 3289, 0)));
        register(new CrypticCluePlugin("pollnivneach-well", Items.CLUE_SCROLL_10216, ClueLevel.MEDIUM, "Dig here if you aren't feeling too<br>well<br>after travelling through the desert.<br>Ali heartily recommends it.", Location.create(3358, 2970, 0)));
        register(new CrypticCluePlugin("mudskipper-point", Items.CLUE_SCROLL_10218, ClueLevel.MEDIUM, "Don't skip here,<br>it's too muddy.<br>You'll feel like a star if you dig here,<br>though.", Location.create(3000, 3110, 0)));
        register(new CrypticCluePlugin("baxtorian-falls-hay", Items.CLUE_SCROLL_10220, ClueLevel.MEDIUM, "Hay! Stop for a bit and admire<br>the scenery, just like the tourism<br>promoter says.", Location.create(2525, 3436, 0), 300, new ZoneBorders(2524, 3435, 2526, 3438)));
        // TODO: Add key drop to Monastery and lock the chest.
        // register(new CrypticCluePlugin("varrock-church", Items.CLUE_SCROLL_10222, ClueLevel.MEDIUM, "You'll need to look for<br>a town with a central<br>fountain. Look for a locked<br>chest in the town's chapel.", Location.create(3256, 3487, 0), 24204, new ZoneBorders(2524, 3435, 2526, 3438)));
        register(new CrypticCluePlugin("monastery-monks", Items.CLUE_SCROLL_10224, ClueLevel.MEDIUM, "Find a crate close to the<br>monks that like to paaarty!", Location.create(2614, 3204, 0), 354, new ZoneBorders(2613, 3203, 2615, 3205)));
        register(new CrypticCluePlugin("mage-training-bookcase", Items.CLUE_SCROLL_10226, ClueLevel.MEDIUM, "For any aspiring mage,<br>I'm sure searching this bookcase<br>will be a rewarding experience.", Location.create(3366, 3319, 1), 10723, new ZoneBorders(3365, 3318, 3367, 3320, 1)));
        register(new CrypticCluePlugin("lumbridge-windmill", Items.CLUE_SCROLL_10228, ClueLevel.HARD, "Four blades I have yet I draw no blood.<br>But I mash and turn my victims to powder.<br>Search in my head,<br>search in my rafters,<br>Where my blades are louder.", Location.create(3166, 3309, 2), 12963, new ZoneBorders(3165, 3308, 3167, 3310, 2)));
        register(new CrypticCluePlugin("lighthouse-drawers", Items.CLUE_SCROLL_10230, ClueLevel.MEDIUM, "Go to this building to be<br>illuminated, and search the<br>drawers while you're there.", Location.create(2512, 3641, 1), 351, new ZoneBorders(2511, 3639, 2513, 3641, 1)));
        register(new CrypticCluePlugin("fishing-platform-crate", Items.CLUE_SCROLL_10232, ClueLevel.MEDIUM, "Try not to step on<br>the crates that dot<br>this platform.", Location.create(2764, 3273, 0), 18502, new ZoneBorders(2763, 3272, 2765, 3274)));
        register(new CrypticCluePlugin("lumbridge-crates", Items.CLUE_SCROLL_10234, ClueLevel.HARD, "You will need to under-cook<br>to solve this one.", Location.create(3219, 9617, 0), 357, new ZoneBorders(3218, 9616, 3220, 9618)));
        // TODO: Add key drop to penda and lock the drawer.
        register(new CrypticCluePlugin("dunstan-burthorpe", Items.CLUE_SCROLL_10236, ClueLevel.MEDIUM, "Go to the village being<br>attacked by trolls, search the<br>drawers while you are there.", Location.create(2921, 3577, 0), 351, new ZoneBorders(2920, 3576, 2922, 3578)));
        register(new CrypticCluePlugin("dungeon-chest", Items.CLUE_SCROLL_10238, ClueLevel.MEDIUM, "This temple is rather sluggish.<br>The chest just inside the entrance,<br>however,<br>is filled with goodies.", Location.create(2698, 9684, 0), 18321, new ZoneBorders(2697, 9683, 2699, 9685)));
        // register(new CrypticCluePlugin("abbot-langley", Items.CLUE_SCROLL_10240, ClueLevel.MEDIUM, "'A bag belt only?' he asked his balding brothers.", Location.create(0, 0, 0)));
        // register(new CrypticCluePlugin("oziac-armor-seller", Items.CLUE_SCROLL_10242, ClueLevel.HARD, "A strange little man who sells armour only to those who've proven themselves to be unafraid of dragons.", Location.create(0, 0, 0)));
        // register(new CrypticCluePlugin("zanaris-forge", Items.CLUE_SCROLL_10244, ClueLevel.MEDIUM, "After a hard slays spraying back the vegetation, why not pop off to the nearby forge and search the crates?", Location.create(0, 0, 0)));
        register(new CrypticCluePlugin("port-khazard-anvil", Items.CLUE_SCROLL_10246, ClueLevel.MEDIUM, "After trawling for bars,<br>go to the nearest place and smith<br>them and dig by the door.", Location.create(2656, 3161, 0)));
        // register(new CrypticCluePlugin("citric-cellar", Items.CLUE_SCROLL_10248, ClueLevel.HARD, "Citric Cellar.", Location.create(0, 0, 0)));
        // register(new CrypticCluePlugin("blue-moon-inn", Items.CLUE_SCROLL_10250, ClueLevel.HARD, "Find a bar with a centre fountain in its city. Go upstairs and get changed.", Location.create(0, 0, 0)));
        // register(new CrypticCluePlugin("general-bentnoze", Items.CLUE_SCROLL_10252, ClueLevel.HARD, "Generally speaking, his nose was very bent.", Location.create(0, 0, 0)));
        register(new CrypticCluePlugin("lumbridge-castle-spinning-wheel", Items.CLUE_SCROLL_10254, ClueLevel.HARD, "My home is grey and made of stone;<br>A castle with a search for a meal.<br>Hidden in some drawers I am,<br>Across from a wooden wheel.", Location.create(3213, 3216, 1), 37012, new ZoneBorders(3212, 3215, 3214, 3217, 1)));
        register(new CrypticCluePlugin("tai-bwo-wannai-crates", Items.CLUE_SCROLL_10256, ClueLevel.HARD, "In a village made of bamboo,<br>look for some crates<br>under one of the houses.", Location.create(2800, 3074, 0), 356, new ZoneBorders(2799, 3073, 2801, 3075)));
        register(new CrypticCluePlugin("shilo-village-bookcase", Items.CLUE_SCROLL_10258, ClueLevel.HARD, "This village has a problem<br>with cartloads of the undead.<br>Try checking the bookcase<br>to find the answer.", Location.create(2833, 2991, 0), 394, new ZoneBorders(2832, 2990, 2834, 2993)));
        register(new CrypticCluePlugin("necromancer-tower", Items.CLUE_SCROLL_10260, ClueLevel.HARD, "Throat mage seeks companionship.<br>Seek answers inside my furniture<br>if interested.", Location.create(2668, 3238, 1), 353, new ZoneBorders(2667, 3237, 2670, 3239, 1)));
        // register(new CrypticCluePlugin("braine-death-island-lake", Items.CLUE_SCROLL_10262, ClueLevel.HARD, "You will need to wash the old ash off of your spade when you dig here, but the only water nearby is stagnant.", Location.create(0, 0, 0)));
        register(new CrypticCluePlugin("digsite-doug-deep", Items.CLUE_SCROLL_10264, ClueLevel.HARD, "You'll need to have Doug Deep<br>into the distant<br>past to get to these sacks.", Location.create(3348, 9758, 0), 32049, new ZoneBorders(3347, 9757, 3349, 9759)));
        // TODO: Add OnUseWith Fire interaction.
        // register(new CrypticCluePlugin("captain-klemfoodle", Items.CLUE_SCROLL_10266, ClueLevel.MEDIUM, "You can cook food on me,<br>but don't cook any foodles -<br>That would be just wrong.", Location.create(0, 0, 0)));
        register(new CrypticCluePlugin("lubufu-brimhaven", Items.CLUE_SCROLL_10268, ClueLevel.MEDIUM, "The owner of this crate has a hunch that he put more than fish inside.", Location.create(2770, 3172, 0), 366, new ZoneBorders(2769, 3171, 2771, 3173)));
        return this;
    }
}