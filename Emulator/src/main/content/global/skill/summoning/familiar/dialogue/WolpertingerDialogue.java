package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Wolpertinger dialogue.
 */
@Initializable
public class WolpertingerDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new WolpertingerDialogue(player);
    }

    /**
     * Instantiates a new Wolpertinger dialogue.
     */
    public WolpertingerDialogue() {}

    /**
     * Instantiates a new Wolpertinger dialogue.
     *
     * @param player the player
     */
    public WolpertingerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npcl(FaceAnim.CHILD_NORMAL, "Raaar! Mewble, whurf whurf.");
        stage = END_DIALOGUE;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.WOLPERTINGER_6869, NPCs.WOLPERTINGER_6870 };
    }
}
