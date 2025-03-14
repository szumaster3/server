package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Void familiar npc.
 */
@Initializable
public final class VoidFamiliarNPC implements Plugin<Object> {

    private static final Item[] ITEMS = new Item[]{new Item(434), new Item(440), new Item(453), new Item(444), new Item(447)};

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ClassScanner.definePlugin(new VoidRavagerNPC());
        ClassScanner.definePlugin(new VoidShifterNPC());
        ClassScanner.definePlugin(new VoidSpinnerNPC());
        ClassScanner.definePlugin(new VoidTorcherNPC());
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Call to arms boolean.
     *
     * @param familiar the familiar
     * @param special  the special
     * @return the boolean
     */
    public boolean callToArms(content.global.skill.summoning.familiar.Familiar familiar, content.global.skill.summoning.familiar.FamiliarSpecial special) {
        final Player owner = familiar.getOwner();
        owner.lock();
        GameWorld.getPulser().submit(new Pulse(1, owner) {
            int counter;

            @Override
            public boolean pulse() {
                switch (++counter) {
                    case 1:
                        owner.visualize(Animation.create(8136), Graphics.create(1503));
                        break;
                    case 3:
                        owner.unlock();
                        owner.getProperties().setTeleportLocation(Location.create(2659, 2658, 0));
                        owner.visualize(Animation.create(8137), Graphics.create(1502));
                        return true;
                }
                return false;
            }
        });
        return true;
    }

    /**
     * The type Void ravager npc.
     */
    public final class VoidRavagerNPC extends Forager {

        /**
         * Instantiates a new Void ravager npc.
         */
        public VoidRavagerNPC() {
            this(null, 7370);
        }

        /**
         * Instantiates a new Void ravager npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public VoidRavagerNPC(Player owner, int id) {
            super(owner, id, 2700, 12818, 3, WeaponInterface.STYLE_AGGRESSIVE, ITEMS);
            boosts.add(new SkillBonus(Skills.MINING, 1));
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new VoidRavagerNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return callToArms(this, special);
        }

        @Override
        public int[] getIds() {
            return new int[]{7370, 7371};
        }

    }

    /**
     * The type Void shifter npc.
     */
    public class VoidShifterNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Void shifter npc.
         */
        public VoidShifterNPC() {
            this(null, 7367);
        }

        /**
         * Instantiates a new Void shifter npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public VoidShifterNPC(Player owner, int id) {
            super(owner, id, 9400, 12814, 3, WeaponInterface.STYLE_ACCURATE);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new VoidShifterNPC(owner, id);
        }

        @Override
        public void adjustPlayerBattle(BattleState state) {
            super.adjustPlayerBattle(state);
            int percentage = (int) (owner.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10);
            if (owner.getSkills().getLifepoints() < percentage) {
                owner.getProperties().setTeleportLocation(Location.create(2659, 2658, 0));
            }
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return callToArms(this, special);
        }

        @Override
        public int[] getIds() {
            return new int[]{7367, 7368};
        }

    }

    /**
     * The type Void spinner npc.
     */
    public class VoidSpinnerNPC extends content.global.skill.summoning.familiar.Familiar {

        private int healDelay;

        /**
         * Instantiates a new Void spinner npc.
         */
        public VoidSpinnerNPC() {
            this(null, 7333);
        }

        /**
         * Instantiates a new Void spinner npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public VoidSpinnerNPC(Player owner, int id) {
            super(owner, id, 2700, 12780, 3, WeaponInterface.STYLE_DEFENSIVE);
        }

        @Override
        public void handleFamiliarTick() {
            super.handleFamiliarTick();
            if (healDelay < GameWorld.getTicks()) {
                getSkills().heal(1);
                healDelay = GameWorld.getTicks() + 25;
            }
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new VoidSpinnerNPC(owner, id);
        }

        @Override
        protected boolean specialMove(content.global.skill.summoning.familiar.FamiliarSpecial special) {
            return callToArms(this, special);
        }

        @Override
        public int[] getIds() {
            return new int[]{7333, 7334};
        }

    }

    /**
     * The type Void torcher npc.
     */
    public class VoidTorcherNPC extends content.global.skill.summoning.familiar.Familiar {

        /**
         * Instantiates a new Void torcher npc.
         */
        public VoidTorcherNPC() {
            this(null, 7351);
        }

        /**
         * Instantiates a new Void torcher npc.
         *
         * @param owner the owner
         * @param id    the id
         */
        public VoidTorcherNPC(Player owner, int id) {
            super(owner, id, 9400, 12798, 3, WeaponInterface.STYLE_CAST);
        }

        @Override
        public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
            return new VoidTorcherNPC(owner, id);
        }

        @Override
        public void configureFamiliar() {
            ClassScanner.definePlugin(new OptionHandler() {

                @Override
                public Plugin<Object> newInstance(Object arg) throws Throwable {
                    for (int i : getIds()) {
                        NPCDefinition.forId(i).getHandlers().put("option:strike", this);
                    }
                    return this;
                }

                @Override
                public boolean handle(Player player, Node node, String option) {
                    final content.global.skill.summoning.familiar.Familiar familiar = (Familiar) node;
                    if (!player.getFamiliarManager().isOwner(familiar)) {
                        return true;
                    }
                    // TODO:
                    return true;
                }

            });
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return callToArms(this, special);
        }

        @Override
        public int[] getIds() {
            return new int[]{7351, 7352};
        }

    }
}
