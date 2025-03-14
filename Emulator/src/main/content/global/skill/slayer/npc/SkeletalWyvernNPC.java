package content.global.skill.slayer.npc;

import content.global.skill.slayer.Tasks;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.MultiSwingHandler;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.NPCBehavior;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import org.rs.consts.Animations;
import org.rs.consts.Items;

/**
 * The type Skeletal wyvern npc.
 */
public class SkeletalWyvernNPC extends NPCBehavior {

    private final CombatSwingHandler COMBAT_HANDLER = new MultiSwingHandler(
        new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(Animations.STAND_2985)),
        new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(2989), new Graphics(499)),
        DragonfireSwingHandler.get(false, 54, new Animation(2988), new Graphics(501), null, null, false)
    );

    private final CombatSwingHandler COMBAT_HANDLER_FAR = new MultiSwingHandler(
        new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(2989), new Graphics(499))
    );

    private static final int[] SHIELDS = {
        Items.DRAGONFIRE_SHIELD_11283,
        Items.DRAGONFIRE_SHIELD_11285,
        Items.ELEMENTAL_SHIELD_2890,
        Items.MIND_SHIELD_9731
    };

    /**
     * Instantiates a new Skeletal wyvern npc.
     */
    public SkeletalWyvernNPC() {
        super(Tasks.SKELETAL_WYVERN.getNpcs());
    }

    @Override
    public CombatSwingHandler getSwingHandlerOverride(NPC self, CombatSwingHandler original) {
        Player victim = (Player) self.getProperties().getCombatPulse().getVictim();
        if (victim == null) {
            return original;
        }

        if (victim.getLocation().getDistance(self.getLocation()) >= 5) {
            return COMBAT_HANDLER_FAR;
        } else {
            return COMBAT_HANDLER;
        }
    }
}
