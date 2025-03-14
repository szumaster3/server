package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.anyInEquipment;
import static core.api.ContentAPIKt.anyInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Thorny snail dialogue.
 */
@Initializable
public class ThornySnailDialogue extends Dialogue {

    private static final int[] SNELM = {
        3345, 3327, 3355, 3337, 3349, 3341, 3341, 3359, 3347, 3329,
        3357, 3339, 3351, 3333, 3361, 3343, 3353, 3335
    };

    @Override
    public Dialogue newInstance(Player player) {
        return new ThornySnailDialogue(player);
    }

    /**
     * Instantiates a new Thorny snail dialogue.
     */
    public ThornySnailDialogue() {}

    /**
     * Instantiates a new Thorny snail dialogue.
     *
     * @param player the player
     */
    public ThornySnailDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInInventory(player, SNELM) || anyInEquipment(player, SNELM)) {
            npcl(FaceAnim.OLD_NORMAL, "...");
            stage = 0;
        } else {
            int randomChoice = (int) (Math.random() * 4);
            switch (randomChoice) {
                case 0:
                    npcl(FaceAnim.OLD_NORMAL, "All this running around the place is fun!");
                    stage = 7;
                    break;
                case 1:
                    npcl(FaceAnim.CHILD_NORMAL, "I think my stomach is drying out...");
                    stage = 12;
                    break;
                case 2:
                    npcl(FaceAnim.CHILD_NORMAL, "Okay, I have to ask, what are those things you people totter about on?");
                    stage = 15;
                    break;
                case 3:
                    npcl(FaceAnim.OLD_NORMAL, "Can you slow down?");
                    stage = 20;
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "...");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "What's the matter?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Check your head...");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "What about it... Oh, wait! Oh, this is pretty awkward...");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "You're wearing the spine of one of my relatives as a hat...");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Well more of a faux-pas, then.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Just a bit...");
                stage = END_DIALOGUE;
                break;

            case 7:
                playerl(FaceAnim.FRIENDLY, "I'll bet it's a step up from your usually sedentary lifestyle!");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.OLD_NORMAL, "True, but it's mostly seeing the sort of sights you don't get at home.");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Such as?");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.OLD_NORMAL, "Living things for a start.");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Those are in short supply in Mort Myre, I admit.");
                stage = END_DIALOGUE;
                break;

            case 12:
                playerl(FaceAnim.FRIENDLY, "Your stomach? How do you know how it's feeling?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.OLD_NORMAL, "I am walking on it, you know...");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "Urrgh...");
                stage = END_DIALOGUE;
                break;

            case 15:
                playerl(FaceAnim.HALF_ASKING, "You mean my legs?");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.OLD_NORMAL, "Yes, those. How are you supposed to eat anything through them?");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Well, we don't. That's what our mouths are for.");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.OLD_NORMAL, "Oh, right! I thought those were for expelling waste gas and hot air!");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.HALF_ASKING, "Well, for a lot of people they are.");
                stage = END_DIALOGUE;
                break;

            case 20:
                playerl(FaceAnim.FRIENDLY, "Are we going too fast for you?");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.OLD_NORMAL, "I bet if you had to run on your internal organs you'd want a break now and then!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.THORNY_SNAIL_6806, NPCs.THORNY_SNAIL_6807 };
    }
}
