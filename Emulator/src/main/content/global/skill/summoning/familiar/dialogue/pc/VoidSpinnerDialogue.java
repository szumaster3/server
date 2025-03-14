package content.global.skill.summoning.familiar.dialogue.pc;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.anyInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Void spinner dialogue.
 */
@Initializable
public class VoidSpinnerDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new VoidSpinnerDialogue(player);
    }

    /**
     * Instantiates a new Void spinner dialogue.
     */
    public VoidSpinnerDialogue() {}

    /**
     * Instantiates a new Void spinner dialogue.
     *
     * @param player the player
     */
    public VoidSpinnerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInInventory(player, Items.PURPLE_SWEETS_4561, Items.PURPLE_SWEETS_10476)) {
            npcl(FaceAnim.CHILD_NORMAL, "You have sweeties for spinner?");
            stage = 0;
            return true;
        }
        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Let's go play hide an' seek!");
                stage = 6;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "My mummy told me I was clever.");
                stage = 9;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!");
                stage = 13;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Where's the sweeties?");
                stage = 16;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Sweeties? No sweeties here.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "You do! You do! Gimmie sweeties!");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I don't have any sweeties!");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "What you hiding in your backpack, then?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "That? Oh, that's...erm...worms! Yes, worms. Purple worms.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Yucky!");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Okay, you hide and I'll come find you.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "You'll never find me!");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "What a disaster that would be...");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.HALF_ASKING, "Aren't you meant to be the essence of a spinner? How do you have a mother?");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "What you mean, 'essence'?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Never mind, I don't think it matters.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "My logimical powers has proved me smarterer than you!");
                stage = END_DIALOGUE;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "No! You've got so many tentacles!");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "I'm coming to tickle you!");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Aieee!");
                stage = END_DIALOGUE;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "They are wherever good spinners go.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Yay for me!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.VOID_SPINNER_7333, NPCs.VOID_SPINNER_7334};
    }
}
