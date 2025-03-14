package content.global.skill.summoning.familiar.dialogue.spirit;

import content.global.skill.summoning.familiar.Familiar;
import core.game.dialogue.Dialogue;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.world.map.Location;
import core.game.world.map.zone.impl.WildernessZone;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.*;

/**
 * The type Spirit kyatt dialogue.
 */
@Initializable
public class SpiritKyattDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritKyattDialogue(player);
    }

    /**
     * Instantiates a new Spirit kyatt dialogue.
     */
    public SpiritKyattDialogue() {}

    /**
     * Instantiates a new Spirit kyatt dialogue.
     *
     * @param player the player
     */
    public SpiritKyattDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!(npc instanceof Familiar)) {
            return false;
        }
        Familiar f = (Familiar) npc;
        if (!f.getOwner().equals(player)) {
            sendMessage(player, "This is not your follower.");
            return true;
        } else {
            sendDialogueOptions(player, "Select an Option", "Chat", "Teleport");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (buttonId) {
            case 1:
                openDialogue(player, new SpiritKyattDialogueFile());
                break;
            case 2:
                if (!WildernessZone.checkTeleport(player, 20)) {
                    end();
                } else {
                    teleport(player, new Location(2326, 3634, 0), TeleportManager.TeleportType.NORMAL);
                    end();
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_KYATT_7365, NPCs.SPIRIT_KYATT_7366};
    }
}
