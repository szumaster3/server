package content.minigame.castlewars.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.openNpcShop;
import static core.api.ContentAPIKt.sendMessage;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * Represents the Lanthus dialogue.
 */
@Initializable
public class LanthusDialogue extends Dialogue {

    /**
     * Instantiates a new Lanthus dialogue.
     */
    public LanthusDialogue() {
    }

    /**
     * Instantiates a new Lanthus dialogue.
     *
     * @param player the player
     */
    public LanthusDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 1:
                options("What is this place?", "What do you have for trade?", "Do you have a manual? I'd like to learn how to play!");
                stage++;
                break;

            case 2:
                switch (buttonId) {
                    case 1:
                        player("What is this place?");
                        stage = 4;
                        break;
                    case 2:
                        openNpcShop(player, NPCs.LANTHUS_1526);
                        stage = END_DIALOGUE;
                        break;
                    case 3:
                        player("Do you have a manual? I'd like to learn how to play!");
                        stage = 50;
                        break;
                }
                break;

            case 4:
                npcl(FaceAnim.FRIENDLY, "This is the great Castle Wars arena! Here you can fight for the glory of Saradomin or Zamorak.");
                stage++;
                break;

            case 5:
                options("Really, how do I do that?", "Are there any rules?", "What can I win?");
                stage++;
                break;

            case 6:
                switch (buttonId) {
                    case 1:
                        player("Really, how do I do that?");
                        stage = 7;
                        break;
                    case 2:
                        player("Are there any rules?");
                        stage = 80;
                        break;
                    case 3:
                        player("What can I win?");
                        stage = 90;
                        break;
                }
                break;

            case 7:
                npcl(FaceAnim.FRIENDLY, "Easy, you just step through one of the three portals. To join Zamorak, pass through the red portal. To join Saradomin, pass through the blue portal. If you don't mind then pass through the green portal.");
                stage++;
                break;

            case 8:
                options("Are there any rules?", "What can I win?", "What do you have for trade?", "Do you have a manual? I'd like to learn how to play!");
                stage++;
                break;

            case 9:
                switch (buttonId) {
                    case 1:
                        player("Are there any rules?");
                        stage = 80;
                        break;
                    case 2:
                        player("What can I win?");
                        stage = 90;
                        break;
                    case 3:
                        openNpcShop(player, NPCs.LANTHUS_1526);
                        stage = END_DIALOGUE;
                        break;
                    case 4:
                        player("Do you have a manual? I'd like to learn how to play!");
                        stage = 50;
                        break;
                }
                break;

            case 80:
                npcl(FaceAnim.FRIENDLY, "Of course, there are always rules. Firstly you can't wear a cape as you enter the portal, you'll be given your team colours to wear while in the arena. You're also prohibited from taking non-combat related items in.");
                stage++;
                break;

            case 81:
                npcl(FaceAnim.FRIENDLY, "with you. So you should only have equipment, potions, and runes on you. Secondly, attacking your own team or your team's defences isn't allowed. You don't want to be angering");
                stage++;
                break;

            case 82:
                npcl(FaceAnim.FRIENDLY, "your patron god, do you? Other than that, just have fun and enjoy it!");
                stage++;
                break;

            case 50:
                npcl(FaceAnim.FRIENDLY, "Sure, here you go.");
                player.getInventory().add(new Item(Items.CASTLEWARS_MANUAL_4055));
                stage = END_DIALOGUE;
                break;

            case 83:
                player("Great! Oh, how do I win the game?");
                stage++;
                break;

            case 84:
                npcl(FaceAnim.FRIENDLY, "The aim is to get into your opponents' castle and take their team standard. Then bring that back and capture it on your team's standard.");
                stage++;
                break;

            case 85:
                options("What can I win?", "What do you have to trade?", "Do you have a manual? I'd like to learn how to play!");
                stage++;
                break;

            case 86:
                switch (buttonId) {
                    case 1:
                        player("What can I win?");
                        stage = 90;
                        break;
                    case 2:
                        openNpcShop(player, NPCs.LANTHUS_1526);
                        stage = END_DIALOGUE;
                        break;
                    case 3:
                        player("Do you have a manual? I'd like to learn how to play!");
                        stage = 50;
                        break;
                }
                break;

            case 90:
                npcl(FaceAnim.FRIENDLY, "Players on the winning team will receive 2 Castle Wars Tickets which you can trade back to me for other items. In the event of a draw every player will get 1 ticket.");
                stage++;
                break;

            case 91:
                options("Are there any rules?", "What do you have to trade?", "Do you have a manual? I'd like to learn how to play!");
                stage++;
                break;

            case 92:
                switch (buttonId) {
                    case 1:
                        player("Are there any rules?");
                        stage = 80;
                        break;
                    case 2:
                        openNpcShop(player, NPCs.LANTHUS_1526);
                        stage = END_DIALOGUE;
                        break;
                    case 3:
                        player("Do you have a manual? I'd like to learn how to play!");
                        stage = 50;
                        break;
                }
                break;

            default:
                sendMessage(player, "Error - unknown stage " + stage);
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new LanthusDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.LANTHUS_1526};
    }
}
