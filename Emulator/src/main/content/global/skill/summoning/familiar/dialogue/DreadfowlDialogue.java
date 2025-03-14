package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Dreadfowl dialogue.
 */
@Initializable
public class DreadfowlDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new DreadfowlDialogue(player);
    }

    /**
     * Instantiates a new Dreadfowl dialogue.
     */
    public DreadfowlDialogue() {}

    /**
     * Instantiates a new Dreadfowl dialogue.
     *
     * @param player the player
     */
    public DreadfowlDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 3)) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "Attack! Fight! Annihilate!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "Can it be fightin' time, please?");
                stage = 1;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "I want to fight something.");
                stage = 2;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "It always worries me when you're so happy saying that.");
                stage = END_DIALOGUE;
                break;

            case 1:
                playerl(FaceAnim.FRIENDLY, "Look I'll find something for you to fight, just give me a second.");
                stage = END_DIALOGUE;
                break;

            case 2:
                playerl(FaceAnim.FRIENDLY, "I'll find something for you in a minute - just be patient.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.DREADFOWL_6825, NPCs.DREADFOWL_6826};
    }
}
