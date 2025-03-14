package content.global.skill.summoning.familiar.npc;

import content.global.skill.crafting.casting.gold.Jewellery;
import content.global.skill.firemaking.FiremakingPulse;
import content.global.skill.firemaking.Log;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Pyre lord npc.
 */
@Initializable
public class PyreLordNPC extends content.global.skill.summoning.familiar.Familiar {

    private static final Animation FIREMAKE_ANIMATION = Animation.create(8085);

    /**
     * Instantiates a new Pyre lord npc.
     */
    public PyreLordNPC() {
        this(null, 7377);
    }

    /**
     * Instantiates a new Pyre lord npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public PyreLordNPC(Player owner, int id) {
        super(owner, id, 3200, 12816, 6, WeaponInterface.STYLE_AGGRESSIVE);
        boosts.add(new SkillBonus(Skills.FIREMAKING, 3));
    }

    @Override
    public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
        return new PyreLordNPC(owner, id);
    }

    @Override
    public void configureFamiliar() {
        ClassScanner.definePlugin(new PyreLordFiremake());
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Item item = (Item) special.getNode();
        if (item.getId() != 2357) {
            owner.getPacketDispatch().sendMessage("You can only use this special on gold bars.");
            return false;
        }
        owner.lock(1);
        animate(Animation.create(8081));
        owner.graphics(Graphics.create(1463));
        Jewellery.open(owner);
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{7377, 7378};
    }

    /**
     * The type Pyre lord firemake.
     */
    public final class PyreLordFiremake extends UseWithHandler {

        /**
         * Instantiates a new Pyre lord firemake.
         */
        public PyreLordFiremake() {
            super(1511, 2862, 1521, 1519, 6333, 10810, 1517, 6332, 12581, 1515, 1513, 13567, 10329, 10328, 7406, 7405, 7404);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int id : getIds()) {
                addHandler(id, NPC_TYPE, this);
            }
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            final Player player = event.getPlayer();
            final Log log = Log.forId(event.getUsedItem().getId());
            final content.global.skill.summoning.familiar.Familiar familiar = (Familiar) event.getUsedWith();
            final int ticks = FIREMAKE_ANIMATION.getDefinition().getDurationTicks();
            if (!player.getFamiliarManager().isOwner(familiar)) {
                return true;
            }
            if (RegionManager.getObject(familiar.getLocation()) != null || familiar.getZoneMonitor().isInZone("bank")) {
                player.getPacketDispatch().sendMessage("You can't light a fire here.");
                return false;
            }
            familiar.lock(ticks);
            familiar.animate(FIREMAKE_ANIMATION);
            if (player.getInventory().remove(event.getUsedItem())) {
                final GroundItem ground = GroundItemManager.create(event.getUsedItem(), familiar.getLocation(), player);
                GameWorld.getPulser().submit(new Pulse(ticks, player, familiar) {
                    @Override
                    public boolean pulse() {
                        if (!ground.isActive()) {
                            return true;
                        }
                        final Scenery object = new Scenery(log.getFireId(), familiar.getLocation());
                        familiar.moveStep();
                        GroundItemManager.destroy(ground);
                        player.getSkills().addExperience(Skills.FIREMAKING, log.getXp() + 10);
                        familiar.faceLocation(object.getFaceLocation(familiar.getLocation()));
                        SceneryBuilder.add(object, log.getLife(), FiremakingPulse.getAsh(player, log, object));
                        if (player.getViewport().getRegion().getId() == 10806) {
                            player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 1, 9);
                        }
                        return true;
                    }
                });
            }
            return true;
        }

    }
}
