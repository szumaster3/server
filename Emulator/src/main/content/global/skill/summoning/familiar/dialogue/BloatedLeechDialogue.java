package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Bloated leech dialogue.
 */
@Initializable
public class BloatedLeechDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new BloatedLeechDialogue(player);
    }

    /**
     * Instantiates a new Bloated leech dialogue.
     */
    public BloatedLeechDialogue() {}

    /**
     * Instantiates a new Bloated leech dialogue.
     *
     * @param player the player
     */
    public BloatedLeechDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch (new java.util.Random().nextInt(4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "I'm afraid it's going to have to come off, " + player.getUsername() + ".");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "You're in a critical condition.");
                stage = 3;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Let's get a look at that brain of yours.");
                stage = 6;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I think we're going to need to operate.");
                stage = 9;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "What is?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Never mind. Trust me, I'm almost a doctor.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I think I'll get a second opinion.");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "Is it terminal?");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Not yet. Let me get a better look and I'll see what I can do about it.");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "There are two ways to take that...and I think I'll err on the side of caution.");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "What? My brains stay inside my head, thanks.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "That's ok, I can just drill a hole.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "How about you don't and pretend you did?");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "I think we can skip that for now.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "Who's the doctor here?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Not you.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "I may not be a doctor, but I'm keen. Does that not count?");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "In most other fields, yes; in medicine, no.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BLOATED_LEECH_6843, NPCs.BLOATED_LEECH_6844};
    }
}
