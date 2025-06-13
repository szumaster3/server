package content.region.misthalin.barbarian_village.plugin;

import content.global.plugin.iface.warning.WarningManager;
import content.global.plugin.iface.warning.Warnings;
import content.region.misthalin.barbarian_village.dialogue.CradleOfLifeDialogue;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentPlugin;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.dialogue.FaceAnim;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Animations;
import org.rs.consts.Components;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * Represents the Stronghold plugin.
 */
@Initializable
public final class StrongholdPlugin extends MapZone implements Plugin<Object> {

    private static final Object[][] PORTALS = new Object[][]{{16150, Location.create(1914, 5222, 0)}, {16082, Location.create(2021, 5223, 0)}, {16116, Location.create(2146, 5287, 0)}, {16050, Location.create(2341, 5219, 0)}};

    /**
     * Instantiates a new Stronghold plugin.
     */
    public StrongholdPlugin() {
        super("strong hold of security", true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        ClassScanner.definePlugin(new StrongholdDialogue());
        ClassScanner.definePlugin(new CradleOfLifeDialogue());
        ClassScanner.definePlugin(new OptionHandler() {
            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                SceneryDefinition.forId(16154).getHandlers().put("option:climb-down", this);
                return this;
            }

            @Override
            public boolean handle(Player player, Node node, String option) {
                flagDoor(player, false);
                ClimbActionHandler.climb(player, null, Location.create(1859, 5243, 0));
                sendDialogueLines(player, "You squeeze through the hole and find a ladder a few feet down", "leading into the Stronghold of Security.");
                return true;
            }
        });
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            final Player player = (Player) e;
            switch (target.getId()) {
                case 16148:// ladder
                    ladder(player, Location.create(3081, 3421, 0));
                    player.getPacketDispatch().sendMessage("You climb up the ladder to the surface.");
                    return true;
                case 16146:// ladder.
                    ladder(player, new Location(1859, 5243, 0));
                    player.getPacketDispatch().sendMessage("You climb the ladder which seems to twist and wind in all directions.");
                    return true;
                case 16078:// ladder
                    ladder(player, new Location(2042, 5245, 0));
                    player.getPacketDispatch().sendMessage("You climb the ladder which seems to twist and wind in all directions.");
                    return true;
                case 16080:// ladder
                    ladder(player, Location.create(1859, 5243, 0));
                    return true;
                case 16048:// bone chain.
                    ladder(player, Location.create(3081, 3421, 0));
                    player.getPacketDispatch().sendMessage("You climb the ladder which seems to twist and wind in all directions.");
                    return true;
                case 16049:// boney ladder to get to third floor.
                    ladder(player, Location.create(2123, 5252, 0));
                    return true;
                case 16112:// third vine.
                    ladder(player, Location.create(2123, 5252, 0));
                    player.getPacketDispatch().sendMessage("You climb the ladder which seems to twist and wind in all directions.");
                    return true;
                case 16114:// third ladder.
                    ladder(player, Location.create(2042, 5245, 0));
                    player.getPacketDispatch().sendMessage("You climb up the ladder to the level above.");
                    return true;
                case 16149: // Ladder from Level 1 to Level 2 (Flesh crawlers)
                    if (!WarningManager.isDisabled(player, Warnings.STRONGHOLD_OF_SECURITY_LADDERS)) {
                        openComponent(player, Location.create(2042, 5245, 0));
                    } else {
                        ClimbActionHandler.climb(player, new Animation(Animations.USE_LADDER_828), Location.create(2042, 5245, 0));
                    }
                    return true;
                case 16081: // Ladder from Level 2 to Level 3 (Poison spiders)
                    if (!WarningManager.isDisabled(player, Warnings.STRONGHOLD_OF_SECURITY_LADDERS)) {
                        openComponent(player, Location.create(2123, 5252, 0));
                    } else {
                        ClimbActionHandler.climb(player, new Animation(Animations.USE_LADDER_828), Location.create(2123, 5252, 0));
                    }
                    return true;
                case 16115: // Ladder from Level 3 to Level 4 (Shades)
                    if (!WarningManager.isDisabled(player, Warnings.STRONGHOLD_OF_SECURITY_LADDERS)) {
                        openComponent(player, Location.create(2358, 5215, 0));
                    } else {
                        ClimbActionHandler.climb(player, new Animation(Animations.USE_LADDER_828), Location.create(2358, 5215, 0));
                    }
                    return true;
                case 16150:
                case 16082:
                case 16116:
                case 16050:
                    handlePortal(player, (Scenery) target);
                    return true;
                case 16123:
                case 16124:
                case 16126:
                case 16127:
                case 16065:
                case 16066:
                case 16067:
                case 16068:
                case 16089:
                case 16090:
                case 16092:
                case 16043:
                case 16044:
                case 16045:
                case 16046:
                    handleDoor(player, (Scenery) target);
                    return true;
            }
        }
        return super.interact(e, target, option);
    }

    /**
     * Handles a ladder in strong hold.
     *
     * @param player      the player.
     * @param destination the destination.
     */
    private void ladder(final Player player, final Location destination) {
        ClimbActionHandler.climb(player, new Animation(Animations.USE_LADDER_828), destination);
    }

    /**
     * Handles a portal.
     *
     * @param player the player.
     */
    private void handlePortal(final Player player, final Scenery object) {
        final int index = getPortalIndex(object.getId());
        if (!player.getSavedData().globalData.hasStrongholdReward(index + 1)) {
            player.getPacketDispatch().sendMessage("You are not of sufficient experience to take the shortcut through this level.");
        } else {
            player.getProperties().setTeleportLocation((Location) PORTALS[index][1]);
            player.getPacketDispatch().sendMessage("You enter the portal to be whisked through to the treasure room.");
        }
    }

    /**
     * Handles a stronghold of security door.
     *
     * @param player the player.
     * @param object the object.
     */
    private static void handleDoor(final Player player, final Scenery object) {
        final boolean force = isForced(player, object);
        if (force || RandomFunction.random(40) < 2) {
            openDoor(player, object);
            return;
        }
        player.getDialogueInterpreter().open("strong-hold", object);
    }

    /**
     * Opens the door.
     *
     * @param player the player.
     * @param object the object.
     */
    private static void openDoor(final Player player, final Scenery object) {
        player.lock(3);
        player.animate(Animation.create(4282));
        playAudio(player, Sounds.SOS_THROUGH_DOOR_2858);
        GameWorld.getPulser().submit(new Pulse(1, player) {
            int counter;

            @Override
            public boolean pulse() {
                switch (++counter) {
                    case 1:
                        final java.awt.Point p = DoorActionHandler.getRotationPoint(object.getRotation());
                        Location destination = !player.getLocation().equals(object.getLocation()) ? object.getLocation() : object.getLocation().transform((int) p.getX(), (int) p.getY(), 0);
                        player.getProperties().setTeleportLocation(destination);
                        break;
                    case 2:
                        player.animate(new Animation(4283));
                        flagDoor(player, !isFlagged(player));
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Opens the component.
     *
     * @param player   the player.
     * @param location the location.
     */
    private void openComponent(Player player, Location location) {
        final Component component = new Component(Components.CWS_WARNING_4_579);
        component.setPlugin(new StrongholdComponentPlugin(location));
        player.getInterfaceManager().open(component);
    }

    /**
     * Flags a door as set or not.
     *
     * @param player    the player.
     * @param completed if completed.
     */
    private static void flagDoor(Player player, boolean completed) {
        setAttribute(player, "/save:strong-hold:door", completed);
    }

    /**
     * Checks if the door is flagged.
     *
     * @param player the player.
     * @return {@code True} if flagged.
     */
    private static boolean isFlagged(final Player player) {
        return player.getAttribute("strong-hold:door", false);
    }

    /**
     * Checks if a door is forced to let you through.
     *
     * @param player the player.
     * @param object the object.
     * @return {@code True} if its forced.
     */
    private static boolean isForced(final Player player, final Scenery object) {
        if (player.inCombat() || player.getProperties().getCombatPulse().isAttacking()) {
            return true;
        }
        return isFlagged(player);
    }

    /**
     * Gets the portal index.
     *
     * @param id the id.
     * @return the portal index.
     */
    private int getPortalIndex(int id) {
        for (int i = 0; i < PORTALS.length; i++) {
            if ((int) PORTALS[i][0] == id) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(1836, 5174, 1930, 5257));// first level.
        register(new ZoneBorders(1977, 5176, 2066, 5265));// second level.
        register(new ZoneBorders(2090, 5246, 2197, 5336));// third level.
        register(new ZoneBorders(2297, 5166, 2391, 5261));// fourth level.
    }

    /**
     * Stronghold dialogue.
     */
    public static final class StrongholdDialogue extends Dialogue {
        private Scenery door;
        private int npcId;

        /**
         * Instantiates a new Stronghold dialogue.
         *
         * @param player the player
         */
        public StrongholdDialogue(final Player player) {
            super(player);
        }

        /**
         * Instantiates a new Stronghold dialogue.
         */
        public StrongholdDialogue() {
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new StrongholdDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            door = (Scenery) args[0];
            npcId = getNpcId(door.getName());
            if (player.getLocation().getX() == 1859 && player.getLocation().getY() == 5239 || player.getLocation().getX() == 1858 && player.getLocation().getY() == 5239) {
                sendNPCDialogue(player, npcId, "Greetings Adventurer. This place is kept safe by the spirits within the doors. As you pass through you will be asked questions about security. Hopefully you will learn much from us.", FaceAnim.OLD_NORMAL);
                stage = 900;
                return true;
            }
            final int rand = RandomFunction.random(0, 18);
            switch (rand) {
                case 0:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What do I do if a moderator asks me for my account details?", FaceAnim.OLD_NORMAL);
                    stage = 100;
                    break;
                case 1:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: My friend uses this great add-on program he got from a website, should I?", FaceAnim.OLD_NORMAL);
                    stage = 1000;
                    break;
                case 2:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Who is it ok to share my account with?", FaceAnim.OLD_NORMAL);
                    stage = 200;
                    break;
                case 3:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Why do I need to type in recovery questions?", FaceAnim.OLD_NORMAL);
                    stage = 300;
                    break;
                case 4:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Who is it ok to share my account with?", FaceAnim.OLD_NORMAL);
                    stage = 400;
                    break;
                case 5:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Who can I give my password to?", FaceAnim.OLD_NORMAL);
                    stage = 500;
                    break;
                case 6:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: How will " + GameWorld.getSettings().getName() + " contact me if I have been chosen to be a moderator?", FaceAnim.OLD_NORMAL);
                    stage = 600;
                    break;
                case 7:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: How often should you change your recovery questions?", FaceAnim.OLD_NORMAL);
                    stage = 700;
                    break;
                case 8:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: A website says I can become a player moderator by giving them my password what do I do?", FaceAnim.OLD_NORMAL);
                    stage = 800;
                    break;
                case 9:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Will " + GameWorld.getSettings().getName() + " block me from saying my PIN in game?", FaceAnim.OLD_NORMAL);
                    stage = 1900;
                    break;
                case 10:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Can I leave my account logged in while I'm out of the room?", FaceAnim.OLD_NORMAL);
                    stage = 1100;
                    break;
                case 11:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Where should I enter my " + GameWorld.getSettings().getName() + " Password?", FaceAnim.OLD_NORMAL);
                    stage = 1111;
                    break;
                case 12:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What is an example of a good bank PIN?", FaceAnim.OLD_NORMAL);
                    stage = 1200;
                    break;
                case 13:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What do I do if I think I have a keylogger or virus?", FaceAnim.OLD_NORMAL);
                    stage = 1300;
                    break;
                case 14:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Recovery answers should be...", FaceAnim.OLD_NORMAL);
                    stage = 1400;
                    break;
                case 15:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What do you do if someone tells you that you have won the " + GameWorld.getSettings().getName() + " Lottery and asks for your password or recoveries?", FaceAnim.OLD_NORMAL);
                    stage = 1500;
                    break;
                case 16:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What should I do if I think someone knows my recoveries?", FaceAnim.OLD_NORMAL);
                    stage = 1600;
                    break;
                case 17:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: What do you do if someone asks you for your password or recoveries to make you a player moderator?", FaceAnim.OLD_NORMAL);
                    stage = 1700;
                    break;
                case 18:
                    sendNPCDialogue(player, npcId, "To pass you must answer me this: Where can I find cheats for " + GameWorld.getSettings().getName() + "?", FaceAnim.OLD_NORMAL);
                    stage = 1800;
                    break;
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 900:
                    sendNPCDialogue(player, npcId, "Please pass through and begin your adventure, beware of the various monsters that dwell within.", FaceAnim.OLD_NORMAL);
                    stage = 901;
                    break;
                case 901:
                    openDoor(player, door);
                    interpreter.sendDialogues(player, FaceAnim.AMAZED, "Oh my! I just got sucked through that door... what a", "weird feeling! Still, I guess I should expect it as these", "evidently aren't your average kind of doors.... they talk", "and look creepy!");
                    stage = 902;
                    break;
                case 902:
                    end();
                    break;
                case 100:
                    sendDialogueOptions(player, "Tell them whatever they want to know.", "Politely tell them no.", "Politely tell them no and then use the 'Report Abuse' button.");
                    stage = 101;
                    break;
                case 101:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! Never give your account details to anyone! This includes things like recovery answers, contact details and passwords. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Ok! Don't tell them the details. But reporting the incident to " + GameWorld.getSettings().getName() + " would help. Use the Report Abuse button. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! Report any attempt to gain your account details as it is a very serious breach of " + GameWorld.getSettings().getName() + "'s rules. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 99:
                    end();
                    break;
                case 1000:
                    sendDialogueOptions(player, "Select an Option", "No, it might steal my password.", "I'll gave it a try and see if I like it.", "Sure, he's used it a lot, so can I.");
                    stage = 10001;
                    break;
                case 10001:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Correct! The only safe add-on for " + GameWorld.getSettings().getName() + " is the Window client available from our " + GameWorld.getSettings().getName() + " Website.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! Some programs steal your password without you even knowing, this only requires running the program once, even if you don't use it.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! The program may steal your password and is against the rules to use.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 200:
                    sendDialogueOptions(player, "Select an Option", "My friends.", "Relatives.", "Nobody.");
                    stage = 201;
                    break;
                case 201:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! You account may only be used by you.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! Your account may only be used by you.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! You, can you only may use your account.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 300:
                    sendDialogueOptions(player, "Select an Option", "To help me recover my password if I forget it or it is stolen.", "To let " + GameWorld.getSettings().getName() + " know more about its players.", "To see if I can type in random letters on my keyboard.");
                    stage = 301;
                    break;
                case 301:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Correct! Your recovery questions will help " + GameWorld.getSettings().getName() + " staff protect and return your account if it is stolen. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! " + GameWorld.getSettings().getName() + " values players opinions, but we use polls and forums to see what you think. The recoveries are not there to gain personal information about anybody but to protect your account. Never use personal details", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! Typing random letters into your recoveries won't help you or the " + GameWorld.getSettings().getName() + " staff - you'll never remember them anyway! Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 400:
                    sendDialogueOptions(player, "Select an Option", "My friends", "Relatives", "Nobody");
                    stage = 401;
                    break;
                case 401:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! You account may only be used by you.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! You account may only be used by you.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! You, and you only may use your account.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 500:
                    sendDialogueOptions(player, "Select an Option", "My friends", "My brother", "Nobody");
                    stage = 501;
                    break;
                case 501:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! Your password should be kept secret from everyone. You should *never* give it out under any circumstances.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! Your password should be kept secret from everyone. You should *never* give it out under any circumstances.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! Your password should be kept secret from everyone. You should *never* give it out under any circumstances.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 600:
                    sendDialogueOptions(player, "Select an Option", "Email.", "Website popup.", "Game Inbox on the " + GameWorld.getSettings().getName() + " Website.");
                    stage = 601;
                    break;
                case 601:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! " + GameWorld.getSettings().getName() + " never uses email to contact you, this is a scam and a fake, do not reply to it and delete it straight away. " + GameWorld.getSettings().getName() + " will only contact you through your Game Inbox available on our website.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! " + GameWorld.getSettings().getName() + " would never use such an insecure method to pick you. We will contact you through your Game Inbox available on our website.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! We only contact our players via the game Inbox which you can access from our " + GameWorld.getSettings().getName() + " website.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 700:
                    sendDialogueOptions(player, "Select an Option", "Never", "Every couple of months", "Every day");
                    stage = 701;
                    break;
                case 701:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Never changing your recovery questions will lead to an insecure account due to keyloggers or friends knowing enough about you to guess them. Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Correct! it is the ideal option to change your questions but make sure you can remember the answers! Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Normally recovery questions will take 14 days to become active, so there's no point in changing them everyday! Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 800:
                    sendDialogueOptions(player, "Select an Option", "Nothing.", "Give them my password.", "Don't tell them anything and inform " + GameWorld.getSettings().getName() + " through the game website.");
                    stage = 801;
                    break;
                case 801:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "This is one solution, however someone will fall for this scam sooner or later. Tell us about it through the " + GameWorld.getSettings().getName() + " website. Remember that moderators are hand picked by " + GameWorld.getSettings().getName() + ".", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! This will almost certainly lead to your account being hijacked. No website can make you a moderator as they are hand picked by " + GameWorld.getSettings().getName() + ".", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! By informing us we can have the site taken down so other people will not have their accounts hijacked by this scam.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 1900:
                    sendDialogueOptions(player, "Select an Option", "Yes.", "No.");
                    stage = 1901;
                    break;
                case 1901:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! " + GameWorld.getSettings().getName() + " does NOT block your PIN so don't type it!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Correct! " + GameWorld.getSettings().getName() + " will not block your PIN so don't type it! Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 1100:
                    sendDialogueOptions(player, "Select an Option", "Yes.", "No.", "If I'm going to be quick.");
                    stage = 1101;
                    break;
                case 1101:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! You should logout in case you are attacked or receive a random event. Leaving your character logged in can also allow someone to steal your items or entire account!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Correct! This is the safest, both in terms of security and keeping your items! Leaving you character logged in can also allow someone to steal your items or entire account!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! You should logout in case you are attacked or receive a random event. Leaving your character logged in can also allow someone to steal your items or entire account!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 1111:
                    sendDialogueOptions(player, "Select an Option", "On " + GameWorld.getSettings().getName() + " and all fansites.", "Only on the " + GameWorld.getSettings().getName() + " website.", "On all websites I visit.");
                    stage = 1112;
                    break;
                case 1112:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! Always use a unique password purely for your " + GameWorld.getSettings().getName() + " account.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Correct! Always make sure you are entering your password only on the " + GameWorld.getSettings().getName() + " Website as other sites may try to steal it.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! This is very insecure and will may lead to your account being stolen.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 1200:
                    sendDialogueOptions(player, "Select an Option", "Your real life bank PIN.", "Your birthday.", "The birthday of a famous person or event.");
                    stage = 1201;
                    break;
                case 1201:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "This is a bad idea as if someone happens to find out your bank PIN on " + GameWorld.getSettings().getName() + ", they then have access to your bank account.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Not a good idea because you know how many presents you get for your birthday. So you can imagine how many people know this date. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Well done! Unless you tell someone, they are unlikely to guess who or what you have chosen, and you can always look it up, Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 1300:
                    sendDialogueOptions(player, "Select an Option", "Virus scan my computer then change my password and recoveries.", "Change my password and recoveries then virus scan my computer.", "Nothing, it will go away on its own.");
                    stage = 1301;
                    break;
                case 1301:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Correct! Removing the keylogger must be the priority otherwise anything you type can be given away. Remember to change your password and recovery questions afterwards.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! If you change your password and recoveries while you still have the keylogger, they will still be insecure. Remove the keylogger first. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! This could mean your account may be accessed by someone else. Remove the keylogger then change your password and recoveries. Never use personal details for recoveries or bank PINs!", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 1400:
                    sendDialogueOptions(player, "Select an Option", "Memorable", "Easy to guess", "Random gibberish");
                    stage = 1401;
                    break;
                case 1401:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Correct! A good recovery answer that not many people will know, you won't forget, will stay the same and that you wouldn't accidentally give away. Remember: Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "This is a bad idea as anyone who knows you could guess them. Remember: Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "This is a bad idea because it is very difficult to remember and you won't be able to recover your account! Remember: Don't use personal details for your recoveries.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 1500:
                    sendDialogueOptions(player, "Select an Option", "Give them the information they asked for.", "Don't tell them anything and ignore them.");
                    stage = 1501;
                    break;
                case 1501:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! There is no " + GameWorld.getSettings().getName() + " Lottery! Never give your account details to anyone. Press the 'Report Abuse' button and fill in the offending player's name and the correct category. Don't tell them anything and click the 'Report Abuse' button.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Quite good. But we should try to stop scammers. So please report them using the 'Report Abuse' button.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! Press the 'Report Abuse' button and fill in the offending player's name and the correct category.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 1600:
                    sendDialogueOptions(player, "Select an Option", "Tell them never to use them.", "Use the Account Management section on the " + GameWorld.getSettings().getName() + " website.", "'Recover a Lost Password' section on the " + GameWorld.getSettings().getName() + " website.");
                    stage = 1601;
                    break;
                case 1601:
                    if (buttonId == 1) {
                        sendNPCDialogue(player, npcId, "Wrong! This does nothing to help the security of your account. You should reset your questions through the 'Lost password' link on our website.", FaceAnim.OLD_NORMAL);
                        stage = 99;
                    } else if (buttonId == 2) {
                        sendNPCDialogue(player, npcId, "Wrong! If you use the Account Management section to change your recovery questions, it will take 14 days to come into effect, someone may have access to your account this time.", FaceAnim.OLD_NORMAL);
                        stage = 99;
                    } else if (buttonId == 3) {
                        sendNPCDialogue(player, npcId, "Correct! If you provide all the correct information this will reset your questions within 24 hours and make your account secure again.", FaceAnim.OLD_NORMAL);
                        stage = 69;
                    }
                    break;
                case 1700:
                    sendDialogueOptions(player, "Select an Option", " Don't give them any information and send an 'Abuse report'.", "Don't tell them anything and ignore them.", "Give them the information they asked for.");
                    stage = 1701;
                    break;
                case 1701:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Correct! Press the 'Report Abuse' button and fill in the offending player's name and the correct category.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Quite good. But we should try to stop scammers. So please report them using the 'Report Abuse' button.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Wrong! " + GameWorld.getSettings().getName() + " never ask for your account information especially to become a player moderator. Press the 'Report Abuse' button and fill in the offending player's name and the correct category.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                    }
                    break;
                case 1800:
                    sendDialogueOptions(player, "Select an Option", "On the " + GameWorld.getSettings().getName() + " website.", "By searching the internet.", "Nowhere.");
                    stage = 1801;
                    break;
                case 1801:
                    switch (buttonId) {
                        case 1:
                            sendNPCDialogue(player, npcId, "Wrong! There are NO " + GameWorld.getSettings().getName() + " cheats coded into the game and any sites claiming to have cheats are fakes and may lead to your account being stolen if you give them your password.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 2:
                            sendNPCDialogue(player, npcId, "Wrong! There are NO " + GameWorld.getSettings().getName() + " cheats coded into the game and any sites claiming to have cheats are fakes and may lead to your account being stolen if you give them your password.", FaceAnim.OLD_NORMAL);
                            stage = 99;
                            break;
                        case 3:
                            sendNPCDialogue(player, npcId, "Correct! There are NO " + GameWorld.getSettings().getName() + " cheats coded into the game. Any sites claiming to have cheats are fakes and may lead to your account being stolen if you give them your password.", FaceAnim.OLD_NORMAL);
                            stage = 69;
                            break;
                    }
                    break;
                case 69:
                    end();
                    openDoor(player, door);
                    break;
            }
            return true;
        }


        /**
         * Gets the npc id from the door name.
         *
         * @param name the name.
         * @return the name.
         */
        private int getNpcId(String name) {
            switch (name) {
                case "Gate of War":
                    return 4377;
                case "Rickety door":
                    return 4378;
                case "Oozing barrier":
                    return 4379;
                case "Portal of Death":
                    return 4380;
            }
            return 0;
        }

        @Override
        public int[] getIds() {
            return new int[]{DialogueInterpreter.getDialogueKey("strong-hold")};
        }

    }

    /**
     * Stronghold component plugin.
     */
    public final class StrongholdComponentPlugin extends ComponentPlugin {
        private final Location destination;

        /**
         * Instantiates a new Stronghold component plugin.
         *
         * @param destination the destination
         */
        public StrongholdComponentPlugin(final Location destination) {
            this.destination = destination;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            switch (button) {
                case 18:
                    if (player.getInterfaceManager().close())
                        sendPlayerDialogue(player, "No thanks, I don't want to die!", FaceAnim.AFRAID);
                    return true;
                case 17:
                    player.getInterfaceManager().close();
                    ladder(player, destination);
                    player.getPacketDispatch().sendMessage("You climb down the ladder to the next level.");
                    return true;
            }
            return true;
        }

    }
}
