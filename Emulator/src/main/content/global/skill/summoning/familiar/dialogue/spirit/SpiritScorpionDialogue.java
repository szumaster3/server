package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit scorpion dialogue.
 */
@Initializable
public class SpiritScorpionDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritScorpionDialogue(player);
    }

    /**
     * Instantiates a new Spirit scorpion dialogue.
     */
    public SpiritScorpionDialogue() {}

    /**
     * Instantiates a new Spirit scorpion dialogue.
     *
     * @param player the player
     */
    public SpiritScorpionDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Hey, boss, how about we go to the bank?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Say hello to my little friend!");
                stage = 9;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Hey, boss, I've been thinking.");
                stage = 13;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Why do we never go to crossroads and rob travelers?");
                stage = 20;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "And do what?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Well, we could open by shouting, 'Stand and deliver!'");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Why does everything with you end with something getting held up?");
                stage++;
                break;

            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "That isn't true! Give me one example.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "How about the post office?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "How about another?");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Those junior White Knights? The ones selling the gnome crunchies?");
                stage++;
                break;

            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "That was self defence.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "No! No more hold-ups, stick-ups, thefts, or heists, you got that?");
                stage = END_DIALOGUE;
                break;

            case 9:
                playerl(FaceAnim.ASKING, "What?");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "My little friend: you ignored him last time you met him.");
                stage++;
                break;

            case 11:
                playerl(FaceAnim.FRIENDLY, "So, who is your friend?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "If I tell you, what is the point?");
                stage = END_DIALOGUE;
                break;

            case 13:
                playerl(FaceAnim.FRIENDLY, "That's never a good sign.");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "See, I heard about this railway...");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "We are not robbing it!");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "I might not have wanted to suggest that, boss...");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Then what were you going to suggest?");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "That isn't important right now.");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "I thought as much.");
                stage = END_DIALOGUE;
                break;

            case 20:
                playerl(FaceAnim.FRIENDLY, "There are already highwaymen at the good spots.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "Maybe we need to think bigger.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_SCORPION_6837, NPCs.SPIRIT_SCORPION_6838};
    }
}
