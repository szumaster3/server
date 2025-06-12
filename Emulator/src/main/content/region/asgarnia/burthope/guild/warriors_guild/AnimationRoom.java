package content.region.asgarnia.burthope.guild.warriors_guild;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.NPCs;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Animation room.
 */
@Initializable
public final class AnimationRoom extends MapZone implements Plugin<java.lang.Object> {

    /**
     * The enum Armour set.
     */
    enum ArmourSet {
        /**
         * Bronze armour set.
         */
        BRONZE(NPCs.ANIMATED_BRONZE_ARMOUR_4278, 5, Items.BRONZE_FULL_HELM_1155, Items.BRONZE_PLATEBODY_1117, Items.BRONZE_PLATELEGS_1075),
        /**
         * Iron armour set.
         */
        IRON(NPCs.ANIMATED_IRON_ARMOUR_4279, 10, Items.IRON_FULL_HELM_1153, Items.IRON_PLATEBODY_1115, Items.IRON_PLATELEGS_1067),
        /**
         * Steel armour set.
         */
        STEEL(NPCs.ANIMATED_STEEL_ARMOUR_4280, 15, Items.STEEL_FULL_HELM_1157, Items.STEEL_PLATEBODY_1119, Items.STEEL_PLATELEGS_1069),
        /**
         * Black armour set.
         */
        BLACK(NPCs.ANIMATED_BLACK_ARMOUR_4281, 20, Items.BLACK_FULL_HELM_1165, Items.BLACK_PLATEBODY_1125, Items.BLACK_PLATELEGS_1077),
        /**
         * Mithril armour set.
         */
        MITHRIL(NPCs.ANIMATED_MITHRIL_ARMOUR_4282, 25, Items.MITHRIL_FULL_HELM_1159, Items.MITHRIL_PLATEBODY_1121, Items.MITHRIL_PLATELEGS_1071),
        /**
         * Adamant armour set.
         */
        ADAMANT(NPCs.ANIMATED_ADAMANT_ARMOUR_4283, 30, Items.ADAMANT_FULL_HELM_1161, Items.ADAMANT_PLATEBODY_1123, Items.ADAMANT_PLATELEGS_1073),
        /**
         * Rune armour set.
         */
        RUNE(NPCs.ANIMATED_RUNE_ARMOUR_4284, 40, Items.RUNE_FULL_HELM_1163, Items.RUNE_PLATEBODY_1127, Items.RUNE_PLATELEGS_1079);

        private final int npcId;
        private final int tokenAmount;
        private final int[] pieces;

        ArmourSet(int npcId, int tokenAmount, int... pieces) {
            this.npcId = npcId;
            this.tokenAmount = tokenAmount;
            this.pieces = pieces;
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
         * Gets token amount.
         *
         * @return the token amount
         */
        public int getTokenAmount() {
            return tokenAmount;
        }

        /**
         * Get pieces int [ ].
         *
         * @return the int [ ]
         */
        public int[] getPieces() {
            return pieces;
        }

    }

    /**
     * Instantiates a new Animation room.
     */
    public AnimationRoom() {
        super("wg animation", true);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            NPC npc = e.getAttribute("animated_set");
            if (npc != null && npc.isActive()) {
                npc.finalizeDeath(null);
            }
        }
        return true;
    }

    private void animateArmour(final Player player, final Scenery scenery, final ArmourSet set) {
        if (!player.getInventory().containItems(set.getPieces())) {
            player.getDialogueInterpreter().sendDialogue("You need a plate body, plate legs and full helm of the same type to", "activate the armour animator.");
            return;
        }
        if (player.getAttribute("animated_set") != null) {
            player.getPacketDispatch().sendMessage("You already have a set animated.");
            return;
        }
        player.lock(10);
        player.animate(Animation.create(Animations.MULTI_BEND_OVER_827));
        player.getDialogueInterpreter().sendPlainMessage(true, "You place your armour on the platform where it", "disappears...");
        GameWorld.getPulser().submit(new Pulse(5, player) {
            boolean spawn;

            @Override
            public boolean pulse() {
                if (!spawn) {
                    for (int id : set.getPieces()) {
                        if (!player.getInventory().remove(new Item(id))) {
                            return true;
                        }
                    }
                    playAudio(player, Sounds.WARGUILD_ANIMATE_1909);
                    player.logoutListeners.put("animation-room", player1 -> {
                        for (int item : set.getPieces()) player1.getInventory().add(new Item(item));
                        return Unit.INSTANCE;
                    });
                    player.getDialogueInterpreter().sendPlainMessage(true, "The animator hums, something appears to be working.", "You stand back...");
                    spawn = true;
                    super.setDelay(4);
                    return false;
                }
                if (getDelay() == 4) {
                    setDelay(1);
                    playAudio(player, Sounds.WARGUILD_ANIMATOR_ACTIVATE_1910);
                    ForceMovement.run(player, player.getLocation().transform(0, 1, 0)).setDirection(Direction.SOUTH);
                    return false;
                }
                player.getInterfaceManager().closeChatbox();
                NPC npc = new AnimatedArmour(player, scenery.getLocation(), set);
                setAttribute(player, "animated_set", npc);
                npc.init();
                return true;
            }
        });
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        int[] ids = new int[ArmourSet.values().length * 3];
        int index = 0;
        for (ArmourSet set : ArmourSet.values()) {
            for (int id : set.getPieces()) {
                ids[index++] = id;
            }
        }
        UseWithHandler.addHandler(org.rs.consts.Scenery.MAGICAL_ANIMATOR_15621, UseWithHandler.OBJECT_TYPE, new UseWithHandler(ids) {

            @Override
            public boolean handle(NodeUsageEvent event) {
                Item item = event.getUsedItem();
                ArmourSet set = null;
                roar:
                {
                    for (ArmourSet s : ArmourSet.values()) {
                        for (int id : s.getPieces()) {
                            if (id == item.getId()) {
                                set = s;
                                break roar;
                            }
                        }
                    }
                }
                if (set != null) {
                    animateArmour(event.getPlayer(), (Scenery) event.getUsedWith(), set);
                }
                return true;
            }

            @Override
            public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
                return this;
            }

        });
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }

    @Override
    public void configure() {
        super.register(new ZoneBorders(2849, 3534, 2861, 3545));
    }

}
