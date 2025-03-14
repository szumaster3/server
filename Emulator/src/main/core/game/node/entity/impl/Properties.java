package core.game.node.entity.impl;

import core.game.container.Container;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatPulse;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.game.system.config.NPCConfigParser;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * A class that holds various properties related to an entity's combat, equipment, animations, and other settings.
 * It manages combat-related data, including combat pulse, teleporting status, combat style, animations, and equipment bonuses.
 * <p>
 * These properties are used to control how entities behave in the game world, including NPCs and players, during combat and other interactions.
 */
public final class Properties {

    private final Entity entity;
    private CombatPulse combatPulse;
    private boolean retaliating = true;
    private boolean teleporting;
    private int combatLevel;
    private WeaponInterface.AttackStyle attackStyle;
    private Location teleportLocation;
    private Location spawnLocation;
    private int[] bonuses = new int[15];
    private int attackSpeed = 4;
    private long lastAnimationEnd;
    private Animation attackAnimation = new Animation(422, Animator.Priority.HIGH);
    private Animation defenceAnimation = new Animation(404);
    private Animation deathAnimation = new Animation(9055, Animator.Priority.HIGH);
    /**
     * The Death gfx.
     */
    public Graphics deathGfx = new Graphics(-1);
    private Animation rangeAnimation;
    private Animation magicAnimation;
    private CombatSpell spell;
    private CombatSpell autocastSpell;
    private ArmourSet armourSet;
    private boolean multiZone;
    private boolean safeZone;
    /**
     * The Safe respawn.
     */
    public Location safeRespawn;
    private int combatTimeOut = 10;
    private boolean npcWalkable;
    private CombatStyle protectStyle;

    /**
     * Constructs a new Properties object for the given entity.
     *
     * @param entity The entity that these properties belong to.
     */
    public Properties(Entity entity) {
        this.entity = entity;
        this.combatPulse = new CombatPulse(entity);
    }

    /**
     * Updates the defence animation based on the entity's equipment.
     * If the entity is an NPC, it checks the NPC's configuration for the defence animation.
     * If the entity is a player, it checks the player's equipped shield or weapon.
     */
    public void updateDefenceAnimation() {
        if (entity instanceof NPC) {
            Animation animation = ((NPC) entity).getDefinition().getConfiguration(NPCConfigParser.DEFENCE_ANIMATION);
            if (animation != null) {
                defenceAnimation = animation;
            }
            return;
        }
        Container c = ((Player) entity).getEquipment();
        Item item = c.get(EquipmentContainer.SLOT_SHIELD);
        if (item != null) {
            defenceAnimation = item.getDefinition().getConfiguration(ItemConfigParser.DEFENCE_ANIMATION, Animation.create(1156));
        } else if ((item = c.get(EquipmentContainer.SLOT_WEAPON)) != null) {
            defenceAnimation = item.getDefinition().getConfiguration(ItemConfigParser.DEFENCE_ANIMATION, Animation.create(424));
        } else {
            defenceAnimation = Animation.create(397);
        }
    }

    /**
     * Retrieves the combat animation for a given index.
     *
     * @param index The index of the animation to retrieve (0 for attack, 1 for magic, 2 for range, 3 for defence, or 4 for death).
     * @return The corresponding animation.
     */
    public Animation getCombatAnimation(int index) {
        return index == 0 ? attackAnimation : index == 1 ? magicAnimation : index == 2 ? rangeAnimation : index == 3 ? defenceAnimation : deathAnimation;
    }

    /**
     * Gets the entity associated with these properties.
     *
     * @return The entity associated with these properties.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Checks if the entity is currently teleporting.
     *
     * @return {@code true} if the entity is teleporting, {@code false} otherwise.
     */
    public boolean isTeleporting() {
        return teleporting;
    }

    /**
     * Sets whether the entity is teleporting.
     *
     * @param teleporting {@code true} if the entity is teleporting, {@code false} otherwise.
     */
    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    /**
     * Retrieves the combat level of the entity.
     *
     * @return The combat level of the entity.
     * @deprecated Use {@link #getCurrentCombatLevel()} instead.
     */
    @Deprecated
    public int getCombatLevel() {
        return combatLevel;
    }

    /**
     * Retrieves the current combat level of the entity, taking into account any additional combat level modifiers.
     * For players, it considers familiar combat levels in PvP or wilderness zones.
     *
     * @return The current combat level of the entity.
     */
    public int getCurrentCombatLevel() {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if ((GameWorld.getSettings().isPvp() || (GameWorld.getSettings().getWild_pvp_enabled() && player.getSkullManager().isWilderness()))
                && !player.getFamiliarManager().isUsingSummoning()) {
                return combatLevel;
            } else {
                return combatLevel + player.getFamiliarManager().getSummoningCombatLevel();
            }
        }
        return combatLevel;
    }

    /**
     * Sets the combat level for the entity.
     *
     * @param combatLevel The combat level to set.
     */
    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    /**
     * Gets the attack style used by the entity in combat.
     *
     * @return The attack style used by the entity.
     */
    public WeaponInterface.AttackStyle getAttackStyle() {
        return attackStyle;
    }

    /**
     * Sets the attack style for the entity in combat.
     *
     * @param attackStyle The attack style to set.
     */
    public void setAttackStyle(WeaponInterface.AttackStyle attackStyle) {
        this.attackStyle = attackStyle;
    }

    /**
     * Gets the teleportation location for the entity.
     *
     * @return The location to which the entity teleports.
     */
    public Location getTeleportLocation() {
        return teleportLocation;
    }

    /**
     * Sets the teleportation location for the entity.
     *
     * @param teleportLocation The location to set for teleportation.
     */
    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    /**
     * Gets the spawn location of the entity.
     *
     * @return The spawn location of the entity.
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Sets the spawn location for the entity.
     *
     * @param spawnLocation The spawn location to set.
     */
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * Gets the array of bonuses for the entity.
     *
     * @return The array of bonuses for the entity.
     */
    public int[] getBonuses() {
        return bonuses;
    }

    /**
     * Sets the array of bonuses for the entity.
     *
     * @param bonuses The bonuses to set.
     */
    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    /**
     * Gets the timestamp of the last animation end for the entity.
     *
     * @return The timestamp of the last animation end.
     */
    public long getLastAnimationEnd() {
        return lastAnimationEnd;
    }

    /**
     * Gets the combat pulse associated with the entity.
     *
     * @return The combat pulse for the entity.
     */
    public CombatPulse getCombatPulse() {
        return combatPulse;
    }

    /**
     * Sets the combat pulse for the entity.
     *
     * @param combatPulse The combat pulse to set.
     */
    public void setCombatPulse(CombatPulse combatPulse) {
        this.combatPulse = (CombatPulse) combatPulse;
    }

    /**
     * Checks if the entity is retaliating.
     *
     * @return {@code true} if the entity is retaliating, {@code false} otherwise.
     */
    public boolean isRetaliating() {
        return retaliating;
    }

    /**
     * Sets whether the entity is retaliating.
     *
     * @param retaliating {@code true} if the entity should retaliate, {@code false} otherwise.
     */
    public void setRetaliating(boolean retaliating) {
        this.retaliating = retaliating;
    }

    /**
     * Gets the attack speed for the entity.
     *
     * @return The attack speed of the entity.
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Sets the attack speed for the entity.
     *
     * @param attackSpeed The attack speed to set.
     */
    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    /**
     * Gets the attack animation for the entity.
     *
     * @return The attack animation for the entity.
     */
    public Animation getAttackAnimation() {
        return attackAnimation;
    }

    /**
     * Sets the attack animation for the entity.
     *
     * @param attackAnimation The attack animation to set.
     */
    public void setAttackAnimation(Animation attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    /**
     * Gets the defence animation for the entity.
     *
     * @return The defence animation for the entity.
     */
    public Animation getDefenceAnimation() {
        return defenceAnimation;
    }

    /**
     * Sets the defence animation for the entity.
     *
     * @param defenceAnimation The defence animation to set.
     */
    public void setDefenceAnimation(Animation defenceAnimation) {
        this.defenceAnimation = defenceAnimation;
    }

    /**
     * Sets the combat spell for the entity.
     *
     * @param spell The combat spell to set.
     */
    public void setSpell(CombatSpell spell) {
        this.spell = spell;
        if (spell != null) {
            combatPulse.updateStyle();
        }
    }

    /**
     * Gets the combat spell currently set for the entity.
     *
     * @return The combat spell of the entity.
     */
    public CombatSpell getSpell() {
        return spell;
    }

    /**
     * Gets the autocast spell for the entity.
     *
     * @return The autocast spell of the entity.
     */
    public CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * Sets the autocast spell for the entity.
     *
     * @param autocastSpell The autocast spell to set.
     */
    public void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * Gets the armour set equipped by the entity.
     *
     * @return The armour set of the entity.
     */
    public ArmourSet getArmourSet() {
        return armourSet;
    }

    /**
     * Sets the armour set for the entity.
     *
     * @param armourSet The armour set to set.
     */
    public void setArmourSet(ArmourSet armourSet) {
        this.armourSet = armourSet;
    }

    /**
     * Gets the death animation for the entity.
     *
     * @return The death animation of the entity.
     */
    public Animation getDeathAnimation() {
        return deathAnimation;
    }

    /**
     * Sets the death animation for the entity.
     *
     * @param deathAnimation The death animation to set.
     */
    public void setDeathAnimation(Animation deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

    /**
     * Checks if the entity is in a multi-zone.
     *
     * @return {@code true} if the entity is in a multi-zone, {@code false} otherwise.
     */
    public boolean isMultiZone() {
        return multiZone;
    }

    /**
     * Sets whether the entity is in a multi-zone.
     *
     * @param multiZone {@code true} if the entity should be in a multi-zone, {@code false} otherwise.
     */
    public void setMultiZone(boolean multiZone) {
        this.multiZone = multiZone;
    }

    /**
     * Checks if the entity is in a safe zone.
     *
     * @return {@code true} if the entity is in a safe zone, {@code false} otherwise.
     */
    public boolean isSafeZone() {
        return safeZone;
    }

    /**
     * Sets whether the entity is in a safe zone.
     *
     * @param safeZone {@code true} if the entity should be in a safe zone, {@code false} otherwise.
     */
    public void setSafeZone(boolean safeZone) {
        this.safeZone = safeZone;
    }

    /**
     * Gets the combat timeout for the entity.
     *
     * @return The combat timeout for the entity.
     */
    public int getCombatTimeOut() {
        return combatTimeOut;
    }

    /**
     * Sets the combat timeout for the entity.
     *
     * @param combatTimeOut The combat timeout to set.
     */
    public void setCombatTimeOut(int combatTimeOut) {
        this.combatTimeOut = combatTimeOut;
    }

    /**
     * Checks if the entity is walkable as an NPC.
     *
     * @return {@code true} if the entity is walkable as an NPC, {@code false} otherwise.
     */
    public boolean isNPCWalkable() {
        return npcWalkable;
    }

    /**
     * Sets whether the entity is walkable as an NPC.
     *
     * @param npcWalkable {@code true} if the entity should be walkable as an NPC, {@code false} otherwise.
     */
    public void setNPCWalkable(boolean npcWalkable) {
        this.npcWalkable = npcWalkable;
    }

    /**
     * Gets the range animation for the entity.
     *
     * @return The range animation of the entity.
     */
    public Animation getRangeAnimation() {
        return rangeAnimation;
    }

    /**
     * Sets the range animation for the entity.
     *
     * @param rangeAnimation The range animation to set.
     */
    public void setRangeAnimation(Animation rangeAnimation) {
        this.rangeAnimation = rangeAnimation;
    }

    /**
     * Gets the magic animation for the entity.
     *
     * @return The magic animation of the entity.
     */
    public Animation getMagicAnimation() {
        return magicAnimation;
    }

    /**
     * Sets the magic animation for the entity.
     *
     * @param magicAnimation The magic animation to set.
     */
    public void setMagicAnimation(Animation magicAnimation) {
        this.magicAnimation = magicAnimation;
    }

    /**
     * Gets the protection combat style for the entity.
     *
     * @return The protection style of the entity.
     */
    public CombatStyle getProtectStyle() {
        return protectStyle;
    }

    /**
     * Sets the protection combat style for the entity.
     *
     * @param protectStyle The protection style to set.
     */
    public void setProtectStyle(CombatStyle protectStyle) {
        this.protectStyle = protectStyle;
    }
}
