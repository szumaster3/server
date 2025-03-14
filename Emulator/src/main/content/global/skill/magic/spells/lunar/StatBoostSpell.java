package content.global.skill.magic.spells.lunar;

import content.data.consumables.Consumables;
import core.game.bots.AIPlayer;
import core.game.consumable.Potion;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Animations;
import org.rs.consts.Sounds;

import java.util.List;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Stat boost spell.
 */
@Initializable
public final class StatBoostSpell extends MagicSpell {
    private static final Animation ANIMATION = new Animation(Animations.LUNAR_BOOST_POT_SHARE_4413);
    private static final Graphics GRAPHICS = new Graphics(733, 130);

    /**
     * The constant VIAL.
     */
    public static final int VIAL = 229;

    /**
     * Instantiates a new Stat boost spell.
     */
    public StatBoostSpell() {
        super(SpellBook.LUNAR, 84, 88, null, null, null, new Item[]{new Item(Runes.ASTRAL_RUNE.getId(), 3), new Item(Runes.EARTH_RUNE.getId(), 12), new Item(Runes.WATER_RUNE.getId(), 10)});
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(26, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        Item item = ((Item) target);
        
        if (item == null) {
            player.getPacketDispatch().sendMessage("Invalid item target.");
            return false;
        }

        var consumable = Consumables.getConsumableById(item.getId());
        if (consumable == null) {
            player.getPacketDispatch().sendMessage("You can only cast this spell on a potion.");
            return false;
        }

        final Potion potion = (Potion) consumable.getConsumable();
        if (potion == null) {
            player.getPacketDispatch().sendMessage("You can only cast this spell on a valid potion.");
            return false;
        }

        player.getInterfaceManager().setViewedTab(6);

        if (!item.getDefinition().isTradeable() ||
                item.getName().toLowerCase().contains("restore") ||
                item.getName().toLowerCase().contains("zamorak") ||
                item.getName().toLowerCase().contains("saradomin") ||
                item.getName().toLowerCase().contains("combat")) {
            player.getPacketDispatch().sendMessage("You can't cast this spell on that item.");
            return false;
        }

        List<Player> pl = RegionManager.getLocalPlayers(player, 1);
        if (pl.isEmpty()) {
            player.getPacketDispatch().sendMessage("There is nobody around to share the potion with.");
            return false;
        }

        int doses = potion.getDose(item);
        if (!super.meetsRequirements(player, true, false)) {
            return false;
        }

        int size = 0;
        for (Player nearbyPlayer : pl) {
            if (size >= doses) break;

            if (!nearbyPlayer.isActive() || nearbyPlayer.getLocks().isInteractionLocked() || nearbyPlayer == player) {
                continue;
            }

            if (!nearbyPlayer.getSettings().isAcceptAid() && !(nearbyPlayer instanceof AIPlayer)) {
                continue;
            }

            nearbyPlayer.graphics(GRAPHICS);
            playGlobalAudio(nearbyPlayer.getLocation(), Sounds.LUNAR_STRENGTH_SHARE2_2902);
            potion.getEffect().activate(nearbyPlayer);
            size++;
        }

        if (size == 0) {
            player.getPacketDispatch().sendMessage("There is nobody around with accept aid turned on to share the potion with.");
            return false;
        }

        super.meetsRequirements(player, true, true);
        potion.getEffect().activate(player);
        playGlobalAudio(player.getLocation(), Sounds.LUNAR_STRENGTH_SHARE_2901);
        player.animate(ANIMATION);
        player.graphics(GRAPHICS);

        player.getInventory().remove(item);

        int newIndex = (potion.getIds().length - doses) + size;
        if (newIndex > potion.getIds().length - 1) {
            player.getInventory().add(new Item(VIAL));
        } else {
            player.getInventory().add(new Item(potion.getIds()[newIndex]));
        }

        return true;
    }
}
