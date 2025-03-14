package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit tz kih dialogue.
 */
@Initializable
public class SpiritTzKihDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritTzKihDialogue(player);
    }

    /**
     * Instantiates a new Spirit tz kih dialogue.
     */
    public SpiritTzKihDialogue() {}

    /**
     * Instantiates a new Spirit tz kih dialogue.
     *
     * @param player the player
     */
    public SpiritTzKihDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "How's it going, Tz-kih?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Does JalYt think Tz-kih as strong as Jad Jad?");
                stage = 3;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Have you heard of blood bat, JalYt?");
                stage = 5;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Pray pray pray pray pray pray pray pray!");
                stage = 10;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "You drink pray, me drink pray.");
                stage = 13;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Pray pray?");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "Don't start with all that again.");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Hmph, silly JalYt.");
                stage = END_DIALOGUE;
                break;

            case 3:
                playerl(FaceAnim.FRIENDLY, "Are you as strong as TzTok-Jad? Yeah, sure, why not.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Really? Thanks, JalYt. Tz-Kih strong and happy.");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.FRIENDLY, "Blood bats? You mean vampire bats?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Blood bat.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Yes, I've heard of them. What about them?");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Tz-Kih like blood bat, but drink pray pray not blood blood. Blood blood is yuck.");
                stage++;
                break;

            case 9:
                playerl(FaceAnim.FRIENDLY, "Thanks, Tz-Kih, that's nice to know.");
                stage = END_DIALOGUE;
                break;

            case 10:
                playerl(FaceAnim.FRIENDLY, "FRIENDLY down, Tz-Kih, we'll find you something to drink soon.");
                stage++;
                break;

            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Pray praaaaaaaaaaaaaay!");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "Okay, okay. FRIENDLY down!");
                stage = END_DIALOGUE;
                break;

            case 13:
                playerl(FaceAnim.FRIENDLY, "What's that, Tz-Kih?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "You got pray pray pot. Tz-Kih drink pray pray you, you drink pray pray pot.");
                stage++;
                break;

            case 15:
                playerl(FaceAnim.FRIENDLY, "You want to drink my Prayer points?");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Pray pray.");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Err, not right now, Tz-Kih. I, er, need them myself.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "Sorry.");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "But, pray praaaay...?");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_TZ_KIH_7361, NPCs.SPIRIT_TZ_KIH_7362};
    }
}
