package core.game.node.entity.player.link.diary;

import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.diary.DiaryLevel;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rs.consts.Components;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Diary.
 */
public class Diary {

    /**
     * The diary component.
     */
    public static final int DIARY_COMPONENT = Components.QUESTJOURNAL_SCROLL_275;

    /**
     * The constant completedLevels.
     */
    public static final ArrayList<Integer> completedLevels = new ArrayList<Integer>();

    private static final String RED = "<col=8A0808>";

    private static final String BLUE = "<col=08088A>";

    private static final String YELLOW = "<col=F7FE2E>";

    private static final String GREEN = "<col=3ADF00>";

    private final DiaryType type;

    private final boolean[] levelStarted = new boolean[3];

    private final boolean[] levelRewarded = new boolean[3];

    private final boolean[][] taskCompleted;

    /**
     * Instantiates a new Diary.
     *
     * @param type the type
     */
    public Diary(DiaryType type) {
        this.type = type;
        this.taskCompleted = new boolean[type.getAchievements().length][25];
    }

    /**
     * Open.
     *
     * @param player the player
     */
    public void open(Player player) {
        clear(player);
        sendString(player, "<red>Achievement Diary - " + type.getName(), 2);
        int child = 12;

        sendString(player, (isComplete() ? GREEN : isStarted() ? YELLOW : "<red>") + type.getName() + " Area Tasks", child++);
        //child++;

        if (!type.getInfo().isEmpty() && !this.isStarted()) {
            sendString(player, type.getInfo(), child++);
            child += type.getInfo().split("<br><br>").length;
        }
        child++;

        boolean complete;
        String line;
        for (int level = 0; level < type.getAchievements().length; level++) {
            sendString(player, getStatus(level) + getLevel(level) + "", child++);
            child++;
            for (int i = 0; i < type.getAchievements(level).length; i++) {
                complete = isComplete(level, i);
                line = type.getAchievements(level)[i];
                if (line.contains("<br><br>")) {
                    String[] lines = line.split("<br><br>");
                    for (String l : lines) {
                        sendString(player, complete ? "<str><str>" + l : l, child++);
                    }
                } else {
                    sendString(player, complete ? "<str><str>" + line : line, child++);
                }
                sendString(player, "*", child++);
            }
            child++;
        }
        //sendString(player, builder.toString(), 11);
        // Changes the size of the scroll bar
        // player.getPacketDispatch().sendRunScript(1207, "i", new Object[] { 330 });
        // sendString(player, builder.toString(), 11);

        if (!player.getInterfaceManager().isOpened()) {
            player.getInterfaceManager().open(new Component(DIARY_COMPONENT));
        }
        player.getPacketDispatch().sendRunScript(1207, "ii", 1, child - 10);
    }

    private void clear(Player player) {
        for (int i = 0; i < 311; i++) {
            player.getPacketDispatch().sendString("", DIARY_COMPONENT, i);
        }
    }

    /**
     * Parse.
     *
     * @param data the data
     */
    public void parse(JSONObject data) {
        JSONArray startedArray = (JSONArray) data.get("startedLevels");
        for (int i = 0; i < startedArray.size(); i++) {
            levelStarted[i] = (boolean) startedArray.get(i);
        }
        JSONArray completedArray = (JSONArray) data.get("completedLevels");
        for (int i = 0; i < completedArray.size(); i++) {
            JSONArray level = (JSONArray) completedArray.get(i);
            boolean completed = true;
            for (int j = 0; j < level.size(); j++) {
                taskCompleted[i][j] = (boolean) level.get(j);
                if (!taskCompleted[i][j]) {
                    completed = !completed;
                }
                completedLevels.add(i);
            }
        }
        JSONArray rewardedArray = (JSONArray) data.get("rewardedLevels");
        for (int i = 0; i < rewardedArray.size(); i++) {
            levelRewarded[i] = (boolean) rewardedArray.get(i);
        }
    }

    /**
     * Draw status.
     *
     * @param player the player
     */
    public void drawStatus(Player player) {
        if (isStarted()) {
            player.getPacketDispatch().sendString((isComplete() ? GREEN : YELLOW) + type.getName(), Components.AREA_TASK_259, type.getChild());
            for (int i = 0; i < 3; i++) {
                player.getPacketDispatch().sendString((isComplete(i) ? GREEN : isStarted(i) ? YELLOW : "<col=FF0000>") + getLevel(i), 259, type.getChild() + (i + 1));
            }
        }
    }

    /**
     * Update task.
     *
     * @param player   the player
     * @param level    the level
     * @param index    the index
     * @param complete the complete
     */
    public void updateTask(Player player, int level, int index, boolean complete) {
        if (!levelStarted[level]) {
            levelStarted[level] = true;
        }
        if (!complete) {
            player.sendMessage("Well done! A " + type.getName() + " task has been updated.");
        } else {
            taskCompleted[level][index] = true;
            int tempLevel = this.type == DiaryType.LUMBRIDGE ? level - 1 : level;
            player.sendMessages("Well done! You have completed "
                    + (tempLevel == -1 ? "a beginner" : tempLevel == 0 ? "an easy" : tempLevel == 1 ? "a medium" : "a hard")
                    + " task in the " + type.getName() + " area. Your", "Achievement Diary has been updated.");
        }
        if (isComplete(level)) {
            player.sendMessages("Congratulations! You have completed all of the " + getLevel(level).toLowerCase()
                    + " tasks in the " + type.getName() + " area.", "Speak to "
                    + NPCDefinition.forId(type.getNpc(level)).getName() + " to claim your reward.");
        }
        drawStatus(player);
    }

    /**
     * Finish task.
     *
     * @param player the player
     * @param level  the level
     * @param index  the index
     */
    public void finishTask(Player player, int level, int index) {
        if (!this.isComplete(level, index)) {
            this.updateTask(player, level, index, true);
            boolean complete = true;
            for (int i = 0; i < taskCompleted[level].length; i++) {
                if (!taskCompleted[level][i]) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                completedLevels.add(level);
            } else if (completedLevels.contains(level)) completedLevels.remove(level);
        }
    }

    /**
     * Reset task.
     *
     * @param player the player
     * @param level  the level
     * @param index  the index
     */
    public void resetTask(Player player, int level, int index) {
        taskCompleted[level][index] = false;
        if (!isStarted(level)) {
            this.levelStarted[level] = false;
        }
        if (!isComplete(level)) {
            this.levelRewarded[level] = false;
        }
        drawStatus(player);
    }

    /**
     * Check complete boolean.
     *
     * @param level the level
     * @return the boolean
     */
    public boolean checkComplete(DiaryLevel level) {
        if (type != DiaryType.LUMBRIDGE && level == DiaryLevel.BEGINNER) {
            return false;
        }

        if (level == DiaryLevel.BEGINNER) {
            return completedLevels.contains(level.ordinal());
        }

        return completedLevels.contains(level.ordinal() - 1);
    }

    private void sendString(Player player, String string, int child) {
        player.getPacketDispatch().sendString(string.replace("<blue>", BLUE).replace("<red>", RED), DIARY_COMPONENT, child);
    }

    /**
     * Sets level started.
     *
     * @param level the level
     */
    public void setLevelStarted(int level) {
        this.levelStarted[level] = true;
    }

    /**
     * Sets completed.
     *
     * @param level the level
     * @param index the index
     */
    public void setCompleted(int level, int index) {
        this.taskCompleted[level][index] = true;
    }

    /**
     * Is started boolean.
     *
     * @param level the level
     * @return the boolean
     */
    public boolean isStarted(int level) {
        return this.levelStarted[level];
    }

    /**
     * Is started boolean.
     *
     * @return the boolean
     */
    public boolean isStarted() {
        for (int j = 0; j < type.getLevelNames().length; j++) {
            if (isStarted(j)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is complete boolean.
     *
     * @param level the level
     * @param index the index
     * @return the boolean
     */
    public boolean isComplete(int level, int index) {
        return taskCompleted[level][index];
    }

    /**
     * Is complete boolean.
     *
     * @param level the level
     * @return the boolean
     */
    public boolean isComplete(int level) {
        for (int i = 0; i < type.getAchievements(level).length; i++) {
            if (!taskCompleted[level][i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is complete boolean.
     *
     * @param level      the level
     * @param cumulative the cumulative
     * @return the boolean
     */
    public boolean isComplete(int level, boolean cumulative) {
        if (isComplete(level)) {
            return !cumulative || level <= 0 || isComplete(level - 1, true);
        } else {
            return false;
        }
    }

    /**
     * Is complete boolean.
     *
     * @return the boolean
     */
    public boolean isComplete() {
        for (int i = 0; i < taskCompleted.length; i++) {
            for (int x = 0; x < type.getAchievements(i).length; x++) {
                if (!taskCompleted[i][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return isComplete(2) ? 2 : isComplete(1) ? 1 : isComplete(0) ? 0 : -1;
    }

    /**
     * Gets reward.
     *
     * @return the reward
     */
    public int getReward() {
        return isLevelRewarded(2) ? 2 : isLevelRewarded(1) ? 1 : isLevelRewarded(0) ? 0 : -1;
    }

    /**
     * Gets level.
     *
     * @param level the level
     * @return the level
     */
    public String getLevel(int level) {
        return type.getLevelNames()[level];
    }

    /**
     * Gets status.
     *
     * @param level the level
     * @return the status
     */
    public String getStatus(int level) {
        return !isStarted(level) ? RED : isComplete(level) ? GREEN : YELLOW;
    }

    /**
     * Sets level rewarded.
     *
     * @param level the level
     */
    public void setLevelRewarded(int level) {
        this.levelRewarded[level] = true;
    }

    /**
     * Is level rewarded boolean.
     *
     * @param level the level
     * @return the boolean
     */
    public boolean isLevelRewarded(int level) {
        return levelRewarded[level];
    }

    /**
     * Get task completed boolean [ ] [ ].
     *
     * @return the boolean [ ] [ ]
     */
    public boolean[][] getTaskCompleted() {
        return taskCompleted;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public DiaryType getType() {
        return type;
    }

    /**
     * Get level started boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getLevelStarted() {
        return levelStarted;
    }

    /**
     * Get level rewarded boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getLevelRewarded() {
        return levelRewarded;
    }

    /**
     * Remove rewards for boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean removeRewardsFor(Player player, DiaryType type, int level) {
        Item[] rewards = type.getRewards(level);
        //lamps are always the 2nd reward for a level, don't remove lamps
        boolean hasRemoved =
                player.getInventory().remove(rewards[0])
                        || player.getBank().remove(rewards[0])
                        || player.getEquipment().remove(rewards[0]);

        if (hasRemoved) {
            player.debug("Removed previous reward");
        }

        return hasRemoved;
    }

    /**
     * Add rewards for boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean addRewardsFor(Player player, DiaryType type, int level) {
        Item[] rewards = type.getRewards(level);

        int freeSlots = player.getInventory().freeSlots();
        if (freeSlots < rewards.length)
            return false;

        boolean allRewarded = true;
        for (Item reward : rewards) {
            allRewarded &= player.getInventory().add(reward);
        }

        if (!allRewarded) {
            Arrays.stream(rewards).forEach((item) -> {
                boolean _ignored = player.getInventory().remove(item);
            });
        }

        return allRewarded;
    }

    /**
     * Flag rewarded boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean flagRewarded(Player player, DiaryType type, int level) {
        if (level > 0) {
            removeRewardsFor(player, type, level - 1);
        }
        if (addRewardsFor(player, type, level))
            player.getAchievementDiaryManager().getDiary(type).setLevelRewarded(level);
        else {
            player.sendMessage("You do not have enough space in your inventory to claim these rewards.");
            return false;
        }

        return true;
    }

    /**
     * Can replace reward boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean canReplaceReward(Player player, DiaryType type, int level) {
        Item reward = type.getRewards(level)[0];
        boolean claimed = hasCompletedLevel(player, type, level)
                && hasClaimedLevelRewards(player, type, level)
                && !player.hasItem(reward);
        return level == 2 ? claimed : claimed && !hasClaimedLevelRewards(player, type, level + 1);
    }

    /**
     * Grant replacement boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean grantReplacement(Player player, DiaryType type, int level) {
        Item reward = type.getRewards(level)[0]; //Can only replace non-lamp reward
        return canReplaceReward(player, type, level) && player.getInventory().add(reward);
    }

    /**
     * Has completed level boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean hasCompletedLevel(Player player, DiaryType type, int level) {
        if (level > type.getLevelNames().length - 1)
            return false;
        return player.getAchievementDiaryManager().getDiary(type).isComplete(level, true);
    }

    /**
     * Has claimed level rewards boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean hasClaimedLevelRewards(Player player, DiaryType type, int level) {
        return player.getAchievementDiaryManager().getDiary(type).isLevelRewarded(level);
    }

    /**
     * Can claim level rewards boolean.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @return the boolean
     */
    public static boolean canClaimLevelRewards(Player player, DiaryType type, int level) {
        if (level == 2)
            // Cannot be a higher level to claim
            return hasCompletedLevel(player, type, level) && !hasClaimedLevelRewards(player, type, level);
        else
            return !hasClaimedLevelRewards(player, type, level + 1) && hasCompletedLevel(player, type, level) && !hasClaimedLevelRewards(player, type, level);
    }

    /**
     * Get rewards item [ ].
     *
     * @param type  the type
     * @param level the level
     * @return the item [ ]
     */
    public static Item[] getRewards(DiaryType type, int level) {
        return type.getRewards(level);
    }
}
