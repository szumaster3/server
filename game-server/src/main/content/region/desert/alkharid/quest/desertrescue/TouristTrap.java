package content.region.desert.alkharid.quest.desertrescue;

import content.region.desert.alkharid.quest.desertrescue.dialogue.*;
import content.region.desert.alkharid.quest.desertrescue.npc.MineSlaveNPC;
import content.region.desert.alkharid.quest.desertrescue.npc.RowdySlaveNPC;
import content.region.desert.alkharid.quest.desertrescue.plugin.TouristTrapPlugin;
import core.game.component.Component;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import org.rs.consts.*;

import static core.api.ContentAPIKt.setVarp;

/**
 * The type Tourist trap.
 */
@Initializable
public final class TouristTrap extends Quest {
    /**
     * The constant METAL_KEY.
     */
    public static final Item METAL_KEY = new Item(Items.METAL_KEY_1839);
    /**
     * The constant DESERT_CLOTHES.
     */
    public static final Item[] DESERT_CLOTHES = new Item[]{new Item(Items.DESERT_SHIRT_1833), new Item(Items.DESERT_ROBE_1835), new Item(Items.DESERT_BOOTS_1837)};
    /**
     * The constant SLAVE_CLOTHES.
     */
    public static final Item[] SLAVE_CLOTHES = new Item[]{new Item(Items.SLAVE_SHIRT_1844), new Item(Items.SLAVE_ROBE_1845), new Item(Items.SLAVE_BOOTS_1846)};
    /**
     * The constant TENTI_PINEAPPLE.
     */
    public static final Item TENTI_PINEAPPLE = new Item(Items.TENTI_PINEAPPLE_1851);
    /**
     * The constant CELL_DOOR_KEY.
     */
    public static final Item CELL_DOOR_KEY = new Item(Items.CELL_DOOR_KEY_1840);
    /**
     * The constant WROUGHT_IRON_KEY.
     */
    public static final Item WROUGHT_IRON_KEY = new Item(Items.WROUGHT_IRON_KEY_1843);
    /**
     * The constant BEDABIN_KEY.
     */
    public static final Item BEDABIN_KEY = new Item(Items.BEDABIN_KEY_1852);
    /**
     * The constant CONFIG_ID.
     */
    public static final int CONFIG_ID = 907;

    /**
     * The constant TECHNICAL_PLANS.
     */
    public static final Item TECHNICAL_PLANS = new Item(Items.TECHNICAL_PLANS_1850);
    /**
     * The constant PROTOTYPE_DART_TIP.
     */
    public static final Item PROTOTYPE_DART_TIP = new Item(Items.PROTOTYPE_DART_TIP_1853);
    /**
     * The constant PROTOTYPE_DART.
     */
    public static final Item PROTOTYPE_DART = new Item(Items.PROTOTYPE_DART_1849);
    /**
     * The constant BARREL.
     */
    public static final Item BARREL = new Item(Items.BARREL_1841);
    /**
     * The constant ANNA_BARREL.
     */
    public static final Item ANNA_BARREL = new Item(Items.ANA_IN_A_BARREL_1842);
    /**
     * The constant JAIL.
     */
    public static final Location JAIL = Location.create(3285, 3034, 0);
    /**
     * The constant JAIL_BORDER.
     */
    public static final ZoneBorders JAIL_BORDER = new ZoneBorders(3284, 3032, 3287, 3037);
    /**
     * The constant SLOTS.
     */
    public static final int[] SLOTS = new int[]{EquipmentContainer.SLOT_WEAPON, EquipmentContainer.SLOT_FEET, EquipmentContainer.SLOT_SHIELD, EquipmentContainer.SLOT_HAT, EquipmentContainer.SLOT_CHEST, EquipmentContainer.SLOT_LEGS};

    /**
     * Instantiates a new Tourist trap.
     */
    public TouristTrap() {
        super(Quests.THE_TOURIST_TRAP, 123, 122, 2, Vars.VARP_QUEST_TOURIST_TRAP_PROGRESS_197, 0, 1, 30);
    }

    @Override
    public Quest newInstance(Object object) {
        ClassScanner.definePlugins(new TouristTrapPlugin(), new AnaDialogue(), new CaptainSiadDialogue(), new DesertGuardDialogue(), new IrenaDialogue(), new MaleSlaveDialogue(), new MercenaryCaptainDialogue(), new MercenaryDialogue(), new MinecartDriverDialogue(), new MineSlaveNPC(), new MiningCampZone(), new RowdySlaveNPC(), new AlShabimDialogue(), new BedabinNomadDialogue());
        return this;
    }

    @Override
    public void drawJournal(Player player, int stage) {
        super.drawJournal(player, stage);
        switch (getStage(player)) {
            case 0:
                line(player, "<blue>I can start this quest by speaking to <red>Irena <blue>after I have<br><br><blue>gone through the <red>Shantay Pass, South of Al-Kharid.<br><br><blue>To complete this quest I need:-<br><br>" + (player.getSkills().getStaticLevel(Skills.FLETCHING) > 9 ? "<str>" : "<blue>") + "Level 10 Fletching <br><br>" + (player.getSkills().getStaticLevel(Skills.SMITHING) > 19 ? "<str>" : "<blue>") + "Level 20 Smithing" + (hasRequirements(player) ? "<br><br><blue>I have all the <red>requirements<blue> to begin and complete this<br><br><red>quest." : ""), 11);
                break;
            case 10:
            case 11:
            case 30:
            case 40:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I need to head into <red>the desert<blue> and search for <red>Ana", 11);
                break;
            case 50:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I need to find the guard a <red>Tenti Pineapple<blue> for the guard.", 11);
                break;
            case 51:
            case 52:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I have found a way to get <red>Tenti Pineapple<blue> I need to find<br><br><blue>the research plans that <red>Captain Siad<blue> has.", 11);
                break;
            case 53:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I have found a way to get <red>Tenti Pineapple<blue><br><br><blue>I found the technical plans <red>Al Shabim<blue> was looking for.", 11);
                break;
            case 54:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I have found a way to get <red>Tenti Pineapple<blue><br><br><blue>I need to manufacture the <red>Prototype<blue> weapon for <red>Al Shabim<blue>.", 11);
                break;
            case 60:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I manufactured the <red>Prototype<blue> weapon and received<br><br><blue>a tasty <red>Tenti Pineapple<blue>.", 11);
                break;
            case 61:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I finally found <red>Anna<blue>. I just need to find a way to smuggle<br><br><blue>her out of here.", 11);
                break;
            case 71:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I need to operate the <red>Winch<blue> to lift <red>Ana<blue> back up here.", 11);
                break;
            case 72:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I need to get <red>Ana<blue> from one of the barrels lifted.", 11);
                break;
            case 80:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I loaded <red>Ana<blue> into the <red>Cart<blue> I now need to get the <red>cart driver<blue> <br><br><blue>to transport it.", 11);
                break;
            case 90:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I payed the <red>Mine cart driver<blue> and he agreed to smuggle me and <red>Anna<blue><br><br><blue>out of the <red>Mining camp<blue>.", 11);
                break;
            case 95:
            case 98:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I smuggled both me and <red>Anna<blue> from the <red>Mining camp<blue>. I should<br><br><blue>go tell <red>Irena<blue> straight away.", 11);
                break;
            case 100:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><str>I returned <str>Ana<str> back to her mother and was rewarded<br><br><str>with a <str>key<str> and the knowledge in two skills.<br><br><br><br><col=FF0000>QUEST COMPLETE!", 11);
                break;
            default:
                line(player, "<str>Irena was distraught that her daughter Ana had vanished<br><br><str>somewhere in the desert, and I agreed to help find her.<br><br><blue>I need to head into <red>the desert<blue> and search for <red>Ana", 11);
                break;
        }
    }

    @Override
    public void finish(Player player) {
        super.finish(player);
        player.getPacketDispatch().sendString("2 Quest Points", 277, 8 + 2);
        player.getPacketDispatch().sendString("4650 XP in each of the two skills", 277, 9 + 2);
        player.getPacketDispatch().sendString("Ability to make throwing darts", 277, 10 + 2);
        player.getPacketDispatch().sendString("Access to desert mining camp", 277, 11 + 2);
        player.getPacketDispatch().sendString("mithril and adamantite rocks.", 277, 12 + 2);
        player.getPacketDispatch().sendItemZoomOnInterface(Items.BRONZE_DART_806, 230, Components.QUEST_COMPLETE_SCROLL_277, 3 + 2);
        player.getQuestRepository().syncronizeTab(player);
        player.getInventory().remove(new Item(Items.ANA_IN_A_BARREL_1842, player.getInventory().getAmount(ANNA_BARREL)));
        player.getBank().remove(new Item(Items.ANA_IN_A_BARREL_1842, player.getBank().getAmount(ANNA_BARREL)));
    }

    /**
     * Jail.
     *
     * @param player   the player
     * @param dialogue the dialogue
     */
    public static void jail(final Player player, String dialogue) {
        player.getDialogueInterpreter().sendDialogues(NPCs.GUARD_4999, null, dialogue);
        player.lock();
        GameWorld.getPulser().submit(new Pulse(1) {
            int counter;

            @Override
            public boolean pulse() {
                switch (counter++) {
                    case 1:
                        player.lock();
                        player.getInterfaceManager().openOverlay(new Component(Components.FADE_TO_BLACK_115));
                        break;
                    case 3:
                        player.getProperties().setTeleportLocation(Location.create(3285, 3034, 0));
                        player.getPacketDispatch().sendMessage("You are roughed up by the guards and manhandled into a cell.");
                        player.getInterfaceManager().closeOverlay();
                        player.getInterfaceManager().close();
                        player.unlock();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Jail.
     *
     * @param player the player
     */
    public static void jail(final Player player) {
        jail(player, "Hey you! You're not supposed to be in here!");
    }

    /**
     * Is jailable boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean isJailable(final Player player) {
        if (inJail(player)) {
            return false;
        }
        if (player.getEquipment().itemCount() > 3 || (!hasDesertClothes(player) && !hasSlaveClothes(player))) {
            for (int i : SLOTS) {
                if (player.getEquipment().get(i) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add config.
     *
     * @param player the player
     * @param value  the value
     */
    public static void addConfig(final Player player, final int value) {
        setVarp(player, CONFIG_ID, value, true);
    }

    /**
     * Has desert clothes boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasDesertClothes(final Player player) {
        for (Item i : DESERT_CLOTHES) {
            if (!player.getEquipment().containsItem(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Has slave clothes boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasSlaveClothes(final Player player) {
        for (Item i : SLAVE_CLOTHES) {
            if (!player.getEquipment().containsItem(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Has armour boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasArmour(final Player player) {
        return player.getEquipment().itemCount() > 0 && !hasDesertClothes(player) && !hasSlaveClothes(player);
    }

    /**
     * In jail boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean inJail(final Player player) {
        return JAIL_BORDER.insideBorder(player);
    }

    @Override
    public boolean hasRequirements(Player player) {
        return player.getSkills().getStaticLevel(Skills.FLETCHING) > 9 && player.getSkills().getStaticLevel(Skills.SMITHING) > 19;
    }

}
