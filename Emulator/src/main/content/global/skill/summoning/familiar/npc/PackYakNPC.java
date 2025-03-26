package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.SummoningScroll;
import content.global.skill.summoning.familiar.BurdenBeast;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.api.Container;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.LogType;
import core.game.node.entity.player.info.PlayerMonitor;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.addItem;
import static core.api.ContentAPIKt.removeItem;

/**
 * The type Pack yak npc.
 */
@Initializable
public class PackYakNPC extends BurdenBeast {

    /**
     * Instantiates a new Pack yak npc.
     */
    public PackYakNPC() {
        this(null, 6873);
    }

    /**
     * Instantiates a new Pack yak npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public PackYakNPC(Player owner, int id) {
        super(owner, id, 5800, 12093, 12, 30, WeaponInterface.STYLE_AGGRESSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new PackYakNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        Player player = owner;
        Item item = new Item(special.getItem().getId(), 1);
        if (item.getId() == SummoningScroll.WINTER_STORAGE_SCROLL.getItemId()) {
            return false;
        }
        if (!item.getDefinition().getConfiguration(ItemConfigParser.BANKABLE, true)) {
            player.sendMessage("A magical force prevents you from banking this item.");
            return false;
        }
        Item remove = item;
        if (!item.getDefinition().isUnnoted) {
            remove = new Item(item.getId(), 1);
            item = new Item(item.getNoteChange(), 1);
        }
        boolean success = addItem(player, item.getId(), item.getAmount(), Container.BANK);
        if (success) {
            success = removeItem(player, remove, Container.INVENTORY);
            if (!success) {
                boolean recovered = removeItem(player, item, Container.BANK);
                if (recovered) {
                    PlayerMonitor.log(player, LogType.DUPE_ALERT, "Successfully recovered from potential dupe attempt involving the winter storage scroll");
                } else {
                    PlayerMonitor.log(player, LogType.DUPE_ALERT, "Failed to recover from potentially successful dupe attempt involving the winter storage scroll");
                }
            }
        }
        if (success) {
            player.getDialogueInterpreter().close();
            graphics(Graphics.create(1358));
            player.getPacketDispatch().sendMessage("The pack yak has sent an item to your bank.");
        } else {
            player.getPacketDispatch().sendMessage("The pack yak can't send that item to your bank.");
        }
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316));
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.PACK_YAK_6873, NPCs.PACK_YAK_6874};
    }

    @Override
    protected String getText() {
        return "Baroo!";
    }

}
