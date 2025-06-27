package content.global.skill.magic.spells.modern;

import core.game.component.Component;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Sounds;

/**
 * Represents the Teleport other spell.
 */
@Initializable
public final class TeleportOtherSpell extends MagicSpell {

    private String destination;

    private Location location;

    /**
     * Instantiates a new Teleport other spell.
     */
    public TeleportOtherSpell() {

    }

    /**
     * Instantiates a new Teleport other spell.
     *
     * @param level       the level
     * @param experience  the experience
     * @param destination the destination
     * @param location    the location
     * @param runes       the runes
     */
    public TeleportOtherSpell(int level, double experience, String destination, Location location, Item... runes) {
        super(SpellBook.MODERN, level, experience, Animation.create(1818), Graphics.create(org.rs.consts.Graphics.TELEOTHER_CAST_343), new Audio(Sounds.TELE_OTHER_CAST_199, 1, 0), runes);
        this.destination = destination;
        this.location = location;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(54, new TeleportOtherSpell(74, 84, "Lumbridge", Location.create(3222, 3217, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(1)));
        SpellBook.MODERN.register(59, new TeleportOtherSpell(82, 92, "Falador", Location.create(2965, 3378, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1)));
        SpellBook.MODERN.register(62, new TeleportOtherSpell(90, 100, "Camelot", Location.create(2758, 3478, 0), Runes.SOUL_RUNE.getItem(2), Runes.LAW_RUNE.getItem(1)));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Player p = (Player) entity;
        if (!(target instanceof Player)) {
            p.getPacketDispatch().sendMessage("You can only cast this spell on other players.");
            return false;
        }
        if (!entity.getZoneMonitor().teleport(0, null)) {
            return false;
        }
        Player o = (Player) target;
        if (!o.isActive() || o.isTeleBlocked() || o.getInterfaceManager().isOpened()) {
            p.getPacketDispatch().sendMessage("The other player is currently busy.");
            return false;
        }
        if (o.getZoneMonitor().isInZone("Wilderness") && o.getProperties().getCombatPulse().isInCombat()) {
            p.sendMessage("The other player has their hands full at the moment!");
            return true;
        }
        if (!o.getSettings().isAcceptAid()) {
            p.getPacketDispatch().sendMessage("The player is not accepting any aid.");
            return false;
        }
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        visualize(entity, target);
        Graphics.send(new Graphics(342), o.getLocation());
        p.faceLocation(o.getLocation());
        o.setAttribute("t-o_location", location);
        o.getPacketDispatch().sendString(p.getUsername(), 326, 1);
        o.getPacketDispatch().sendString(destination, 326, 3);
        o.getInterfaceManager().open(new Component(326));
        return true;
    }

}
