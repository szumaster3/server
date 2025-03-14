package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Bull ant dialogue.
 */
@Initializable
public class BullAntDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new BullAntDialogue(player);
    }

    /**
     * Instantiates a new Bull ant dialogue.
     */
    public BullAntDialogue() {}

    /**
     * Instantiates a new Bull ant dialogue.
     *
     * @param player the player
     */
    public BullAntDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (player.getSettings().getRunEnergy() < 50) {
            npcl(FaceAnim.CHILD_NORMAL, "What's the matter, Private? Not enjoying the run?");
            stage = 0;
            return true;
        }
        switch (new java.util.Random().nextInt(4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "All right you worthless biped, fall in!");
                stage = 5;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Aten...hut!");
                stage = 9;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I can't believe they stuck me with you...");
                stage = 14;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "What in the name of all the layers of the abyss do you think you're doing, biped?");
                stage = 17;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Sir...wheeze...yes Sir!");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Not enjoying the run? You need more training biped?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Sir, no Sir! Sir, I'm enjoying the run a great deal, Sir!");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Then hop to, Private!");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "We're going to work you so hard your boots fall off, understood?");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Carry on Private!");
                stage = END_DIALOGUE;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Aten...hut!");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "I can't believe they stuck me with you...");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "What in the name of all the layers of the abyss do you think you're doing, biped?");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "Sir, Private Player reporting for immediate active duty, Sir!");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "As you were, Private!");
                stage = END_DIALOGUE;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "Buck up, Sir, it's not that bad.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Stow that, Private, and get back to work!");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!");
                stage = END_DIALOGUE;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Sir, nothing Sir!");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "Well double-time it, Private, whatever it is!");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BULL_ANT_6867, NPCs.BULL_ANT_6868};
    }
}
