package content.region.misthalin.lumbridge.npc;

import core.api.utils.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.NPCs;

import java.util.concurrent.TimeUnit;

/**
 * The type Tormented demon npc.
 */
@Initializable
public class TormentedDemonNPC extends AbstractNPC {
    private static final int[][] MELEE = new int[][]{{8349, 8352, 8355}, {8358, 8361, 8364}};
    private static final int[][] MAGE = new int[][]{{8350, 8353, 8356}, {8359, 8362, 8365}};
    private static final int[][] RANGE = new int[][]{{8351, 8354, 8357}, {8360, 8363, 8366}};

    private final TormentedDemonSwingHandler TD_SWING_HANDLER = new TormentedDemonSwingHandler();

    private long lastSwitch = System.currentTimeMillis() + 15000;

    private boolean fireShield = true;

    private long shieldDelay;

    private final int[] damageLog = new int[3];

    @Override
    public void init() {
        super.init();
        getAggressiveHandler().setChanceRatio(10);
        getAggressiveHandler().setRadius(64);
        getAggressiveHandler().setAllowTolerance(false);
    }

    /**
     * Instantiates a new Tormented demon npc.
     */
    public TormentedDemonNPC() {
        this(-1, null);
    }

    /**
     * Instantiates a new Tormented demon npc.
     *
     * @param id       the id
     * @param location the location
     */
    public TormentedDemonNPC(int id, Location location) {
        super(id, location);
        setWalks(true);
        setAggressive(true);
        this.setDefaultBehavior();
    }

    @Override
    public boolean shouldPreventStacking(Entity other) {
        return other instanceof TormentedDemonNPC;
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (!fireShield && shieldDelay < System.currentTimeMillis() && shieldDelay > 0) {
            Player p = getAttribute("shield-player", null);
            fireShield = true;
            shieldDelay = 0;
            if (p != null && p.isActive() && p.getLocation().withinDistance(getLocation()) && isActive() && !isHidden(p)) {
                p.sendMessage("The Tormented demon regains its strength against your weapon.");
            }
        }
    }

    @Override
    public void sendImpact(BattleState state) {
        int max = 0;
        switch (state.getStyle()) {
            case MAGIC:
            case RANGE:
                max = 26;
                break;
            case MELEE:
                max = 18;
                break;
        }

        if (state.getEstimatedHit() > max) {
            state.setEstimatedHit(RandomFunction.random(max - 5));
        }
    }

    @Override
    public void checkImpact(BattleState state) {
        int formattedHit = (int) state.getAttacker().getFormattedHit(state, state.getEstimatedHit());
        if (state.getAttacker().isPlayer() && formattedHit > 0 && state.getWeapon() != null && (state.getWeapon().getId() == 6746 || state.getWeapon().getId() == 732)) {
            if (fireShield) {
                state.getAttacker().asPlayer().sendMessage("The demon is temporarily weakened by your weapon.");
            }
            shieldDelay = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
            fireShield = false;
            setAttribute("shield-player", state.getAttacker());
        }
        if (fireShield) {
            state.setEstimatedHit((int) (state.getEstimatedHit() * 0.25));
            graphics(Graphics.create(1885));
        }
        if (state.getStyle() == null) {
            return;
        }

        int hit = formattedHit > 0 ? formattedHit : 1;
        damageLog[state.getStyle().ordinal()] = damageLog[state.getStyle().ordinal()] + hit;
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        super.onImpact(entity, state);
        CombatStyle damaged = getMostDamagedStyle();
        if (damaged != null && damageLog[damaged.ordinal()] >= 31 && damaged != getProperties().getProtectStyle()) {
            for (int i = 0; i < 3; i++) {
                damageLog[i] = 0;
            }
            transformDemon(null, damaged);
            return;
        } else if (lastSwitch < System.currentTimeMillis()) {
            transformDemon(RandomFunction.getRandomElement(getAlternateStyle(TD_SWING_HANDLER.style)), null);
            lastSwitch = System.currentTimeMillis() + 15000;
            animate(new Animation(10917, Priority.HIGH));
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        reTransform();
        fireShield = true;
        BossKillCounter.addToKillCount((Player) killer, this.getId());
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new TormentedDemonNPC(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean hit) {
        return TD_SWING_HANDLER;
    }

    /**
     * Transform demon.
     *
     * @param attackStyle     the attack style
     * @param protectionStyle the protection style
     */
    public void transformDemon(CombatStyle attackStyle, CombatStyle protectionStyle) {
        if (attackStyle == null) {
            attackStyle = getProperties().getCombatPulse().getStyle();
        }
        if (protectionStyle == null) {
            protectionStyle = getProperties().getProtectStyle();
        }
        int id = getCombatStyleDemon(protectionStyle, attackStyle);
        int oldHp = getSkills().getLifepoints();
        transform(id);
        getSkills().setLifepoints(oldHp);

        TD_SWING_HANDLER.style = getProperties().getCombatPulse().getStyle();
    }

    /**
     * Gets most damaged style.
     *
     * @return the most damaged style
     */
    public CombatStyle getMostDamagedStyle() {
        int highestDamage = 0;
        CombatStyle style = null;
        for (int i = 0; i < damageLog.length; i++) {
            if (damageLog[i] > highestDamage) {
                highestDamage = damageLog[i];
                style = CombatStyle.values()[i];
            }
        }
        return style;
    }

    /**
     * Gets combat style demon.
     *
     * @param protection the protection
     * @param style      the style
     * @return the combat style demon
     */
    public int getCombatStyleDemon(CombatStyle protection, CombatStyle style) {
        return getDemonIds(protection)[2 - style.ordinal()];
    }

    /**
     * Get demon ids int [ ].
     *
     * @param style the style
     * @return the int [ ]
     */
    public int[] getDemonIds(CombatStyle style) {
        int[][] ids = style == CombatStyle.MELEE ? MELEE : style == CombatStyle.RANGE ? RANGE : MAGE;
        return ids[getStartId() == getIds()[0] ? 0 : 1];
    }

    /**
     * Get alternate style combat style [ ].
     *
     * @param style the style
     * @return the combat style [ ]
     */
    public CombatStyle[] getAlternateStyle(CombatStyle style) {
        CombatStyle[] styles = new CombatStyle[2];
        int index = 0;
        for (int i = 0; i < CombatStyle.values().length; i++) {
            if (CombatStyle.values()[i] != style) {
                styles[index] = CombatStyle.values()[i];
                index++;
            }
        }
        return styles;
    }

    /**
     * Gets start id.
     *
     * @return the start id
     */
    public int getStartId() {
        return getId() <= 8357 ? getIds()[0] : getIds()[10];
    }

    @Override
    public int[] getIds() {
        return new int[]{
                NPCs.TORMENTED_DEMON_8349,
                NPCs.TORMENTED_DEMON_8350,
                NPCs.TORMENTED_DEMON_8351,
                NPCs.TORMENTED_DEMON_8352,
                NPCs.TORMENTED_DEMON_8353,
                NPCs.TORMENTED_DEMON_8354,
                NPCs.TORMENTED_DEMON_8355,
                NPCs.TORMENTED_DEMON_8356,
                NPCs.TORMENTED_DEMON_8357,
                NPCs.TORMENTED_DEMON_8358,
                NPCs.TORMENTED_DEMON_8359,
                NPCs.TORMENTED_DEMON_8360,
                NPCs.TORMENTED_DEMON_8361,
                NPCs.TORMENTED_DEMON_8362,
                NPCs.TORMENTED_DEMON_8363,
                NPCs.TORMENTED_DEMON_8364,
                NPCs.TORMENTED_DEMON_8365,
                NPCs.TORMENTED_DEMON_8366
        };
    }

    /**
     * The type Tormented demon swing handler.
     */
    public class TormentedDemonSwingHandler extends CombatSwingHandler {

        private CombatStyle style = CombatStyle.MELEE;

        /**
         * Instantiates a new Tormented demon swing handler.
         */
        public TormentedDemonSwingHandler() {
            super(CombatStyle.MELEE);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            return style.getSwingHandler().canSwing(entity, victim);
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            return style.getSwingHandler().swing(entity, victim, state);
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            style.getSwingHandler().impact(entity, victim, state);
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            style.getSwingHandler().visualizeImpact(entity, victim, state);
        }

        @Override
        public void visualize(Entity entity, Entity victim, BattleState state) {
            switch (style) {
                case MELEE:
                    entity.animate(entity.getProperties().getAttackAnimation());
                    entity.graphics(Graphics.create(1886));
                    break;
                case RANGE:
                    Projectile.ranged(entity, victim, 1887, 88, 36, 50, 15).send();
                    entity.animate(entity.getProperties().getRangeAnimation());
                    break;
                case MAGIC:
                    Projectile.magic(entity, victim, 1884, 88, 36, 50, 15).send();
                    entity.animate(entity.getProperties().getMagicAnimation());
                    break;
            }
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            return style.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            return style.getSwingHandler().calculateHit(entity, victim, modifier);
        }

        @Override
        public int calculateDefence(Entity victim, Entity attacker) {
            return style.getSwingHandler().calculateDefence(victim, attacker);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return style.getSwingHandler().getSetMultiplier(e, skillId);
        }

    }
}
