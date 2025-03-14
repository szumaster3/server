package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.world.map.Location;
import core.game.world.map.zone.impl.WildernessZone;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.*;

/**
 * The type Lava titan dialogue.
 */
@Initializable
public class LavaTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new LavaTitanDialogue(player);
    }

    /**
     * Instantiates a new Lava titan dialogue.
     */
    public LavaTitanDialogue() {}

    /**
     * Instantiates a new Lava titan dialogue.
     *
     * @param player the player
     */
    public LavaTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!(npc instanceof content.global.skill.summoning.familiar.Familiar)) {
            return false;
        }
        content.global.skill.summoning.familiar.Familiar f = (content.global.skill.summoning.familiar.Familiar) npc;
        if (f.getOwner() != player) {
            sendMessage(player, "This is not your follower.");
            return true;
        } else {
            options("Chat", "Teleport to Lava Maze");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (buttonId) {
            case 1:
                openDialogue(player, new LavaTitanDialogueFile());
                break;
            case 2:
                if (!WildernessZone.checkTeleport(player, 20)) {
                    end();
                } else {
                    teleport(player, new Location(3030, 3842, 0), TeleportType.NORMAL);
                    end();
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.LAVA_TITAN_7341, NPCs.LAVA_TITAN_7342};
    }

}
