package content.region.kandarin.handlers.guilds;

import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.*;

/**
 * The type Legends guild plugin.
 */
@Initializable
public final class LegendsGuildPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(org.rs.consts.Scenery.GATE_2391).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.GATE_2392).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LEGENDS_GUILD_DOOR_2896).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LEGENDS_GUILD_DOOR_2897).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.STAIRCASE_32048).getHandlers().put("option:climb-up", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final int id = node instanceof Scenery ? ((Scenery) node).getId() : 0;

        switch (option) {
            case "open":
                switch (id) {
                    case 2391:
                    case 2392:
                        DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
                        sendMessage(player, "The guards salute you as you walk past.");
                        NPC guard = findLocalNPC(player, NPCs.LEGENDS_GUARD_398);
                        if (guard != null) {
                            sendChat(guard, "Legends' Guild member approaching!", 1);
                        }
                        break;
                    case 2896:
                    case 2897:
                        DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
                        sendMessage(player, "You push the huge Legends Guild doors open.");
                        if (player.getLocation().getY() < 3374) {
                            sendMessage(player, "You approach the Legends Guild main doors.");
                        }
                        break;
                }
                break;
            case "climb-up":
                if (id == org.rs.consts.Scenery.STAIRCASE_32048) {
                    teleport(player, Location.create(2723, 3375, 0), TeleportManager.TeleportType.INSTANT);
                }
                break;
        }
        return true;
    }
}