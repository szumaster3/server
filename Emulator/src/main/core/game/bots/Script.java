package core.game.bots;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Script.
 */
public abstract class Script {

    /**
     * The Script api.
     */
    public ScriptAPI scriptAPI;
    /**
     * The Inventory.
     */
    public ArrayList<Item> inventory = new ArrayList<>(20);
    /**
     * The Equipment.
     */
    public ArrayList<Item> equipment = new ArrayList<>(20);
    /**
     * The Skills.
     */
    public Map<Integer, Integer> skills = new HashMap<>();
    /**
     * The Quests.
     */
    public ArrayList<String> quests = new ArrayList<>(20);


    /**
     * The Bot.
     */
    public Player bot;

    /**
     * The Running.
     */
    public boolean running = true;
    /**
     * The End dialogue.
     */
    public boolean endDialogue = true;

    /**
     * Init.
     *
     * @param isPlayer the is player
     */
    public void init(boolean isPlayer) {
        scriptAPI = new ScriptAPI(bot);

        if (!isPlayer) {
            for (Map.Entry<Integer, Integer> skill : skills.entrySet()) {
                setLevel(skill.getKey(), skill.getValue());
            }
            for (String quest : quests) {
                bot.getQuestRepository().setStage(bot.getQuestRepository().getQuest(quest), 100);
            }
            for (Item i : equipment) {
                bot.getEquipment().add(i, true, false);
            }
            bot.getInventory().clear();
            for (Item i : inventory) {
                bot.getInventory().add(i);
            }
        }
    }

    @Override
    public String toString() {
        return bot.getName() + " is a " + this.getClass().getSimpleName() + " at location " + bot.getLocation().toString() + " Current pulse: " + bot.getPulseManager().getCurrent();
    }

    /**
     * Tick.
     */
    public abstract void tick();

    /**
     * Sets level.
     *
     * @param skill the skill
     * @param level the level
     */
    public void setLevel(int skill, int level) {
        bot.getSkills().setLevel(skill, level);
        bot.getSkills().setStaticLevel(skill, level);
        bot.getSkills().updateCombatLevel();
        bot.getAppearance().sync();
    }

    /**
     * New instance script.
     *
     * @return the script
     */
    public abstract Script newInstance();
}
