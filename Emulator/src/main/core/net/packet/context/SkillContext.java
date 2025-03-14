package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Skill context.
 */
public final class SkillContext implements Context {

    private final Player player;

    private final int skillId;

    /**
     * Instantiates a new Skill context.
     *
     * @param player  the player
     * @param skillId the skill id
     */
    public SkillContext(Player player, int skillId) {
        this.player = player;
        this.skillId = skillId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets skill id.
     *
     * @return the skill id
     */
    public int getSkillId() {
        return skillId;
    }

}