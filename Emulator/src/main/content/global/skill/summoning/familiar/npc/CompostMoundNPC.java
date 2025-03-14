package content.global.skill.summoning.familiar.npc;

import content.global.skill.farming.CompostBin;
import content.global.skill.farming.CompostBins;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Items;

/**
 * The type Compost mound npc.
 */
@Initializable
public class CompostMoundNPC extends content.global.skill.summoning.familiar.Forager {

    private static final Item[] ITEMS = new Item[]{new Item(6032), new Item(6034), new Item(5318), new Item(5319), new Item(5324), new Item(5322), new Item(5320), new Item(5323), new Item(5321), new Item(5305), new Item(5307), new Item(5308), new Item(5306), new Item(5309), new Item(5310), new Item(5311), new Item(5101), new Item(5102), new Item(5103), new Item(5104), new Item(5105), new Item(5106), new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100), new Item(5291), new Item(5292), new Item(5293), new Item(5294), new Item(5295), new Item(12176), new Item(5296), new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5303), new Item(5304)};

    /**
     * Instantiates a new Compost mound npc.
     */
    public CompostMoundNPC() {
        this(null, 6871);
    }

    /**
     * Instantiates a new Compost mound npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public CompostMoundNPC(Player owner, int id) {
        super(owner, id, 2400, 12091, 12, WeaponInterface.STYLE_AGGRESSIVE, ITEMS);
    }

    @Override
    public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
        return new CompostMoundNPC(owner, id);
    }

    @Override
    public void configureFamiliar() {
        ClassScanner.definePlugin(new CompostBucketPlugin());
    }

    @Override
    public boolean isPoisonImmune() {
        return true;
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Scenery object = (Scenery) special.getNode();
        if (!object.getName().equals("Compost Bin")) {
            owner.getPacketDispatch().sendMessage("This scroll can only be used on an empty compost bin.");
            return false;
        }
        CompostBins cbin = CompostBins.forObject(special.getNode().asScenery());
        if (cbin == null) {
            return false;
        }
        CompostBin bin = cbin.getBinForPlayer(owner);
        if (bin.isFinished() || bin.isFull() || bin.isClosed()) {
            return false;
        }
        final boolean superCompost = RandomFunction.random(10) == 1;
        faceLocation(object.getLocation());
        Item toAdd = new Item(superCompost ? Items.PINEAPPLE_2114 : Items.POTATO_1942);
        toAdd.setAmount(15);
        bin.addItem(toAdd);
        bin.close();
        animate(Animation.create(7775));
        graphics(Graphics.create(1424));
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6871, 6872};
    }

    /**
     * The type Compost bucket plugin.
     */
    public class CompostBucketPlugin extends UseWithHandler {

        /**
         * Instantiates a new Compost bucket plugin.
         */
        public CompostBucketPlugin() {
            super(1925);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(6871, NPC_TYPE, this);
            addHandler(6872, NPC_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            final Player player = event.getPlayer();
            final content.global.skill.summoning.familiar.Familiar familiar = (Familiar) event.getUsedWith();
            if (!player.getFamiliarManager().isOwner(familiar)) {
                return true;
            }
            player.animate(Animation.create(895));
            familiar.animate(Animation.create(7775));
            player.getInventory().replace(ITEMS[0], event.getUsedItem().getSlot());
            familiar.getImpactHandler().manualHit(player, 2, HitsplatType.NORMAL);
            return true;
        }

    }
}
