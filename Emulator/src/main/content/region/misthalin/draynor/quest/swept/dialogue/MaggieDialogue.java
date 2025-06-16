package content.region.misthalin.draynor.quest.swept.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;
import org.rs.consts.Vars;

import static core.api.ContentAPIKt.*;
import static core.api.quest.QuestAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * Represents the Maggie dialogue.
 */
@Initializable
public class MaggieDialogue extends Dialogue {

    private boolean hasBroomstick = false;

    /**
     * Instantiates a new Maggie dialogue.
     */
    public MaggieDialogue() {

    }

    /**
     * Instantiates a new Maggie dialogue.
     *
     * @param player the player
     */
    public MaggieDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        hasBroomstick = hasAnItem(player, Items.BROOMSTICK_14057).getContainer() != null;
        if (!hasBroomstick && isQuestComplete(player, Quests.SWEPT_AWAY)) {
            npcl(FaceAnim.FRIENDLY, "First things first. Where's that broomstick that I gave you?");
            stage = 1;
        } else {
            npc(FaceAnim.FRIENDLY, "Welcome, traveller! What can I do for you, this fine", "day, cha?");
            stage = 5;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 1:
                player("Uh, actually, I'm not sure where it is.");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.FRIENDLY, "Quite. That's because I have it right here. You're just lucky that someone came across it in their wanderings and thought to return it.");
                stage++;
                break;
            case 3:
                player.getInventory().add(new Item(Items.BROOMSTICK_14057));
                npcl(FaceAnim.FRIENDLY, "Here you go, but please look after it from now on. This is a pretty special little jobbie.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.FRIENDLY, "Now that we have that out of the way, what can I do for you, this fine day, cha?");
                stage = 5;
                break;
            case 5:
                if (getQuestStage(player, Quests.SWEPT_AWAY) < 3) {
                    options("Oo, something smells rather...interesting!", "Your house appears to be on wheels.", "So, about those oxen.");
                } else if (getQuestStage(player, Quests.SWEPT_AWAY) >= 4) {
                    options("I've stirred the cauldron for you.", "Your house appears to be on wheels.", "So, about those oxen.");
                } else {
                    options("I have good news for you.", "Your house appears to be on wheels.", "So, about those oxen.");
                }
                stage = 10;
                break;

            case 10:
                switch (buttonId) {
                    case 1:
                        if (getQuestStage(player, Quests.SWEPT_AWAY) < 3) {
                            playerl(FaceAnim.HALF_GUILTY, "Oo, something smells rather...interesting around here.");
                            stage = 20;
                        } else if (getQuestStage(player, Quests.SWEPT_AWAY) >= 4) {
                            player("I've stirred the caudron for you - that stuff has a kick", "to it.");
                            stage = 80;
                        } else {
                            player(FaceAnim.HAPPY, "I have good news for you.");
                            stage = 62;
                        }
                        break;
                    case 2:
                        playerl(FaceAnim.HALF_GUILTY, "Your house appears to be on wheels.");
                        stage = 50;
                        break;
                    case 3:
                        playerl(FaceAnim.HALF_GUILTY, "So, about those oxen.");
                        stage = 60;
                        break;
                }
                break;

            case 20:
                npc("Cha! That would be the cauldron, that would.", "I'm brewing up a mean batch of good stuff, don'tcha", "know.");
                stage++;
                break;
            case 21:
                player("What's that then?");
                stage++;
                break;
            case 22:
                npc("Oh, it'll be the most top-notch, bees-knees, sock-off-", "knockin' premium batcheroo you ever did see. But, such", "an extraordinary brew isn't easy to make, don'tchaknow.");
                stage++;
                break;
            case 23:
                npc("What it really needs is some sparkle - some pizzazz.", "Some of the old razzle-dazzle.");
                stage++;
                break;
            case 24:
                player("Razzle-dazzle?");
                stage++;
                break;
            case 25:
                npc("Classic, old-school enchantment, that's what I'm talking", "about, cha! Without a bit of witchly charm, this slop will", "be about as useful as an axe made out of toadstools.");
                stage++;
                break;
            case 26:
                npc(FaceAnim.SAD, "Only thing is, ol' Babe over there has the sniffles, so", "I'm not sure I'm going to be able to finish it off. I", "don't want to leave her alone in this condition.");
                stage++;
                break;
            case 27:
                options("Is there any way I could help?", "Well, good luck with that.", "But aren't you a witch?");
                stage++;
                break;

            case 28:
                switch (buttonId) {
                    case 1:
                        playerl(FaceAnim.HALF_GUILTY, "Is there any way I could help?");
                        stage = 31;
                        break;
                    case 2:
                        playerl(FaceAnim.HALF_ASKING, "Well, good luck with that.");
                        stage = 30;
                        break;
                    case 3:
                        playerl(FaceAnim.HALF_GUILTY, "But aren't you a witch?");
                        stage = 29;
                        break;
                }
                break;

            case 29:
                npcl(FaceAnim.SAD, "That I am, cha! However, a brew this goods needs the help of at least three experienced witches, not including the cook.");
                stage = END_DIALOGUE;
                break;
            case 30:
                npc("Thanks, cha.");
                stage = END_DIALOGUE;
                break;
            case 31:
                npc("Why yes, come to think of it, there is something you", "could do, cha.");
                stage++;
                break;
            case 32:
                npc(FaceAnim.HAPPY, "Really, the only thing left to finish off this old", "brewerooni is to give it a stir with an enchanted", "broomstick. Adds a bit of flavour and spark,", "don'tchaknow.");
                stage++;
                break;
            case 33:
                npc("So it happens, I have a broom right here!");
                stage++;
                break;
            case 34:
                player("Well, what's the problem then?");
                stage++;
                break;
            case 35:
                npc("Well, the broom hasn't been properly enchanted, that's", "what. Good stuff like this will only come out when there", "are several witches involved. Hetty, Betty, and Aggie are", "the perfect ladies for the job.");
                stage++;
                break;
            case 36:
                npc("We've worked together before and, if you can get", "them to work their magic on this ol' broom of mine, we'll", "have a cauldron fill o' something quite spectacular when", "all is said and done.");
                stage++;
                break;
            case 37:
                npc("If I gave you my broom, could you take it to Hetty in", "Rimmington, Betty in Port Sarim, and Aggie in", "Draynor, and ask each of them to enchant it for me?");
                stage++;
                break;
            case 38:
                npc("In return, I'd be more than willing to share the good", "stuff all around!");
                stage++;
                break;
            case 39:
                options("Sure, I can do that for you.", "What actually are you brewing?", "Sorry I'm a bit short of time right now.");
                stage++;
                break;

            case 40:
                switch (buttonId) {
                    case 1:
                        playerl(FaceAnim.HALF_GUILTY, "Sure, I can do that for you.");
                        stage = 43;
                        break;
                    case 2:
                        playerl(FaceAnim.HALF_ASKING, "What actually are you brewing?");
                        stage = 42;
                        break;
                    case 3:
                        playerl(FaceAnim.HALF_GUILTY, "Sorry I'm a bit short of time right now.");
                        stage = 41;
                        break;
                }
                break;

            case 41:
                npcl(FaceAnim.SAD, "Shame, cha. Well, you know where to find me if you change your mind.");
                stage = END_DIALOGUE;
                break;
            case 42:
                npcl(FaceAnim.HAPPY, "Didn't I tell you? I'm only brewing the best of the best of the most spectacular of brewifications, cha!");
                stage++;
                break;
            case 43:
                player("But what does it do?");
                stage++;
                break;
            case 44:
                npcl(FaceAnim.HAPPY, "Do? Ye blazes, man! There's not time in the day to describe the glory of the good stuff! So are you in or not?");
                stage++;
                break;
            case 45:
                npcl(FaceAnim.HALF_ASKING, "Can I count on you to get my broom enchanted so that we can finish this thing off?");
                stage++;
                break;
            case 46:
                end();
                player("Of course.");
                player.getInventory().add(new Item(Items.BROOMSTICK_14057));
                setQuestStage(player, Quests.SWEPT_AWAY, 1);
                setVarbit(player, Vars.VARBIT_QUEST_SWEPT_AWAY_PROGRESS_5448, 1);
                break;
            case 50:
                npcl(FaceAnim.HAPPY, "How observant you are, cha! Of course it's on wheels - how would I get around, otherwise?");
                stage++;
                break;
            case 51:
                player("Get around?");
                stage++;
                break;
            case 52:
                npcl(FaceAnim.HALF_GUILTY, "Oh yes, I like to keep moving, don'tchaknow. There's nothing I hate more than being stuck in the same place for too long.");
                stage = 5;
                break;
            case 60:
                npcl(FaceAnim.FRIENDLY, "Yes, poor ol' Babe, cha. I hope she gets over her sniffles soon.");
                stage++;
                break;
            case 61:
                npcl(FaceAnim.FRIENDLY, "After all, I could hardly ask her to pull this ol' caravan in the condition she's in, cha!");
                stage = 5;
                break;
            case 62:
                npc(FaceAnim.FRIENDLY, "Cha, and how! What's that crackle in the air and that", "smile on your face, then?");
                stage = 63;
                break;
            case 63:
                player(FaceAnim.HAPPY, "I visited Betty in Port Sarim, Hetty in Rimmington", "and Aggie in Draynor and they've all enchanted the", "broomstick for us.");
                stage = 64;
                break;
            case 64:
                npc(FaceAnim.FRIENDLY, "Aye, you say? Brilliant! Oh, I can't tell you how", "greatful I am. You're in for a treat, you are - it you'd", "just do one more little thing for me.");
                stage = 65;
                break;
            case 65:
                player("What's that, then?");
                stage = 66;
                break;
            case 66:
                npc(FaceAnim.FRIENDLY, "To finish the good stuff off, we just need to give it a", "good ol' stir with that broomstick there. It'll come out all", "the better if you do it, since you're the one who had it", "enchanted.");
                stage = 67;
                break;
            case 67:
                npc("Could you stir the good stuff in that cauldron for me?", "Pretty please with sweeties on top?");
                stage = 68;
                break;
            case 68:
                options("Of course I could.", "Well, I could, but I won't right now.");
                stage = 69;
                break;
            case 69:
                switch (buttonId) {
                    case 1:
                        player("Of course I could.");
                        stage = 71;
                        break;
                    case 2:
                        player("Well, I could, but I won't right now.");
                        stage = 70;
                        break;
                }
                break;
            case 70:
                npc("Cha and nonsense! You're a strange one, you are.");
                stage = END_DIALOGUE;
                break;
            case 71:
                npc("Drill! Just a quick stir at two with the broom should be", "all it needs.");
                stage = 72;
                break;
            case 72:
                player("Sounds easy enough.");
                setQuestStage(player, Quests.SWEPT_AWAY, 4);
                stage = END_DIALOGUE;
                break;
            case 80:
                npc("I should think so, cha! With all that added spice, that", "ought to be one spicy goulash. Would you like to try", "some?");
                stage = 81;
                break;
            case 81:
                player("Wait, what? Goulash? I thought you were brewing some", "amazingly magical witchly potion.");
                stage = 82;
                break;
            case 82:
                npc("Well, what witch doesn't love goulash, I ask you? What", "could be more witchly than a big ol' cauldron full of", "spicy yumminess?");
                stage = 83;
                break;
            case 83:
                player("But what about all that effort I went to enchant that", "broom?");
                stage = 84;
                break;
            case 84:
                npc("Cha! Well, you don't think spicing something this good is", "easy, do you?");
                stage = 85;
                break;
            case 85:
                npc("Look, I'm so grateful for your help. Why don't you", "help yourself to 10 bowlfuls? Just dip a bowl in the", "cauldron and help yourself - but hang on to your hat.");
                stage = 86;
                break;
            case 86:
                npc("That stuff's magic, after all. Not to blow my own horn,", "but one bowl can improve almost any aspect of", "your life!");
                stage = 87;
                break;
            case 87:
                npc("Here's a bowl you can use.");
                addItemOrDrop(player, Items.GOULASH_14058, 1);
                stage = 88;
                break;
            case 88:
                npc("You can also keep my broom, if you'd like. Now that", "we've used it to infuse the brew with some good ol'", "spice, it's not much use to me.");
                stage = 89;
                break;
            case 89:
                npc("You can't do much but sweep with it at the moment,", "but there are other witches out there who would be", "happy to enchant it for you, if you're interested.");
                stage = 90;
                break;
            case 90:
                finishQuest(player, Quests.SWEPT_AWAY);
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new MaggieDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.MAGGIE_8078};
    }
}
