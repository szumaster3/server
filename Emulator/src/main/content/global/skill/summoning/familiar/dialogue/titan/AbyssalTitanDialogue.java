package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;
import static core.tools.DialogueHelperKt.START_DIALOGUE;

/**
 * The type Abyssal titan dialogue.
 */
@Initializable
public class AbyssalTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new AbyssalTitanDialogue(player);
    }

    /**
     * Instantiates a new Abyssal titan dialogue.
     */
    public AbyssalTitanDialogue() {}

    /**
     * Instantiates a new Abyssal titan dialogue.
     *
     * @param player the player
     */
    public AbyssalTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        if (stage == START_DIALOGUE) {
            npcl(FaceAnim.CHILD_NORMAL, "Scruunt, scraaan.");
            stage = END_DIALOGUE;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.ABYSSAL_TITAN_7349, NPCs.ABYSSAL_TITAN_7350 };
    }
}
