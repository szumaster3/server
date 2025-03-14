package core.game.world.update.flag.context;

import core.game.node.entity.Entity;

/**
 * The type Hit mark.
 */
public class HitMark {

    private final int damage;

    private final int type;

    private int lifepoints;

    private final Entity entity;

    /**
     * The Show health bar.
     */
    public boolean showHealthBar = true;

    /**
     * Instantiates a new Hit mark.
     *
     * @param damage the damage
     * @param type   the type
     * @param entity the entity
     */
    public HitMark(int damage, int type, Entity entity) {
        this.damage = damage;
        this.type = type;
        this.entity = entity;
    }

    /**
     * Instantiates a new Hit mark.
     *
     * @param damage        the damage
     * @param type          the type
     * @param entity        the entity
     * @param showHealthBar the show health bar
     */
    public HitMark(int damage, int type, Entity entity, boolean showHealthBar) {
        this.damage = damage;
        this.type = type;
        this.entity = entity;
        this.showHealthBar = showHealthBar;
    }

    /**
     * Gets damage.
     *
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets lifepoints.
     *
     * @return the lifepoints
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * Sets lifepoints.
     *
     * @param lifepoints the lifepoints
     */
    public void setLifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
    }
}
