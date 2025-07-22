package core.game.node.entity.player.link.music;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.world.GameWorld;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.MusicPacket;
import core.net.packet.out.StringPacket;
import org.rs.consts.Music;

import java.util.HashMap;
import java.util.Set;

import static core.api.ContentAPIKt.setVarp;

/**
 * Handles the music system for a player.
 */
public final class MusicPlayer {

    /**
     * The ID of the tutorial music track, automatically unlocked on start.
     */
    public static final int TUTORIAL_MUSIC = 62;

    /**
     * The ID of the default music track played if no other is selected.
     */
    public static final int DEFAULT_MUSIC_ID = 76;

    /**
     * The configuration IDs used to send unlock state to the client interface.
     */
    private static final int[] CONFIG_IDS = {20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136, 1180, 1202};

    /**
     * The player this music player is associated with.
     */
    private final Player player;

    /**
     * The map of unlocked music tracks, keyed by their index.
     */
    private final HashMap<Integer, MusicEntry> unlocked;

    /**
     * The ID of the currently playing music track.
     */
    private int currentMusicId;

    /**
     * Indicates whether a track is currently playing.
     */
    private boolean playing;

    /**
     * Indicates whether music looping is enabled.
     */
    private boolean looping;

    /**
     * Represents the music ids unlocked immediately upon login after 2008.
     */
    private static final Set<Integer> LOGIN_MUSIC = Set.of(Music.ADVENTURE_177, Music.BITTERSWEET_BUNNY_502, Music.THE_DANCE_OF_THE_SNOW_QUEEN_593, Music.DIANGOS_LITTLE_HELPERS_532, Music.LAND_OF_SNOW_189, Music.FUNNY_BUNNIES_603, Music.HIGH_SPIRITS_205, Music.GRIMLY_FIENDISH_432, Music.EASTER_JIG_273, Music.SEA_SHANTY_XMAS_210, Music.JUNGLE_BELLS_209, Music.JUNGLE_ISLAND_XMAS_208, Music.SCAPE_MAIN_16, Music.HOMESCAPE_621, Music.SCAPE_HUNTER_207, Music.SCAPE_ORIGINAL_400, Music.SCAPE_SUMMON_457, Music.SCAPE_SANTA_547, Music.SCAPE_SCARED_321, Music.GROUND_SCAPE_466);

    /**
     * Constructs a new {@code MusicPlayer} for the specified player.
     *
     * @param player the player whose music this class manages
     */
    public MusicPlayer(Player player) {
        this.player = player;
        this.unlocked = new HashMap<>();
    }

    /**
     * Initializes the music system: refreshes unlock states, enables looping if set,
     * unlocks tutorial music if needed, plays default track, and handles Air Guitar emote.
     */
    public void init() {
        refreshList();
        setVarp(player, 19, looping ? 1 : 0);
        int value = 0;
        for (int i = 0; i < CONFIG_IDS.length; i++) {
            value |= 2 << i;
        }
        player.getPacketDispatch().sendIfaceSettings(value, 1, 187, 0, CONFIG_IDS.length * 64);
        if (!unlocked.containsKey(TUTORIAL_MUSIC)) {
            unlock(TUTORIAL_MUSIC, false);
        }
        if (!isMusicPlaying()) {
            playDefault();
        }
        if (!hasAirGuitar() && player.getEmoteManager().isUnlocked(Emotes.AIR_GUITAR)) {
            player.getPacketDispatch().sendMessage("As you no longer have all music unlocked, the Air Guitar emote is locked again.");
            player.getEmoteManager().lock(Emotes.AIR_GUITAR);
        }
    }

    /**
     * Clears all unlocked music tracks.
     */
    public void clearUnlocked() {
        this.unlocked.clear();
    }

    /**
     * Checks if the player has unlocked the Air Guitar emote.
     *
     * @return true if 200 or all tracks are unlocked, false otherwise.
     */
    public boolean hasAirGuitar() {
        return unlocked.size() >= 200 || unlocked.size() == MusicEntry.getSongs().size();
    }

    /**
     * Checks if the given music id has been unlocked by the player.
     *
     * @param musicId the ID of the track.
     * @return true if unlocked, false otherwise.
     */
    public boolean hasUnlocked(int musicId) {
        MusicEntry entry = MusicEntry.forId(musicId);
        return entry != null && hasUnlockedIndex(entry.getIndex());
    }

    /**
     * Checks if a track index has been unlocked.
     *
     * @param index the index to check.
     * @return true if unlocked, false otherwise.
     */
    public boolean hasUnlockedIndex(int index) {
        return unlocked.containsKey(index);
    }

    /**
     * Refreshes the client music interface with the current unlock state.
     */
    public void refreshList() {
        int[] values = new int[CONFIG_IDS.length];
        for (MusicEntry entry : unlocked.values()) {
            int listIndex = entry.getIndex();
            int index = listIndex / 32;
            if (index >= CONFIG_IDS.length) {
                continue;
            }
            values[index] |= 1 << (listIndex & 31);
        }
        for (int i = 0; i < CONFIG_IDS.length; i++) {
            setVarp(player, CONFIG_IDS[i], values[i]);
        }
    }

    /**
     * Plays the default music track.
     */
    public void playDefault() {
        MusicEntry entry = MusicEntry.forId(DEFAULT_MUSIC_ID);
        if (entry != null) {
            play(entry);
        }
    }

    /**
     * Replays the currently selected music track.
     */
    public void replay() {
        MusicEntry entry = MusicEntry.forId(currentMusicId);
        if (entry != null) {
            play(entry);
        }
    }

    /**
     * Plays the specified music track.
     *
     * @param entry the track to play.
     */
    public void play(MusicEntry entry) {
        if (!looping || currentMusicId != entry.getId()) {
            PacketRepository.send(MusicPacket.class, new OutgoingContext.Music(player, entry.getId(), false));
            PacketRepository.send(StringPacket.class, new OutgoingContext.StringContext(player, entry.getName() + (player.isDebug() ? (" (" + entry.getId() + ")") : ""), 187, 14));
            currentMusicId = entry.getId();
            playing = true;
        }
    }

    /**
     * Unlocks a music track by id and plays it if specified.
     *
     * @param id   the id of the track.
     * @param play whether to immediately play the track.
     */
    public void unlock(int id, boolean play) {
        MusicEntry entry = MusicEntry.forId(id);
        if (entry == null) {
            return;
        }
        if (!unlocked.containsKey(entry.getIndex())) {
            unlocked.put(entry.getIndex(), entry);

            if (!LOGIN_MUSIC.contains(id)) {
                player.getPacketDispatch().sendMessage(
                        "<col=FF0000>You have unlocked a new music track: " + entry.getName() + ".</col>"
                );
            }

            refreshList();

            if (!player.getEmoteManager().isUnlocked(Emotes.AIR_GUITAR) && hasAirGuitar()) {
                player.getEmoteManager().unlock(Emotes.AIR_GUITAR);
                String message = unlocked.size() >= 200
                        ? "You've unlocked 200 music tracks and can use a new emote!"
                        : "You've unlocked all music tracks and can use a new emote!";
                player.getPacketDispatch().sendMessage(message);
            }
        }
        if (play) {
            play(entry);
        }
    }

    /**
     * Unlocks a music track by id and plays it.
     *
     * @param id the id of the track to unlock.
     */
    public void unlock(int id) {
        unlock(id, true);
    }

    /**
     * Called periodically to recheck music playback.
     */
    public void tick() {
        if (GameWorld.getTicks() % 20 == 0) {
            if (!isPlaying()) {
                try {
                    PacketRepository.send(MusicPacket.class, new OutgoingContext.Music(player, currentMusicId, false));
                } catch (Exception e) {
                    //
                }
            }
        }
    }

    /**
     * Toggles the looping state of music playback.
     */
    public void toggleLooping() {
        looping = !looping;
        setVarp(player, 19, looping ? 1 : 0);
    }

    /**
     * Checks if music is currently playing.
     *
     * @return true if a track is playing, false otherwise
     */
    private boolean isMusicPlaying() {
        return currentMusicId > 0 && playing;
    }

    /**
     * Gets the map of unlocked music entries.
     *
     * @return the unlocked entries
     */
    public HashMap<Integer, MusicEntry> getUnlocked() {
        return unlocked;
    }

    /**
     * Gets the currently playing music track id.
     *
     * @return the current music id
     */
    public int getCurrentMusicId() {
        return currentMusicId;
    }

    /**
     * Sets the currently playing music track ID.
     *
     * @param currentMusicId the new music ID
     */
    public void setCurrentMusicId(int currentMusicId) {
        this.currentMusicId = currentMusicId;
    }

    /**
     * Checks if a music track is currently playing.
     *
     * @return true if a track is playing, false otherwise
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets whether a music track is playing.
     *
     * @param playing true if playing, false if not
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Gets the looping state of music playback.
     *
     * @return true if looping is enabled, false otherwise
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Sets the looping state of music playback.
     *
     * @param looping true to enable looping, false to disable
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
        setVarp(player, 19, looping ? 1 : 0);
    }
}
