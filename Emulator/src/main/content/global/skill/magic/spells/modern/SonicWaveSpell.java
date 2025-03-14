package content.global.skill.magic.spells.modern;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Sonic wave spell.
 */
@Initializable
public final class SonicWaveSpell extends CombatSpell {

    private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WHITE_BAR_OR_SOMETHING_GOES_THROUGH_YOU_337, 8, 8, 52, 100, 15, 1);

    /**
     * Instantiates a new Sonic wave spell.
     */
    public SonicWaveSpell() {

    }

    private SonicWaveSpell(SpellType type, int level, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, 0.0, sound, sound + 1, null, start, projectile, end, runes);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 5);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(63232, new SonicWaveSpell(SpellType.STRIKE, 1, -1, new Graphics(337), STRIKE_PROJECTILE, null));
        return this;
    }

}
