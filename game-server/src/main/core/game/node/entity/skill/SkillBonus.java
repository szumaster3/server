package core.game.node.entity.skill;

/**
 * The type Skill bonus.
 */
public final class SkillBonus {

    private final int skillId;

    private final double bonus;

    private final int baseBonus;

    /**
     * Instantiates a new Skill bonus.
     *
     * @param skillId the skill id
     * @param bonus   the bonus
     */
    public SkillBonus(int skillId, double bonus) {
        this(skillId, bonus, 0);
    }

    /**
     * Instantiates a new Skill bonus.
     *
     * @param skillId   the skill id
     * @param bonus     the bonus
     * @param baseBonus the base bonus
     */
    public SkillBonus(int skillId, double bonus, int baseBonus) {
        this.skillId = skillId;
        this.bonus = bonus;
        this.baseBonus = baseBonus;
    }

    /**
     * Gets skill id.
     *
     * @return the skill id
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * Gets bonus.
     *
     * @return the bonus
     */
    public double getBonus() {
        return bonus;
    }

    /**
     * Gets base bonus.
     *
     * @return the base bonus
     */
    public int getBaseBonus() {
        return baseBonus;
    }

}
