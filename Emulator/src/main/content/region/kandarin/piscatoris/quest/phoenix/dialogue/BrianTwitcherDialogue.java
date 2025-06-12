package content.region.kandarin.piscatoris.quest.phoenix.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Brian twitcher dialogue.
 */
// TODO: Find varbit to swap Priest with him.
@Initializable
public class BrianTwitcherDialogue extends Dialogue {

    /**
     * Instantiates a new Brian twitcher dialogue.
     */
    public BrianTwitcherDialogue() {

    }

    /**
     * Instantiates a new Brian twitcher dialogue.
     *
     * @param player the player
     */
    public BrianTwitcherDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npcl(FaceAnim.NEUTRAL, "Hail, " + player.getUsername() + "! How are things?");
        return true;
    }

    @Override
    public boolean handle(int componentID, int buttonID) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.NEUTRAL, "I'm doing okay, thanks. Could I ask you some questions?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.FRIENDLY, "What would you like to ask about?");
                stage++;
                break;
            case 2:
                options(
                        "The phoenix.",
                        "The ritual.",
                        "The phoenix's lair.",
                        "Going back inside.",
                        "No more questions."
                );
                stage++;
                break;
            case 3:
                switch (buttonID) {
                    case 1:
                        npcl(FaceAnim.FRIENDLY, "The phoenix is an ancient creature - older than either of us can comprehend.");
                        stage++;
                        break;

                    case 2:
                        npcl(FaceAnim.FRIENDLY, "The ritual is not that complicated, really; it just requires certain ingredients and a set of dexterous hands.");
                        stage = 9;
                        break;

                    case 3:
                        npcl(FaceAnim.FRIENDLY, "The phoenix's lair is a lava-filled cave, guarded by its thralls.");
                        stage = 22;
                        break;

                    case 4:
                        npcl(FaceAnim.FRIENDLY, "Yes, she told me about this a short while ago. I was quite shocked; I didn't realise she was telepathic!");
                        stage = 28;
                        break;

                    case 5:
                        npcl(FaceAnim.FRIENDLY, "Good day, " + player.getUsername() + ", saviour of the phoenix, hero of Guthix!");
                        stage = 39;
                        break;
                }
                break;
            case 4:
                npcl(FaceAnim.FRIENDLY, "It is a large, powerful avian creature, surrounded by flames and covered in a fiery plumage. It also has strong magical abilities.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.FRIENDLY, "Every five hundred years, as it reaches the limits of its lifespan, it returns to its roost to complete a ritual and be reborn.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.FRIENDLY, "You see, while not truly immortal, the phoenix can be reborn any number of times, so long as it completes its rebirth ritual.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.FRIENDLY, "Thanks to you, the phoenix will live on!");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.SUSPICIOUS, "Thanks for the information. Now, back to my other questions.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.FRIENDLY, "In order to be reborn, the phoenix must be burnt alive on a funeral pyre woven from the wood of five particular trees.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.FRIENDLY, "You must weave a basket from the fletched twigs of cinnamon, sassafras, ailanthus, cedar and mastic trees.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.FRIENDLY, "This basket must be placed on the base of the pyre in the phoenix's roost, and then it must be lit.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.FRIENDLY, "You will need to best the phoenix in combat to weaken it so that it can succumb to the flames.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.FRIENDLY, "The phoenix will enter the pyre and be burnt to ashes. From these ashes, it will be reborn - young and strong again.");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.FRIENDLY, "There are specimens of each tree growing inside the phoenix's lair, tended to by the phoenix's thralls in its absence and revived from the trees' ashes whenever they die.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.FRIENDLY, "Each tree is on a separate level of the lair. You must collect the twigs before moving on. It's pointless to continue on to a deeper level without having the twigs from all the levels before it.");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.FRIENDLY, "Only fresh twigs will suffice for the ritual. If you leave the lair at any time, you will have to go through and collect fresh twigs from every tree.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.FRIENDLY, "There are some other items required for the ritual that you cannot get from inside the lair.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.SUSPICIOUS, "Oh? What would those be then?");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.FRIENDLY, "Tools. Secateurs, to prune the trees and gather the twigs, a knife to fletch them and a tinderbox to light the pyre.");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.FRIENDLY, "I did bring a bunch of the necessary tools with me, but I sold them to a passing trader once the phoenix was saved...");
                stage++;
                break;
            case 21:
                playerl(FaceAnim.HAPPY, "Thanks for the information. Now, back to my other questions.");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.FRIENDLY, "The phoenix's lair is a lava-filled cave, guarded by its thralls.");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.FRIENDLY, "You see, the phoenix has power over life and death that is not limited to itself. It has the ability to resurrect lesser creatures.");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.FRIENDLY, "These revived creatures now roam the phoenix's lair, guarding it from interlopers during the phoenix's long absences.");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.FRIENDLY, "They attack all who enter, to weed out the weaklings and ensure only the worthy reach the phoenix in its roost, deep inside the cave.");
                stage++;
                break;
            case 26:
                playerl(FaceAnim.HAPPY, "Thanks for the information. Now, back to my other questions.");
                stage++;
                break;
            case 27:
                npcl(FaceAnim.FRIENDLY, "What would you like to ask about?");
                stage = 2;
                break;
            case 28:
                playerl(FaceAnim.THINKING, "I see. I'm afraid I've forgotten the terms of our agreement.");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.FRIENDLY, "Worry not, " + player.getUsername() + "! I shall remind your tired brain.");
                stage++;
                break;
            case 30:
                npcl(FaceAnim.FRIENDLY, "Once a day, you may enter into the phoenix's lair, and if you make it to the phoenix's roost you may challenge her to combat.");
                stage++;
                break;
            case 31:
                npcl(FaceAnim.FRIENDLY, "You'll need to complete the same ritual as you did when you first helped the phoenix in order for her to appear before you; she will only allow you to fight her if her rebirth is assured.");
                stage++;
                break;
            case 32:
                npcl(FaceAnim.FRIENDLY, "If you defeat the phoenix, she will give you another five quills, which you can use to create Summoning pouches for summoning the phoenix's essence.");
                stage++;
                break;
            case 33:
                npcl(FaceAnim.FRIENDLY, "You should take care on your way through the lair, though; the phoenix's guardians will try their best to defeat you now that their mistress is safe.");
                stage++;
                break;
            case 34:
                npcl(FaceAnim.FRIENDLY, "Once the duel with the phoenix is over and you have said your farewells, you must leave the lair and you will be unable to return until the next day.");
                stage++;
                break;
            case 35:
                npcl(FaceAnim.FRIENDLY, "Immediately after rebirth, the phoenix is small and exhausted. It will quickly regain its former size and strength though, and this is the reason for the day's respite.");
                stage++;
                break;
            case 36:
                npcl(FaceAnim.FRIENDLY, "I think that's everything.");
                stage++;
                break;
            case 37:
                playerl(FaceAnim.HAPPY, "Yes, you covered everything, and in detail. Thank you. Now, back to my other questions.");
                stage++;
                break;
            case 38:
                npcl(FaceAnim.FRIENDLY, "What would you like to ask about?");
                stage = 2;
                break;
            case 39:
                playerl(FaceAnim.FRIENDLY, "Farewell!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BRIAN_TWITCHER_8556};
    }
}
