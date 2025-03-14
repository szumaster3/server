package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.stun;

/**
 * The type Minotaur familiar npc.
 */
@Initializable
public final class MinotaurFamiliarNPC implements Plugin<Object> {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ClassScanner.definePlugin(new BronzeMinotaurNPC());
        ClassScanner.definePlugin(new IronMinotaurNPC());
        ClassScanner.definePlugin(new SteelMinotaurNPC());
        ClassScanner.definePlugin(new MithrilMinotaurNPC());
        ClassScanner.definePlugin(new AdamantMinotaurNPC());
        ClassScanner.definePlugin(new RuneMinotaurNPC());
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Bull rush boolean.
     *
     * @param familiar the familiar
     * @param special  the special
     * @param maxHit   the max hit
     * @return the boolean
     */
    public boolean bullRush(final content.global.skill.summoning.familiar.Familiar familiar, final content.global.skill.summoning.familiar.FamiliarSpecial special, final int maxHit) {
        final Entity target = (Entity) special.getNode();
        if (!familiar.canCombatSpecial(target)) {
            return false;
        }
        familiar.sendFamiliarHit(target, RandomFunction.random(maxHit));
        Projectile.magic(familiar, target, 1497, 80, 36, 70, 10).send();
        familiar.visualize(Animation.create(8026), Graphics.create(1496));
        if (!(familiar instanceof BronzeMinotaurNPC || familiar instanceof RuneMinotaurNPC) && RandomFunction.random(10) < 6) {
            final int ticks = 2 + (int) Math.floor(familiar.getLocation().getDistance(target.getLocation()) * 0.5);
            GameWorld.getPulser().submit(new Pulse(ticks) {
                @Override
                public boolean pulse() {
                    stun(target, 4);
                    return true;
                }
            });
        }
        return true;
    }

    /**
     * The type Bronze minotaur npc.
     */
    public class BronzeMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Bronze minotaur npc.
         */
        public BronzeMinotaurNPC() {
            this(null, 6853);
        }

        /**
         * Instantiates a new Bronze minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public BronzeMinotaurNPC(Player owner, int id) {
            super(owner, id, 3000, 12073, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new BronzeMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return bullRush(this, special, 4);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6853, 6854};
        }

    }

    /**
     * The type Iron minotaur npc.
     */
    public class IronMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Iron minotaur npc.
         */
        public IronMinotaurNPC() {
            this(null, 6855);
        }

        /**
         * Instantiates a new Iron minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public IronMinotaurNPC(Player owner, int id) {
            super(owner, id, 3700, 12075, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new IronMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return bullRush(this, special, 6);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6855, 6856};
        }

    }

    /**
     * The type Steel minotaur npc.
     */
    public class SteelMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Steel minotaur npc.
         */
        public SteelMinotaurNPC() {
            this(null, 6857);
        }

        /**
         * Instantiates a new Steel minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public SteelMinotaurNPC(Player owner, int id) {
            super(owner, id, 4600, 12077, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new SteelMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return bullRush(this, special, 9);
        }

        @Override
        public CombatStyle getCombatStyle() {
            return CombatStyle.MELEE;
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6857, 6858};
        }

    }

    /**
     * The type Mithril minotaur npc.
     */
    public class MithrilMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Mithril minotaur npc.
         */
        public MithrilMinotaurNPC() {
            this(null, 6859);
        }

        /**
         * Instantiates a new Mithril minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public MithrilMinotaurNPC(Player owner, int id) {
            super(owner, id, 5500, 12079, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new MithrilMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return bullRush(this, special, 13);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6859, 6860};
        }

    }

    /**
     * The type Adamant minotaur npc.
     */
    public class AdamantMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Adamant minotaur npc.
         */
        public AdamantMinotaurNPC() {
            this(null, 6861);
        }

        /**
         * Instantiates a new Adamant minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public AdamantMinotaurNPC(Player owner, int id) {
            super(owner, id, 6600, 12081, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new AdamantMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return bullRush(this, special, 16);
        }

        @Override
        public CombatStyle getCombatStyle() {
            return CombatStyle.MELEE;
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6861, 6862};
        }

    }

    /**
     * The type Rune minotaur npc.
     */
    public class RuneMinotaurNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Rune minotaur npc.
         */
        public RuneMinotaurNPC() {
            this(null, 6863);
        }

        /**
         * Instantiates a new Rune minotaur npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public RuneMinotaurNPC(Player owner, int id) {
            super(owner, id, 15100, 12083, 6, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new RuneMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 20);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{6863, 6864};
        }

    }
}
