package core.game.node.entity.combat;

import content.data.EnchantedJewellery;
import content.data.GameAttributes;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.pet.Pet;
import core.game.bots.AIPlayer;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.zone.ZoneType;
import core.game.world.repository.Repository;
import org.rs.consts.Items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * The type Impact handler.
 */
public final class ImpactHandler {

    private final Entity entity;

    private int disabledTicks;

    private final Map<Entity, Integer> npcImpactLog = new HashMap<>();

    private final Map<Integer, Integer> playerImpactLog = new HashMap<>();

    private final Queue<Impact> impactQueue = new LinkedList<Impact>();

    /**
     * Instantiates a new Impact handler.
     *
     * @param entity the entity
     */
    public ImpactHandler(Entity entity) {
        this.entity = entity;
    }

    /**
     * Manual hit impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param type   the type
     * @return the impact
     */
    public Impact manualHit(Entity source, int hit, HitsplatType type) {
        if (hit > entity.getSkills().getLifepoints()) {
            hit = entity.getSkills().getLifepoints();
        }
        return handleImpact(source, hit, null, null, type);
    }

    /**
     * Visual hit impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param type   the type
     * @return the impact
     */
    public Impact visualHit(Entity source, int hit, HitsplatType type) {
        if (type == null || type == HitsplatType.NORMAL) {
            type = hit == 0 ? HitsplatType.MISS : HitsplatType.NORMAL;
        }
        Impact impact = new Impact(source, hit, null, type);
        impactQueue.add(impact);
        return impact;
    }

    /**
     * Manual hit impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param type   the type
     * @param ticks  the ticks
     * @return the impact
     */
    public Impact manualHit(final Entity source, int hit, final HitsplatType type, int ticks) {
        if (ticks > 0) {
            final int damage = hit;
            GameWorld.getPulser().submit(new Pulse(ticks, entity) {
                @Override
                public boolean pulse() {
                    manualHit(source, damage, type);
                    return true;
                }
            });
            return null;
        }
        return manualHit(source, hit, type);
    }

    /**
     * Handle impact impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param style  the style
     * @return the impact
     */
    public Impact handleImpact(Entity source, int hit, CombatStyle style) {
        return handleImpact(source, hit, style, null, null, false);
    }

    /**
     * Handle impact impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param style  the style
     * @param state  the state
     * @return the impact
     */
    public Impact handleImpact(Entity source, int hit, CombatStyle style, BattleState state) {
        return handleImpact(source, hit, style, state, null, false);
    }

    /**
     * Handle impact impact.
     *
     * @param source the source
     * @param hit    the hit
     * @param style  the style
     * @param state  the state
     * @param type   the type
     * @return the impact
     */
    public Impact handleImpact(Entity source, int hit, CombatStyle style, BattleState state, HitsplatType type) {
        return handleImpact(source, hit, style, state, type, false);
    }

    /**
     * Handle impact impact.
     *
     * @param source    the source
     * @param hit       the hit
     * @param style     the style
     * @param state     the state
     * @param type      the type
     * @param secondary the secondary
     * @return the impact
     */
    public Impact handleImpact(Entity source, int hit, final CombatStyle style, final BattleState state, HitsplatType type, boolean secondary) {
        if (DeathTask.isDead(this.entity)) {
            this.entity.getProperties().getCombatPulse().setVictim(null);
            this.entity.getProperties().getCombatPulse().stop();
            return null;
        }
        boolean fam = source instanceof Familiar;
        if (fam) {
            source = ((Familiar) source).getOwner();
        }
        if (disabledTicks > GameWorld.getTicks()) {
            return null;
        }
        if (entity instanceof Player && !(entity instanceof AIPlayer) && !(entity.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false))) {
            Impact impact = new Impact(source, 0, style, HitsplatType.MISS);
            impactQueue.add(impact);
            return impact;
        }
        hit -= entity.getSkills().hit(hit);
        if (type == null || type == HitsplatType.NORMAL) {
            if (hit == 0) {
                type = HitsplatType.MISS;
            } else {
                type = HitsplatType.NORMAL;
            }
        } else if (hit == 0 && type == HitsplatType.POISON) {
            return null;
        }
        if (hit > 0) {
            if (source instanceof Player) {
                int uid = source.asPlayer().getDetails().getUid();
                Integer value = playerImpactLog.get(uid);
                playerImpactLog.put(uid, value == null ? hit : hit + value);
            } else {
                Integer value = npcImpactLog.get(source);
                npcImpactLog.put(source, value == null ? hit : hit + value);
            }
        }
        if (style != null && style.getSwingHandler() != null && source instanceof Player) {
            Player player = source.asPlayer();
            if (fam && player.getFamiliarManager().hasFamiliar() && !(player.getFamiliarManager().getFamiliar() instanceof Pet)) {
                source.setAttribute("fam-exp", true);
            }
            source.removeAttribute("fam-exp");
        }
        boolean dead = false;
        if (entity.getSkills().getLifepoints() < 1) {
            entity.getProperties().getCombatPulse().stop();
            entity.face(source);
            entity.startDeath(getMostDamageEntity(source));
            dead = true;
        } else if (entity.getSkills().getLifepoints() < (entity.getSkills().getMaximumLifepoints() * 0.1)) {
            if (entity instanceof Player && ((Player) entity).getPrayer().get(PrayerType.REDEMPTION)) {
                ((Player) entity).getPrayer().startRedemption();
            }
        }
        if (entity instanceof Player) {
            Player p = entity.asPlayer();
            if (p.getAttribute("godMode", false)) {
                p.getSkills().heal(10000);
            }
        }
        Impact impact = new Impact(source, hit, style, type);
        impactQueue.add(impact);
        if (entity instanceof Player && !dead) {
            final Player p = entity.asPlayer();
            if (p.getZoneMonitor().getType() != ZoneType.SAFE.getId() && (p.getEquipment().contains(Items.RING_OF_LIFE_2570, 1))) {
                int percentage = (int) (entity.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10);
                if (p.getSkills().getLifepoints() <= percentage) {
                    Item rolItem = new Item(Items.RING_OF_LIFE_2570);
                    if (EnchantedJewellery.RING_OF_LIFE.attemptTeleport(p, rolItem, 0, true)) {
                        p.sendMessage("Your ring of life saves you and in the process is destroyed.");
                    }
                }
            }
        }
        return impact;
    }

    /**
     * Handle recoil effect.
     *
     * @param attacker the attacker
     * @param hit      the hit
     */
    public void handleRecoilEffect(Entity attacker, int hit) {
        int damage = (int) Math.ceil(hit * 0.1);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int current = player.getSavedData().globalData.getRecoilDamage();
            if (damage >= current) {
                damage = current;
                player.getPacketDispatch().sendMessage("Your Ring of Recoil has shattered.");
                player.getEquipment().replace(null, EquipmentContainer.SLOT_RING);
                player.getSavedData().globalData.setRecoilDamage(40);
            } else {
                player.getSavedData().globalData.setRecoilDamage(current - damage);
            }
        }
        if (damage > 0) {
            attacker.getImpactHandler().manualHit(entity, damage, HitsplatType.NORMAL);
        }
    }

    /**
     * Gets most damage entity.
     *
     * @param killer the killer
     * @return the most damage entity
     */
    public Entity getMostDamageEntity(Entity killer) {
        Entity entity = this.entity;
        if (entity instanceof Player) {
            return killer;
        }

        int damage = -1;
        if (playerImpactLog.isEmpty()) {
            for (Entity e : npcImpactLog.keySet()) {
                if (e == this.entity) {
                    continue;
                }
                int amount = npcImpactLog.get(e);
                if (amount > damage) {
                    damage = amount;
                    entity = e;
                }
            }
            return entity;
        }

        int player = 0; //needs to be fake-initialized because java is dumb
        for (int uid : playerImpactLog.keySet()) {
            int amount = playerImpactLog.get(uid);
            if (amount > damage) {
                damage = amount;
                player = uid;
            }
        }
        return Repository.getPlayerByUid(player);
    }

    /**
     * Gets npc impact log.
     *
     * @return the npc impact log
     */
    public Map<Entity, Integer> getNpcImpactLog() {
        return npcImpactLog;
    }

    /**
     * Gets player impact log.
     *
     * @return the player impact log
     */
    public Map<Integer, Integer> getPlayerImpactLog() {
        return playerImpactLog;
    }

    /**
     * Is hit update boolean.
     *
     * @return the boolean
     */
    public boolean isHitUpdate() {
        return impactQueue.peek() != null;
    }

    /**
     * Gets impact queue.
     *
     * @return the impact queue
     */
    public Queue<Impact> getImpactQueue() {
        return impactQueue;
    }

    /**
     * Gets disabled ticks.
     *
     * @return the disabled ticks
     */
    public int getDisabledTicks() {
        return disabledTicks;
    }

    /**
     * Sets disabled ticks.
     *
     * @param ticks the ticks
     */
    public void setDisabledTicks(int ticks) {
        this.disabledTicks = GameWorld.getTicks() + ticks;
    }

    /**
     * The type Impact.
     */
    public static class Impact {

        private final Entity source;

        private final int amount;

        private final CombatStyle style;

        private final HitsplatType type;

        /**
         * Instantiates a new Impact.
         *
         * @param source the source
         * @param amount the amount
         * @param style  the style
         * @param type   the type
         */
        public Impact(Entity source, int amount, CombatStyle style, HitsplatType type) {
            this.source = source;
            this.amount = amount;
            this.style = style;
            this.type = type;
        }

        /**
         * Gets source.
         *
         * @return the source
         */
        public Entity getSource() {
            return source;
        }

        /**
         * Gets amount.
         *
         * @return the amount
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Gets style.
         *
         * @return the style
         */
        public CombatStyle getStyle() {
            return style;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public HitsplatType getType() {
            return type;
        }
    }

    /**
     * The enum Hitsplat type.
     */
    public static enum HitsplatType {

        /**
         * Miss hitsplat type.
         */
        MISS,

        /**
         * Normal hitsplat type.
         */
        NORMAL,

        /**
         * Poison hitsplat type.
         */
        POISON,

        /**
         * Disease hitsplat type.
         */
        DISEASE,

        /**
         * Normal 1 hitsplat type.
         */
        NORMAL_1,

        /**
         * Venom hitsplat type.
         */
        VENOM;
    }
}
