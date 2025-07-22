package content.global.skill.magic.spells.lunar;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Magic imbue spell.
 */
@Initializable
public class MagicImbueSpell extends MagicSpell {

    private static final Graphics GRAPHICS = new Graphics(141, 96);
    private static final Animation ANIMATION = new Animation(722);

    /**
     * Instantiates a new Magic imbue spell.
     */
    public MagicImbueSpell() {
        super(SpellBook.LUNAR, 82, 86, null, null, null, new Item[]{new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.FIRE_RUNE.getId(), 7), new Item(Runes.WATER_RUNE.getId(), 7)});
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(13, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        if (player == null) {
            return false;
        }
        if (player.getAttribute("spell:imbue", 0) > GameWorld.getTicks()) {
            player.sendMessage("You already have this activated.");
            return false;
        }
        if (!super.meetsRequirements(player, true, true)) {
            return false;
        }
        setAttribute(player, "spell:imbue", GameWorld.getTicks() + 20);
        player.lock(ANIMATION.getDuration() + 1);
        player.graphics(GRAPHICS);
        player.animate(ANIMATION);
        playAudio(player, Sounds.LUNAR_EMBUE_RUNES_2888);
        player.getPacketDispatch().sendMessage("You are charged to combine runes!");
        return true;
    }
}
