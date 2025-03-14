package content.global.skill.summoning.familiar.npc;

import content.global.skill.farming.FarmingPatch;
import content.global.skill.farming.PatchType;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Items;

/**
 * The type Giant ent npc.
 */
@Initializable
public class GiantEntNPC extends Forager {
    private static final Item[] ITEMS = new Item[]{new Item(Items.OAK_LOGS_1521)};

    /**
     * Instantiates a new Giant ent npc.
     */
    public GiantEntNPC() {
        this(null, 6800);
    }

    /**
     * Instantiates a new Giant ent npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public GiantEntNPC(Player owner, int id) {
        super(owner, id, 4900, 12013, 6, WeaponInterface.STYLE_CONTROLLED, ITEMS);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new GiantEntNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{6800, 6801};
    }

    @Override
    protected void configureFamiliar() {
        UseWithHandler.addHandler(6800, UseWithHandler.NPC_TYPE, new UseWithHandler(Items.PURE_ESSENCE_7936) {
            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                addHandler(6800, UseWithHandler.NPC_TYPE, this);
                return this;
            }

            @Override
            public boolean handle(NodeUsageEvent event) {
                Player player = event.getPlayer();
                player.lock(1);
                int runeType = RandomFunction.random(9) < 4 ? Items.EARTH_RUNE_557 : Items.NATURE_RUNE_561;
                Item runes = new Item(runeType, 1);
                if (player.getInventory().remove(event.getUsedItem())) {
                    player.getInventory().add(runes);
                    player.sendMessage(String.format("The giant ent transmutes the pure essence into a %s.", runes.getName().toLowerCase()));
                }
                return true;
            }
        });
    }

    /**
     * Modify farming reward.
     *
     * @param fPatch the f patch
     * @param reward the reward
     */
    public void modifyFarmingReward(FarmingPatch fPatch, Item reward) {
        PatchType patchType = fPatch.getType();
        if (patchType == PatchType.FRUIT_TREE_PATCH ||
                patchType == PatchType.BUSH_PATCH ||
                patchType == PatchType.BELLADONNA_PATCH ||
                patchType == PatchType.CACTUS_PATCH) {
            if (RandomFunction.roll(2)) {
                reward.setAmount(2 * reward.getAmount());
            }
        }
    }
}
