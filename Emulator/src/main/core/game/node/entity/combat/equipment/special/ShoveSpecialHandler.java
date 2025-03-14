package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;
import static core.api.ContentAPIKt.stun;

/**
 * The type Shove special handler.
 */
@Initializable
public final class ShoveSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(Animations.TIPTOE_A_1064, Priority.HIGH);
    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.DRAGON_SPEAR_SPECIAL_253, 96);
    private static Animation STUN_ANIM = new Animation(Animations.DEFEND_UNARMED_424);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SPEAR_1249, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SPEARP_1263, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SPEARKP_3176, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SPEARP_PLUS_5716, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SPEARP_PLUS_PLUS_5730, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.ZAMORAKIAN_SPEAR_11716, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.ZAMORAKIAN_HASTA_14638, this);
        return this;
    }

    @Override
    public int swing(Entity entity, final Entity victim, BattleState state) {
        if (victim.size() > 1) {
            ((Player) entity).getPacketDispatch().sendMessage("That creature is too large to knock back!");
            ((Player) entity).getSettings().toggleSpecialBar();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setEstimatedHit(-1);
        Direction dir = null;
        int vx = victim.getLocation().getX();
        int vy = victim.getLocation().getY();
        int sx = entity.getLocation().getX();
        int sy = entity.getLocation().getY();
        if (vx == sx && vy > sy) {
            dir = Direction.NORTH;
        } else if (vx == sx && vy < sy) {
            dir = Direction.SOUTH;
        } else if (vx > sx && vy == sy) {
            dir = Direction.EAST;
        } else if (vx < sx && vy == sy) {
            dir = Direction.WEST;
        } else if (vx > sx && vy > sy) {
            dir = Direction.NORTH_EAST;
        } else if (vx < sx && vy > sy) {
            dir = Direction.NORTH_WEST;
        } else if (vx > sx && vy < sy) {
            dir = Direction.SOUTH_EAST;
        } else if (vx < sx && vy < sy) {
            dir = Direction.SOUTH_WEST;
        }
        victim.getWalkingQueue().reset();
        victim.getPulseManager().clear();
        stun(victim, 5);
        if (dir != null) {
            Point p = Direction.getWalkPoint(dir);
            Location dest = victim.getLocation().transform(p.getX(), p.getY(), 0);
            if (Pathfinder.find(victim, dest, false, Pathfinder.DUMB).isSuccessful()) {
                victim.getWalkingQueue().addPath(dest.getX(), dest.getY());
            }
        }
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.SHOVE_2544);
        entity.visualize(ANIMATION, GRAPHICS);
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {
    }

}
