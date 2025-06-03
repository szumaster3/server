package content.region.karamja.quest.junglepotion.handlers;

import content.global.skill.herblore.HerbItem;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import core.tools.StringUtils;

import static core.api.ContentAPIKt.sendMessage;

/**
 * The enum Jungle objective.
 */
public enum JungleObjective {
    /**
     * The Jungle vine.
     */
    JUNGLE_VINE(2575, HerbItem.SNAKE_WEED, 10, "It grows near vines in an area to the south west where", "the ground turns soft and the water kisses your feet.") {
        @Override
        public void search(final Player player, final Scenery scenery) {
            final Animation animation = Animation.create(2094);
            player.animate(animation);
            player.getPulseManager().run(new Pulse(animation.getDefinition().getDurationTicks(), player, scenery) {
                @Override
                public boolean pulse() {
                    boolean success = RandomFunction.random(3) == 1;
                    if (success) {
                        sendMessage(player, "You search the vine...");
                        switchObject(scenery);
                        findHerb(player);
                        return true;
                    }
                    player.animate(animation);
                    return false;
                }
            });
        }
    },
    /**
     * The Palm tree.
     */
    PALM_TREE(2577, HerbItem.ARDRIGAL, 20, "You are looking for Ardrigal. It is related to the palm", "and grows in its brothers shady profusion."),
    /**
     * The Sito foil.
     */
    SITO_FOIL(2579, HerbItem.SITO_FOIL, 30, "You are looking for Sito Foil, and it grows best where", "the ground has been blackened by the living flame."),
    /**
     * The Volencia moss.
     */
    VOLENCIA_MOSS(2581, HerbItem.VOLENCIA_MOSS, 40, "You are looking for Volencia Moss. It clings to rocks", "for its existence. It is difficult to see, so you must", "search for it well."),
    /**
     * The Rogues purse.
     */
    ROGUES_PURSE(32106, HerbItem.ROGUES_PUSE, 50, "It inhabits the darkness of the underground, and grows", "in the caverns to the north. A secret entrance to the", "caverns is set into the northern cliffs, be careful Bwana.") {
        @Override
        public void search(final Player player, final Scenery scenery) {
            final Animation animation = Animation.create(2097);
            player.animate(animation);
            player.getPulseManager().run(new Pulse(animation.getDefinition().getDurationTicks(), player, scenery) {
                @Override
                public boolean pulse() {
                    boolean success = RandomFunction.random(4) == 1;
                    if (success) {
                        switchObject(scenery);
                        findHerb(player);
                        return true;
                    }
                    player.animate(animation, 1);
                    return false;
                }
            });
        }
    };

    private final int objectId;

    private final HerbItem herb;

    private final int stage;

    private final String[] clue;

    JungleObjective(int objectId, HerbItem herb, int stage, final String... clue) {
        this.objectId = objectId;
        this.herb = herb;
        this.stage = stage;
        this.clue = clue;
    }

    /**
     * Search.
     *
     * @param player  the player
     * @param scenery the scenery
     */
    public void search(final Player player, final Scenery scenery) {
        findHerb(player);
        switchObject(scenery);
    }

    /**
     * Switch object.
     *
     * @param scenery the scenery
     */
    public void switchObject(Scenery scenery) {
        if (scenery.isActive()) {
            SceneryBuilder.replace(scenery, scenery.transform(scenery.getId() + 1), 80);
        }
    }

    /**
     * Find herb.
     *
     * @param player the player
     */
    public void findHerb(final Player player) {
        player.getInventory().add(getHerb().herb);
        player.getDialogueInterpreter().sendItemMessage(getHerb().herb, "You find a grimy herb.");
    }

    /**
     * For stage jungle objective.
     *
     * @param stage the stage
     * @return the jungle objective
     */
    public static JungleObjective forStage(int stage) {
        for (JungleObjective o : values()) {
            if (o.getStage() == stage) {
                return o;
            }
        }
        return null;
    }

    /**
     * For id jungle objective.
     *
     * @param objectId the object id
     * @return the jungle objective
     */
    public static JungleObjective forId(int objectId) {
        for (JungleObjective s : values()) {
            if (s.getObjectId() == objectId) {
                return s;
            }
        }
        return null;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return StringUtils.formatDisplayName(herb.product.getName().replace("Clean", "").trim());
    }

    /**
     * Gets object id.
     *
     * @return the object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets herb.
     *
     * @return the herb
     */
    public HerbItem getHerb() {
        return herb;
    }

    /**
     * Gets stage.
     *
     * @return the stage
     */
    public int getStage() {
        return stage;
    }

    /**
     * Get clue string [ ].
     *
     * @return the string [ ]
     */
    public String[] getClue() {
        return clue;
    }
}