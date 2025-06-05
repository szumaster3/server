package content.global.skill.agility;

import core.game.interaction.MovementPulse;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.node.entity.skill.Skills;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import kotlin.Unit;

import static core.api.ContentAPIKt.*;

/**
 * Holds agility-related utility methods.
 *
 * @author Emperor
 */
public final class AgilityHandler {

    /**
     * Determines if the player fails an agility obstacle.
     *
     * @param player     the player
     * @param level      the required level
     * @param failChance base chance to fail
     * @return {@code true} if failed; {@code false} otherwise
     */
    public static boolean hasFailed(Player player, int level, double failChance) {
        int levelDiff = player.getSkills().getLevel(Skills.AGILITY) - level;
        if (levelDiff > 69) return false;
        double chance = (1 + levelDiff) * 0.01 * Math.random();
        return chance <= (Math.random() * failChance);
    }

    /**
     * Executes a failing force movement and hits/damages the player after.
     */
    public static ForceMovement failWalk(Player player, int delay, Location start, Location end, Location dest, Animation anim, int speed, int hit, String message, Direction direction) {
        ForceMovement movement = new ForceMovement(player, start, end, anim, speed) {
            @Override
            public void stop() {
                super.stop();
                player.getProperties().setTeleportLocation(dest);
                if (hit > 0) {
                    player.getImpactHandler().setDisabledTicks(0);
                    player.getImpactHandler().manualHit(player, hit, HitsplatType.NORMAL);
                }
                if (message != null) {
                    player.getPacketDispatch().sendMessage(message);
                }
            }
        };
        if (direction != null) {
            movement.setDirection(direction);
        }
        movement.setDelay(delay);
        movement.start();
        GameWorld.getPulser().submit(movement);
        return movement;
    }

    public static ForceMovement failWalk(Player player, int delay, Location start, Location end, Location dest, Animation anim, int speed, int hit, String message) {
        return failWalk(player, delay, start, end, dest, anim, speed, hit, message, null);
    }

    /**
     * Teleports player and deals damage after a delay on failure.
     */
    public static void fail(Player player, int delay, Location dest, Animation anim, int hit, String message) {
        if (anim == null) return;
        animate(player, anim, true);
        submitWorldPulse(new Pulse(animationDuration(anim), player) {
            boolean dmg = false;

            @Override
            public boolean pulse() {
                teleport(player, dest, TeleportManager.TeleportType.INSTANT);
                animate(player, Animation.RESET, true);
                if (!dmg) {
                    if (hit > 0) {
                        player.getImpactHandler().setDisabledTicks(0);
                        impact(player, hit, HitsplatType.NORMAL);
                    }
                    if (message != null) {
                        sendMessage(player, message);
                    }
                    dmg = true;
                }
                setDelay(0);
                return player.getLocation().equals(dest);
            }
        });
    }

    /**
     * Forces player to walk with animation and gain XP.
     */
    public static ForceMovement forceWalk(Player player, int courseIndex, Location start, Location end, Animation anim, int speed, double experience, String message) {
        player.logoutListeners.put("forcewalk", p -> {
            p.setLocation(player.getLocation().transform(0, 0, 0));
            return Unit.INSTANCE;
        });
        lock(player, ((int) start.getDistance(end)) * 3);

        ForceMovement movement = new ForceMovement(player, start, end, anim, speed) {
            @Override
            public void stop() {
                super.stop();
                if (message != null) player.getPacketDispatch().sendMessage(message);
                if (experience > 0.0) player.getSkills().addExperience(Skills.AGILITY, experience, true);
                setObstacleFlag(player, courseIndex);
                player.logoutListeners.remove("forcewalk");
            }
        };
        movement.start();
        GameWorld.getPulser().submit(movement);
        return movement;
    }

    public static ForceMovement forceWalk(Player player, int courseIndex, Location start, Location end, Animation anim, int speed, double experience, String message, int delay) {
        player.logoutListeners.put("forcewalk", p -> {
            p.setLocation(player.getLocation().transform(0, 0, 0));
            return Unit.INSTANCE;
        });
        lock(player, ((int) start.getDistance(end)) * 3);

        if (delay < 1) {
            return forceWalk(player, courseIndex, start, end, anim, speed, experience, message);
        }

        ForceMovement movement = new ForceMovement(player, start, end, anim, speed) {
            @Override
            public void stop() {
                super.stop();
                if (message != null) player.getPacketDispatch().sendMessage(message);
                if (experience > 0.0) player.getSkills().addExperience(Skills.AGILITY, experience, true);
                setObstacleFlag(player, courseIndex);
                player.logoutListeners.remove("forcewalk");
            }
        };

        GameWorld.getPulser().submit(new Pulse(delay, player) {
            @Override
            public boolean pulse() {
                movement.start();
                GameWorld.getPulser().submit(movement);
                return true;
            }
        });

        return movement;
    }

    /**
     * Performs climbing movement.
     */
    public static void climb(Player player, int courseIndex, Animation animation, Location destination, double experience, String message) {
        climb(player, courseIndex, animation, destination, experience, message, 2);
    }

    public static void climb(Player player, int courseIndex, Animation animation, Location destination, double experience, String message, int delay) {
        player.lock(delay + 1);
        player.animate(animation);
        GameWorld.getPulser().submit(new Pulse(delay) {
            @Override
            public boolean pulse() {
                if (message != null) player.getPacketDispatch().sendMessage(message);
                if (experience > 0.0) player.getSkills().addExperience(Skills.AGILITY, experience, true);
                player.getProperties().setTeleportLocation(destination);
                setObstacleFlag(player, courseIndex);
                return true;
            }
        });
    }

    /**
     * Performs walking animation over obstacle.
     */
    public static void walk(Player player, int courseIndex, Location start, Location end, Animation animation, double experience, String message) {
        walk(player, courseIndex, start, end, animation, experience, message, false);
    }

    public static void walk(Player player, int courseIndex, Location start, Location end, Animation animation, double experience, String message, boolean infiniteRun) {
        if (!player.getLocation().equals(start)) {
            player.getPulseManager().run(new MovementPulse(player, start) {
                @Override
                public boolean pulse() {
                    walk(player, courseIndex, start, end, animation, experience, message, infiniteRun);
                    return true;
                }
            }, PulseType.STANDARD);
            return;
        }

        player.getWalkingQueue().reset();
        player.getWalkingQueue().addPath(end.getX(), end.getY(), !infiniteRun);

        int ticks = player.getWalkingQueue().getQueue().size();
        player.getImpactHandler().setDisabledTicks(ticks);
        player.lock(1 + ticks);

        player.logoutListeners.put("agility", p -> {
            p.setLocation(start);
            return Unit.INSTANCE;
        });

        if (animation != null) {
            player.getAppearance().setAnimations(animation);
        }

        player.getSettings().setRunEnergy(100.0);

        GameWorld.getPulser().submit(new Pulse(ticks, player) {
            @Override
            public boolean pulse() {
                if (animation != null) {
                    player.getAppearance().setAnimations();
                    player.getAppearance().sync();
                }
                if (message != null) {
                    player.getPacketDispatch().sendMessage(message);
                }
                if (experience > 0.0) {
                    player.getSkills().addExperience(Skills.AGILITY, experience, true);
                }
                setObstacleFlag(player, courseIndex);
                player.logoutListeners.remove("agility");
                return true;
            }
        });
    }

    /**
     * Flags obstacle as completed.
     */
    public static void setObstacleFlag(Player player, int courseIndex) {
        if (courseIndex < 0) return;
        AgilityCourse course = player.getExtension(AgilityCourse.class);
        if (course != null && courseIndex < course.getPassedObstacles().length) {
            course.flag(courseIndex);
        }
    }
}