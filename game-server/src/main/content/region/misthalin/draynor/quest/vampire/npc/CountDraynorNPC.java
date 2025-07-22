package content.region.misthalin.draynor.quest.vampire.npc;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;

import static core.api.ContentAPIKt.inInventory;
import static core.api.ContentAPIKt.sendMessage;

/**
 * The type Count draynor npc.
 */
@Initializable
public class CountDraynorNPC extends AbstractNPC {

    /**
     * Instantiates a new Count draynor npc.
     *
     * @param id       the id
     * @param location the location
     */
    public CountDraynorNPC(int id, Location location) {
        super(id, location);
    }

    /**
     * Instantiates a new Count draynor npc.
     */
    public CountDraynorNPC() {
        super(0, null);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CountDraynorNPC(id, location);
    }

    private static final Location[] CANDLE_LOCATION = {
            Location.create(3076, 9772, 0),
            Location.create(3079, 9772, 0),
            Location.create(3075, 9778, 0),
            Location.create(3080, 9778, 0)
    };

    private static final String[] FORCE_CHAT = {
            "Eeek!", "Oooch!", "Gah!", "Ow!"
    };

    @Override
    public void init() {
        super.init();
        getSkills().setLifepoints(40);
        getSkills().setStaticLevel(Skills.HITPOINTS, 40);
        getSkills().setLevel(Skills.HITPOINTS, 40);
        this.faceLocation(Location.create(3078, 9770, 0));
        this.getAnimator().animate(Animation.create(3114));
    }

    @Override
    public void tick() {
        Player p = getAttribute("player", null);
        if (p != null) {
            if (p.getLocation().getDistance(getLocation()) >= 16) {
                clear();
                return;
            }
            if (!getProperties().getCombatPulse().isAttacking() && !this.getAnimator().isAnimating()) {
                getProperties().getCombatPulse().attack(p);
            }
            if (p.getProperties().getCombatPulse().isAttacking() && p.getProperties().getCombatPulse().getVictim() == this) {
                for (Location l : CANDLE_LOCATION) {
                    if (p.getLocation().equals(l)) {
                        p.sendChat(FORCE_CHAT[RandomFunction.random(FORCE_CHAT.length)]);
                        sendMessage(p, "The candles burn your feet!");
                        break;
                    }
                }
                if (!inInventory(p, Items.HAMMER_2347, 1) || !inInventory(p, Items.STAKE_1549, 1)) {
                    getSkills().heal(10);
                }
                if (RandomFunction.random(7) == 2) {
                    getSkills().heal(RandomFunction.random(1, inInventory(p, Items.GARLIC_1550, 1) ? 2 : 12));
                }
            }
        }
        super.tick();
    }

    @Override
    public void onImpact(Entity entity, BattleState state) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (!inInventory(p, Items.HAMMER_2347, 1) || !inInventory(p, Items.STAKE_1549, 1)) {
                getSkills().heal(10);
            }
        }
        super.onImpact(entity, state);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        setRespawn(false);
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player p = (Player) killer;
            if (inInventory(p, Items.HAMMER_2347, 1) && p.getInventory().remove(new Item(Items.STAKE_1549))) {
                if (p.getQuestRepository().getQuest(Quests.VAMPIRE_SLAYER).getStage(p) == 30) {
                    p.getQuestRepository().getQuest(Quests.VAMPIRE_SLAYER).finish(p);
                    sendMessage(p, "You hammer the stake into the vampire's chest!");
                }
            } else {
                sendMessage(p, "You're unable to push the stake far enough in!");
            }
        }
        setRespawn(false);
    }

    @Override
    public void checkImpact(BattleState state) {
        if (state.getAttacker() instanceof Player) {
            Player p = (Player) state.getAttacker();
            if (!inInventory(p, Items.HAMMER_2347, 1) || !inInventory(p, Items.STAKE_1549, 1)) {
                if (state.getEstimatedHit() > -1) {
                    state.setEstimatedHit(0);
                }
                if (state.getSecondaryHit() > -1) {
                    state.setSecondaryHit(0);
                }
            }
        }
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
        Player player = (Player) entity;
        Player pl = getAttribute("player", null);
        return pl != null && pl == player;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.COUNT_DRAYNOR_757};
    }
}
