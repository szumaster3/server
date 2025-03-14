package content.global.skill.summoning.familiar.npc;

import content.global.skill.firemaking.FiremakingPulse;
import content.global.skill.firemaking.Log;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.container.impl.EquipmentContainer;
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
import core.tools.RandomFunction;

/**
 * The type Forge regent npc.
 */
@Initializable
public class ForgeRegentNPC extends content.global.skill.summoning.familiar.Familiar {

    private static final Animation FIREMAKE_ANIMATION = Animation.create(8085);

    /**
     * Instantiates a new Forge regent npc.
     */
    public ForgeRegentNPC() {
        this(null, 7335);
    }

    /**
     * Instantiates a new Forge regent npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public ForgeRegentNPC(Player owner, int id) {
        super(owner, id, 4500, 12782, 6, WeaponInterface.STYLE_RANGE_ACCURATE);
        boosts.add(new SkillBonus(Skills.FIREMAKING, 4));
    }

    @Override
    public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
        return new ForgeRegentNPC(owner, id);
    }

    @Override
    public void configureFamiliar() {
        ClassScanner.definePlugin(new ForgeRegentFiremake());
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!(special.getTarget() instanceof Player)) {
            owner.sendMessage("You can't use this special on an npc.");
            return false;
        }
        Player target = special.getTarget().asPlayer();
        if (!canCombatSpecial(target)) {
            return false;
        }
        if (target.getInventory().freeSlots() < 1) {
            owner.sendMessage("The target doesn't have enough inventory space.");
            return false;
        }
        Item weapon = target.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
        Item shield = target.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
        if (weapon == null && shield == null) {
            owner.sendMessage("The target doesn't have a weapon or shield.");
            return false;
        }
        Item remove = null;
        while (remove == null) {
            if (RandomFunction.random(2) == 1) {
                remove = weapon;
            } else {
                remove = shield;
            }
        }
        graphics(Graphics.create(1394));
        target.graphics(Graphics.create(1393));
        if (target.getEquipment().remove(remove)) {
            target.getInventory().add(remove);
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{7335, 7336};
    }

    /**
     * The type Forge regent firemake.
     */
    public final class ForgeRegentFiremake extends UseWithHandler {

        /**
         * Instantiates a new Forge regent firemake.
         */
        public ForgeRegentFiremake() {
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
