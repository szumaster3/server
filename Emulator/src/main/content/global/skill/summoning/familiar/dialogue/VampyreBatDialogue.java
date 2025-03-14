package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.inBorders;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Vampyre bat dialogue.
 */
@Initializable
public class VampyreBatDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new VampyreBatDialogue(player);
    }

    /**
     * Instantiates a new Vampyre bat dialogue.
     */
    public VampyreBatDialogue() {}

    /**
     * Instantiates a new Vampyre bat dialogue.
     *
     * @param player the player
     */
    public VampyreBatDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inBorders(player, 3139, 9535, 3306, 9657)) {
            npc(FaceAnim.CHILD_NORMAL, "Ze creatures ov ze dark; vat vonderful music zey make.");
            stage = 0;
            return true;
        }
        int randomChoice = (int) (Math.random() * 3);
        switch (randomChoice) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "You're vasting all that blood, can I have some?");
                stage = 3;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Ven are you going to feed me?");
                stage = 4;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Ven can I eat somethink?");
                stage = 5;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Riiight.");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "I like it down here. Let's stay and eat moths!");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I think I'll pass, thanks.");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "No!");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Well for a start, I'm not giving you any of my blood.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Just as soon as I find something to attack.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.VAMPIRE_BAT_6835, NPCs.VAMPIRE_BAT_6836 };
    }
}
