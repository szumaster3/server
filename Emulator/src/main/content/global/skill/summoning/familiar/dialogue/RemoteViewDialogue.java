package content.global.skill.summoning.familiar.dialogue;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.RemoteViewer;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * The type Remote view dialogue.
 */
@Initializable
public class RemoteViewDialogue extends Dialogue {

    private Familiar familiar;

    @Override
    public Dialogue newInstance(Player player) {
        return new RemoteViewDialogue(player);
    }

    /**
     * Instantiates a new Remote view dialogue.
     */
    public RemoteViewDialogue() {}

    /**
     * Instantiates a new Remote view dialogue.
     *
     * @param player the player
     */
    public RemoteViewDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        familiar = (Familiar) args[0];
        options("North", "East", "South", "West", "Straight up");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        end();
        RemoteViewer.create(
            player,
            familiar,
            familiar.getViewAnimation(),
            RemoteViewer.ViewType.values()[buttonId - 1]
        ).startViewing();
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { DialogueInterpreter.getDialogueKey(RemoteViewer.DIALOGUE_NAME) };
    }
}
