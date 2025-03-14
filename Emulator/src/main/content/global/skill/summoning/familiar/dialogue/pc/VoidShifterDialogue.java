package content.global.skill.summoning.familiar.dialogue.pc;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Void shifter dialogue.
 */
@Initializable
public class VoidShifterDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new VoidShifterDialogue(player);
    }

    /**
     * Instantiates a new Void shifter dialogue.
     */
    public VoidShifterDialogue() {}

    /**
     * Instantiates a new Void shifter dialogue.
     *
     * @param player the player
     */
    public VoidShifterDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "What a splendid day, " + (player.isMale() ? "sir" : "madam") + "!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "I'm sorry to bother you, but could you assist me briefly?");
                stage = 3;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "How do you do?");
                stage = 9;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Lets go and see to those cads and bounders!");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Yes, it is!");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "It could only be marginally improved, perhaps, by tea and biscuits.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "What a marvellous idea!");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "I suppose so.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "I was wondering, briefly, if perchance you might care to dance?");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "Dance? With a pest?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Well, you see, I'm dreadfully out of practice and now I can barely leap, let alone teleport.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "I'm not going to help you remember how to destroy the world!");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "What a beastly world we live in where one " + (player.isMale() ? "gentleman" : "lady") + " will not aid a pest in need...");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Okay, I suppose.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "Marvellous, simply marvellous!");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.HALF_ASKING, "Which 'cads and bounders' did you mean, exactly?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Why, the ones with no honour, of course.");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "I don't think he knows what pests do...");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.VOID_SHIFTER_7367, NPCs.VOID_SHIFTER_7368, NPCs.VOID_SHIFTER_7369};
    }
}
