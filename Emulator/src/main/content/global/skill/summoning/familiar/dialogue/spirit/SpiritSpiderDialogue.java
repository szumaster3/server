package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit spider dialogue.
 */
@Initializable
public class SpiritSpiderDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritSpiderDialogue(player);
    }

    /**
     * Instantiates a new Spirit spider dialogue.
     */
    public SpiritSpiderDialogue() {}

    /**
     * Instantiates a new Spirit spider dialogue.
     *
     * @param player the player
     */
    public SpiritSpiderDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Who is that?");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "What are you doing?");
                stage = 12;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Sigh...");
                stage = 17;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "So, do I get any of those flies?");
                stage = 20;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "I've not decided yet.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Fine, don't tell me...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Oh, okay, well, we are going...");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Don't want to know now.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.FRIENDLY, "Who?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "The two-legs over there.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "I can't see who you mean...");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Never mind...");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Can you describe them a little better...");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "It doesn't matter now.");
                stage = 7;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.");
                stage = END_DIALOGUE;
                break;

            case 12:
                playerl(FaceAnim.FRIENDLY, "Nothing that you should concern yourself with.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "I see, you don't think I'm smart enough to understand...");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "That's not it at all! Look, I was...");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Don't wanna know now.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.");
                stage = END_DIALOGUE;
                break;

            case 17:
                playerl(FaceAnim.FRIENDLY, "What is it now?");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "Nothing really.");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "Oh, well that's a relief.");
                stage = END_DIALOGUE;
                break;

            case 20:
                playerl(FaceAnim.FRIENDLY, "I don't know, I was saving these for a pet.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "I see...");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "Look, you can have some if you want.");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, don't do me any favours.");
                stage++;
                break;
            case 24:
                playerl(FaceAnim.FRIENDLY, "Look, here, have some!");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.CHILD_NORMAL, "Don't want them now.");
                stage = 7;
                break;
            case 26:
                playerl(FaceAnim.FRIENDLY, "Siiiigh...spiders.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.SPIRIT_SPIDER_6841, NPCs.SPIRIT_SPIDER_6842 };
    }
}
