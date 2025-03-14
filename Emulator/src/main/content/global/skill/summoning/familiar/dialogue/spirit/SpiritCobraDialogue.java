package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import java.util.Random;

import static core.api.ContentAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit cobra dialogue.
 */
@Initializable
public class SpiritCobraDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritCobraDialogue(player);
    }

    /**
     * Instantiates a new Spirit cobra dialogue.
     */
    public SpiritCobraDialogue() {}

    /**
     * Instantiates a new Spirit cobra dialogue.
     *
     * @param player the player
     */
    public SpiritCobraDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inInventory(player, Items.RING_OF_CHAROSA_6465, 1) || inEquipment(player, Items.RING_OF_CHAROSA_6465, 1)) {
            npcl(FaceAnim.OLD_NORMAL, "You are under my power!");
            stage = 20;
            return true;
        }

        int randomIndex = new Random().nextInt(5);
        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "Do we have to do thissss right now?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "You are feeling ssssleepy...");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "I'm bored, do ssssomething to entertain me...");
                stage = 11;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Your will is my command...");
                stage = 13;
                break;
            case 4:
                npcl(FaceAnim.OLD_NORMAL, "I am king of the world!");
                stage = 15;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Yes, I'm afraid so.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "You are under my sssspell...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I will do as you ask...");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "Do we have to do thissss right now?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Not at all, I had just finished!");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "I am feeling sssso ssssleepy...");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.OLD_NORMAL, "You will bring me lotssss of sssstuff!");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "What ssssort of sssstuff?");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.OLD_NORMAL, "What ssssort of sssstuff have you got?");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "All kindsss of sssstuff.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.OLD_NORMAL, "Then just keep bringing sssstuff until I'm ssssatissssfied!");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Errr, I'm not here to entertain you, you know.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.OLD_NORMAL, "You will do as I assssk...");
                stage = END_DIALOGUE;
                break;
            case 13:
                npcl(FaceAnim.OLD_NORMAL, "I'm bored, do ssssomething to entertain me...");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "I'll dance for you!");
                end();
                animate(player, Emotes.DANCE, false);
                stage = END_DIALOGUE;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "You know, I think there is a law against snakes being the king.");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.OLD_NORMAL, "My will is your command...");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "I am yours to command...");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.OLD_NORMAL, "I am king of the world!");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "All hail King Serpentor!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_COBRA_6802, NPCs.SPIRIT_COBRA_6803};
    }
}
