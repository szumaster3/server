package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Unicorn stallion dialogue.
 */
@Initializable
public class UnicornStallionDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new UnicornStallionDialogue(player);
    }

    /**
     * Instantiates a new Unicorn stallion dialogue.
     */
    public UnicornStallionDialogue() {}

    /**
     * Instantiates a new Unicorn stallion dialogue.
     *
     * @param player the player
     */
    public UnicornStallionDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomChoice = (int) (Math.random() * 5);
        switch (randomChoice) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Neigh neigh neighneigh snort?", "(Isn't everything so awesomely wonderful?)");
                stage = 0;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Whicker whicker. Neigh, neigh, whinny.", "(I feel so, like, enlightened. Let's meditate and enhance our auras.)");
                stage = 5;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Whinny whinny whinny.", "(I think I'm astrally projecting.)");
                stage = 7;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Whinny, neigh!", "(Oh, happy day!)");
                stage = 9;
                break;
            case 4:
                npc(FaceAnim.CHILD_NORMAL, "Whicker snort! Whinny whinny whinny.", "(You're hurt! Let me try to heal you.)");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Neigh neigh neighneigh snort?", "(Isn't everything so awesomely wonderful?)");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.ASKING, "Err...yes?");
                stage++;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Whicker whicker snuffle.", "(I can see you're not tuning in, Player name.)");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "No, no, I'm completely at one with...you know...everything.");
                stage++;
                break;
            case 4:
                npc(FaceAnim.CHILD_NORMAL, "Whicker!", "(Cosmic.)");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.FRIENDLY, "I can't do that! I barely even know you.");
                stage++;
                break;
            case 6:
                npc(FaceAnim.CHILD_NORMAL, "Whicker...", "(Bipeds...)");
                stage = END_DIALOGUE;
                break;

            case 7:
                playerl(FaceAnim.HALF_ASKING, "Okay... Hang on. Seeing as I summoned you here, wouldn't that mean you are physically projecting instead?");
                stage++;
                break;
            case 8:
                npc(FaceAnim.CHILD_NORMAL, "Whicker whicker whicker.", "(You're, like, no fun at all, man.)");
                stage = END_DIALOGUE;
                break;

            case 9:
                playerl(FaceAnim.HALF_ASKING, "Happy day? Is that some sort of holiday or something?");
                stage++;
                break;
            case 10:
                npc(FaceAnim.CHILD_NORMAL, "Snuggle whicker", "(Man, you're totally, like, uncosmic, " + player.getUsername() + ".)");
                stage = END_DIALOGUE;
                break;

            case 11:
                playerl(FaceAnim.FRIENDLY, "Yes, please do!");
                stage++;
                break;
            case 12:
                npc(FaceAnim.CHILD_NORMAL, "Snuffle whicker whicker neigh neigh...", "(Okay, we'll begin with acupuncture and some reiki, then I'll get my crystals...)");
                stage++;
                break;

            case 13:
                playerl(FaceAnim.FRIENDLY, "Or you could use some sort of magic...like the other unicorns...");
                stage++;
                break;

            case 14:
                npc(FaceAnim.CHILD_NORMAL, "Whicker whinny whinny neigh.", "(Yes, but I believe in alternative medicine.)");
                stage++;
                break;

            case 15:
                playerl(FaceAnim.FRIENDLY, "Riiight. Don't worry about it, then; I'll be fine.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.UNICORN_STALLION_6822, NPCs.UNICORN_STALLION_6823 };
    }
}
