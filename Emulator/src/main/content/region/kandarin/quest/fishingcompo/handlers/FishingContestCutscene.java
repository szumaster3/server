package content.region.kandarin.quest.fishingcompo.handlers;

import content.region.kandarin.quest.fishingcompo.FishingContest;
import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;

import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Fishing contest cutscene.
 */
@Initializable
public class FishingContestCutscene extends CutscenePlugin {

    /**
     * The S stranger.
     */
    public final int S_STRANGER = 3678;

    /**
     * The Big dave.
     */
    public final int BIG_DAVE = 228;

    /**
     * Instantiates a new Fishing contest cutscene.
     */
    public FishingContestCutscene() {
        super("Fishing Contest Cutscene");
    }

    /**
     * Instantiates a new Fishing contest cutscene.
     *
     * @param player the player
     */
    public FishingContestCutscene(Player player) {
        super("Fishing Contest Cutscene");
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new FishingContestCutscene(p);
    }

    @Override
    public boolean start(Player player, boolean login, Object... args) {
        NPC Bonzo = NPC.create(225, base.transform(17, 45, 0));
        NPC Stranger = NPC.create(S_STRANGER, base.transform(14, 51, 0));
        NPC Dave = NPC.create(BIG_DAVE, base.transform(9, 34, 0));
        Bonzo.init();
        Stranger.init();
        Dave.init();
        Bonzo.faceLocation(base.transform(16, 45, 0));
        Stranger.faceLocation(base.transform(13, 51, 0));
        Dave.faceLocation(base.transform(8, 34, 0));
        this.npcs.add(Bonzo);
        this.npcs.add(Dave);
        this.npcs.add(Stranger);
        player.faceLocation(base.transform(6, 43, 0));
        return super.start(player, login, args);
    }

    @Override
    public void register() {
        register(new ZoneBorders(2623, 3426, 2644, 3448));
        super.register();
    }

    @Override
    public void open() {
        setAttribute(player, "cutscene:original-loc", Location.create(2639, 3437, 0));
        setAttribute(player, "real-end", Location.create(2639, 3437, 0));

        if (player.getAttribute("fishing_contest:garlic", false)) {
            NPC stranger = npcs.get(2);
            stranger.sendChat("What is this smell??");
            GameWorld.getPulser().submit(new SwitchPulse());
        } else {
            GameWorld.getPulser().submit(new FishingPulse());
        }
        player.lock();
    }

    private
    class FishingPulse extends Pulse {

        /**
         * The Counter.
         */
        int counter = 0;

        @Override
        public boolean pulse() {
            switch (counter++) {
                case 6:
                    for (NPC n : npcs) {
                        n.faceLocation(n.getLocation().transform(Direction.WEST, 1));
                        n.getAnimator().animate(new Animation(622));
                    }
                    player.faceLocation(player.getLocation().transform(Direction.WEST, 1));
                    player.getAnimator().animate(new Animation(622));
                    break;
                case 12:
                    if (player.getAttribute("fishing_contest:garlic", false) && player.getInventory().containsItem(FishingContest.RED_VINE_WORM)) {
                        player.getInventory().remove(FishingContest.RED_VINE_WORM);
                        player.getInventory().add(FishingContest.RAW_GIANT_CARP);
                        setAttribute(player, "/save:fishing_contest:won", true);
                        player.getWalkingQueue().setRunDisabled(false);
                    }
                    GameWorld.getPulser().submit(getEndPulse());
                    break;
                case 15:
                    GameWorld.getPulser().submit(new Pulse() {
                        int counter = 0;

                        @Override
                        public boolean pulse() {
                            switch (counter++) {
                                case 2:
                                    player.faceLocation(Location.create(2642, 3437, 0));
                                    break;
                                case 4:
                                    player.getDialogueInterpreter().open(225, true, true);
                                    return true;
                            }
                            return false;
                        }
                    });
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        return true;
    }

    @Override
    public void stop(boolean fade) {
        super.stop(fade);
    }

    /**
     * The type Switch pulse.
     */
    public class SwitchPulse extends Pulse {

        /**
         * The Stranger.
         */
        NPC stranger = npcs.get(2);

        /**
         * The Counter.
         */
        int counter = 0;

        @Override
        public boolean pulse() {
            switch (counter++) {
                case 1:
                    stranger.getPulseManager().run(new MovementPulse(stranger, base.getLocation().transform(8, 42, 0)) {
                        @Override
                        public boolean pulse() {
                            player.getWalkingQueue().setRunDisabled(true);
                            return true;
                        }
                    }, PulseType.STANDARD);
                    break;
                case 12:
                    stranger.sendChat("You're switching with me. Go.");
                    break;
                case 16:
                    player.getPulseManager().run(new MovementPulse(player, base.getLocation().transform(14, 51, 0)) {
                        @Override
                        public boolean pulse() {
                            player.getDialogueInterpreter().sendDialogue("Your spot is now by the pipes.");
                            return true;
                        }
                    }, PulseType.STANDARD);
                    break;
                case 22:
                    stranger.getPulseManager().run(new MovementPulse(stranger, base.getLocation().transform(7, 43, 0)) {
                        @Override
                        public boolean pulse() {
                            return true;
                        }
                    }, PulseType.STANDARD);
                    GameWorld.getPulser().submit(new FishingPulse());
                    return true;
            }
            return false;
        }
    }

    @Override
    public Location getStartLocation() {
        return base.transform(7, 43, 0);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(10549);
        setRegionBase();
        registerRegion(region.getId());
    }
}
