package core.game.node.entity.player.link.quest;

import core.game.node.entity.player.Player;
import core.tools.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static core.api.ContentAPIKt.*;

/**
 * The type Quest repository.
 */
public final class QuestRepository {

    private static final Map<String, Quest> QUESTS = new TreeMap<>();

    private final Map<Integer, Integer> quests = new HashMap<>();

    private final Player player;

    private int points;

    /**
     * Instantiates a new Quest repository.
     *
     * @param player the player
     */
    public QuestRepository(final Player player) {
        this.player = player;
        for (Quest quest : QUESTS.values()) {
            quests.put(quest.getIndex(), 0);
        }
    }

    /**
     * Parse.
     *
     * @param questData the quest data
     */
    public void parse(JSONObject questData) {
        points = Integer.parseInt(questData.get("points").toString());
        JSONArray questArray = (JSONArray) questData.get("questStages");
        questArray.forEach(quest -> {
            JSONObject q = (JSONObject) quest;
            quests.put(Integer.parseInt(q.get("questId").toString()), Integer.parseInt(q.get("questStage").toString()));
        });
        syncPoints();
    }

    /**
     * Syncronize tab.
     *
     * @param player the player
     */
    public void syncronizeTab(Player player) {
        setVarp(player, 101, points);
        int[] config = null;
        for (Quest quest : QUESTS.values()) {
            config = quest.getConfig(player, getStage(quest));

            // {questVarpId, questVarbitId, valueToSet}
            if (config.length == 3) {
                // This is to set quests with VARPBIT, ignoring VARP value
                setVarbit(player, config[1], config[2]);
            } else {
                // This is the original VARP quests
                // {questVarpId, valueToSet}
                setVarp(player, config[0], config[1]);
            }

            quest.updateVarps(player);
        }
    }

    /**
     * Sets stage.
     *
     * @param quest the quest
     * @param stage the stage
     */
    public void setStage(Quest quest, int stage) {
        int oldStage = getStage(quest);
        if (oldStage < stage) {
            quests.put(quest.getIndex(), stage);
        } else {
            log(this.getClass(), Log.WARN, String.format("Nonmonotonic QuestRepository.setStage call for player \"%s\", quest \"%s\", old stage %d, new stage %d", player.getName(), quest.getName(), oldStage, stage));
        }
    }

    /**
     * Sets stage nonmonotonic.
     *
     * @param quest the quest
     * @param stage the stage
     */
    public void setStageNonmonotonic(Quest quest, int stage) {
        quests.put(quest.getIndex(), stage);
    }

    /**
     * Increment points.
     *
     * @param value the value
     */
    public void incrementPoints(int value) {
        points += value;
    }

    /**
     * Dock points.
     *
     * @param value the value
     */
    public void dockPoints(int value) {
        points -= value;
    }

    /**
     * Sync points.
     */
    public void syncPoints() {
        int points = 0;
        for (Quest quest : QUESTS.values()) {
            if (getStage(quest) >= 100) {
                points += quest.getQuestPoints();
            }
        }
        this.points = points;
    }

    /**
     * Gets available points.
     *
     * @return the available points
     */
    public int getAvailablePoints() {
        int points = 0;
        for (Quest quest : QUESTS.values()) {
            points += quest.getQuestPoints();
        }
        return points;
    }

    /**
     * For button id quest.
     *
     * @param buttonId the button id
     * @return the quest
     */
    public Quest forButtonId(int buttonId) {
        for (Quest q : QUESTS.values()) {
            if (q.getButtonId() == buttonId) {
                return q;
            }
        }
        return null;
    }

    /**
     * For index quest.
     *
     * @param index the index
     * @return the quest
     */
    public Quest forIndex(int index) {
        for (Quest q : QUESTS.values()) {
            if (q.getIndex() == index) {
                return q;
            }
        }
        return null;
    }

    /**
     * Has completed all boolean.
     *
     * @return the boolean
     */
    public boolean hasCompletedAll() {
        return getPoints() >= getAvailablePoints();
    }

    /**
     * Is complete boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isComplete(String name) {
        Quest quest = getQuest(name);
        if (quest == null) {
            log(this.getClass(), Log.ERR, "Error can't check if quest is complete for " + name);
            return false;
        }
        return quest.getStage(player) >= 100;
    }

    /**
     * Has started boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean hasStarted(String name) {
        Quest quest = getQuest(name);
        if (quest == null) {
            log(this.getClass(), Log.ERR, "Error can't check if quest is complete for " + name);
            return false;
        }
        return quest.getStage(player) > 0;
    }

    /**
     * Gets stage.
     *
     * @param name the name
     * @return the stage
     */
    public int getStage(String name) {
        var quest = QUESTS.get(name);
        if (quest == null) {
            return 0;
        }
        return getStage(quest);
    }

    /**
     * Gets stage.
     *
     * @param quest the quest
     * @return the stage
     */
    public int getStage(Quest quest) {
        return quests.get(quest.getIndex());
    }

    /**
     * Gets quest.
     *
     * @param name the name
     * @return the quest
     */
    public Quest getQuest(String name) {
        return QUESTS.get(name);
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    public int getPoints() {
        return points;
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
     * Register.
     *
     * @param quest the quest
     */
    public static void register(Quest quest) {
        QUESTS.put(quest.getName(), quest);
    }

    /**
     * Gets quests.
     *
     * @return the quests
     */
    public static Map<String, Quest> getQuests() {
        return QUESTS;
    }

    /**
     * Gets quest list.
     *
     * @return the quest list
     */
    public Map<Integer, Integer> getQuestList() {
        return quests;
    }

}
