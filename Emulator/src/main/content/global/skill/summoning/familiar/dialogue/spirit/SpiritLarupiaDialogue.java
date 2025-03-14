package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit larupia dialogue.
 */
@Initializable
public class SpiritLarupiaDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritLarupiaDialogue(player);
    }

    /**
     * Instantiates a new Spirit larupia dialogue.
     */
    public SpiritLarupiaDialogue() {}

    /**
     * Instantiates a new Spirit larupia dialogue.
     *
     * @param player the player
     */
    public SpiritLarupiaDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Kitty cat!");
                stage = 0;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "Hello friend!");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "What are we doing today, master?");
                stage = 11;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Master, do you ever worry that I might eat you?");
                stage = 14;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "What is your wish master?");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "Have you ever thought about doing something other than hunting and serving me?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "You mean, like stand-up comedy, master?");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Umm...yes, like that.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "No, master.");
                stage = END_DIALOGUE;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "'Friend', master? I do not understand this word.");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Friends are people, or animals, who like one another. I think we are friends.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Ah, I think I understand friends, master.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Great!");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "A friend is someone who looks tasty, but you don't eat.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "!");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "I don't know, what do you want to do?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "I desire only to hunt and to serve my master.");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "Err...great! I guess I'll decide then.");
                stage = END_DIALOGUE;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "No, of course not! We're pals.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "That is good, master.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Should I?");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Of course not, master.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "Oh. Good.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_LARUPIA_7337, NPCs.SPIRIT_LARUPIA_7338};
    }
}
