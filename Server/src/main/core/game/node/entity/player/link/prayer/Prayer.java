package core.game.node.entity.player.link.prayer;

import core.game.event.PrayerDeactivatedEvent;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import shared.consts.Sounds;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Prayer.
 */
public final class Prayer {

    private final List<PrayerType> active = new ArrayList<>(20);

    private final Player player;

    private int prayerActiveTicks = 0;

    /**
     * Instantiates a new Prayer.
     *
     * @param player the player
     */
    public Prayer(Player player) {
        this.player = player;
    }

    /**
     * Toggle boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public final boolean toggle(final PrayerType type) {
        if (!permitted(type)) {
            return false;
        }
        return type.toggle(player, !active.contains(type));
    }

    /**
     * Reset.
     */
    public void reset() {
        // Immediately clear the lights on the client interface and terminate any bonuses
        for (PrayerType type : getActive()) {
            setVarp(player, type.getConfig(), 0, false);
            player.dispatch(new PrayerDeactivatedEvent(type));
        }
        getActive().clear();
        // Clear the overhead prayer icon a tick later
        GameWorld.getPulser().submit(new Pulse(1) {
            @Override
            public boolean pulse() {
                player.getAppearance().setHeadIcon(-1);
                player.getAppearance().sync();
                return true;
            }
        });
    }

    /**
     * Start redemption.
     */
    public void startRedemption() {
        playAudio(player, Sounds.REDEMPTION_HEAL_2681);
        player.graphics(Graphics.create(436));
        player.getSkills().heal((int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25));
        player.getSkills().setPrayerPoints(0.0);
        reset();
    }

    /**
     * Start retribution.
     *
     * @param killer the killer
     */
    public void startRetribution(Entity killer) {
        Location l = player.getLocation();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x != 0 || y != 0) {
                    Projectile.create(l, l.transform(x, y, 0), 438, 0, 0, 10, 20, 0, 11).send();
                }
            }
        }
        playAudio(player, Sounds.FIREBREATH_159);
        player.graphics(Graphics.create(437));
        int maximum = (int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25) - 1;
        if (killer != player && killer.getLocation().withinDistance(player.getLocation(), 1)) {
            killer.getImpactHandler().manualHit(player, 1 + RandomFunction.randomize(maximum), HitsplatType.NORMAL);
        }
        if (player.getProperties().isMultiZone()) {
            @SuppressWarnings("rawtypes")
            List targets = null;
            if (killer instanceof NPC) {
                targets = RegionManager.getSurroundingNPCs(player, player, killer);
            } else {
                targets = RegionManager.getSurroundingPlayers(player, player, killer);
            }
            for (Object o : targets) {
                Entity entity = (Entity) o;
                if (entity.isAttackable(player, CombatStyle.MAGIC, false)) {
                    entity.getImpactHandler().manualHit(player, 1 + RandomFunction.randomize(maximum), HitsplatType.NORMAL);
                }
            }
        }
    }

    /**
     * Tick.
     */
    public void tick() {
        if (!getActive().isEmpty()) prayerActiveTicks++;
        else prayerActiveTicks = 0;

        if (prayerActiveTicks > 0 && prayerActiveTicks % 2 == 0) {
            if (getPlayer().getSkills().getPrayerPoints() == 0) {
                playAudio(getPlayer(), Sounds.PRAYER_DRAIN_2672);
                getPlayer().sendMessage("You have run out of prayer points; you must recharge at an altar.");
                reset();
                return;
            }
            double amountDrain = 0;
            for (PrayerType type : getActive()) {
                double drain = type.getDrain();
                double bonus = (1 / 30f) * getPlayer().getProperties().getBonuses()[12];
                drain = drain * (1 + bonus);
                drain = 0.6 / drain;
                amountDrain += drain;
            }

            getPlayer().getSkills().decrementPrayerPoints(amountDrain);
        }
    }

    /**
     * Gets skill bonus.
     *
     * @param skillId the skill id
     * @return the skill bonus
     */
    public double getSkillBonus(int skillId) {
        double bonus = 0.0;
        for (PrayerType type : active) {
            for (SkillBonus b : type.getBonuses()) {
                if (b.getSkillId() == skillId) {
                    bonus += b.getBonus();
                }
            }
        }
        return bonus;
    }

    private boolean permitted(final PrayerType type) {
        return player.getSkills().getPrayerPoints() > 0 && type.permitted(player);
    }

    /**
     * Get boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean get(PrayerType type) {
        return active.contains(type);
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets active.
     *
     * @return the active
     */
    public List<PrayerType> getActive() {
        return active;
    }

}
