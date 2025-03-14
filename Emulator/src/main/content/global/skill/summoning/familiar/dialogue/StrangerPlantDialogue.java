package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Stranger plant dialogue.
 */
@Initializable
public class StrangerPlantDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new StrangerPlantDialogue(player);
    }

    /**
     * Instantiates a new Stranger plant dialogue.
     */
    public StrangerPlantDialogue() {}

    /**
     * Instantiates a new Stranger plant dialogue.
     *
     * @param player the player
     */
    public StrangerPlantDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomChoice = (int) (Math.random() * 4);
        switch (randomChoice) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "I'M STRANGER PLANT!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "WILL WE HAVE TO BE HERE LONG?");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "DIIIIVE!");
                stage = 16;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I THINK I'M WILTING!");
                stage = 21;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "I know you are.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "I KNOW! I'M JUST SAYING!");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "Do you have to shout like that all of the time?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "WHO'S SHOUTING?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "If this is you speaking normally, I'd hate to hear you shouting.");
                stage++;
                break;

            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "OH, SNAP!");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "We'll be here until I am finished.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "BUT THERE'S NO DRAMA HERE!");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Well, how about you pretend to be an undercover agent.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "WONDERFUL! WHAT'S MY MOTIVATION?");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "You're trying to remain stealthy and secretive, while looking out for clues.");
                stage++;
                break;

            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "I'LL JUST GET INTO CHARACTER! AHEM!");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "PAPER! PAPER! VARROCK HERALD FOR SALE!");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.HALF_ASKING, "What kind of spy yells loudly like that?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "ONE WHOSE COVER IDENTITY IS A PAPER-SELLER, OF COURSE!");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Ask a silly question...");
                stage = END_DIALOGUE;
                break;

            case 16:
                playerl(FaceAnim.HALF_ASKING, "What? Help! Why dive?");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "OH, DON'T WORRY! I JUST LIKE TO YELL THAT FROM TIME TO TIME!");
                stage++;
                break;

            case 18:
                playerl(FaceAnim.HALF_ASKING, "Well, can you give me a little warning next time?");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "WHAT, AND TAKE ALL THE FUN OUT OF LIFE?");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "If by 'fun' you mean 'sudden heart attacks', then yes, please take them out of my life!");
                stage = END_DIALOGUE;
                break;

            case 21:
                playerl(FaceAnim.HALF_ASKING, "Do you need some water?");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.CHILD_NORMAL, "DON'T BE SILLY! I CAN PULL THAT OUT OF THE GROUND!");
                stage++;
                break;
            case 23:
                playerl(FaceAnim.HALF_ASKING, "Then why are you wilting?");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.CHILD_NORMAL, "IT'S SIMPLE: THERE'S A DISTINCT LACK OF DRAMA!");
                stage++;
                break;
            case 25:
                playerl(FaceAnim.HALF_ASKING, "Drama?");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.CHILD_NORMAL, "YES, DRAMA!");
                stage++;
                break;
            case 27:
                playerl(FaceAnim.FRIENDLY, "Okay...");
                stage++;
                break;
            case 28:
                playerl(FaceAnim.FRIENDLY, "Let's see if we can find some for you.");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.CHILD_NORMAL, "LEAD ON!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.STRANGER_PLANT_6827, NPCs.STRANGER_PLANT_6828 };
    }
}
