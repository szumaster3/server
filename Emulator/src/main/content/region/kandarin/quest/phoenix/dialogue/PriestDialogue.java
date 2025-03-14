package content.region.kandarin.quest.phoenix.dialogue;

import content.data.GameAttributes;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;
import org.rs.consts.Vars;

import static core.api.ContentAPIKt.*;
import static core.api.quest.QuestAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Priest dialogue.
 */
@Initializable
public class PriestDialogue extends Dialogue {

    /**
     * Instantiates a new Priest dialogue.
     */
    public PriestDialogue() {

    }

    /**
     * Instantiates a new Priest dialogue.
     *
     * @param player the player
     */
    public PriestDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761)) {
            case 1:
                npc(FaceAnim.FRIENDLY, "You've returned!");
                stage = 27;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                npc(FaceAnim.FRIENDLY, player.getUsername() + "! How is your quest to save the phoenix", "progressing?");
                stage = 111;
                break;
            case 10:
                npc(FaceAnim.FRIENDLY, player.getUsername() + "! I saw everything through a small vent in", "the roof of the phoenix roost! You have succeeded!");
                stage = 100;
                break;
            default:
                player(FaceAnim.FRIENDLY, "Hello, there.");
        }
        return true;
    }

    @Override
    public boolean handle(int componentID, int buttonID) {
        switch (stage) {
            case 0:
                npc(FaceAnim.SCARED, "Please, adventurer, I beg you. You must help her!");
                stage++;
                break;
            case 1:
                player(FaceAnim.FRIENDLY, "Woah, hold your unicorns! Help who?");
                stage++;
                break;
            case 2:
                npc(FaceAnim.HALF_GUILTY, "I...I'm sorry. My emotions have run away with me. ", "Allow me to explain.");
                stage++;
                break;
            case 3:
                npc(FaceAnim.HALF_GUILTY, "This cave is a place of great significance. Do you know", "why?");
                stage++;
                break;
            case 4:
                player(FaceAnim.FRIENDLY, "I can't say I do.");
                stage++;
                break;
            case 5:
                npc(FaceAnim.HALF_GUILTY, "It is the roost of the legendary firebird, the phoenix!");
                stage++;
                break;
            case 6:
                player(FaceAnim.FRIENDLY, "Really?");
                stage++;
                break;
            case 7:
                player(FaceAnim.FRIENDLY, "But wait, how is this related to who I have to help?");
                stage++;
                break;
            case 8:
                npc(FaceAnim.HALF_GUILTY, "It is related because it is the phoenix that is in need of", "your assistance.");
                stage++;
                break;
            case 9:
                player(FaceAnim.FRIENDLY, "You're starting to confuse me.");
                stage++;
                break;
            case 10:
                player(FaceAnim.FRIENDLY, "Perhaps you should take a deep breath and start from", "the beginning.");
                stage++;
                break;
            case 11:
                npc(FaceAnim.HALF_GUILTY, "Okay...*sigh*");
                stage++;
                break;
            case 12:
                npc(FaceAnim.HALF_GUILTY, "I have been studying the phoenix for some time now,", "out of curiosity and admiration.");
                stage++;
                break;
            case 13:
                npc(FaceAnim.HALF_GUILTY, "While not truly immortal, the phoenix has the capacity", "to live forever if certain...conditions...are met.");
                stage++;
                break;
            case 14:
                player(FaceAnim.FRIENDLY, "Conditions?");
                stage++;
                break;
            case 15:
                npc(FaceAnim.HALF_GUILTY, "The phoenix has a natural lifespan of 500 years. There", "is a set ritual it must go through when its life is ending,", "in order for it to be reborn and live for another five", "centuries.");
                stage++;
                break;
            case 16:
                npc(FaceAnim.HALF_GUILTY, "The end of the phoenix's current lifespan is nigh and it", "has returned to its roost to complete this ritual. I came", "here to witness this once in a lifetime event.");
                stage++;
                break;
            case 17:
                player(FaceAnim.FRIENDLY, "So, the phoenix has returned to its roost and you came", "to watch it be reborn. I fail to see the problem.");
                stage++;
                break;
            case 18:
                npc(FaceAnim.HALF_GUILTY, "The phoenix was gravely wounded on its way back to", "its roost. It barely managed to reach its lair.");
                stage++;
                break;
            case 19:
                npc(FaceAnim.HALF_GUILTY, "It now rests there, slowly slipping from this world and", "soon it will be gone forever, incapable of completing its", "rebirth ritual owing to its wounds.");
                stage++;
                break;
            case 20:
                player(FaceAnim.FRIENDLY, "A tragic tale, but what can I do about it?");
                stage++;
                break;
            case 21:
                npc(FaceAnim.HALF_GUILTY, "I know the ritual that must be completed. To save the", "phoenix, you must go into the cave and perform it.");
                stage++;
                break;
            case 22:
                npc(FaceAnim.HAPPY, "Completing the ritual before the phoenix takes its last", "breath will ensure its revival, and continue the", "potentially eternal life of this magnificent beast.");
                stage++;
                break;
            case 23:
                player(FaceAnim.FRIENDLY, "If you know the ritual, why can't you venture forth", "and help the phoenix yourself?");
                stage++;
                break;
            case 24:
                npc(FaceAnim.HALF_GUILTY, "Because I am an old man whose mind and fingers lack", "the necessary skill to complete the ritual...");
                stage++;
                break;
            case 25:
                npc(FaceAnim.HALF_GUILTY, "...and whose body lacks the necessary heat-resistance.");
                stage++;
                break;
            case 26:
                player(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 27:
                npc(FaceAnim.HALF_GUILTY, "The task I ask of you will not take long, and you will", "be well rewarded for your efforts. Will you please", "complete the phoenix's ritual?");
                stage++;
                break;
            case 28:
                options("Yes, I will help.", "No, I have other things to do.");
                stage++;
                break;
            case 29:
                switch (buttonID) {
                    case 1:
                        player("Yes, I will help.");
                        setAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0);
                        setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 1, true);
                        setQuestStage(player, Quests.IN_PYRE_NEED, 2);
                        stage = 31;
                        break;
                    case 2:
                        player("No, I have other things to do.");
                        stage++;
                        break;
                }
                break;
            case 30:
                npc(FaceAnim.STRUGGLE, "Well, I can't force you. If you change your mind, hurry back. The phoenix's life could fade at any moment.");
                stage++;
                break;
            case 31:
                npc(FaceAnim.HAPPY, "You'll help? Oh, praise be to Guthix! I knew I could", "trust you from the moment I saw you.");
                stage++;
                break;
            case 32:
                npc(FaceAnim.FRIENDLY, "What is your name, adventurer?");
                stage++;
                break;
            case 33:
                player(FaceAnim.FRIENDLY, "You can call me " + player.getUsername() + ".");
                stage++;
                break;
            case 34:
                npc(FaceAnim.FRIENDLY, "Well, " + player.getUsername() + ", it's nice to meet you. No doubt you", "have questions about the task at hand.");
                stage++;
                break;
            case 35:
                npc(FaceAnim.FRIENDLY, "What would you like to ask about?");
                stage++;
                break;
            case 36:
                options("The phoenix.", "The ritual.", "The phoenix's lair.", "No more questions.");
                stage++;
                break;
            case 37:
                switch (buttonID) {
                    case 1:
                        player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix?");
                        player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST);
                        stage++;
                        break;
                    case 2:
                        player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix's rebirth ritual?");
                        player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST);
                        stage = 44;
                        break;
                    case 3:
                        player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix's lair?");
                        player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST);
                        stage = 72;
                        break;
                    case 4:
                        player("That's all the information I need for now.");
                        stage = 79;
                        break;
                }
                break;
            case 38:
                npc(FaceAnim.FRIENDLY, "The phoenix is an ancient creature - older than either", "of us can comprehend.");
                stage++;
                break;
            case 39:
                npc(FaceAnim.FRIENDLY, "It is a large, powerful avian creature, surrounded by", "flames and covered in a fiery plumage. It also has", "strong magical abilities.");
                stage++;
                break;
            case 40:
                npc(FaceAnim.FRIENDLY, "Every five hundred years, as it reaches the limits of its", "lifespan, it returns to its roost to complete a ritual and", "be reborn.");
                stage++;
                break;
            case 41:
                npc(FaceAnim.FRIENDLY, "You see, while not truly immortal, the phoenix can be", "reborn any number of times, so long as it completes its", "rebirth ritual.");
                stage++;
                break;
            case 42:
                npc(FaceAnim.SAD, "But, thanks to its wounds, the phoenix could soon be", "gone from this world forever.");
                stage++;
                break;
            case 43:
                player(FaceAnim.HALF_ASKING, "Thanks for the information. Now, back to my other", "questions.");
                stage = 35;
                break;
            case 44:
                npc(FaceAnim.FRIENDLY, "The ritual is not that complicated, really; it just requires", "certain ingredients and a set of dexterous hands.");
                stage++;
                break;
            case 45:
                npc(FaceAnim.FRIENDLY, "In order to be reborn, the phoenix must be burnt alive", "on a funeral pyre woven from the wood of five", "particular trees.");
                stage++;
                break;
            case 46:
                npc(FaceAnim.FRIENDLY, "You must weave a basket from the fletched twigs of", "cinnamon, sassafras, ailanthus, cedar and mastic trees.");
                stage++;
                break;
            case 47:
                npc(FaceAnim.FRIENDLY, "This basket must be placed on the base of the pyre in", "the phoenix's roost, and then it must be lit.");
                stage++;
                break;
            case 48:
                npc(FaceAnim.FRIENDLY, "Usually, you would also need to best the phoenix in", "combat to weaken it, so that it can succumb to the", "flames.");
                stage++;
                break;
            case 49:
                npc(FaceAnim.FRIENDLY, "This time, however, the phoenix is already weak and", "dying.");
                stage++;
                break;
            case 50:
                npc(FaceAnim.FRIENDLY, "The phoenix will enter the pyre and be burnt to ashes.", "From these ashes, it will be reborn - young and strong", "again.");
                stage++;
                break;
            case 51:
                if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) < 2) {
                    // First time
                    player("Oh, I see. I need to search the four corners of the", "world for five trees. Well, I suppose I'd best get", "packing-");
                    stage++;
                    break;
                } else {
                    // Second time
                    npc(FaceAnim.HAPPY, "There are specimens of each tree growing inside the phoenix's lair, tended to by the phoenix's thralls in its absence and revived from the trees ashes whenever they die.");
                    stage++;
                    break;
                }
            case 52:
                npc(FaceAnim.HAPPY, "While I understand your concern, that won't be a", "problem at all.");
                stage++;
                break;
            case 53:
                player("How so?");
                stage++;
                break;
            case 54:
                npc(FaceAnim.FRIENDLY,"As I have said, the phoenix is an ancient creature with", "some limited power over life and death.");
                stage++;
                break;
            case 55:
                npc(FaceAnim.FRIENDLY,"The trees you require are growing inside its very lair,", "and have been for millenia! The phoenix revives them", "from their own ashes each time they die.");
                stage++;
                break;
            case 56:
                player("That's amazing. What a foresighted creature the", "phoenix is!");
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 2, true);
                stage = 58;
                break;

            // Continue
            case 58:
                npc(FaceAnim.FRIENDLY,"Each tree is on a separate level of the lair. You must", "collect the twigs before moving on. It's pointless to", "continue on to a deeper level without having the twigs", "from all the levels before it.");
                stage++;
                break;
            case 59:
                npc(FaceAnim.FRIENDLY,"Only fresh twigs will suffice for the ritual. If you leave", "the lair at any time, you will have to go through and", "collect fresh twigs from every tree.");
                stage++;
                break;
            case 60:
                npc(FaceAnim.FRIENDLY,"There are some other items required for the ritual that", "you cannot get from inside the lair.");
                stage++;
                break;
            case 61:
                player("Oh? What would those be then?");
                stage++;
                break;
            case 62:
                npc(FaceAnim.FRIENDLY,"Tools. Secateurs, to prune the trees and gather the", "twigs, a knife to fletch them and a tinderbox to light the", "pyre.");
                stage++;
                break;
            case 63:
                npc(FaceAnim.SAD, "Given the circumstances, I'd be happy to give you the", "necessary tools for free. I brought a set to complete the", "ritual myself, but...I'm too old.");
                stage++;
                break;
            case 64:
                options("Yes, please give me the tools I need.", "No thank you, I will gather the tools myself.");
                stage++;
                break;
            case 65:
                switch (buttonID) {
                    case 1:
                        player(FaceAnim.FRIENDLY, "Yes, please give me the tools I need.");
                        stage++;
                        break;
                    case 2:
                        player(FaceAnim.FRIENDLY, "No thank you, I will gather the tools myself.");
                        stage = 71;
                        break;
                }
                break;
            case 66:
                sendItemDialogue(player, Items.SECATEURS_5329, "The priest hands you some secateurs.");
                addItemOrDrop(player, Items.SECATEURS_5329, 1);
                stage++;
                break;
            case 67:
                sendItemDialogue(player, Items.KNIFE_946, "The priest hands you a knife.");
                addItemOrDrop(player, Items.KNIFE_946, 1);
                stage++;
                break;
            case 68:
                sendItemDialogue(player, Items.TINDERBOX_590, "The priest hands you a tinderbox.");
                addItemOrDrop(player, Items.TINDERBOX_590, 1);
                stage++;
                break;
            case 69:
                npc(FaceAnim.FRIENDLY,"You should now have all the tools you need for the", "ritual");
                stage++;
                break;
            case 70:
                player("Thank you!");
                stage++;
                break;
            case 71:
                player("Now, back to my other questions.");
                stage = 35;
                break;
            case 72:
                npc(FaceAnim.FRIENDLY,"The phoenix's lair is a lava-filled cave, guarded by its", "thralls.");
                stage++;
                break;
            case 73:
                npc(FaceAnim.FRIENDLY,"You see, the phoenix has power over life and death that", "is not limited to itself. It has the ability to resurrect", "lesser creatures.");
                stage++;
                break;
            case 74:
                npc(FaceAnim.FRIENDLY,"These revived creatures now roam the phoenix's lair,", "guarding it from interlopers during the phoenix's long", "absences.");
                stage++;
                break;
            case 75:
                npc(FaceAnim.FRIENDLY,"They usually attack anyone who enters, weeding out", "the weaklings to ensure only the worthy reach the", "phoenix in its roost, deep inside the lair.");
                stage++;
                break;
            case 76:
                npc(FaceAnim.FRIENDLY,"I have had a peek inside, however, and the creatures", "seem unusually passive at the moment.");
                stage++;
                break;
            case 77:
                npc(FaceAnim.SAD, "Standing out here, I occasionally hear them cry out, as", "if in pain.");
                stage++;
                break;
            case 78:
                player("Thanks for the information. Now, back to my other", "questions.");
                stage = 35;
                break;
            case 79:
                npc(FaceAnim.HALF_GUILTY,"Please, help the phoenix. Such a wondrous creature", "should not be taken from this world in this way.");
                stage++;
                break;
            case 80:
                player("I'll try my best. I'll be back soon.");
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 4, true);
                stage = END_DIALOGUE;
                break;
            case 81:
                player(FaceAnim.HALF_ASKING, "I'm a little unsure on what I have to do next. Could I ask you some questions?");
                stage = 35;
                break;
            case 100:
                player("Yes, I have!");
                stage++;
                break;
            case 101:
                npc(FaceAnim.FRIENDLY,"The phoenix is saved and you, " + player.getUsername() + ", are a hero", "of nature! As an added bonus, I got to see it. My 50", "years of waiting were not in vain after all!");
                stage++;
                break;
            case 102:
                player("Yes, I'm glad you watched all my hard work from a", "comfortable vantage point outside of the lair.");
                stage++;
                break;
            case 103:
                npc(FaceAnim.SAD, "Come now, Player. It wasn't like that. I'm a frail", "old man! What possible good could I have done?");
                stage++;
                break;
            case 104:
                player("I suppose you're right.");
                stage++;
                break;
            case 105:
                npc(FaceAnim.FRIENDLY,"Besides, the most important point is that the phoenix is", "saved!");
                stage++;
                break;
            case 106:
                player("Oh, that reminds me – she also said to thank you for", "the shrine, and asks when you plan to return her", "trinkets.");
                stage++;
                break;
            case 107:
                npc(FaceAnim.FRIENDLY,"She...she knew about me?");
                stage++;
                break;
            case 108:
                npc(FaceAnim.SAD, "I should have known. Had I not been welcome in the", "lair, I would have ended up like the rest of them. I was", "a fool to think it was my skill that kept me alive.");
                stage++;
                break;
            case 109:
                npc(FaceAnim.FRIENDLY,"Anyway, " + player.getUsername() + ", I wouldn't expect you to help for", "mere thanks. Here is your reward!");
                stage++;
                break;
            case 110:
                end();
                finishQuest(player, Quests.IN_PYRE_NEED);
                updateQuestTab(player);
                break;
            case 111:
                playerl(FaceAnim.HALF_ASKING, "I'm a little unsure on what I have to do next. Could I ask you some questions?");
                stage = 36;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.PRIEST_OF_GUTHIX_8555};
    }
}
