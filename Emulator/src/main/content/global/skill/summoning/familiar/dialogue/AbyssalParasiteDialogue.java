package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import java.util.Random;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Abyssal parasite dialogue.
 */
@Initializable
public class AbyssalParasiteDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new AbyssalParasiteDialogue(player);
    }

    /**
     * Instantiates a new Abyssal parasite dialogue.
     */
    public AbyssalParasiteDialogue() {}

    /**
     * Instantiates a new Abyssal parasite dialogue.
     *
     * @param player the player
     */
    public AbyssalParasiteDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        Random random = new Random();
        int randomIndex = random.nextInt(5);

        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Ongk n'hd?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Noslr'rh...");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Ace'e e ur'y!");
                stage = 9;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Tdsa tukk!");
                stage = 10;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Tdsa tukk!");
                stage = 12;
                break;
        }

        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_WORRIED, "Oh, I'm not feeling so well.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Uge f't es?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.SAD, "Please have mercy!");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "F'tp ohl't?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.AFRAID, "I shouldn't have eaten that kebab. Please stop talking!");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.HALF_ASKING, "What's the matter?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Kdso Seo...");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.HALF_ASKING, "Could you...could you mime what the problem is?");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Yiao itl!");
                stage++;
                break;

            case 9:
                playerl(FaceAnim.HALF_ASKING, "I want to help it but, aside from the language gap its noises make me retch!");
                stage = END_DIALOGUE;
                break;

            case 10:
                playerl(FaceAnim.HALF_WORRIED, "I think I'm going to be sick... The noises! Oh, the terrifying noises.");
                stage = END_DIALOGUE;
                break;

            case 11:
                playerl(FaceAnim.AFRAID, "Oh, the noises again.");
                stage = END_DIALOGUE;
                break;

            case 12:
                playerl(FaceAnim.AFRAID, "Oh, the noises again.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Hem s'htee?");
                stage = END_DIALOGUE;
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.ABYSSAL_PARASITE_6818, NPCs.ABYSSAL_PARASITE_6819};
    }
}
