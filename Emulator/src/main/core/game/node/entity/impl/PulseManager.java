package core.game.node.entity.impl;

import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatPulse;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the pulses (tasks) that run within the game. Pulses are used to control various actions and
 * interactions within the game, such as movement, combat, or custom player actions.
 * <p>
 * The PulseManager ensures that pulses are run in a controlled manner, allowing for priority-based
 * execution of different pulse types (standard, strong, etc.) and provides methods to manage pulse
 * execution and cancellation.
 */
public final class PulseManager {

    /**
     * A map of currently active pulses, keyed by their respective pulse type.
     */
    private final HashMap<PulseType, Pulse> currentPulses = new HashMap<>();

    /**
     * Runs a pulse with the default pulse type of {@link PulseType#STANDARD}.
     *
     * @param pulse The pulse to run.
     */
    public void run(Pulse pulse) {
        run(pulse, PulseType.STANDARD);
    }

    /**
     * Runs a pulse with the specified pulse type.
     * <p>
     * This method ensures that pulses are handled in the correct order, with priority given to strong pulses.
     *
     * @param pulse     The pulse to run.
     * @param pulseType The type of pulse (e.g., {@link PulseType#STANDARD}, {@link PulseType#STRONG}).
     */
    public void run(Pulse pulse, PulseType pulseType) {
        ArrayList<PulseType> toRemove = new ArrayList<>(currentPulses.size());
        currentPulses.forEach((key, value) -> {
            if (value != null && !value.isRunning()) {
                toRemove.add(key);
            }
        });
        for (PulseType t : toRemove) currentPulses.remove(t);

        if (currentPulses.get(PulseType.STRONG) != null) {
            return;
        }

        if (!clear(pulseType)) {
            return;
        }

        if (pulseType == PulseType.STRONG) {
            clear();
        }

        currentPulses.put(pulseType, pulse);
        pulse.start();
        if (pulse.isRunning()) {
            GameWorld.getPulser().submit(pulse);
        }
    }

    /**
     * Clears all non-strong pulses from the manager, stopping them if necessary.
     */
    public void clear() {
        currentPulses.forEach((type, pulse) -> {
            if (type != PulseType.STRONG && pulse != null) pulse.stop();
        });
    }

    /**
     * Clears the specified pulse type from the manager and stops the pulse if it's running.
     *
     * @param pulseType The pulse type to clear.
     * @return {@code true} if the pulse was successfully cleared, {@code false} otherwise.
     */
    public boolean clear(PulseType pulseType) {
        Pulse pulse = currentPulses.get(pulseType);

        if (pulse != null) {
            pulse.stop();
            currentPulses.remove(pulseType);
        }
        return true;
    }

    /**
     * Runs a default unhandled action pulse for the given player, sending a default message when executed.
     *
     * @param player    The player to run the pulse for.
     * @param pulseType The type of pulse to run.
     * @return The created pulse.
     */
    public Pulse runUnhandledAction(final Player player, PulseType pulseType) {
        Pulse pulse = new Pulse(1, player) {
            @Override
            public boolean pulse() {
                player.getPacketDispatch().sendMessage("Nothing interesting happens.");
                return true;
            }
        };
        run(pulse, pulseType);
        return pulse;
    }

    /**
     * Checks if the current pulse is a movement or combat pulse.
     *
     * @return {@code true} if the current pulse is a movement or combat pulse, {@code false} otherwise.
     */
    public boolean isMovingPulse() {
        if (!hasPulseRunning()) {
            return false;
        }

        Pulse current = getCurrent();
        return current instanceof MovementPulse || current instanceof CombatPulse;
    }

    /**
     * Checks if there is any pulse currently running.
     *
     * @return {@code true} if a pulse is running, {@code false} otherwise.
     */
    public boolean hasPulseRunning() {
        return getCurrent() != null && getCurrent().isRunning();
    }

    /**
     * Cancels the death task for the given entity, if one exists.
     *
     * @param e The entity whose death task should be canceled.
     */
    public static void cancelDeathTask(Entity e) {
        if (!DeathTask.isDead(e) || e.getPulseManager().getCurrent() == null) {
            return;
        }
        e.getPulseManager().getCurrent().stop();
    }

    /**
     * Retrieves the current pulse that is running, or {@code null} if no pulse is running.
     *
     * @return The current pulse, or {@code null} if none is running.
     */
    public Pulse getCurrent() {
        PulseType[] types = PulseType.values();
        for (PulseType type : types) {
            if (currentPulses.get(type) != null) {
                return currentPulses.get(type);
            }
        }
        return null;
    }
}
