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
 * The type Agility handler.
 */
public final class AgilityHandler {

    /**
     * Has failed boolean.
     *
     * @param player     the player
     * @param level      the level
     * @param failChance the fail chance
     * @return the boolean
     */
    public static boolean hasFailed(Player player, int level, double failChance) {
        int levelDiff = player.getSkills().getLevel(Skills.AGILITY) - level;
        if (levelDiff > 69) {
            return false;
        }
        double chance = (1 + levelDiff) * 0.01;
        chance *= Math.random();
        return chance <= (Math.random() * failChance);
    }

    /**
     * Fail walk force movement.
     *
     * @param player    the player
     * @param delay     the delay
     * @param start     the start
     * @param end       the end
     * @param dest      the dest
     * @param anim      the anim
     * @param speed     the speed
     * @param hit       the hit
     * @param message   the message
     * @param direction the direction
     * @return the force movement
     */
    public static ForceMovement failWalk(final Player player, int delay, Location start, Location end, final Location dest, Animation anim, int speed, final int hit, final String message, Direction direction) {
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
        movement.start();
        movement.setDelay(delay);
        GameWorld.getPulser().submit(movement);
        return movement;
    }

    /**
     * Fail walk force movement.
     *
     * @param player  the player
     * @param delay   the delay
     * @param start   the start
     * @param end     the end
     * @param dest    the dest
     * @param anim    the anim
     * @param speed   the speed
     * @param hit     the hit
     * @param message the message
     * @return the force movement
     */
    public static ForceMovement failWalk(final Player player, int delay, Location start, Location end, final Location dest, Animation anim, int speed, final int hit, final String message) {
        return failWalk(player, delay, start, end, dest, anim, speed, hit, message, null);
    }

    /**
     * Fail.
     *
     * @param player  the player
     * @param delay   the delay
     * @param dest    the dest
     * @param anim    the anim
     * @param hit     the hit
     * @param message the message
     */
    public static void fail(final Player player, int delay, final Location dest, Animation anim, final int hit, final String message) {
        if (anim != null) {
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
    }

    /**
     * Force walk force movement.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param start       the start
     * @param end         the end
     * @param animation   the animation
     * @param speed       the speed
     * @param experience  the experience
     * @param message     the message
     * @return the force movement
     */
    public static ForceMovement forceWalk(final Player player, final int courseIndex, Location start, Location end, Animation animation, int speed, final double experience, final String message) {
        player.logoutListeners.put("forcewalk", p -> {
            p.setLocation(player.getLocation().transform(0, 0, 0));
            return Unit.INSTANCE;
        });
        lock(player, ((int) start.getDistance(end)) * 3);
        ForceMovement movement = new ForceMovement(player, start, end, animation, speed) {
            @Override
            public void stop() {
                super.stop();
                if (message != null) {
                    player.getPacketDispatch().sendMessage(message);
                }
                if (experience > 0.0) {
                    player.getSkills().addExperience(Skills.AGILITY, experience, true);
                }
                setObstacleFlag(player, courseIndex);
                player.logoutListeners.remove("forcewalk");
            }
        };
        movement.start();
        GameWorld.getPulser().submit(movement);
        return movement;
    }

    /**
     * Force walk force movement.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param start       the start
     * @param end         the end
     * @param animation   the animation
     * @param speed       the speed
     * @param experience  the experience
     * @param message     the message
     * @param delay       the delay
     * @return the force movement
     */
    public static ForceMovement forceWalk(final Player player, final int courseIndex, Location start, Location end, Animation animation, int speed, final double experience, final String message, int delay) {
        player.logoutListeners.put("forcewalk", p -> {
            p.setLocation(player.getLocation().transform(0, 0, 0));
            return Unit.INSTANCE;
        });
        lock(player, ((int) start.getDistance(end)) * 3);
        if (delay < 1) {
            return forceWalk(player, courseIndex, start, end, animation, speed, experience, message);
        }
        final ForceMovement movement = new ForceMovement(player, start, end, animation, speed) {
            @Override
            public void stop() {
                super.stop();
                if (message != null) {
                    player.getPacketDispatch().sendMessage(message);
                }
                if (experience > 0.0) {
                    player.getSkills().addExperience(Skills.AGILITY, experience, true);
                }
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
     * Climb.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param animation   the animation
     * @param destination the destination
     * @param experience  the experience
     * @param message     the message
     */
    public static void climb(final Player player, final int courseIndex, Animation animation, final Location destination, final double experience, final String message) {
        climb(player, courseIndex, animation, destination, experience, message, 2);
    }

    /**
     * Climb.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param animation   the animation
     * @param destination the destination
     * @param experience  the experience
     * @param message     the message
     * @param delay       the delay
     */
    public static void climb(final Player player, final int courseIndex, Animation animation, final Location destination, final double experience, final String message, int delay) {
        player.lock(delay + 1);
        player.animate(animation);
        GameWorld.getPulser().submit(new Pulse(delay) {
            @Override
            public boolean pulse() {
                if (message != null) {
                    player.getPacketDispatch().sendMessage(message);
                }
                if (experience > 0.0) {
                    player.getSkills().addExperience(Skills.AGILITY, experience, true);
                }
                player.getProperties().setTeleportLocation(destination);
                setObstacleFlag(player, courseIndex);
                return true;
            }
        });
    }

    /**
     * Walk.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param start       the start
     * @param end         the end
     * @param animation   the animation
     * @param experience  the experience
     * @param message     the message
     */
    public static void walk(final Player player, final int courseIndex, final Location start, final Location end, final Animation animation, final double experience, final String message) {
        walk(player, courseIndex, start, end, animation, experience, message, false);
    }

    /**
     * Walk.
     *
     * @param player      the player
     * @param courseIndex the course index
     * @param start       the start
     * @param end         the end
     * @param animation   the animation
     * @param experience  the experience
     * @param message     the message
     * @param infiniteRun the infinite run
     */
    public static void walk(final Player player, final int courseIndex, final Location start, final Location end, final Animation animation, final double experience, final String message, final boolean infiniteRun) {
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
     * Sets obstacle flag.
     *
     * @param player      the player
     * @param courseIndex the course index
     */
    public static void setObstacleFlag(Player player, int courseIndex) {
        if (courseIndex < 0) {
            return;
        }
        AgilityCourse course = player.getExtension(AgilityCourse.class);
        if (course != null && courseIndex < course.getPassedObstacles().length) {
            course.flag(courseIndex);
        }
    }

}