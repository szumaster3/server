package content.global.skill.summoning;

import core.game.node.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The enum Summoning pouch.
 */
public enum SummoningPouch {
    /**
     * The Spirit wolf pouch.
     */
    SPIRIT_WOLF_POUCH(0, 12047, 1, 4.8, 6829, 0.1, 1, false, new Item(12158), new Item(12155), new Item(2859), new Item(12183, 7)),
    /**
     * The Dreadfowl pouch.
     */
    DREADFOWL_POUCH(1, 12043, 4, 9.3, 6825, 0.1, 1, false, new Item(12158), new Item(12155), new Item(2138), new Item(12183, 8)),
    /**
     * The Spirit spider pouch.
     */
    SPIRIT_SPIDER_POUCH(2, 12059, 10, 12.6, 6841, 0.2, 2, false, new Item(12158), new Item(12155), new Item(6291), new Item(12183, 8)),
    /**
     * The Thorny snail pouch.
     */
    THORNY_SNAIL_POUCH(3, 12019, 13, 12.6, 6806, 0.2, 2, false, new Item(12158), new Item(12155), new Item(3363), new Item(12183, 9)),
    /**
     * The Granite crab pouch.
     */
    GRANITE_CRAB_POUCH(4, 12009, 16, 21.6, 6796, 0.2, 2, false, new Item(12158), new Item(12155), new Item(440), new Item(12183, 7)),
    /**
     * The Spirit mosquito pouch.
     */
    SPIRIT_MOSQUITO_POUCH(5, 12778, 17, 46.5, 7331, 0.5, 2, false, new Item(12158), new Item(12155), new Item(6319), new Item(12183, 1)),
    /**
     * The Desert wyrm pouch.
     */
    DESERT_WYRM_POUCH(6, 12049, 18, 31.2, 6831, 0.4, 1, false, new Item(12159), new Item(12155), new Item(1783), new Item(12183, 45)),
    /**
     * The Spirit scorpion pouch.
     */
    SPIRIT_SCORPION_POUCH(7, 12055, 19, 83.2, 6837, 0.9, 2, false, new Item(12160), new Item(12155), new Item(3095), new Item(12183, 57)),
    /**
     * The Spirit tz kih pouch.
     */
    SPIRIT_TZ_KIH_POUCH(8, 12808, 22, 96.8, 7361, 1.1, 3, false, new Item(12160), new Item(12168), new Item(12155), new Item(12183, 64)),
    /**
     * The Albino rat pouch.
     */
    ALBINO_RAT_POUCH(9, 12067, 23, 202.4, 6847, 2.3, 1, false, new Item(12163), new Item(12155), new Item(2134), new Item(12183, 75)),
    /**
     * The Spirit kalphite pouch.
     */
    SPIRIT_KALPHITE_POUCH(10, 12063, 25, 220, 6994, 2.5, 3,false,  new Item(12163), new Item(12155), new Item(3138), new Item(12183, 51)),
    /**
     * The Compost mound pouch.
     */
    COMPOST_MOUND_POUCH(11, 12091, 28, 49.8, 6871, 0.6, 6, false, new Item(12159), new Item(12155), new Item(6032), new Item(12183, 47)),
    /**
     * The Giant chinchompa pouch.
     */
    GIANT_CHINCHOMPA_POUCH(12, 12800, 29, 255.2, 7353, 2.9, 1, false, new Item(12163), new Item(12155), new Item(10033), new Item(12183, 84)),
    /**
     * The Vampire bat pouch.
     */
    VAMPIRE_BAT_POUCH(13, 12053, 31, 136, 6835, 1.5, 4, false, new Item(12160), new Item(12155), new Item(3325), new Item(12183, 81)),
    /**
     * The Honey badger pouch.
     */
    HONEY_BADGER_POUCH(14, 12065, 32, 140.8, 6845, 1.6, 4, false, new Item(12160), new Item(12155), new Item(12156), new Item(12183, 84)),
    /**
     * The Beaver pouch.
     */
    BEAVER_POUCH(15, 12021, 33, 57.6, 6808, 0.7, 4, true, new Item(12159), new Item(12155), new Item(1519), new Item(12183, 72)),
    /**
     * The Void ravager pouch.
     */
    VOID_RAVAGER_POUCH(16, 12818, 34, 59.6, 7370, 0.7, 4, false, new Item(12159), new Item(12164), new Item(12155), new Item(12183, 74)),
    /**
     * The Void spinner pouch.
     */
    VOID_SPINNER_POUCH(17, 12780, 34, 59.6, 7333, 0.7, 4, true,  new Item(12163), new Item(12166), new Item(12155), new Item(12183, 74)),
    /**
     * The Void torcher pouch.
     */
    VOID_TORCHER_POUCH(18, 12798, 34, 59.6, 7351, 0.7, 4, false, new Item(12163), new Item(12167), new Item(12155), new Item(12183, 74)),
    /**
     * The Void shifter pouch.
     */
    VOID_SHIFTER_POUCH(19, 12814, 34, 59.6, 7367, 0.7, 4, false, new Item(12163), new Item(12165), new Item(12155), new Item(12183, 74)),
    /**
     * The Bronze minotaur pouch.
     */
    BRONZE_MINOTAUR_POUCH(64, 12073, 36, 316.8, 6853, 3.6, 3, false, new Item(12163), new Item(12155), new Item(2349), new Item(12183, 102)),
    /**
     * The Iron minotaur pouch.
     */
    IRON_MINOTAUR_POUCH(65, 12075, 46, 404.8, 6855, 4.6, 9, false, new Item(12163), new Item(12155), new Item(2351), new Item(12183, 125)),
    /**
     * The Steel minotaur pouch.
     */
    STEEL_MINOTAUR_POUCH(66, 12077, 56, 492.8, 6857, 5.6, 9, false, new Item(12163), new Item(12155), new Item(2353), new Item(12183, 141)),
    /**
     * The Mithril minotaur pouch.
     */
    MITHRIL_MINOTAUR_POUCH(67, 12079, 66, 580.8, 6859, 6.6, 9,false,  new Item(12163), new Item(12155), new Item(2359), new Item(12183, 152)),
    /**
     * The Adamant minotaur pouch.
     */
    ADAMANT_MINOTAUR_POUCH(68, 12081, 76, 668.8, 6861, 7.6, 9, false, new Item(12163), new Item(12155), new Item(2361), new Item(12183, 144)),
    /**
     * The Rune minotaur pouch.
     */
    RUNE_MINOTAUR_POUCH(69, 12083, 86, 756.8, 6863, 8.6, 9, false, new Item(12163), new Item(12155), new Item(2363), new Item(12183, 1)),
    /**
     * The Bull ant pouch.
     */
    BULL_ANT_POUCH(20, 12087, 40, 52.8, 6867, 0.6, 5, false,  new Item(12158), new Item(12155), new Item(6010), new Item(12183, 11)),
    /**
     * The Macaw pouch.
     */
    MACAW_POUCH(21, 12071, 41, 72.4, 6851, 0.8, 5, true,  new Item(12159), new Item(12155), new Item(249), new Item(12183, 78)),
    /**
     * The Evil turnip pouch.
     */
    EVIL_TURNIP_POUCH(22, 12051, 42, 184.8, 6833, 2.1, 5, false,  new Item(12160), new Item(12155), new Item(12153), new Item(12183, 104)),
    /**
     * The Spirit cockatrice pouch.
     */
    SPIRIT_COCKATRICE_POUCH(23, 12095, 43, 75.2, 6875, 0.9, 5, false,  new Item(12159), new Item(12155), new Item(12109), new Item(12183, 88)),
    /**
     * The Spirit guthatrice pouch.
     */
    SPIRIT_GUTHATRICE_POUCH(24, 12097, 43, 75.2, 6877, 0.9, 5, false,  new Item(12159), new Item(12155), new Item(12111), new Item(12183, 88)),
    /**
     * The Spirit saratrice pouch.
     */
    SPIRIT_SARATRICE_POUCH(25, 12099, 43, 75.2, 6879, 0.9, 5, false, new Item(12159), new Item(12155), new Item(12113), new Item(12183, 88)),
    /**
     * The Spirit zamatrice pouch.
     */
    SPIRIT_ZAMATRICE_POUCH(26, 12101, 43, 75.2, 6881, 0.9, 5, false, new Item(12159), new Item(12155), new Item(12115), new Item(12183, 88)),
    /**
     * The Spirit pengatrice pouch.
     */
    SPIRIT_PENGATRICE_POUCH(27, 12103, 43, 75.2, 6883, 0.9, 5, false,  new Item(12159), new Item(12155), new Item(12117), new Item(12183, 88)),
    /**
     * The Spirit coraxatrice pouch.
     */
    SPIRIT_CORAXATRICE_POUCH(28, 12105, 43, 75.2, 6885, 0.9, 5, false, new Item(12159), new Item(12155), new Item(12119), new Item(12183, 88)),
    /**
     * The Spirit vulatrice.
     */
    SPIRIT_VULATRICE(29, 12107, 43, 75.2, 6887, 0.9, 5, false, new Item(12159), new Item(12155), new Item(12121), new Item(12183, 88)),
    /**
     * The Pyrelord pouch.
     */
    PYRELORD_POUCH(30, 12816, 46, 202.4, 7377, 2.3, 5, false, new Item(12160), new Item(12155), new Item(590), new Item(12183, 111)),
    /**
     * The Magpie pouch.
     */
    MAGPIE_POUCH(31, 12041, 47, 83.2, 6824, 0.9, 5, true, new Item(12159), new Item(12155), new Item(1635), new Item(12183, 88)),
    /**
     * The Bloated leech pouch.
     */
    BLOATED_LEECH_POUCH(32, 12061, 49, 215.2, 6843, 2.4, 5, false, new Item(12160), new Item(12155), new Item(2132), new Item(12183, 117)),
    /**
     * The Spirit terrorbird pouch.
     */
    SPIRIT_TERRORBIRD_POUCH(33, 12007, 52, 68.4, 6794, 0.8, 6, true, new Item(12158), new Item(12155), new Item(9978), new Item(12183, 12)),
    /**
     * The Abyssal parasite pouch.
     */
    ABYSSAL_PARASITE_POUCH(34, true,12035, 54, 94.8, 6818, 1.1, 6, false, new Item(12159), new Item(12155), new Item(12161), new Item(12183, 106)),
    /**
     * The Spirit jelly pouch.
     */
    SPIRIT_JELLY_POUCH(35, 12027, 55, 484, 6992, 5.5, 6, false, new Item(12163), new Item(12155), new Item(1937), new Item(12183, 151)),
    /**
     * The Ibis pouch.
     */
    IBIS_POUCH(36, 12531, 56, 98.8, 6991, 1.1, 6, true, new Item(12159), new Item(12155), new Item(311), new Item(12183, 109)),
    /**
     * The Spirit kyatt pouch.
     */
    SPIRIT_KYATT_POUCH(37, 12812, 57, 501.6, 7365, 5.7, 6, false, new Item(12163), new Item(12155), new Item(10103), new Item(12183, 153)),
    /**
     * The Spirit larupia pouch.
     */
    SPIRIT_LARUPIA_POUCH(38, 12784, 57, 501.6, 7337, 5.7, 6, false, new Item(12163), new Item(12155), new Item(10095), new Item(12183, 155)),
    /**
     * The Spirit graahk pouch.
     */
    SPIRIT_GRAAHK_POUCH(39, 12810, 57, 501.6, 7363, 5.7, 6, false, new Item(12163), new Item(12155), new Item(10099), new Item(12183, 154)),
    /**
     * The Karamthulhu pouch.
     */
    KARAMTHULHU_POUCH(40, 12023, 58, 510.4, 6809, 5.8, 6, false, new Item(12163), new Item(12155), new Item(6667), new Item(12183, 144)),
    /**
     * The Smoke devil pouch.
     */
    SMOKE_DEVIL_POUCH(41, 12085, 61, 268, 6865, 3, 7, false, new Item(12160), new Item(12155), new Item(9736), new Item(12183, 141)),
    /**
     * The Abyssal lukrer.
     */
    ABYSSAL_LUKRER(42, true,12037, 62, 109.6, 6820, 1.9, 9, false, new Item(12159), new Item(12155), new Item(12161), new Item(12183, 119)),
    /**
     * The Spirit cobra pouch.
     */
    SPIRIT_COBRA_POUCH(43, 12015, 63, 276.8, 6802, 3.1, 6, false, new Item(12160), new Item(12155), new Item(6287), new Item(12183, 116)),
    /**
     * The Stranger plant pouch.
     */
    STRANGER_PLANT_POUCH(44, 12045, 64, 281.6, 6827, 3.2, 6, false, new Item(12160), new Item(12155), new Item(8431), new Item(12183, 128)),
    /**
     * The Barker toad pouch.
     */
    BARKER_TOAD_POUCH(45, 12123, 66, 87, 6889, 1, 7, false, new Item(12158), new Item(12155), new Item(2150), new Item(12183, 11)),
    /**
     * The War tortoise pouch.
     */
    WAR_TORTOISE_POUCH(46, 12031, 67, 58.6, 6815, 0.7, 7, true, new Item(12158), new Item(12155), new Item(7939), new Item(12183, 1)),
    /**
     * The Bunyip pouch.
     */
    BUNYIP_POUCH(47, 12029, 68, 119.2, 6813, 1.4, 7, true, new Item(12159), new Item(12155), new Item(383), new Item(12183, 110)),
    /**
     * The Fruit bat pouch.
     */
    FRUIT_BAT_POUCH(48, 12033, 69, 121.2, 6817, 1.4, 8, true, new Item(12159), new Item(12155), new Item(1963), new Item(12183, 130)),
    /**
     * The Ravenous locust pouch.
     */
    RAVENOUS_LOCUST_POUCH(49, 12820, 70, 132, 7372, 1.5, 4, false, new Item(12160), new Item(12155), new Item(1933), new Item(12183, 79)),
    /**
     * The Arctic bear pouch.
     */
    ARCTIC_BEAR_POUCH(50, 12057, 71, 93.2, 6839, 1.1, 8, false, new Item(12158), new Item(12155), new Item(10117), new Item(12183, 14)),
    /**
     * The Phoenix pouch.
     */
    PHOENIX_POUCH(50, 14623, 72, 93.2, 8575, 1.1, 8, false, new Item(12160, 1), new Item(12183, 165), new Item(12155, 1), new Item(14616, 1)),
    /**
     * The Obsidian golem pouch.
     */
    OBSIDIAN_GOLEM_POUCH(51, 12792, 73, 642.4, 7345, 7.3, 8, false, new Item(12163), new Item(12155), new Item(12168), new Item(12183, 195)),
    /**
     * The Granite lobster pouch.
     */
    GRANITE_LOBSTER_POUCH(52, 12069, 74, 325.6, 6849, 3.7, 8, false, new Item(12160), new Item(12155), new Item(6979), new Item(12183, 166)),
    /**
     * The Praying mantis pouch.
     */
    PRAYING_MANTIS_POUCH(53, 12011, 75, 329.6, 6798, 3.6, 8, false, new Item(12160), new Item(12155), new Item(2460), new Item(12183, 168)),
    /**
     * The Forge regent beast.
     */
    FORGE_REGENT_BEAST(54, 12782, 76, 134, 7335, 1.5, 9, false, new Item(12159), new Item(12155), new Item(10020), new Item(12183, 141)),
    /**
     * The Talon beast pouch.
     */
    TALON_BEAST_POUCH(55, 12794, 77, 1015.2, 7347, 3.8, 9, false, new Item(12160), new Item(12155), new Item(12162), new Item(12183, 174)),
    /**
     * The Giant ent pouch.
     */
    GIANT_ENT_POUCH(56, 12013, 78, 136.8, 6800, 1.6, 8, false, new Item(12159), new Item(5933), new Item(12155), new Item(12183, 124)),
    /**
     * The Hydra pouch.
     */
    HYDRA_POUCH(60, 12025, 80, 140.8, 6811, 1.6, 9, false, new Item(12159), new Item(571), new Item(12155), new Item(12183, 128)),
    /**
     * The Spirit dagannoth pouch.
     */
    SPIRIT_DAGANNOTH_POUCH(61, 12017, 83, 364.8, 6804, 4.1, 9, false, new Item(12160), new Item(6155), new Item(12155), new Item(12183, 1)),
    /**
     * The Unicorn stallion pouch.
     */
    UNICORN_STALLION_POUCH(70, 12039, 88, 154.4, 6822, 1.8, 9, true, new Item(12159), new Item(237), new Item(12155), new Item(12183, 140)),
    /**
     * The Wolpertinger pouch.
     */
    WOLPERTINGER_POUCH(72, 12089, 92, 404.8, 6869, 4.5, 10, false, new Item(12160), new Item(2859), new Item(3226), new Item(12155), new Item(12183, 203)),
    /**
     * The Pack yak pouch.
     */
    PACK_YAK_POUCH(75, 12093, 96, 422.4, 6873, 4.8, 10, true, new Item(12160), new Item(10818), new Item(12155), new Item(12183, 211)),
    /**
     * The Fire titan pouch.
     */
    FIRE_TITAN_POUCH(57, 12802, 79, 695.2, 7355, 7.9, 9, false, new Item(12163), new Item(1442), new Item(12155), new Item(12183, 198)),
    /**
     * The Moss titan pouch.
     */
    MOSS_TITAN_POUCH(58, 12804, 79, 695.2, 7357, 7.9, 9, false, new Item(12163), new Item(1440), new Item(12155), new Item(12183, 198)),
    /**
     * The Ice titan pouch.
     */
    ICE_TITAN_POUCH(59, 12806, 79, 695.2, 7359, 7.9, 9, false, new Item(12163), new Item(1438), new Item(1444), new Item(12155), new Item(12183, 198)),
    /**
     * The Lava titan pouch.
     */
    LAVA_TITAN_POUCH(62, 12788, 83, 730.4, 7341, 8.3, 9, false, new Item(12163), new Item(12168), new Item(12155), new Item(12183, 219)),
    /**
     * The Swamp titan pouch.
     */
    SWAMP_TITAN_POUCH(63, 12776, 85, 373.6, 7329, 4.2, 9, false, new Item(12160), new Item(10149), new Item(12155), new Item(12183, 150)),
    /**
     * The Geyser titan pouch.
     */
    GEYSER_TITAN_POUCH(71, 12786, 89, 783.2, 7339, 8.9, 9, false, new Item(12163), new Item(1444), new Item(12155), new Item(12183, 222)),
    /**
     * The Abyssal titan pouch.
     */
    ABYSSAL_TITAN_POUCH(73, true,12796, 93, 163.2, 7349, 1.9, 10, false, new Item(12159), new Item(12161), new Item(12155), new Item(12183, 113)),
    /**
     * The Iron titan pouch.
     */
    IRON_TITAN_POUCH(74, 12822, 95, 417.6, 7375, 4.7, 10, false, new Item(12160), new Item(1115), new Item(12155), new Item(12183, 198)),
    /**
     * The Steel titan pouch.
     */
    STEEL_TITAN_POUCH(76, 12790, 99, 435.2, 7343, 4.9, 10, false, new Item(12160), new Item(1119), new Item(12155), new Item(12183, 178)),
    /**
     * The Sacred clay pouch 1.
     */
    SACRED_CLAY_POUCH_1(-1, 14422, 1, 0, 8240, 0, 1, false, new Item(14182)),
    /**
     * The Sacred clay pouch 2.
     */
    SACRED_CLAY_POUCH_2(-1, 14424, 20, 0, 8242, 0, 3, false, new Item(14184)),
    /**
     * The Sacred clay pouch 3.
     */
    SACRED_CLAY_POUCH_3(-1, 14426, 40, 0, 8244, 0, 5, false, new Item(14186)),
    /**
     * The Sacred clay pouch 4.
     */
    SACRED_CLAY_POUCH_4(-1, 14428, 60, 0, 8246, 0, 7, false, new Item(14188)),
    /**
     * The Sacred clay pouch 5.
     */
    SACRED_CLAY_POUCH_5(-1, 14430, 80, 0, 8248, 0, 9, false, new Item(14190));

	private static final Map<Integer, SummoningPouch> POUCHES = new HashMap<Integer, SummoningPouch>();

	static {
		for (SummoningPouch pouch : SummoningPouch.values()) {
			POUCHES.put(pouch.pouchId, pouch);
		}
	}

    /**
     * Get summoning pouch.
     *
     * @param pouchId the pouch id
     * @return the summoning pouch
     */
    public static SummoningPouch get(int pouchId) {
		return POUCHES.get(pouchId);
	}

	private final int slot;
	private final int pouchId;
	private final int requiredLevel;
	private final double createExperience;
	private final int npcId;
	private final double summonExperience;
	private final int summonCost;
	private final boolean peaceful;
	private final Item[] items;
    /**
     * The Abyssal.
     */
    public boolean abyssal;

	private SummoningPouch(int slot, int pouchId, int requiredLevel, double createExperience, int npcId, double summonExperience, int summonCost, boolean peaceful, Item... items) {
		this.slot = slot;
		this.pouchId = pouchId;
		this.requiredLevel = requiredLevel;
		this.createExperience = createExperience;
		this.npcId = npcId;
		this.summonExperience = summonExperience;
		this.summonCost = summonCost;
		this.peaceful = peaceful;
		this.items = items;
	}

	private SummoningPouch(int slot, boolean abyssal, int pouchId, int requiredLevel, double createExperience, int npcId, double summonExperience, int summonCost, boolean peaceful, Item... items) {
		this(slot, pouchId, requiredLevel, createExperience, npcId, summonExperience, summonCost, peaceful, items);
		this.abyssal = abyssal;
	}

    /**
     * For slot summoning pouch.
     *
     * @param slot the slot
     * @return the summoning pouch
     */
    public static SummoningPouch forSlot(int slot) {
		for (SummoningPouch pouch : SummoningPouch.values()) {
			if (pouch.getSlot() == slot) {
				return pouch;
			}
		}
		return null;
	}

    private static final Map<Integer, List<Item>> pouchIdMap = new HashMap<>();

    static {
        for (SummoningPouch pouch : SummoningPouch.values()) {
            pouchIdMap.put(pouch.getPouchId(), List.of(pouch.getItems()));
        }
    }

    /**
     * Gets pouch id.
     *
     * @return the pouch id
     */
    public int getPouchId() {
		return pouchId;
	}

    /**
     * Gets required level.
     *
     * @return the required level
     */
    public int getRequiredLevel() {
		return requiredLevel;
	}

    /**
     * Gets create experience.
     *
     * @return the create experience
     */
    public double getCreateExperience() {
		return createExperience;
	}

    /**
     * Gets npc id.
     *
     * @return the npc id
     */
    public int getNpcId() {
		return npcId;
	}

    /**
     * Gets summon experience.
     *
     * @return the summon experience
     */
    public double getSummonExperience() {
		return summonExperience;
	}

    /**
     * Gets summon cost.
     *
     * @return the summon cost
     */
    public int getSummonCost() {
		return summonCost;
	}

    /**
     * Gets peaceful.
     *
     * @return the peaceful
     */
    public boolean getPeaceful() { return peaceful; }

    /**
     * Get items item [ ].
     *
     * @return the item [ ]
     */
    public Item[] getItems() {
		return items;
	}

    /**
     * Gets slot.
     *
     * @return the slot
     */
    public int getSlot() {
		return slot;
	}

    /**
     * Gets items by pouch id.
     *
     * @param pouchId the pouch id
     * @return the items by pouch id
     */
    public static List<Item> getItemsByPouchId(int pouchId) {
        return pouchIdMap.get(pouchId);
    }

    /**
     * Gets all pouch items.
     *
     * @return the all pouch items
     */
    public static List<Item> getAllPouchItems() {
		List<Item> allItems = new ArrayList<>();
		for (SummoningPouch pouch : SummoningPouch.values()) {
			allItems.addAll(List.of(pouch.items));
		}
		return new ArrayList<>(allItems);
	}

}
