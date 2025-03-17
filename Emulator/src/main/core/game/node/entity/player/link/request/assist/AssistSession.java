package core.game.node.entity.player.link.request.assist;

import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestModule;
import core.game.node.entity.skill.Skills;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import org.rs.consts.Components;

import java.util.Calendar;
import java.util.Date;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Assist session.
 */
public final class AssistSession extends Pulse implements RequestModule {

    private Player player;

    private Player partner;

    private final Component component = new Component(Components.REQ_ASSIST_301).setCloseEvent(getCloseEvent());

    private static final Animation ANIMATION = Animation.create(7299);

    private static final Graphics GRAPHICS = Graphics.create(org.rs.consts.Graphics.ASSIST_LEVELS_1247);

    private static final long TIME_OUT = 86400000;// 60000; //86400000;

    private static final byte[] CONFIG_VALUES = {3, 4, 6, 8, 9, 11, 13, 14, 15};

    private static final int[] CHILD_VALUES = {46, 48, 50, 52, 54, 56, 58, 60, 62};

    private boolean[] skills = new boolean[9];

    private boolean restricted = false;

    private double exp[] = new double[9];

    private boolean kill = false;

    private long time;

    /**
     * Instantiates a new Assist session.
     *
     * @param player  the player
     * @param partner the partner
     */
    public AssistSession(final Player player, final Player partner) {
        this.player = player;
        this.partner = partner;
    }

    /**
     * Instantiates a new Assist session.
     */
    public AssistSession() {

    }

    /**
     * Extend.
     *
     * @param player  the player
     * @param partner the partner
     */
    public static final void extend(final Player player, final Player partner) {
        player.addExtension(AssistSession.class, new AssistSession(player, partner));
    }

    /**
     * Gets extension.
     *
     * @param player the player
     * @return the extension
     */
    public final static AssistSession getExtension(final Player player) {
        return player.getExtension(AssistSession.class);
    }

    @Override
    public void open(Player player, Player target) {
        if (player.getIronmanManager().checkRestriction() || target.getIronmanManager().checkRestriction()) {
            return;
        }
        player.face(target);
        target.face(player);
        AssistSession.extend(player, target);
        AssistSession.getExtension(player).open();
    }

    /**
     * Open.
     */
    public final void open() {
        partner.addExtension(AssistSession.class, this);
        player.lock();
        player.getInterfaceManager().open(component);
        load();
        player.getPacketDispatch().sendMessage("Sending assistance response.");
        player.getPacketDispatch().sendMessage("You are assisting " + partner.getUsername() + ".");
        partner.getPacketDispatch().sendMessage("You are being assisted by " + player.getUsername() + ".");
        player.getPacketDispatch().sendString("Assist System XP Display - You are assisting " + partner.getUsername() + "", 301, 101);
        player.getPacketDispatch().sendString("", 301, 10);
        player.animate(ANIMATION);
        player.graphics(GRAPHICS);
        partner.animate(ANIMATION);
        playAudio(partner, 4010);
        playAudio(player, 4010);
        toggleIcon(player, false);
        toggleIcon(partner, false);
        GameWorld.getPulser().submit(this);
        refresh();
    }

    /**
     * Gets close event.
     *
     * @return the close event
     */
    public final CloseEvent getCloseEvent() {
        return new CloseEvent() {
            @Override
            public boolean close(Player player, Component c) {
                save();
                player.removeExtension(AssistSession.class);
                partner.removeExtension(AssistSession.class);
                player.unlock();
                toggleIcon(player, true);
                toggleIcon(partner, true);
                player.getInterfaceManager().restoreTabs();
                player.getPacketDispatch().sendMessage("You have stopped assisting " + partner.getUsername() + ".");
                partner.getPacketDispatch().sendMessage(getPlayer().getUsername() + " has stopped assisting you.");
                kill = true;
                return true;
            }
        };
    }

    /**
     * Toggle icon.
     *
     * @param player the player
     * @param hide   the hide
     */
    public final void toggleIcon(final Player player, final boolean hide) {
        for (int i = 79; i < 81; i++) {
            player.getPacketDispatch().sendInterfaceConfig(548, i, hide);
        }
    }

    /**
     * Add experience.
     *
     * @param skill      the skill
     * @param experience the experience
     */
    public final void addExperience(final int skill, final double experience) {
        if (restricted || getTotalExperience() >= 30000) {
            restricted = true;
            return;
        }
        final int skillIndex = getSkillIndex(skill);
        if (exp[skillIndex] + experience >= 30000) {
            exp[skillIndex] = 30000;
            restricted = true;
            return;
        }
        exp[skillIndex] += experience;
        if (exp[skillIndex] >= 30000) {
            exp[skillIndex] = 30000;
        }
        refresh();
    }

    /**
     * Refresh.
     */
    public final void refresh() {
        int value = 0;
        double totalXp = 0;
        for (byte i = 0; i < 9; i++) {
            if (exp[i] >= 30000) {
                exp[i] = 30000;
            }
            player.getPacketDispatch().sendString("" + (int) exp[i], 301, CHILD_VALUES[i]);
            if (skills[i]) {
                value |= 1 << CONFIG_VALUES[i];
            }
            totalXp += exp[i];
        }
        if (getTotalExperience() >= 30000) {
            restricted = true;
        } else {
            restricted = false;
        }
        String message = "";
        if (isRestricted()) {
            message = "You've earned the maximum XP from the Assist System within a 24-hour period. You can assist again in " + getTimeLeft() + ".";
        }
        player.getPacketDispatch().sendString(message, 301, 10);
        setVarp(player, 1087, value);
        setVarp(player, 1088, (int) totalXp * 10);
    }

    /**
     * Toggle button.
     *
     * @param id the id
     */
    public final void toggleButton(final byte id) {
        skills[id] = skills[id] ? false : true;
    }

    /**
     * Is toggled boolean.
     *
     * @param skill the skill
     * @return the boolean
     */
    public final boolean isToggled(final int skill) {
        return skills[getSkillIndex(skill)];
    }

    /**
     * Gets skill index.
     *
     * @param skill the skill
     * @return the skill index
     */
    public final int getSkillIndex(int skill) {
        switch (skill) {
            case Skills.RUNECRAFTING:
                return 0;
            case Skills.CRAFTING:
                return 1;
            case Skills.FLETCHING:
                return 2;
            case Skills.CONSTRUCTION:
                return 3;
            case Skills.FARMING:
                return 4;
            case Skills.MAGIC:
                return 5;
            case Skills.SMITHING:
                return 6;
            case Skills.COOKING:
                return 7;
            case Skills.HERBLORE:
                return 8;
        }
        return -1;
    }

    /**
     * Get skills boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getSkills() {
        return skills;
    }

    /**
     * Gets time left.
     *
     * @return the time left
     */
    public String getTimeLeft() {
        Date date2 = new Date(time);
        Date date1 = new Date(System.currentTimeMillis());
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        if (diffHours > 1) {
            return "" + diffHours + " hours";
        }
        if (diffMinutes > 1) {
            return "" + diffMinutes + " minutes";
        }
        return "" + diffSeconds + " seconds";
    }

    /**
     * Gets total experience.
     *
     * @return the total experience
     */
    public double getTotalExperience() {
        double xp = 0;
        for (int i = 0; i < 9; i++) {
            xp += exp[i];
        }
        return xp;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Load.
     */
    public final void load() {
        time = player.getSavedData().globalData.getAssistTime();
        if (time == 0) {
            player.getSavedData().globalData.setAssistTime(System.currentTimeMillis() + TIME_OUT);
        }
        for (int i = 0; i < 9; i++) {
            exp[i] = player.getSavedData().globalData.getAssistExperience()[i];
        }
    }

    /**
     * Save.
     */
    public final void save() {
        player.getSkills().refresh();
        player.getSavedData().globalData.setAssistTime(time);
        player.getSavedData().globalData.setAssistExperience(exp);
    }

    /**
     * Reset.
     */
    public final void reset() {
        player.getSavedData().globalData.setAssistTime(0L);
        player.getSavedData().globalData.setAssistExperience(new double[9]);
        load();
    }

    @Override
    public boolean pulse() {
        if (!player.getLocation().withinDistance(partner.getLocation(), 20) || !partner.isActive() || !player.isActive()) {
            player.getInterfaceManager().close();
            return true;
        }
        if (time < System.currentTimeMillis()) {
            time = System.currentTimeMillis() + TIME_OUT;
            for (int i = 0; i < 9; i++) {
                exp[i] = 0;
            }
        }
        refresh();
        return kill;
    }

    /**
     * Translate experience boolean.
     *
     * @param p          the p
     * @param slot       the slot
     * @param experience the experience
     * @param mod        the mod
     * @return the boolean
     */
    public boolean translateExperience(Player p, int slot, double experience, double mod) {
        if (getPlayer() != p) {
            int index = getSkillIndex(slot);
            if (index != -1) {
                if (!isRestricted()) {
                    int level = p.getSkills().getLevel(slot);
                    int pLevel = player.getSkills().getLevel(slot);
                    if (pLevel < level) {
                        return false;
                    }
                    if (getSkills()[getSkillIndex(slot)]) {
                        if (getExp()[index] + experience >= 30000) {
                            experience = experience - (getExp()[index] + experience - 30000);
                        }
                        getPlayer().getSkills().addExperience(slot, experience);
                        addExperience(slot, experience * mod);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Is restricted boolean.
     *
     * @return the boolean
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * Get exp double [ ].
     *
     * @return the double [ ]
     */
    public double[] getExp() {
        return exp;
    }

    /**
     * Sets exp.
     *
     * @param exp the exp
     */
    public void setExp(double[] exp) {
        this.exp = exp;
    }

    /**
     * Gets partner.
     *
     * @return the partner
     */
    public Player getPartner() {
        return partner;
    }

    /**
     * Sets partner.
     *
     * @param partner the partner
     */
    public void setPartner(Player partner) {
        this.partner = partner;
    }

}
