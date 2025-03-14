package core.game.node.entity.combat;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.Ammunition;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.combat.equipment.RangeWeapon;
import core.game.node.entity.combat.equipment.Weapon;
import core.game.node.entity.combat.spell.CombatSpell;

/**
 * The type Battle state.
 */
public final class BattleState {

    private Entity entity;

    private Entity victim;

    private int estimatedHit;

    private int maximumHit;

    private int secondaryHit = -1;

    private int recoilDamage = -1;

    private int poisonDamage = -1;

    private boolean frozen;

    private BattleState[] targets;

    private Weapon weapon;

    private RangeWeapon rangeWeapon;

    private Ammunition ammunition;

    private CombatSpell spell;

    private CombatStyle style;

    private ArmourSet armourEffect;

    /**
     * Instantiates a new Battle state.
     */
    public BattleState() {

    }

    /**
     * Instantiates a new Battle state.
     *
     * @param entity the entity
     * @param victim the victim
     */
    public BattleState(Entity entity, Entity victim) {
        this.victim = victim;
        this.entity = entity;
    }

    /**
     * Neutralize hits.
     */
    public void neutralizeHits() {
        if (getEstimatedHit() > 0) {
            setEstimatedHit(0);
        }
        if (getSecondaryHit() > 0) {
            setSecondaryHit(0);
        }
    }

    /**
     * Gets victim.
     *
     * @return the victim
     */
    public Entity getVictim() {
        return victim;
    }

    /**
     * Sets victim.
     *
     * @param victim the victim
     */
    public void setVictim(Entity victim) {
        this.victim = victim;
    }

    /**
     * Gets estimated hit.
     *
     * @return the estimated hit
     */
    public int getEstimatedHit() {
        return estimatedHit;
    }

    /**
     * Sets estimated hit.
     *
     * @param estimatedHit the estimated hit
     */
    public void setEstimatedHit(int estimatedHit) {
        this.estimatedHit = estimatedHit;
    }

    /**
     * Gets secondary hit.
     *
     * @return the secondary hit
     */
    public int getSecondaryHit() {
        return secondaryHit;
    }

    /**
     * Sets secondary hit.
     *
     * @param secondaryHit the secondary hit
     */
    public void setSecondaryHit(int secondaryHit) {
        this.secondaryHit = secondaryHit;
    }

    /**
     * Gets recoil damage.
     *
     * @return the recoil damage
     */
    public int getRecoilDamage() {
        return recoilDamage;
    }

    /**
     * Sets recoil damage.
     *
     * @param recoilDamage the recoil damage
     */
    public void setRecoilDamage(int recoilDamage) {
        this.recoilDamage = recoilDamage;
    }

    /**
     * Get targets battle state [ ].
     *
     * @return the battle state [ ]
     */
    public BattleState[] getTargets() {
        return targets;
    }

    /**
     * Sets targets.
     *
     * @param targets the targets
     */
    public void setTargets(BattleState[] targets) {
        this.targets = targets;
    }

    /**
     * Gets poison damage.
     *
     * @return the poison damage
     */
    public int getPoisonDamage() {
        return poisonDamage;
    }

    /**
     * Sets poison damage.
     *
     * @param poisonDamage the poison damage
     */
    public void setPoisonDamage(int poisonDamage) {
        this.poisonDamage = poisonDamage;
    }

    /**
     * Gets weapon.
     *
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Sets weapon.
     *
     * @param weapon the weapon
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets spell.
     *
     * @return the spell
     */
    public CombatSpell getSpell() {
        return spell;
    }

    /**
     * Sets spell.
     *
     * @param spell the spell
     */
    public void setSpell(CombatSpell spell) {
        this.spell = spell;
    }

    /**
     * Gets maximum hit.
     *
     * @return the maximum hit
     */
    public int getMaximumHit() {
        return maximumHit;
    }

    /**
     * Sets maximum hit.
     *
     * @param maximumHit the maximum hit
     */
    public void setMaximumHit(int maximumHit) {
        this.maximumHit = maximumHit;
    }

    /**
     * Gets range weapon.
     *
     * @return the range weapon
     */
    public RangeWeapon getRangeWeapon() {
        return rangeWeapon;
    }

    /**
     * Sets range weapon.
     *
     * @param rangeWeapon the range weapon
     */
    public void setRangeWeapon(RangeWeapon rangeWeapon) {
        this.rangeWeapon = rangeWeapon;
    }

    /**
     * Gets ammunition.
     *
     * @return the ammunition
     */
    public Ammunition getAmmunition() {
        return ammunition;
    }

    /**
     * Sets ammunition.
     *
     * @param ammunition the ammunition
     */
    public void setAmmunition(Ammunition ammunition) {
        this.ammunition = ammunition;
    }

    /**
     * Is frozen boolean.
     *
     * @return the boolean
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets frozen.
     *
     * @param frozen the frozen
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Gets style.
     *
     * @return the style
     */
    public CombatStyle getStyle() {
        return style;
    }

    /**
     * Sets style.
     *
     * @param style the style
     */
    public void setStyle(CombatStyle style) {
        this.style = style;
    }

    /**
     * Gets armour effect.
     *
     * @return the armour effect
     */
    public ArmourSet getArmourEffect() {
        return armourEffect;
    }

    /**
     * Sets armour effect.
     *
     * @param armourEffect the armour effect
     */
    public void setArmourEffect(ArmourSet armourEffect) {
        this.armourEffect = armourEffect;
    }

    /**
     * Gets attacker.
     *
     * @return the attacker
     */
    public Entity getAttacker() {
        return entity;
    }

    /**
     * Gets total damage.
     *
     * @return the total damage
     */
    public int getTotalDamage() {
        int hit = Math.max(estimatedHit, 0) + Math.max(secondaryHit, 0);

        if (targets != null) {
            for (BattleState s : targets) {
                if (s != null) {
                    hit += Math.max(s.getEstimatedHit(), 0) + Math.max(s.getSecondaryHit(), 0);
                }
            }
        }
        return hit;
    }

}