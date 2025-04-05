package core.game.node.entity.player.link.emote;

import core.game.node.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.setVarp;

/**
 * Manages emotes for a player.
 */
public class EmoteManager {

    /**
     * The list of currently unlocked emotes for the player.
     */
    private final List<Emotes> emotes = new ArrayList<>(20);

    /**
     * The player this emote manager belongs to.
     */
    private final Player player;

    /**
     * Creates a new {@code EmoteManager} for the specified player.
     * By default, the first 22 emotes are considered unlocked.
     *
     * @param player the player to associate with this emote manager
     */
    public EmoteManager(Player player) {
        this.player = player;
        for (int i = 0; i < 22; i++) {
            emotes.add(Emotes.values()[i]);
        }
    }

    /**
     * Refreshes the client's view of unlocked emotes by setting configuration variables (varps).
     * Also ensures the Skillcape emote is always unlocked.
     */
    public void refresh() {
        int value1 = 0;
        if (isUnlocked(Emotes.IDEA)) value1 += 4;
        if (isUnlocked(Emotes.FLAP)) value1 += 1;
        if (isUnlocked(Emotes.STOMP)) value1 += 8;
        if (isUnlocked(Emotes.SLAP_HEAD)) value1 += 2;
        setVarp(player, 802, value1, false);

        int value2 = 0;
        if (isUnlocked(Emotes.GLASS_BOX)) value2 += 2;
        if (isUnlocked(Emotes.CLIMB_ROPE)) value2 += 4;
        if (isUnlocked(Emotes.LEAN_ON_AIR)) value2 += 8;
        if (isUnlocked(Emotes.GLASS_WALL)) value2 += 1;
        if (isUnlocked(Emotes.SCARED)) value2 += 16;
        if (isUnlocked(Emotes.ZOMBIE_DANCE)) value2 += 32;
        if (isUnlocked(Emotes.ZOMBIE_WALK)) value2 += 64;
        if (isUnlocked(Emotes.BUNNY_HOP)) value2 += 128;
        if (!isUnlocked(Emotes.SKILLCAPE)) {
            unlock(Emotes.SKILLCAPE);
        }
        value2 += 256;
        if (isUnlocked(Emotes.SNOWMAN_DANCE)) value2 += 512;
        if (isUnlocked(Emotes.AIR_GUITAR)) value2 += 1024;
        if (isUnlocked(Emotes.SAFETY_FIRST)) value2 += 2048;
        if (isUnlocked(Emotes.TRICK)) value2 += 8192;
        if (isUnlocked(Emotes.EXPLORE)) value2 += 4096;
        if (isUnlocked(Emotes.FREEZE)) value2 += 32768;
        if (isUnlocked(Emotes.GIVE_THANKS)) value2 += 16384;
        setVarp(player, 313, value2, false);
        int value3 = 0;
        if (isUnlocked(Emotes.ZOMBIE_HAND)) {
            value3 = 12;
        }
        setVarp(player, 1085, value3, false);
    }

    /**
     * Locks the specified emote, removing it from the unlocked list.
     *
     * @param emote the emote to lock
     * @return {@code true} if the emote was previously unlocked and is now locked,
     * {@code false} otherwise (e.g., if it's a default emote)
     */
    public boolean lock(Emotes emote) {
        if (emote.ordinal() <= 22) {
            return false; // Cannot lock default emotes
        }
        boolean locked = emotes.remove(emote);
        refresh();
        return locked;
    }

    /**
     * Unlocks the specified emote, adding it to the player's unlocked emotes.
     *
     * @param emote the emote to unlock
     * @return {@code true} if the emote was successfully unlocked (was not already unlocked),
     * {@code false} if it was already unlocked
     */
    public boolean unlock(Emotes emote) {
        if (emotes.contains(emote)) {
            return true;
        }
        boolean unlocked = emotes.add(emote);
        refresh();
        return unlocked;
    }

    /**
     * Checks whether the emote is unlocked.
     *
     * @param emote the emote to check
     * @return {@code true} if the emote is unlocked, {@code false} otherwise
     */
    public boolean isUnlocked(Emotes emote) {
        return emotes.contains(emote);
    }

    /**
     * Checks if there are any emotes unlocked beyond the default set.
     *
     * @return {@code true} if saving is required, {@code false} otherwise
     */
    public boolean isSaveRequired() {
        for (Emotes emote : emotes) {
            if (emote.ordinal() > 21) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of currently unlocked emotes.
     *
     * @return a list of unlocked {@link Emotes}
     */
    public List<Emotes> getEmotes() {
        return emotes;
    }

    /**
     * Gets the player associated with this emote manager.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
