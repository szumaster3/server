package core.game.node.entity.player.link;

import com.google.gson.JsonObject;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.net.packet.IoBuffer;

import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.setVarp;

/**
 * The type Settings.
 */
public final class Settings {
    private final Player player;
    private double runEnergy = 100.0;
    private double weight;
    private int brightness = 2;
    private int musicVolume;
    private int soundEffectVolume;
    private int areaSoundVolume;
    private boolean singleMouseButton;
    private boolean disableChatEffects;
    private boolean splitPrivateChat;
    private boolean acceptAid;
    private boolean runToggled;
    private int publicChatSetting = 0;
    private int privateChatSetting = 0;
    private int clanChatSetting = 0;
    private int tradeSetting = 0;
    private int assistSetting = 0;
    private boolean specialToggled;
    private int specialEnergy = 100;
    private int attackStyleIndex = 0;

    /**
     * Instantiates a new Settings.
     *
     * @param player the player
     */
    public Settings(Player player) {
        this.player = player;
    }

    /**
     * Update.
     */
    public void update() {
        setVarp(player, 166, brightness + 1);
        setVarp(player, 168, musicVolume);
        setVarp(player, 169, soundEffectVolume);
        setVarp(player, 872, areaSoundVolume);
        setVarp(player, 170, singleMouseButton ? 1 : 0);
        setVarp(player, 171, disableChatEffects ? 1 : 0);
        setVarp(player, 287, splitPrivateChat ? 1 : 0);
        setVarp(player, 427, acceptAid ? 1 : 0);
        setVarp(player, 172, !player.getProperties().isRetaliating() ? 1 : 0);
        setVarp(player, 173, runToggled ? 1 : 0);
        setVarp(player, 1054, clanChatSetting);
        setVarp(player, 1055, assistSetting);
        setVarp(player, 300, specialEnergy * 10);
        setVarp(player, 43, attackStyleIndex);
        player.getPacketDispatch().sendRunEnergy();
        updateChatSettings();
    }

    /**
     * Toggle attack style index.
     *
     * @param index the index
     */
    public void toggleAttackStyleIndex(int index) {
        this.attackStyleIndex = index;
        setVarp(player, 43, attackStyleIndex);
    }

    /**
     * Update chat settings.
     */
    public void updateChatSettings() {
        player.getSession().write(new IoBuffer(232).put(publicChatSetting).put(privateChatSetting).put(tradeSetting));
    }

    /**
     * Update chat settings.
     *
     * @param pub   the pub
     * @param priv  the priv
     * @param trade the trade
     */
    public void updateChatSettings(int pub, int priv, int trade) {
        boolean update = false;
        if (publicChatSetting != pub) {
            publicChatSetting = pub;
            update = true;
        }
        if (tradeSetting != trade) {
            tradeSetting = trade;
            update = true;
        }
        if (update) {
            updateChatSettings();
        }
    }

    /**
     * Sets chat settings.
     *
     * @param pub   the pub
     * @param priv  the priv
     * @param trade the trade
     */
    public void setChatSettings(int pub, int priv, int trade) {
        publicChatSetting = pub;
        privateChatSetting = priv;
        tradeSetting = trade;
    }

    /**
     * Save.
     *
     * @param buffer the buffer
     */
    public void save(ByteBuffer buffer) {
        buffer.put((byte) 1).put((byte) brightness).put((byte) musicVolume).put((byte) soundEffectVolume).put((byte) areaSoundVolume).put((byte) (singleMouseButton ? 1 : 0)).put((byte) (disableChatEffects ? 1 : 0)).put((byte) (splitPrivateChat ? 1 : 0)).put((byte) (acceptAid ? 1 : 0)).put((byte) (runToggled ? 1 : 0)).put((byte) publicChatSetting).put((byte) privateChatSetting).put((byte) clanChatSetting).put((byte) tradeSetting).put((byte) assistSetting).put(((byte) runEnergy));
        if (!player.getProperties().isRetaliating()) {
            buffer.put((byte) 2);
        }
        if (specialEnergy != 100) {
            buffer.put((byte) 3).put((byte) specialEnergy);
        }
        if (attackStyleIndex != 0) {
            buffer.put((byte) 4).put((byte) attackStyleIndex);
        }
        buffer.put((byte) 0);
    }

    /**
     * Parse.
     *
     * @param buffer the buffer
     */
    public void parse(ByteBuffer buffer) {
        int opcode;
        while ((opcode = buffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    brightness = buffer.get();
                    musicVolume = buffer.get();
                    soundEffectVolume = buffer.get();
                    areaSoundVolume = buffer.get();
                    singleMouseButton = buffer.get() == 1;
                    disableChatEffects = buffer.get() == 1;
                    splitPrivateChat = buffer.get() == 1;
                    acceptAid = buffer.get() == 1;
                    runToggled = buffer.get() == 1;
                    publicChatSetting = buffer.get();
                    privateChatSetting = buffer.get();
                    clanChatSetting = buffer.get();
                    tradeSetting = buffer.get();
                    assistSetting = buffer.get();
                    runEnergy = buffer.get();
                    break;
                case 2:
                    player.getProperties().setRetaliating(false);
                    break;
                case 3:
                    specialEnergy = buffer.get() & 0xFF;
                    break;
                case 4:
                    attackStyleIndex = buffer.get();
                    break;
            }
        }
    }

    /**
     * Parse.
     *
     * @param settingsData the settings data
     */
    public void parse(JsonObject settingsData) {
        brightness = settingsData.get("brightness").getAsInt();
        musicVolume = settingsData.get("musicVolume").getAsInt();
        soundEffectVolume = settingsData.get("soundEffectVolume").getAsInt();
        areaSoundVolume = settingsData.get("areaSoundVolume").getAsInt();
        singleMouseButton = settingsData.get("singleMouse").getAsBoolean();
        disableChatEffects = settingsData.get("disableChatEffects").getAsBoolean();
        splitPrivateChat = settingsData.get("splitPrivate").getAsBoolean();
        acceptAid = settingsData.get("acceptAid").getAsBoolean();
        runToggled = settingsData.get("runToggled").getAsBoolean();
        publicChatSetting = settingsData.get("publicChatSetting").getAsInt();
        privateChatSetting = settingsData.get("privateChatSetting").getAsInt();
        clanChatSetting = settingsData.get("clanChatSetting").getAsInt();
        tradeSetting = settingsData.get("tradeSetting").getAsInt();
        assistSetting = settingsData.get("assistSetting").getAsInt();
        runEnergy = settingsData.get("runEnergy").getAsDouble();
        specialEnergy = settingsData.get("specialEnergy").getAsInt();
        attackStyleIndex = settingsData.get("attackStyle").getAsInt();
        player.getProperties().setRetaliating(settingsData.get("retaliation").getAsBoolean());
    }

    /**
     * Toggle special bar.
     */
    public void toggleSpecialBar() {
        setSpecialToggled(!specialToggled);
    }

    /**
     * Sets special toggled.
     *
     * @param enable the enable
     */
    public void setSpecialToggled(boolean enable) {
        specialToggled = !specialToggled;
        setVarp(player, 301, specialToggled ? 1 : 0);
    }

    /**
     * Is special toggled boolean.
     *
     * @return the boolean
     */
    public boolean isSpecialToggled() {
        return specialToggled;
    }

    /**
     * Drain special boolean.
     *
     * @param amount the amount
     * @return the boolean
     */
    public boolean drainSpecial(int amount) {
        if (!specialToggled) {
            return false;
        }
        setSpecialToggled(false);
        if (amount > specialEnergy) {
            player.getPacketDispatch().sendMessage("You do not have enough special attack energy left.");
            return false;
        }
        setSpecialEnergy(specialEnergy - amount);
        return true;
    }

    /**
     * Sets special energy.
     *
     * @param value the value
     */
    public void setSpecialEnergy(int value) {
        specialEnergy = value;
        setVarp(player, 300, specialEnergy * 10);
    }

    /**
     * Gets special energy.
     *
     * @return the special energy
     */
    public int getSpecialEnergy() {
        return specialEnergy;
    }

    /**
     * Toggle retaliating.
     */
    public void toggleRetaliating() {
        player.getProperties().setRetaliating(!player.getProperties().isRetaliating());
        setVarp(player, 172, !player.getProperties().isRetaliating() ? 1 : 0);
    }

    /**
     * Toggle mouse button.
     */
    public void toggleMouseButton() {
        singleMouseButton = !singleMouseButton;
        setVarp(player, 170, singleMouseButton ? 1 : 0);
    }

    /**
     * Toggle chat effects.
     */
    public void toggleChatEffects() {
        disableChatEffects = !disableChatEffects;
        setVarp(player, 171, disableChatEffects ? 1 : 0);
    }

    /**
     * Toggle split private chat.
     */
    public void toggleSplitPrivateChat() {
        splitPrivateChat = !splitPrivateChat;
        setVarp(player, 287, splitPrivateChat ? 1 : 0);
    }

    /**
     * Toggle accept aid.
     */
    public void toggleAcceptAid() {
        acceptAid = !acceptAid;
        setVarp(player, 427, acceptAid ? 1 : 0);
    }

    /**
     * Toggle run.
     */
    public void toggleRun() {
        setRunToggled(!runToggled);
    }

    /**
     * Sets run toggled.
     *
     * @param enabled the enabled
     */
    public void setRunToggled(boolean enabled) {
        runToggled = enabled;
        setVarp(player, 173, runToggled ? 1 : 0);
    }

    /**
     * Update run energy.
     *
     * @param drain the drain
     */
    public void updateRunEnergy(double drain) {
        runEnergy -= drain;
        if (runEnergy < 0) {
            runEnergy = 0.0;
        } else if (runEnergy > 100) {
            runEnergy = 100.0;
        }
        player.getPacketDispatch().sendRunEnergy();
    }

    /**
     * Update weight.
     */
    public void updateWeight() {
        weight = 0.0;
        for (int i = 0; i < 28; i++) {
            Item item = player.getInventory().get(i);
            if (item == null) {
                continue;
            }
            double value = item.getDefinition().getConfiguration(ItemConfigParser.WEIGHT, 0.0);
            if (value > 0) {
                weight += value;
            }
        }
        for (int i = 0; i < 11; i++) {
            Item item = player.getEquipment().get(i);
            if (item == null) {
                continue;
            }
            weight += item.getDefinition().getConfiguration(ItemConfigParser.WEIGHT, 0.0);
        }
        player.getPacketDispatch().sendString((int) weight + " kg", 667, 32);
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets brightness.
     *
     * @return the brightness
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Sets brightness.
     *
     * @param brightness the brightness
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
        setVarp(player, 166, brightness + 1);
    }

    /**
     * Gets music volume.
     *
     * @return the music volume
     */
    public int getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets music volume.
     *
     * @param musicVolume the music volume
     */
    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
        setVarp(player, 168, musicVolume);
    }

    /**
     * Gets sound effect volume.
     *
     * @return the sound effect volume
     */
    public int getSoundEffectVolume() {
        return soundEffectVolume;
    }

    /**
     * Sets sound effect volume.
     *
     * @param soundEffectVolume the sound effect volume
     */
    public void setSoundEffectVolume(int soundEffectVolume) {
        this.soundEffectVolume = soundEffectVolume;
        setVarp(player, 169, soundEffectVolume);
    }

    /**
     * Gets area sound volume.
     *
     * @return the area sound volume
     */
    public int getAreaSoundVolume() {
        return areaSoundVolume;
    }

    /**
     * Sets area sound volume.
     *
     * @param areaSoundVolume the area sound volume
     */
    public void setAreaSoundVolume(int areaSoundVolume) {
        this.areaSoundVolume = areaSoundVolume;
        setVarp(player, 872, areaSoundVolume);
    }

    /**
     * Is single mouse button boolean.
     *
     * @return the boolean
     */
    public boolean isSingleMouseButton() {
        return singleMouseButton;
    }

    /**
     * Is disable chat effects boolean.
     *
     * @return the boolean
     */
    public boolean isDisableChatEffects() {
        return disableChatEffects;
    }

    /**
     * Is split private chat boolean.
     *
     * @return the boolean
     */
    public boolean isSplitPrivateChat() {
        return splitPrivateChat;
    }

    /**
     * Is accept aid boolean.
     *
     * @return the boolean
     */
    public boolean isAcceptAid() {
        return acceptAid;
    }

    /**
     * Is run toggled boolean.
     *
     * @return the boolean
     */
    public boolean isRunToggled() {
        return runToggled;
    }

    /**
     * Gets public chat setting.
     *
     * @return the public chat setting
     */
    public int getPublicChatSetting() {
        return publicChatSetting;
    }

    /**
     * Sets public chat setting.
     *
     * @param publicChatSetting the public chat setting
     */
    public void setPublicChatSetting(int publicChatSetting) {
        this.publicChatSetting = publicChatSetting;
        updateChatSettings();
    }

    /**
     * Gets private chat setting.
     *
     * @return the private chat setting
     */
    public int getPrivateChatSetting() {
        return privateChatSetting;
    }

    /**
     * Sets private chat setting.
     *
     * @param privateChatSetting the private chat setting
     */
    public void setPrivateChatSetting(int privateChatSetting) {
        this.privateChatSetting = privateChatSetting;
        updateChatSettings();
    }

    /**
     * Gets clan chat setting.
     *
     * @return the clan chat setting
     */
    public int getClanChatSetting() {
        return clanChatSetting;
    }

    /**
     * Sets clan chat setting.
     *
     * @param clanChatSetting the clan chat setting
     */
    public void setClanChatSetting(int clanChatSetting) {
        this.clanChatSetting = clanChatSetting;
        setVarp(player, 1054, clanChatSetting);
    }

    /**
     * Gets trade setting.
     *
     * @return the trade setting
     */
    public int getTradeSetting() {
        return tradeSetting;
    }

    /**
     * Sets trade setting.
     *
     * @param tradeSetting the trade setting
     */
    public void setTradeSetting(int tradeSetting) {
        this.tradeSetting = tradeSetting;
        updateChatSettings();
    }

    /**
     * Gets assist setting.
     *
     * @return the assist setting
     */
    public int getAssistSetting() {
        return assistSetting;
    }

    /**
     * Sets assist setting.
     *
     * @param assistSetting the assist setting
     */
    public void setAssistSetting(int assistSetting) {
        this.assistSetting = assistSetting;
        setVarp(player, 1055, assistSetting);
    }

    /**
     * Gets run energy.
     *
     * @return the run energy
     */
    public double getRunEnergy() {
        return runEnergy;
    }

    /**
     * Sets run energy.
     *
     * @param runEnergy the run energy
     */
    public void setRunEnergy(double runEnergy) {
        this.runEnergy = runEnergy;
    }

    /**
     * Gets attack style index.
     *
     * @return the attack style index
     */
    public int getAttackStyleIndex() {
        return attackStyleIndex;
    }

    /**
     * Sets attack style index.
     *
     * @param attackStyleIndex the attack style index
     */
    public void setAttackStyleIndex(int attackStyleIndex) {
        this.attackStyleIndex = attackStyleIndex;
    }

}
