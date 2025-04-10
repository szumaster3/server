package content.data.consumables;

import content.data.consumables.effects.*;
import core.game.consumable.*;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import org.rs.consts.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static core.tools.TickUtilsKt.minutesToTicks;
import static core.tools.TickUtilsKt.secondsToTicks;

/**
 * The enum Consumables.
 */
public enum Consumables {
    /**
     * The Fingers.
     */
    FINGERS(new Food(new int[]{Items.FINGERS_10965}, new HealingEffect(2))),
    /**
     * The Coated frog legs.
     */
    COATED_FROG_LEGS(new Food(new int[]{Items.COATED_FROGS_LEGS_10963}, new HealingEffect(2))),
    /**
     * The Cooked meat.
     */
    COOKED_MEAT(new Food(new int[]{Items.COOKED_MEAT_2142}, new HealingEffect(3))),
    /**
     * The Cooked turkey.
     */
    COOKED_TURKEY(new Food(new int[]{Items.COOKED_TURKEY_14540}, new HealingEffect(2))),

    /**
     * The Cooked turkey drumstick.
     */
    COOKED_TURKEY_DRUMSTICK(new Food(new int[]{Items.COOKED_TURKEY_DRUMSTICK_14543}, new HealingEffect(10), "You eat the turkey drumstick.")),
    /**
     * The Shrimps.
     */
    SHRIMPS(new Food(new int[]{Items.SHRIMPS_315}, new HealingEffect(3))),
    /**
     * The Cooked chicken.
     */
    COOKED_CHICKEN(new Food(new int[]{Items.COOKED_CHICKEN_2140}, new HealingEffect(3))),
    /**
     * The Cooked rabbit.
     */
    COOKED_RABBIT(new Food(new int[]{Items.COOKED_RABBIT_3228}, new HealingEffect(5))),
    /**
     * The Anchovies.
     */
    ANCHOVIES(new Food(new int[]{Items.ANCHOVIES_319}, new HealingEffect(1))),
    /**
     * The Sardine.
     */
    SARDINE(new Food(new int[]{Items.SARDINE_325}, new HealingEffect(3))),
    /**
     * The Poison karambwan.
     */
    POISON_KARAMBWAN(new Food(new int[]{Items.POISON_KARAMBWAN_3146}, new PoisonKarambwanEffect())),
    /**
     * The Ugthanki meat.
     */
    UGTHANKI_MEAT(new Food(new int[]{Items.UGTHANKI_MEAT_1861}, new HealingEffect(3))),
    /**
     * The Herring.
     */
    HERRING(new Food(new int[]{Items.HERRING_347}, new HealingEffect(5))),
    /**
     * The Mackerel.
     */
    MACKEREL(new Food(new int[]{Items.MACKEREL_355}, new HealingEffect(6))),
    /**
     * The Roast bird meat.
     */
    ROAST_BIRD_MEAT(new Food(new int[]{Items.ROAST_BIRD_MEAT_9980}, new HealingEffect(6))),
    /**
     * The Thin snail meat.
     */
    THIN_SNAIL_MEAT(new Food(new int[]{Items.THIN_SNAIL_MEAT_3369}, new RandomHealthEffect(5, 7))),
    /**
     * The Trout.
     */
    TROUT(new Food(new int[]{Items.TROUT_333}, new HealingEffect(7))),
    /**
     * The Spider on stick.
     */
    SPIDER_ON_STICK(new Food(new int[]{Items.SPIDER_ON_STICK_6297, Items.SKEWER_STICK_6305}, new RandomHealthEffect(7, 10), "You eat the spider. The meat has a silky texture with a delicate web of flavours.")),
    /**
     * The Spider on shaft.
     */
    SPIDER_ON_SHAFT(new Food(new int[]{Items.SPIDER_ON_SHAFT_6299}, new RandomHealthEffect(7, 10))),
    /**
     * The Roast rabbit.
     */
    ROAST_RABBIT(new Food(new int[]{Items.ROAST_RABBIT_7223}, new HealingEffect(7))),
    /**
     * The Lean snail meat.
     */
    LEAN_SNAIL_MEAT(new Food(new int[]{Items.LEAN_SNAIL_MEAT_3371}, new RandomHealthEffect(6, 8))),
    /**
     * The Cod.
     */
    COD(new Food(new int[]{Items.COD_339}, new HealingEffect(7))),
    /**
     * The Pike.
     */
    PIKE(new Food(new int[]{Items.PIKE_351}, new HealingEffect(8))),
    /**
     * The Roast beast meat.
     */
    ROAST_BEAST_MEAT(new Food(new int[]{Items.ROAST_BEAST_MEAT_9988}, new HealingEffect(8))),
    /**
     * The Cooked crab meat.
     */
    COOKED_CRAB_MEAT(new Food(new int[]{Items.COOKED_CRAB_MEAT_7521, Items.COOKED_CRAB_MEAT_7523, Items.COOKED_CRAB_MEAT_7524, Items.COOKED_CRAB_MEAT_7525, Items.COOKED_CRAB_MEAT_7526}, new HealingEffect(10))),
    /**
     * The Fat snail.
     */
    FAT_SNAIL(new Food(new int[]{Items.FAT_SNAIL_MEAT_3373}, new RandomHealthEffect(7, 9))),
    /**
     * The Salmon.
     */
    SALMON(new Food(new int[]{Items.SALMON_329}, new HealingEffect(9))),
    /**
     * The Slimy eel.
     */
    SLIMY_EEL(new Food(new int[]{Items.COOKED_SLIMY_EEL_3381}, new RandomHealthEffect(7, 9))),
    /**
     * The Tuna.
     */
    TUNA(new Food(new int[]{Items.TUNA_361}, new HealingEffect(10))),
    /**
     * The Chopped tuna.
     */
    CHOPPED_TUNA(new Food(new int[]{Items.CHOPPED_TUNA_7086, Items.BOWL_1923}, new HealingEffect(10))),

    /**
     * The Chopped onion.
     */
    CHOPPED_ONION(new Food(new int[]{Items.CHOPPED_ONION_1871, Items.BOWL_1923}, new HealingEffect(1), "It's sad to see a grown man/woman cry.")),
    /**
     * The Cooked karambwan.
     */
    COOKED_KARAMBWAN(new Food(new int[]{Items.COOKED_KARAMBWAN_3144}, new HealingEffect(18)), true),
    /**
     * The Cooked chompy.
     */
    COOKED_CHOMPY(new Food(new int[]{Items.COOKED_CHOMPY_2878}, new HealingEffect(7))),
    /**
     * The Cooked roasted chompy (was never made available in-game).
     */
    ROASTED_CHOMPY(new Food(new int[]{Items.COOKED_CHOMPY_7228}, new HealingEffect(7))),
    /**
     * The Rainbow fish.
     */
    RAINBOW_FISH(new Food(new int[]{Items.RAINBOW_FISH_10136}, new HealingEffect(11))),
    /**
     * The Cave eel.
     */
    CAVE_EEL(new Food(new int[]{Items.CAVE_EEL_5003}, new RandomHealthEffect(8, 10))),
    /**
     * The Caviar.
     */
    CAVIAR(new Food(new int[]{Items.CAVIAR_11326}, new HealingEffect(5))),
    /**
     * The Lobster.
     */
    LOBSTER(new Food(new int[]{Items.LOBSTER_379}, new HealingEffect(12))),
    /**
     * The Cooked jubbly.
     */
    COOKED_JUBBLY(new Food(new int[]{Items.COOKED_JUBBLY_7568}, new HealingEffect(15))),
    /**
     * The Bass.
     */
    BASS(new Food(new int[]{Items.BASS_365}, new HealingEffect(13))),
    /**
     * The Swordfish.
     */
    SWORDFISH(new Food(new int[]{Items.SWORDFISH_373}, new HealingEffect(14))),
    /**
     * The Lava eel.
     */
    LAVA_EEL(new Food(new int[]{Items.LAVA_EEL_2149}, new RandomHealthEffect(9, 11))),
    /**
     * The Monkfish.
     */
    MONKFISH(new Food(new int[]{Items.MONKFISH_7946}, new HealingEffect(16))),
    /**
     * The Shark.
     */
    SHARK(new Food(new int[]{Items.SHARK_385}, new HealingEffect(20))),
    /**
     * The Sea turtle.
     */
    SEA_TURTLE(new Food(new int[]{Items.SEA_TURTLE_397}, new HealingEffect(21))),
    /**
     * The Manta ray.
     */
    MANTA_RAY(new Food(new int[]{Items.MANTA_RAY_391}, new HealingEffect(22))),
    /**
     * The Karambwanji.
     */
    KARAMBWANJI(new Food(new int[]{Items.KARAMBWANJI_3151}, new HealingEffect(3))),
    /**
     * The Stuffed snake.
     */
    STUFFED_SNAKE(new Food(new int[]{Items.STUFFED_SNAKE_7579}, new HealingEffect(20), "You eat the stuffed snake-it's quite a meal! It tastes like chicken.")),
    /**
     * The Crayfish.
     */
    CRAYFISH(new Food(new int[]{Items.CRAYFISH_13433}, new HealingEffect(1))),
    /**
     * The Giant frog legs.
     */
    GIANT_FROG_LEGS(new Food(new int[]{Items.GIANT_FROG_LEGS_4517}, new HealingEffect(6))),
    /**
     * The Bread.
     */
    BREAD(new Food(new int[]{Items.BREAD_2309}, new HealingEffect(5))),
    /**
     * The Baguette.
     */
    BAGUETTE(new Food(new int[]{Items.BAGUETTE_6961}, new HealingEffect(6))),
    /**
     * The Triangle sandwich.
     */
    TRIANGLE_SANDWICH(new Food(new int[]{Items.TRIANGLE_SANDWICH_6962}, new HealingEffect(6))),
    /**
     * The Square sandwich.
     */
    SQUARE_SANDWICH(new Food(new int[]{Items.SQUARE_SANDWICH_6965}, new HealingEffect(6))),
    /**
     * The Seaweed sandwich.
     */
    SEAWEED_SANDWICH(new FakeConsumable(Items.SEAWEED_SANDWICH_3168, new String[]{"You really, really do not want to eat that."})),
    /**
     * The Frogburger.
     */
    FROGBURGER(new Food(new int[]{Items.FROGBURGER_10962}, new HealingEffect(2))),
    /**
     * The Ugthanki kebab.
     */
    UGTHANKI_KEBAB(new Food(new int[]{Items.UGTHANKI_KEBAB_1883}, new UgthankiKebabEffect())),
    /**
     * The Ugthanki kebab smelling.
     */
    UGTHANKI_KEBAB_SMELLING(new Food(new int[]{Items.UGTHANKI_KEBAB_1885}, new SmellingUgthankiKebabEffect())),
    /**
     * The Kebab.
     */
    KEBAB(new Food(new int[]{Items.KEBAB_1971}, new KebabEffect())),
    /**
     * The Super kebab.
     */
    SUPER_KEBAB(new Food(new int[]{Items.SUPER_KEBAB_4608}, new SuperKebabEffect())),
    /**
     * The Redberry pie.
     */
    REDBERRY_PIE(new HalfableFood(new int[]{Items.REDBERRY_PIE_2325, Items.HALF_A_REDBERRY_PIE_2333, Items.PIE_DISH_2313}, new HealingEffect(5))),
    /**
     * The Minced meat.
     */
    MINCED_MEAT(new Food(new int[]{Items.MINCED_MEAT_7070, Items.BOWL_1923}, new HealingEffect(2))),
    /**
     * The Meat pie.
     */
    MEAT_PIE(new HalfableFood(new int[]{Items.MEAT_PIE_2327, Items.HALF_A_MEAT_PIE_2331, Items.PIE_DISH_2313}, new HealingEffect(6))),
    /**
     * The Apple pie.
     */
    APPLE_PIE(new HalfableFood(new int[]{Items.APPLE_PIE_2323, Items.HALF_AN_APPLE_PIE_2335, Items.PIE_DISH_2313}, new HealingEffect(7))),
    /**
     * The Garden pie.
     */
    GARDEN_PIE(new HalfableFood(new int[]{Items.GARDEN_PIE_7178, Items.HALF_A_GARDEN_PIE_7180, Items.PIE_DISH_2313}, new MultiEffect(new HealingEffect(6), new SkillEffect(Skills.FARMING, 3.0, 0.0)))),
    /**
     * The Fish pie.
     */
    FISH_PIE(new HalfableFood(new int[]{Items.FISH_PIE_7188, Items.HALF_A_FISH_PIE_7190, Items.PIE_DISH_2313}, new MultiEffect(new HealingEffect(6), new SkillEffect(Skills.FISHING, 3.0, 0.0)))),
    /**
     * The Admiral pie.
     */
    ADMIRAL_PIE(new HalfableFood(new int[]{Items.ADMIRAL_PIE_7198, Items.HALF_AN_ADMIRAL_PIE_7200, Items.PIE_DISH_2313}, new MultiEffect(new HealingEffect(8), new SkillEffect(Skills.FISHING, 5.0, 0.0)))),
    /**
     * The Wild pie.
     */
    WILD_PIE(new HalfableFood(new int[]{Items.WILD_PIE_7208, Items.HALF_A_WILD_PIE_7210, Items.PIE_DISH_2313}, new MultiEffect(new SkillEffect(Skills.SLAYER, 5.0, 0.0), new SkillEffect(Skills.RANGE, 4.0, 0.0), new HealingEffect(11)))),
    /**
     * The Summer pie.
     */
    SUMMER_PIE(new HalfableFood(new int[]{Items.SUMMER_PIE_7218, Items.HALF_A_SUMMER_PIE_7220, Items.PIE_DISH_2313}, new MultiEffect(new HealingEffect(11), new SkillEffect(Skills.AGILITY, 5.0, 0.0), new EnergyEffect(10)))),
    /**
     * The Peach.
     */
    PEACH(new Food(new int[]{Items.PEACH_6883}, new HealingEffect(8))),
    /**
     * The White tree fruit.
     */
    WHITE_TREE_FRUIT(new Food(new int[]{Items.WHITE_TREE_FRUIT_6469}, new MultiEffect(new RandomEnergyEffect(5, 10), new HealingEffect(2)))),
    /**
     * The Strange fruit.
     */
    STRANGE_FRUIT(new Food(new int[]{Items.STRANGE_FRUIT_464}, new MultiEffect(new RemoveTimerEffect("poison"), new EnergyEffect(30)), "You eat the fruit.", "It tastes great; some of your run energy is restored!")),
    /**
     * The Toad crunchies.
     */
    TOAD_CRUNCHIES(new Food(new int[]{Items.TOAD_CRUNCHIES_2217}, new HealingEffect(12))),
    /**
     * The Premade td crunch.
     */
    PREMADE_TD_CRUNCH(new Food(new int[]{Items.PREMADE_TD_CRUNCH_2243}, new HealingEffect(12))),
    /**
     * The Spicy crunchies.
     */
    SPICY_CRUNCHIES(new Food(new int[]{Items.SPICY_CRUNCHIES_2213}, new HealingEffect(7))),
    /**
     * The Premade sy crunch.
     */
    PREMADE_SY_CRUNCH(new Food(new int[]{Items.PREMADE_SY_CRUNCH_2241}, new HealingEffect(7))),
    /**
     * The Worm crunchies.
     */
    WORM_CRUNCHIES(new Food(new int[]{Items.WORM_CRUNCHIES_2205}, new HealingEffect(8))),
    /**
     * The Premade wm crunc.
     */
    PREMADE_WM_CRUNC(new Food(new int[]{Items.PREMADE_WM_CRUN_2237}, new HealingEffect(8))),
    /**
     * The Chocchip crunchies.
     */
    CHOCCHIP_CRUNCHIES(new Food(new int[]{Items.CHOCCHIP_CRUNCHIES_2209}, new HealingEffect(7))),
    /**
     * The Premade ch crunch.
     */
    PREMADE_CH_CRUNCH(new Food(new int[]{Items.PREMADE_CH_CRUNCH_2239}, new HealingEffect(7))),
    /**
     * The Fruit batta.
     */
    FRUIT_BATTA(new Food(new int[]{Items.FRUIT_BATTA_2277}, new HealingEffect(11))),
    /**
     * The Premade frt batta.
     */
    PREMADE_FRT_BATTA(new Food(new int[]{Items.PREMADE_FRT_BATTA_2225}, new HealingEffect(11))),
    /**
     * The Toad batta.
     */
    TOAD_BATTA(new Food(new int[]{Items.TOAD_BATTA_2255}, new HealingEffect(11))),
    /**
     * The Premade td batta.
     */
    PREMADE_TD_BATTA(new Food(new int[]{Items.PREMADE_TD_BATTA_2221}, new HealingEffect(11))),
    /**
     * The Worm batta.
     */
    WORM_BATTA(new Food(new int[]{Items.WORM_BATTA_2253}, new HealingEffect(11))),
    /**
     * The Premade wm batta.
     */
    PREMADE_WM_BATTA(new Food(new int[]{Items.PREMADE_WM_BATTA_2219}, new HealingEffect(11))),
    /**
     * The Vegetable batta.
     */
    VEGETABLE_BATTA(new Food(new int[]{Items.VEGETABLE_BATTA_2281}, new HealingEffect(11))),
    /**
     * The Premade veg batta.
     */
    PREMADE_VEG_BATTA(new Food(new int[]{Items.PREMADE_VEG_BATTA_2227}, new HealingEffect(11))),
    /**
     * The Cheese and tomatoes batta.
     */
    CHEESE_AND_TOMATOES_BATTA(new Food(new int[]{Items.CHEESE_PLUSTOM_BATTA_2259}, new HealingEffect(11))),
    /**
     * The Premade ct batta.
     */
    PREMADE_CT_BATTA(new Food(new int[]{Items.PREMADE_C_PLUST_BATTA_2223}, new HealingEffect(11))),
    /**
     * The Worm hole.
     */
    WORM_HOLE(new Food(new int[]{Items.WORM_HOLE_2191}, new HealingEffect(12))),
    /**
     * The Premade worm hole.
     */
    PREMADE_WORM_HOLE(new Food(new int[]{Items.PREMADE_WORM_HOLE_2233}, new HealingEffect(12))),
    /**
     * The Veg ball.
     */
    VEG_BALL(new Food(new int[]{Items.VEG_BALL_2195}, new HealingEffect(12))),
    /**
     * The Premade veg ball.
     */
    PREMADE_VEG_BALL(new Food(new int[]{Items.PREMADE_VEG_BALL_2235}, new HealingEffect(12))),
    /**
     * The Tangled toads legs.
     */
    TANGLED_TOADS_LEGS(new Food(new int[]{Items.TANGLED_TOADS_LEGS_2187}, new HealingEffect(15))),
    /**
     * The Premade ttl.
     */
    PREMADE_TTL(new Food(new int[]{Items.PREMADE_TTL_2231}, new HealingEffect(15))),
    /**
     * The Chocolate bomb.
     */
    CHOCOLATE_BOMB(new Food(new int[]{Items.VEG_BALL_2195}, new HealingEffect(15))),
    /**
     * The Premade choc bomb.
     */
    PREMADE_CHOC_BOMB(new Food(new int[]{Items.PREMADE_CHOC_BOMB_2229}, new HealingEffect(15))),
    /**
     * The Stew.
     */
    STEW(new Food(new int[]{Items.STEW_2003, Items.BOWL_1923}, new HealingEffect(11))),
    /**
     * The Spicy stew.
     */
    SPICY_STEW(new Food(new int[]{Items.SPICY_STEW_7479, Items.BOWL_1923}, new HealingEffect(11))),
    /**
     * The Curry.
     */
    CURRY(new Food(new int[]{Items.CURRY_2011, Items.BOWL_1923}, new HealingEffect(19))),
    /**
     * The Banana stew.
     */
    BANANA_STEW(new Food(new int[]{Items.BANANA_STEW_4016, Items.BOWL_1923}, new HealingEffect(11))),
    /**
     * The Plain pizza.
     */
    PLAIN_PIZZA(new HalfableFood(new int[]{Items.PLAIN_PIZZA_2289, Items.HALF_PLAIN_PIZZA_2291}, new HealingEffect(7))),
    /**
     * The Meat pizza.
     */
    MEAT_PIZZA(new HalfableFood(new int[]{Items.MEAT_PIZZA_2293, Items.HALF_MEAT_PIZZA_2295}, new HealingEffect(8))),
    /**
     * The Anchovy pizza.
     */
    ANCHOVY_PIZZA(new HalfableFood(new int[]{Items.ANCHOVY_PIZZA_2297, Items.HALF_ANCHOVY_PIZZA_2299}, new HealingEffect(9))),
    /**
     * The White pearl.
     */
    WHITE_PEARL(new HalfableFood(new int[]{Items.WHITE_PEARL_4485, Items.WHITE_PEARL_SEED_4486}, new HealingEffect(2), "You eat the white pearl and spit out the seed when you're done. Mmm, tasty.")),
    /**
     * The Pineapple pizza.
     */
    PINEAPPLE_PIZZA(new HalfableFood(new int[]{Items.PINEAPPLE_PIZZA_2301, Items.HALF_PAPPLE_PIZZA_2303}, new HealingEffect(11))),
    /**
     * The Cake.
     */
    CAKE(new Cake(new int[]{Items.CAKE_1891, Items.TWO_THIRDS_CAKE_1893, Items.SLICE_OF_CAKE_1895}, new HealingEffect(4), "You eat part of the cake.", "You eat some more cake.", "You eat the slice of cake.")),
    /**
     * The Chocolate cake.
     */
    CHOCOLATE_CAKE(new Cake(new int[]{Items.CHOCOLATE_CAKE_1897, Items.TWO_THIRDS_CHOCOLATE_CAKE_1899, Items.CHOCOLATE_SLICE_1901}, new HealingEffect(5), "You eat part of the chocolate cake.", "You eat some more of the chocolate cake.", "You eat the slice of cake.")),
    /**
     * The Rock cake.
     */
    ROCK_CAKE(new Food(new int[]{Items.ROCK_CAKE_2379}, new RockCakeEffect(), "The rock cake resists all attempts to eat it.")),
    /**
     * The Cave nightshade.
     */
    CAVE_NIGHTSHADE(new Food(new int[]{Items.CAVE_NIGHTSHADE_2398}, new DamageEffect(15.0, false), "Ahhhh! What have I done!")),
    /**
     * The Dwarven rock cake.
     */
    DWARVEN_ROCK_CAKE(new Food(new int[]{Items.DWARVEN_ROCK_CAKE_7510, Items.DWARVEN_ROCK_CAKE_7510}, new DwarvenRockCakeEffect(), "Ow! You nearly broke a tooth!", "The rock cake resists all attempts to eat it.")),
    /**
     * The Hot dwarven rock cake.
     */
    HOT_DWARVEN_ROCK_CAKE(new Food(new int[]{Items.DWARVEN_ROCK_CAKE_7509, Items.DWARVEN_ROCK_CAKE_7509}, new DwarvenRockCakeEffect(), "Ow! You nearly broke a tooth!", "The rock cake resists all attempts to eat it.")),
    /**
     * The Cooked fishcake.
     */
    COOKED_FISHCAKE(new Food(new int[]{Items.COOKED_FISHCAKE_7530}, new HealingEffect(11))),
    /**
     * The Mint cake.
     */
    MINT_CAKE(new Food(new int[]{Items.MINT_CAKE_9475}, new EnergyEffect(50))),
    /**
     * The Potato.
     */
    POTATO(new Food(new int[]{Items.POTATO_1942}, new HealingEffect(1), "You eat the potato. Yuck!")),
    /**
     * The Baked potato.
     */
    BAKED_POTATO(new Food(new int[]{Items.BAKED_POTATO_6701}, new HealingEffect(4))),
    /**
     * The Poisoned cheese.
     */
    POISONED_CHEESE(new FakeConsumable(Items.POISONED_CHEESE_6768, new String[]{"Ummm... let me think about this one.....No! That would be stupid."})),
    /**
     * The Poison chalice.
     */
    POISON_CHALICE(new Drink(new int[]{Items.POISON_CHALICE_197, Items.COCKTAIL_GLASS_2026}, new PoisonChaliceEffect(), "You drink the strange green liquid.")),
    /**
     * The Spicy sauce.
     */
    SPICY_SAUCE(new Food(new int[]{Items.SPICY_SAUCE_7072, Items.BOWL_1923}, new HealingEffect(2))),
    /**
     * The Locust meat.
     */
    LOCUST_MEAT(new Food(new int[]{Items.LOCUST_MEAT_9052}, new HealingEffect(3), "Juices spurt into your mouth as you chew. It's tastier than it looks.")),
    /**
     * The Chilli con carne.
     */
    CHILLI_CON_CARNE(new Food(new int[]{Items.CHILLI_CON_CARNE_7062, Items.BOWL_1923}, new HealingEffect(5))),
    /**
     * The Scrambled egg.
     */
    SCRAMBLED_EGG(new Food(new int[]{Items.SCRAMBLED_EGG_7078, Items.BOWL_1923}, new HealingEffect(5))),
    /**
     * The Egg and tomato.
     */
    EGG_AND_TOMATO(new Food(new int[]{Items.EGG_AND_TOMATO_7064, Items.BOWL_1923}, new HealingEffect(8))),
    /**
     * The Sweet corn.
     */
    SWEET_CORN(new Food(new int[]{Items.COOKED_SWEETCORN_5988}, new MultiEffect(new HealingEffect(1), new PercentageHealthEffect(10)))),
    /**
     * The Sweetcorn bowl.
     */
    SWEETCORN_BOWL(new Food(new int[]{Items.SWEETCORN_7088, Items.BOWL_1923}, new MultiEffect(new HealingEffect(1), new PercentageHealthEffect(10)))),
    /**
     * The Potato with butter.
     */
    POTATO_WITH_BUTTER(new Food(new int[]{Items.POTATO_WITH_BUTTER_6703}, new HealingEffect(7))),
    /**
     * The Chilli potato.
     */
    CHILLI_POTATO(new Food(new int[]{Items.CHILLI_POTATO_7054}, new HealingEffect(14))),
    /**
     * The Fried onions.
     */
    FRIED_ONIONS(new Food(new int[]{Items.FRIED_ONIONS_7084, Items.BOWL_1923}, new HealingEffect(5))),
    /**
     * The Fried mushrooms.
     */
    FRIED_MUSHROOMS(new Food(new int[]{Items.FRIED_MUSHROOMS_7082, Items.BOWL_1923}, new HealingEffect(5))),
    /**
     * The Black mushroom.
     */
    BLACK_MUSHROOM(new FakeConsumable(Items.BLACK_MUSHROOM_4620, new String[]{"Eugh! It tastes horrible, and stains your fingers black."})),
    /**
     * The chopped tomato.
     */
    CHOPPED_TOMATO(new Food(new int[]{Items.CHOPPED_TOMATO_1869, Items.BOWL_1923}, new HealingEffect(2))),
    /**
     * The Onion and tomato.
     */
    ONION_AND_TOMATO(new Food(new int[]{Items.ONION_AND_TOMATO_1875, Items.BOWL_1923}, new HealingEffect(2))),
    /**
     * The Potato with cheese.
     */
    POTATO_WITH_CHEESE(new Food(new int[]{Items.POTATO_WITH_CHEESE_6705}, new HealingEffect(16))),
    /**
     * The Egg potato.
     */
    EGG_POTATO(new Food(new int[]{Items.EGG_POTATO_7056}, new HealingEffect(11))),
    /**
     * The Mushrooms and onions.
     */
    MUSHROOMS_AND_ONIONS(new Food(new int[]{Items.MUSHROOM_AND_ONION_7066, Items.BOWL_1923}, new HealingEffect(11))),
    /**
     * The Mushroom potato.
     */
    MUSHROOM_POTATO(new Food(new int[]{Items.MUSHROOM_POTATO_7058}, new HealingEffect(20))),
    /**
     * The Tuna and corn.
     */
    TUNA_AND_CORN(new Food(new int[]{Items.TUNA_AND_CORN_7068, Items.BOWL_1923}, new HealingEffect(13))),
    /**
     * The Tuna potato.
     */
    TUNA_POTATO(new Food(new int[]{Items.TUNA_POTATO_7060}, new HealingEffect(22))),
    /**
     * The Onion.
     */
    ONION(new Food(new int[]{Items.ONION_1957}, new HealingEffect(2), "It's always sad to see a grown man/woman cry.")),
    /**
     * The Cabbage.
     */
    CABBAGE(new Food(new int[]{Items.CABBAGE_1965}, new HealingEffect(2), "You eat the cabbage. Yuck!")),
    /**
     * The Draynor cabbage.
     */
    DRAYNOR_CABBAGE(new Food(new int[]{Items.CABBAGE_1967}, new DraynorCabbageEffect(), "You eat the cabbage.", "It seems to taste nicer than normal.")),
    /**
     * The Rotten apple.
     */
    ROTTEN_APPLE(new Food(new int[]{Items.ROTTEN_APPLE_1984}, new HealingEffect(2), "It's rotten, you spit it out.")),
    /**
     * The Evil turnip.
     */
    EVIL_TURNIP(new Food(new int[]{Items.EVIL_TURNIP_12134, Items.TWO_THIRDS_EVIL_TURNIP_12136, Items.ONE_THIRD_EVIL_TURNIP_12138}, new HealingEffect(6))),
    /**
     * The Spinach roll.
     */
    SPINACH_ROLL(new Food(new int[]{Items.SPINACH_ROLL_1969}, new HealingEffect(2), "It tastes a bit weird, but it fills you up.")),
    /**
     * The Pot of cream.
     */
    POT_OF_CREAM(new Food(new int[]{Items.POT_OF_CREAM_2130}, new HealingEffect(1), "You eat the cream. You get some on your nose.")),
    /**
     * The Cheese.
     */
    CHEESE(new Food(new int[]{Items.CHEESE_1985}, new HealingEffect(2))),
    /**
     * The Chocolatey milk.
     */
    CHOCOLATEY_MILK(new Drink(new int[]{Items.CHOCOLATEY_MILK_1977, Items.BUCKET_1925}, new HealingEffect(4))),
    /**
     * The Banana.
     */
    BANANA(new Food(new int[]{Items.BANANA_1963}, new HealingEffect(2))),
    /**
     * The Sliced banana.
     */
    SLICED_BANANA(new Food(new int[]{Items.SLICED_BANANA_3162}, new HealingEffect(2))),
    /**
     * The Red banana.
     */
    RED_BANANA(new Food(new int[]{Items.RED_BANANA_7572}, new HealingEffect(5), "You eat the red banana. It's tastier than your average banana.")),
    /**
     * The Sliced red banana.
     */
    SLICED_RED_BANANA(new Food(new int[]{Items.SLICED_RED_BANANA_7574}, new HealingEffect(5), "You eat the sliced red banana. Yum.")),
    /**
     * The Orange.
     */
    ORANGE(new Food(new int[]{Items.ORANGE_2108}, new HealingEffect(2))),
    /**
     * The Orange chunks.
     */
    ORANGE_CHUNKS(new Food(new int[]{Items.ORANGE_CHUNKS_2110}, new HealingEffect(2))),
    /**
     * The Orange slices.
     */
    ORANGE_SLICES(new Food(new int[]{Items.ORANGE_SLICES_2112}, new HealingEffect(2))),
    /**
     * The Papaya fruit.
     */
    PAPAYA_FRUIT(new Food(new int[]{Items.PAPAYA_FRUIT_5972}, new HealingEffect(2))),
    /**
     * The Tenti pineapple.
     */
    TENTI_PINEAPPLE(new FakeConsumable(Items.TENTI_PINEAPPLE_1851, new String[]{"Try using a knife to slice it into pieces."})),
    /**
     * The Pineapple.
     */
    PINEAPPLE(new FakeConsumable(Items.PINEAPPLE_2114, new String[]{"Try using a knife to slice it into pieces."})),
    /**
     * The Pineapple chunks.
     */
    PINEAPPLE_CHUNKS(new Food(new int[]{Items.PINEAPPLE_CHUNKS_2116}, new HealingEffect(2))),
    /**
     * The Pineapple ring.
     */
    PINEAPPLE_RING(new Food(new int[]{Items.PINEAPPLE_RING_2118}, new HealingEffect(2))),
    /**
     * The Dwellberries.
     */
    DWELLBERRIES(new Food(new int[]{Items.DWELLBERRIES_2126}, new HealingEffect(2))),
    /**
     * The Jangerberries.
     */
    JANGERBERRIES(new Food(new int[]{Items.FLOWERS_2470}, new MultiEffect(new SkillEffect(Skills.ATTACK, 2.0, 0.0), new SkillEffect(Skills.STRENGTH, 1.0, 0.0), new PrayerEffect(1.0, 0.0), new SkillEffect(Skills.DEFENCE, -1.0, 0.0)), "You eat the jangerberries.", "They taste very bitter.")),
    /**
     * The Strawberry.
     */
    STRAWBERRY(new Food(new int[]{Items.STRAWBERRY_5504}, new MultiEffect(new HealingEffect(1), new PercentageHealthEffect(6)))),
    /**
     * The Tomato.
     */
    TOMATO(new Food(new int[]{Items.TOMATO_1982}, new HealingEffect(2))),
    /**
     * The Watermelon.
     */
    WATERMELON(new FakeConsumable(Items.WATERMELON_5982, new String[]{"You can't eat it whole; maybe you should cut it up."})),
    /**
     * The Watermelon slice.
     */
    WATERMELON_SLICE(new Food(new int[]{Items.WATERMELON_SLICE_5984}, new PercentageHealthEffect(5))),
    /**
     * The Lemon.
     */
    LEMON(new Food(new int[]{Items.LEMON_2102}, new HealingEffect(2))),
    /**
     * The Lemon chunks.
     */
    LEMON_CHUNKS(new Food(new int[]{Items.LEMON_CHUNKS_2104}, new HealingEffect(2))),
    /**
     * The Lemon slices.
     */
    LEMON_SLICES(new Food(new int[]{Items.LEMON_SLICES_2106}, new HealingEffect(2))),
    /**
     * The Toad legs.
     */
    TOAD_LEGS(new Food(new int[] {Items.TOADS_LEGS_2152}, new HealingEffect(3), "You eat the toad's legs. They're a bit chewy.")),
    /**
     * The King worm.
     */
    KING_WORM(new Food(new int[] {Items.KING_WORM_2162}, new HealingEffect(2))),
    /**
     * The Asgoldian ale.
     */
    ASGOLDIAN_ALE(new FakeConsumable(Items.ASGOLDIAN_ALE_7508, new String[] {"I don't think I'd like gold in beer thanks. Leave it for the dwarves."})),
    /**
     * The Asgarnian ale.
     */
    ASGARNIAN_ALE(new Drink(new int[] {Items.ASGARNIAN_ALE_1905, Items.BEER_GLASS_1919}, new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.STRENGTH, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.")),
    /**
     * The Asgarnian ale keg.
     */
    ASGARNIAN_ALE_KEG(new Drink(new int[] {Items.ASGARNIAN_ALE4_5785, Items.ASGARNIAN_ALE3_5783, Items.ASGARNIAN_ALE2_5781, Items.ASGARNIAN_ALE1_5779, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), new Animation(2289), "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.")),
    /**
     * The Asgarnian ale m.
     */
    ASGARNIAN_ALE_M(new Drink(new int[] {Items.ASGARNIAN_ALEM_5739, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -6.0, 0.0)))),
    /**
     * The Asgarnian ale m keg.
     */
    ASGARNIAN_ALE_M_KEG(new Drink(new int[] {Items.ASGARNIAN_ALEM4_5865, Items.ASGARNIAN_ALEM3_5863, Items.ASGARNIAN_ALEM2_5861, Items.ASGARNIAN_ALEM1_5859, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -6.0, 0.0)), new Animation(2289))),
    /**
     * The Axemans folly.
     */
    AXEMANS_FOLLY(new Drink(new int[] {Items.AXEMANS_FOLLY_5751, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.WOODCUTTING, 1.0, 0.0), new HealingEffect(1), new SkillEffect(Skills.STRENGTH, -3.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0)))),
    /**
     * The Axemans folly keg.
     */
    AXEMANS_FOLLY_KEG(new Drink(new int[] {Items.AXEMANS_FOLLY4_5825, Items.AXEMANS_FOLLY3_5823, Items.AXEMANS_FOLLY2_5821, Items.AXEMANS_FOLLY1_5819, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.WOODCUTTING, 1.0, 0.0), new HealingEffect(1), new SkillEffect(Skills.STRENGTH, -3.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0)), new Animation(2289))),
    /**
     * The Axemans folly m.
     */
    AXEMANS_FOLLY_M(new Drink(new int[] {Items.AXEMANS_FOLLYM_5753, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.WOODCUTTING, 2.0, 0.0), new HealingEffect(2), new SkillEffect(Skills.STRENGTH, -4.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Axemans folly m keg.
     */
    AXEMANS_FOLLY_M_KEG(new Drink(new int[] {Items.AXEMANS_FOLLYM4_5905, Items.AXEMANS_FOLLYM3_5903, Items.AXEMANS_FOLLYM2_5901, Items.AXEMANS_FOLLYM1_5899, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.WOODCUTTING, 2.0, 0.0), new HealingEffect(2), new SkillEffect(Skills.STRENGTH, -4.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), new Animation(2289))),
    /**
     * The Bandits brew.
     */
    BANDITS_BREW(new Drink(new int[] {Items.BANDITS_BREW_4627, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.THIEVING, 1.0, 0.0), new SkillEffect(Skills.ATTACK, 1.0, 0.0), new SkillEffect(Skills.STRENGTH, -1.0, 0.0), new SkillEffect(Skills.DEFENCE, -6.0, 0.0), new HealingEffect(1)), "You drink the beer. You feel slightly reinvigorated...", "...and slightly dizzy too.")),
    /**
     * The Beer.
     */
    BEER(new Drink(new int[] {Items.BEER_1917, Items.BEER_GLASS_1919}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.STRENGTH, 0.0, 0.04), new SkillEffect(Skills.ATTACK, 0.0, -0.07)), "You drink the beer. You feel slightly reinvigorated...", "...and slightly dizzy too.")),
    /**
     * The Beer tankard.
     */
    BEER_TANKARD(new Drink(new int[] {Items.BEER_3803, Items.TANKARD_3805}, new MultiEffect(new SkillEffect(Skills.ATTACK, -9.0, 0.0), new SkillEffect(Skills.STRENGTH, 4.0, 0.0)), "You quaff the beer. You feel slightly reinvigorated...", "...but very dizzy too.")),
    /**
     * The Keg of beer.
     */
    KEG_OF_BEER(new Drink(new int[] {Items.KEG_OF_BEER_3801}, new KegOfBeerEffect(), new Animation(1330), "You chug the keg. You feel reinvigorated...", "...but extremely drunk too.")),
    /**
     * The Chefs delight.
     */
    CHEFS_DELIGHT(new Drink(new int[] {Items.CHEFS_DELIGHT_5755, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.COOKING, 1.0, 0.05), new HealingEffect(1), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)))),
    /**
     * The Chefs delight keg.
     */
    CHEFS_DELIGHT_KEG(new Drink(new int[] {Items.CHEFS_DELIGHT4_5833, Items.CHEFS_DELIGHT3_5831, Items.CHEFS_DELIGHT2_5829, Items.CHEFS_DELIGHT1_5827, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.COOKING, 1.0, 0.05), new HealingEffect(1), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)), new Animation(2289))),
    /**
     * The Chefs delight m.
     */
    CHEFS_DELIGHT_M(new Drink(new int[] {Items.CHEFS_DELIGHTM_5757, Items.BEER_GLASS_1919}, new MultiEffect(new SkillEffect(Skills.COOKING, 2.0, 0.05), new HealingEffect(2), new SkillEffect(Skills.ATTACK, -3.0, 0.0), new SkillEffect(Skills.STRENGTH, -3.0, 0.0)))),
    /**
     * The Chefs delight m keg.
     */
    CHEFS_DELIGHT_M_KEG(new Drink(new int[] {Items.CHEFS_DELIGHTM4_5913, Items.CHEFS_DELIGHTM3_5911, Items.CHEFS_DELIGHTM2_5909, Items.CHEFS_DELIGHTM1_5907, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.COOKING, 2.0, 0.05), new HealingEffect(2), new SkillEffect(Skills.ATTACK, -3.0, 0.0), new SkillEffect(Skills.STRENGTH, -3.0, 0.0)), new Animation(2289))),
    /**
     * The Cider.
     */
    CIDER(new Drink(new int[] {Items.CIDER_5763, Items.BEER_GLASS_1919}, new MultiEffect(new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.FARMING, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0))))),
    /**
     * The Cider keg.
     */
    CIDER_KEG(new Drink(new int[] {Items.CIDER4_5849, Items.CIDER3_5847, Items.CIDER2_5845, Items.CIDER1_5843, Items.CALQUAT_KEG_5769}, new MultiEffect(new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.FARMING, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0))), new Animation(2289))),
    /**
     * The Mature cider.
     */
    MATURE_CIDER(new Drink(new int[] { Items.MATURE_CIDER_5765, Items.BEER_GLASS_1919 }, new MultiEffect(new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.FARMING, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -5.0, 0.0), new SkillEffect(Skills.STRENGTH, -5.0, 0.0))))),
    /**
     * The Cider m keg.
     */
    CIDER_M_KEG(new Drink(new int[] {Items.CIDERM4_5929, Items.CIDERM3_5927, Items.CIDERM2_5925, Items.CIDERM1_5923, Items.CALQUAT_KEG_5769}, new MultiEffect(new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.FARMING, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -5.0, 0.0), new SkillEffect(Skills.STRENGTH, -5.0, 0.0))), new Animation(2289))),
    /**
     * The Dragon bitter.
     */
    DRAGON_BITTER(new Drink(new int[] { Items.DRAGON_BITTER_1911, Items.BEER_GLASS_1919 }, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.STRENGTH, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), "You drink the Dragon Bitter. You feel slightly reinvigorated...", "...and slightly dizzy too.")),
    /**
     * The Dragon bitter keg.
     */
    DRAGON_BITTER_KEG(new Drink(new int[] {Items.DRAGON_BITTER4_5809, Items.DRAGON_BITTER3_5807, Items.DRAGON_BITTER2_5805, Items.DRAGON_BITTER1_5803, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.STRENGTH, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), new Animation(2289))),
    /**
     * The Dragon bitter m.
     */
    DRAGON_BITTER_M(new Drink(new int[] { Items.DRAGON_BITTERM_5745, Items.BEER_GLASS_1919 }, new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -6.0, 0.0)))),
    /**
     * The Dragon bitter m keg.
     */
    DRAGON_BITTER_M_KEG(new Drink(new int[] {Items.DRAGON_BITTERM4_5889, Items.DRAGON_BITTERM3_5887, Items.DRAGON_BITTERM2_5885, Items.DRAGON_BITTERM1_5883, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -6.0, 0.0)), new Animation(2289))),
    /**
     * The Dwarven stout.
     */
    DWARVEN_STOUT(new Drink(new int[] { Items.DWARVEN_STOUT_1913, Items.BEER_GLASS_1919 }, new MultiEffect(new SkillEffect(Skills.MINING, 1.0, 0.0), new SkillEffect(Skills.SMITHING, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0), new SkillEffect(Skills.DEFENCE, -2.0, 0.0), new HealingEffect(1)), "You drink the Dwarven Stout. It tastes foul.", "It tastes pretty strong too.")),
    /**
     * The Dwarven stout keg.
     */
    DWARVEN_STOUT_KEG(new Drink(new int[] {Items.DWARVEN_STOUT4_5777, Items.DWARVEN_STOUT3_5775, Items.DWARVEN_STOUT2_5773, Items.DWARVEN_STOUT1_5771, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.MINING, 1.0, 0.0), new SkillEffect(Skills.SMITHING, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0), new SkillEffect(Skills.DEFENCE, -2.0, 0.0), new HealingEffect(1)), new Animation(2289), "You drink the Dwarven Stout. It tastes foul.", "It tastes pretty strong too.")),
    /**
     * The Dwarven stout m.
     */
    DWARVEN_STOUT_M(new Drink(new int[] { Items.DWARVEN_STOUTM_5747, Items.BEER_GLASS_1919 }, new MultiEffect(new SkillEffect(Skills.MINING, 2.0, 0.0), new SkillEffect(Skills.SMITHING, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -7.0, 0.0), new SkillEffect(Skills.STRENGTH, -7.0, 0.0), new SkillEffect(Skills.DEFENCE, -7.0, 0.0), new HealingEffect(1)))),
    /**
     * The Dwarven stout m keg.
     */
    DWARVEN_STOUT_M_KEG(new Drink(new int[] {Items.DWARVEN_STOUTM4_5857, Items.DWARVEN_STOUTM3_5855, Items.DWARVEN_STOUTM2_5853, Items.DWARVEN_STOUTM1_5851, Items.CALQUAT_KEG_5769}, new MultiEffect(new SkillEffect(Skills.MINING, 2.0, 0.0), new SkillEffect(Skills.SMITHING, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -7.0, 0.0), new SkillEffect(Skills.STRENGTH, -7.0, 0.0), new SkillEffect(Skills.DEFENCE, -7.0, 0.0), new HealingEffect(1)), new Animation(2289))),
    /**
     * The Greenmans ale.
     */
    GREENMANS_ALE(new Drink(new int[] { Items.GREENMANS_ALE_1909, Items.BEER_GLASS_1919 }, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.HERBLORE, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0), new SkillEffect(Skills.STRENGTH, -3.0, 0.0), new SkillEffect(Skills.DEFENCE, -3.0, 0.0)))),
    /**
     * The Greenmans ale keg.
     */
    GREENMANS_ALE_KEG(new Drink(new int[] {Items.GREENMANS_ALE4_5793, Items.GREENMANS_ALE3_5791, Items.GREENMANS_ALE2_5789, Items.GREENMANS_ALE1_5787, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.HERBLORE, 1.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0), new SkillEffect(Skills.STRENGTH, -3.0, 0.0), new SkillEffect(Skills.DEFENCE, -3.0, 0.0)), new Animation(2289))),
    /**
     * The Greenmans ale m.
     */
    GREENMANS_ALE_M(new Drink(new int[] { Items.GREENMANS_ALEM_5743, Items.BEER_GLASS_1919 }, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.HERBLORE, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)))),
    /**
     * The Greenmans ale m keg.
     */
    GREENMANS_ALE_M_KEG(new Drink(new int[] {Items.GREENMANS_ALEM4_5873, Items.GREENMANS_ALEM3_5871, Items.GREENMANS_ALEM2_5869, Items.GREENMANS_ALEM1_5867, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.HERBLORE, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)), new Animation(2289))),
    /**
     * The Grog.
     */
    GROG(new Drink(new int[] { Items.GROG_1915, Items.BEER_GLASS_1919 }, new MultiEffect(new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -6.0, 0.0)))),
    /**
     * The Moonlight mead.
     */
    MOONLIGHT_MEAD(new Drink(new int[] { Items.MOONLIGHT_MEAD_2955, Items.BEER_GLASS_1919 }, new HealingEffect(4), "It tastes like something just died in your mouth.")),
    /**
     * The Moonlight mead keg.
     */
    MOONLIGHT_MEAD_KEG(new Drink(new int[] {Items.MOONLIGHT_MEAD4_5817, Items.MOONLIGHT_MEAD3_5815, Items.MOONLIGHT_MEAD2_5813, Items.MOONLIGHT_MEAD1_5811, Items.CALQUAT_KEG_5769}, new HealingEffect(4), new Animation(2289), "It tastes like something just died in your mouth.")),
    /**
     * The Moonlight mead m.
     */
    MOONLIGHT_MEAD_M(new Drink(new int[] { Items.MOONLIGHT_MEADM_5749, Items.BEER_GLASS_1919 }, new HealingEffect(6))),
    /**
     * The Moonlight mead m keg.
     */
    MOONLIGHT_MEAD_M_KEG(new Drink(new int[] {Items.MLIGHT_MEADM4_5897, Items.MLIGHT_MEADM3_5895, Items.MLIGHT_MEADM2_5893, Items.MLIGHT_MEADM1_5891, Items.CALQUAT_KEG_5769}, new HealingEffect(6), new Animation(2289))),
    /**
     * The Slayers respite.
     */
    SLAYERS_RESPITE(new Drink(new int[] {Items.SLAYERS_RESPITE_5759, Items.BEER_GLASS_1919}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.SLAYER, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)))),
    /**
     * The Slayers respite keg.
     */
    SLAYERS_RESPITE_KEG(new Drink(new int[] {Items.SLAYERS_RESPITE4_5841, Items.SLAYERS_RESPITE3_5839, Items.SLAYERS_RESPITE2_5837, Items.SLAYERS_RESPITE1_5835, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.SLAYER, 2.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)), new Animation(2289))),
    /**
     * The Slayers respite m.
     */
    SLAYERS_RESPITE_M(new Drink(new int[] {Items.SLAYERS_RESPITEM_5761, Items.BEER_GLASS_1919}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.SLAYER, 4.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)))),
    /**
     * The Slayers respite m keg.
     */
    SLAYERS_RESPITE_M_KEG(new Drink(new int[] {Items.SLAYERS_RESPITE4_5841, Items.SLAYERS_RESPITE3_5839, Items.SLAYERS_RESPITE2_5837, Items.SLAYERS_RESPITE1_5835, Items.CALQUAT_KEG_5769}, new MultiEffect(new HealingEffect(1), new SkillEffect(Skills.SLAYER, 4.0, 0.0), new SkillEffect(Skills.ATTACK, -2.0, 0.0), new SkillEffect(Skills.STRENGTH, -2.0, 0.0)), new Animation(2289))),
    /**
     * The Wizards mind bomb.
     */
    WIZARDS_MIND_BOMB(new Drink(new int[] {Items.WIZARDS_MIND_BOMB_1907, Items.BEER_GLASS_1919}, new WizardsMindBombEffect(), "You drink the Wizard's Mind Bomb.", "You feel very strange.")),
    /**
     * The Mature wmb.
     */
    MATURE_WMB(new Drink(new int[] {Items.MATURE_WMB_5741, Items.BEER_GLASS_1919}, new MatureWmbEffect())),
    /**
     * The Fruit blast.
     */
    FRUIT_BLAST(new Drink(new int[] {Items.FRUIT_BLAST_2084, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9))),
    /**
     * The Premade fr blast.
     */
    PREMADE_FR_BLAST(new Drink(new int[] {Items.PREMADE_FR_BLAST_2034, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9))),
    /**
     * The Crafted fr blast.
     */
    CRAFTED_FR_BLAST(new Drink(new int[] {Items.FRUIT_BLAST_9514, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9))),
    /**
     * The Pineapple punch.
     */
    PINEAPPLE_PUNCH(new Drink(new int[] {Items.PINEAPPLE_PUNCH_2048, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9), "You drink the cocktail. It tastes great.")),
    /**
     * The Premade p punch.
     */
    PREMADE_P_PUNCH(new Drink(new int[] {Items.PREMADE_P_PUNCH_2036, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9), "You drink the cocktail. It tastes great.")),
    /**
     * The Crafted p punch.
     */
    CRAFTED_P_PUNCH(new Drink(new int[] {Items.PINEAPPLE_PUNCH_9512, Items.COCKTAIL_GLASS_2026}, new HealingEffect(9))),
    /**
     * The Wizard blizzard.
     */
    WIZARD_BLIZZARD(new Drink(new int[] {Items.WIZARD_BLIZZARD_2054, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Premade wiz blzd.
     */
    PREMADE_WIZ_BLZD(new Drink(new int[] {Items.PREMADE_WIZ_BLZD_2040, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Crafted wiz blzd.
     */
    CRAFTED_WIZ_BLZD(new Drink(new int[] {Items.WIZARD_BLIZZARD_9508, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Short green guy.
     */
    SHORT_GREEN_GUY(new Drink(new int[] {Items.SHORT_GREEN_GUY_2080, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 4.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0)))),
    /**
     * The Premade sgg.
     */
    PREMADE_SGG(new Drink(new int[] {Items.PREMADE_SGG_2038, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 4.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0)))),
    /**
     * The Crafted sgg.
     */
    CRAFTED_SGG(new Drink(new int[] {Items.SHORT_GREEN_GUY_9510, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 4.0, 0.0), new SkillEffect(Skills.ATTACK, -3.0, 0.0)))),
    /**
     * The Drunk dragon.
     */
    DRUNK_DRAGON(new Drink(new int[] {Items.DRUNK_DRAGON_2092, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Premade dr dragon.
     */
    PREMADE_DR_DRAGON(new Drink(new int[] {Items.PREMADE_DR_DRAGON_2032, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Crafted dr dragon.
     */
    CRAFTED_DR_DRAGON(new Drink(new int[] {Items.DRUNK_DRAGON_9516, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Choc saturday.
     */
    CHOC_SATURDAY(new Drink(new int[] {Items.CHOC_SATURDAY_2074, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Premade choc sdy.
     */
    PREMADE_CHOC_SDY(new Drink(new int[] {Items.PREMADE_CHOC_SDY_2030, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Crafted choc sdy.
     */
    CRAFTED_CHOC_SDY(new Drink(new int[] {Items.CHOC_SATURDAY_9518, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 7.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Blurberry special.
     */
    BLURBERRY_SPECIAL(new Drink(new int[] {Items.BLURBERRY_SPECIAL_2064, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(6), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Premade blurb sp.
     */
    PREMADE_BLURB_SP(new Drink(new int[] {Items.PREMADE_BLURB_SP_2028, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(6), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), "You drink the cocktail. It tastes great,", "though you feel slightly dizzy.")),
    /**
     * The Crafted blurb sp.
     */
    CRAFTED_BLURB_SP(new Drink(new int[] {Items.BLURBERRY_SPECIAL_9520, Items.COCKTAIL_GLASS_2026}, new MultiEffect(new HealingEffect(6), new SkillEffect(Skills.STRENGTH, 6.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Karamjan rum.
     */
    KARAMJAN_RUM(new Drink(new int[] {Items.KARAMJAN_RUM_431}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 5.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)), new Animation(1194))),
    /**
     * The Braindeath rum.
     */
    BRAINDEATH_RUM(new Drink(new int[] {Items.BRAINDEATH_RUM_7157}, new MultiEffect(new SkillEffect(Skills.DEFENCE, 0.0, -0.1), new SkillEffect(Skills.ATTACK, 0.0, -0.05), new SkillEffect(Skills.PRAYER, 0.0, -0.05), new SkillEffect(Skills.RANGE, 0.0, -0.05), new SkillEffect(Skills.MAGIC, 0.0, -0.05), new SkillEffect(Skills.HERBLORE, 0.0, -0.05), new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.MINING, 1.0, 0.0)), "With a sense of impending doom you drink the 'rum'. You try very hard not to die.")),
    /**
     * The Rum trouble brewing red.
     */
    RUM_TROUBLE_BREWING_RED(new Drink(new int[] {Items.RUM_8940, Items.RUM_8940}, new TroubleBrewingRumEffect("Oh gods! It tastes like burning!"), new Animation(9605))),
    /**
     * The Rum trouble brewing blue.
     */
    RUM_TROUBLE_BREWING_BLUE(new Drink(new int[] {Items.RUM_8941, Items.RUM_8941}, new TroubleBrewingRumEffect("My Liver! My Liver is melting!"), new Animation(9604))),
    /**
     * The Vodka.
     */
    VODKA(new Drink(new int[] {Items.VODKA_2015}, new MultiEffect(new HealingEffect(2), new SkillEffect(Skills.ATTACK, -4.0, 0.0), new SkillEffect(Skills.STRENGTH, 4.0, 0.0)))),
    /**
     * The Gin.
     */
    GIN(new Drink(new int[] {Items.GIN_2019}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 1.0, 0.0), new SkillEffect(Skills.ATTACK, 4.0, 0.0), new RandomHealthEffect(3, 4)))),
    /**
     * The Brandy.
     */
    BRANDY(new Drink(new int[] {Items.BRANDY_2021}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.ATTACK, 4.0, 0.0)))),
    /**
     * The Whisky.
     */
    WHISKY(new Drink(new int[] {Items.WHISKY_2017}, new MultiEffect(new HealingEffect(5), new SkillEffect(Skills.STRENGTH, 3.0, 0.0), new SkillEffect(Skills.ATTACK, -4.0, 0.0)))),
    /**
     * The Bottle of wine.
     */
    BOTTLE_OF_WINE(new Drink(new int[] {Items.BOTTLE_OF_WINE_7919, Items.EMPTY_WINE_BOTTLE_7921}, new MultiEffect(new HealingEffect(14), new SkillEffect(Skills.ATTACK, -3.0, 0.0)))),
    /**
     * The Jug of wine.
     */
    JUG_OF_WINE(new Drink(new int[] {Items.JUG_OF_WINE_1993, Items.JUG_1935}, new MultiEffect(new HealingEffect(11), new SkillEffect(Skills.ATTACK, -2.0, 0.0)))),
    /**
     * The Half full wine jug.
     */
    HALF_FULL_WINE_JUG(new Drink(new int[] {Items.HALF_FULL_WINE_JUG_1989, Items.JUG_1935}, new HealingEffect(7))),
    /**
     * The Jug of bad wine.
     */
    JUG_OF_BAD_WINE(new Drink(new int[] {Items.JUG_OF_BAD_WINE_1991, Items.JUG_1935}, new SkillEffect(Skills.ATTACK, -3.0, 0.0))),
    /**
     * The Cup of tea.
     */
    CUP_OF_TEA(new Drink(new int[] {Items.CUP_OF_TEA_712, Items.EMPTY_CUP_1980}, new MultiEffect(new HealingEffect(3), new SkillEffect(Skills.ATTACK, 3.0, 0.0)), "Aaah, nothing like a nice cuppa tea!")),
    /**
     * The Cup of tea nettle.
     */
    CUP_OF_TEA_NETTLE(new Drink(new int[] {Items.CUP_OF_TEA_4242, Items.EMPTY_CUP_1980}, new EnergyEffect(10))),
    /**
     * The Cup of tea milky nettle.
     */
    CUP_OF_TEA_MILKY_NETTLE(new Drink(new int[] {Items.CUP_OF_TEA_4243, Items.EMPTY_CUP_1980}, new EnergyEffect(10))),
    /**
     * The Nettle water.
     */
    NETTLE_WATER(new Drink(new int[] {Items.NETTLE_WATER_4237, Items.BOWL_1923}, new HealingEffect(1))),
    /**
     * The Nettle tea.
     */
    NETTLE_TEA(new Drink(new int[] {Items.NETTLE_TEA_4239, Items.BOWL_1923}, new NettleTeaEffect())),
    /**
     * The Nettle tea milky.
     */
    NETTLE_TEA_MILKY(new Drink(new int[] {Items.NETTLE_TEA_4240, Items.BOWL_1923}, new NettleTeaEffect())),
    /**
     * The Nettle tea porcelain.
     */
    NETTLE_TEA_PORCELAIN(new Drink(new int[] {Items.CUP_OF_TEA_4245, Items.PORCELAIN_CUP_4244}, new NettleTeaEffect())),
    /**
     * The Nettle tea milky porcelain.
     */
    NETTLE_TEA_MILKY_PORCELAIN(new Drink(new int[] {Items.CUP_OF_TEA_4246, Items.PORCELAIN_CUP_4244}, new NettleTeaEffect())),
    /**
     * The Cup of tea clay.
     */
    CUP_OF_TEA_CLAY(new Drink(new int[] {Items.CUP_OF_TEA_7730, Items.EMPTY_CUP_7728}, new SkillEffect(Skills.CONSTRUCTION, 1.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Cup of tea clay milky.
     */
    CUP_OF_TEA_CLAY_MILKY(new Drink(new int[] {Items.CUP_OF_TEA_7731, Items.EMPTY_CUP_7728}, new SkillEffect(Skills.CONSTRUCTION, 1.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Cup of tea white.
     */
    CUP_OF_TEA_WHITE(new Drink(new int[] {Items.CUP_OF_TEA_7733, Items.PORCELAIN_CUP_7732}, new SkillEffect(Skills.CONSTRUCTION, 2.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Cup of tea white milky.
     */
    CUP_OF_TEA_WHITE_MILKY(new Drink(new int[] {Items.CUP_OF_TEA_7734, Items.PORCELAIN_CUP_7732}, new SkillEffect(Skills.CONSTRUCTION, 2.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Cup of tea gold.
     */
    CUP_OF_TEA_GOLD(new Drink(new int[] {Items.CUP_OF_TEA_7736, Items.PORCELAIN_CUP_7735}, new SkillEffect(Skills.CONSTRUCTION, 3.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Cup of tea gold milky.
     */
    CUP_OF_TEA_GOLD_MILKY(new Drink(new int[] {Items.CUP_OF_TEA_7737, Items.PORCELAIN_CUP_7735}, new SkillEffect(Skills.CONSTRUCTION, 3.0, 0.0), "You feel refreshed and ready for more building.")),
    /**
     * The Chocolate bar.
     */
    CHOCOLATE_BAR(new Food(new int[] {Items.CHOCOLATE_BAR_1973}, new HealingEffect(3))),
    /**
     * The Purple sweets.
     */
    PURPLE_SWEETS(new Food(new int[] {Items.PURPLE_SWEETS_4561}, new HealingEffect(0))),
    /**
     * The Purple sweets stackable.
     */
    PURPLE_SWEETS_STACKABLE(new Food(new int[] {Items.PURPLE_SWEETS_10476}, new MultiEffect(new EnergyEffect(10), new RandomHealthEffect(1, 3)), "The sugary goodness heals some energy.", "The sugary goodness is yummy.")),
    /**
     * The Field ration.
     */
    FIELD_RATION(new Food(new int[] {Items.FIELD_RATION_7934}, new HealingEffect(10))),
    /**
     * The Roll.
     */
    ROLL(new Food(new int[] {Items.ROLL_6963}, new HealingEffect(6))),
    /**
     * The Tchiki monkey nuts.
     */
    TCHIKI_MONKEY_NUTS(new Food(new int[] {Items.TCHIKI_MONKEY_NUTS_7573}, new HealingEffect(5), "You eat the Tchiki monkey nuts. They taste nutty.")),
    /**
     * The Tchiki monkey paste.
     */
    TCHIKI_MONKEY_PASTE(new Food(new int[] {Items.TCHIKI_NUT_PASTE_7575}, new HealingEffect(5), "You eat the Tchiki monkey nut paste. It sticks to the roof of your mouth.")),
    /**
     * The Oomlie wrap.
     */
    OOMLIE_WRAP(new Food(new int[] {Items.COOKED_OOMLIE_WRAP_2343}, new MultiEffect(new HealingEffect(14), new AchievementEffect(DiaryType.KARAMJA, 2, 2)))),
    /**
     * The Popcorn ball.
     */
    POPCORN_BALL(new Food(new int[] {Items.POPCORN_BALL_14082}, new HealingEffect(3))),
    /**
     * The Chocolate drop.
     */
    CHOCOLATE_DROP(new Food(new int[] {Items.CHOCOLATE_DROP_14083}, new HealingEffect(3))),
    /**
     * The Wrapped candy.
     */
    WRAPPED_CANDY(new Food(new int[] {Items.WRAPPED_CANDY_14084}, new HealingEffect(3))),
    /**
     * The Roe.
     */
    ROE(new Food(new int[] {Items.ROE_11324}, new HealingEffect(3))),
    /**
     * The Equa leaves.
     */
    EQUA_LEAVES(new Food(new int[] {Items.EQUA_LEAVES_2128}, new HealingEffect(1), "You eat the leaves; chewy but tasty.")),
    /**
     * The Choc ice.
     */
    CHOC_ICE(new Food(new int[] {Items.CHOC_ICE_6794}, new HealingEffect(6))),
    /**
     * The Edible seaweed.
     */
    EDIBLE_SEAWEED(new Food(new int[] {Items.EDIBLE_SEAWEED_403}, new HealingEffect(4))),
    /**
     * The Frog spawn.
     */
    FROG_SPAWN(new Food(new int[] {Items.FROG_SPAWN_5004}, new FrogSpawnEffect(), "You eat the frogspawn. Yuck.")),
    /**
     * The Pumpkin.
     */
    PUMPKIN(new Food(new int[] {Items.PUMPKIN_1959}, new HealingEffect(14))),
    /**
     * The Easter egg.
     */
    EASTER_EGG(new Food(new int[] {Items.EASTER_EGG_1961}, new HealingEffect(14))),
    /**
     * The Strength.
     */
    STRENGTH(new Potion(new int[] {Items.STRENGTH_POTION4_113, Items.STRENGTH_POTION3_115, Items.STRENGTH_POTION2_117, Items.STRENGTH_POTION1_119}, new SkillEffect(Skills.STRENGTH, 3.0, 0.1))),
    /**
     * The Attack.
     */
    ATTACK(new Potion(new int[] {Items.ATTACK_POTION4_2428, Items.ATTACK_POTION3_121, Items.ATTACK_POTION2_123, Items.ATTACK_POTION1_125}, new SkillEffect(Skills.ATTACK, 3.0, 0.1))),
    /**
     * The Defence.
     */
    DEFENCE(new Potion(new int[] {Items.DEFENCE_POTION4_2432, Items.DEFENCE_POTION3_133, Items.DEFENCE_POTION2_135, Items.DEFENCE_POTION1_137}, new SkillEffect(Skills.DEFENCE, 3.0, 0.1))),
    /**
     * The Ranging.
     */
    RANGING(new Potion(new int[] {Items.RANGING_POTION4_2444, Items.RANGING_POTION3_169, Items.RANGING_POTION2_171, Items.RANGING_POTION1_173}, new SkillEffect(Skills.RANGE, 4.0, 0.1))),
    /**
     * The Magic.
     */
    MAGIC(new Potion(new int[] {Items.MAGIC_POTION4_3040, Items.MAGIC_POTION3_3042, Items.MAGIC_POTION2_3044, Items.MAGIC_POTION1_3046}, new SkillEffect(Skills.MAGIC, 4.0, 0.1))),
    /**
     * The Super strength.
     */
    SUPER_STRENGTH(new Potion(new int[] {Items.SUPER_STRENGTH4_2440, Items.SUPER_STRENGTH3_157, Items.SUPER_STRENGTH2_159, Items.SUPER_STRENGTH1_161}, new SkillEffect(Skills.STRENGTH, 5.0, 0.15))),
    /**
     * The Super attack.
     */
    SUPER_ATTACK(new Potion(new int[] {Items.SUPER_ATTACK4_2436, Items.SUPER_ATTACK3_145, Items.SUPER_ATTACK2_147, Items.SUPER_ATTACK1_149}, new SkillEffect(Skills.ATTACK, 5.0, 0.15))),
    /**
     * The Super defence.
     */
    SUPER_DEFENCE(new Potion(new int[] {Items.SUPER_DEFENCE4_2442, Items.SUPER_DEFENCE3_163, Items.SUPER_DEFENCE2_165, Items.SUPER_DEFENCE1_167}, new SkillEffect(Skills.DEFENCE, 5.0, 0.15))),
    /**
     * The Antipoison.
     */
    ANTIPOISON(new Potion(new int[] {Items.ANTIPOISON4_2446, Items.ANTIPOISON3_175, Items.ANTIPOISON2_177, Items.ANTIPOISON1_179}, new AddTimerEffect("poison:immunity", secondsToTicks(90)))),
    /**
     * The Antipoison.
     */
    ANTIPOISON_(new Potion(new int[] {Items.ANTIPOISON_PLUS4_5943, Items.ANTIPOISON_PLUS3_5945, Items.ANTIPOISON_PLUS2_5947, Items.ANTIPOISON_PLUS1_5949}, new AddTimerEffect("poison:immunity", minutesToTicks(9)))),
    /**
     * The Antipoison __.
     */
    ANTIPOISON__(new Potion(new int[] {Items.ANTIPOISON_PLUS_PLUS4_5952, Items.ANTIPOISON_PLUS_PLUS3_5954, Items.ANTIPOISON_PLUS_PLUS2_5956, Items.ANTIPOISON_PLUS_PLUS1_5958}, new AddTimerEffect("poison:immunity", minutesToTicks(12)))),
    /**
     * The Super antip.
     */
    SUPER_ANTIP(new Potion(new int[] {Items.SUPER_ANTIPOISON4_2448, Items.SUPER_ANTIPOISON3_181, Items.SUPER_ANTIPOISON2_183, Items.SUPER_ANTIPOISON1_185}, new AddTimerEffect("poison:immunity", minutesToTicks(6)))),
    /**
     * The Relicym.
     */
    RELICYM(new Potion(new int[] {Items.RELICYMS_BALM4_4842, Items.RELICYMS_BALM3_4844, Items.RELICYMS_BALM2_4846, Items.RELICYMS_BALM1_4848}, new MultiEffect(new CureDiseaseEffect()))),
    /**
     * The Agility.
     */
    AGILITY(new Potion(new int[] {Items.AGILITY_POTION4_3032, Items.AGILITY_POTION3_3034, Items.AGILITY_POTION2_3036, Items.AGILITY_POTION1_3038}, new SkillEffect(Skills.AGILITY, 3.0, 0.0))),
    /**
     * The Hunter.
     */
    HUNTER(new Potion(new int[] {Items.HUNTER_POTION4_9998, Items.HUNTER_POTION3_10000, Items.HUNTER_POTION2_10002, Items.HUNTER_POTION1_10004}, new SkillEffect(Skills.HUNTER, 3.0, 0.0))),
    /**
     * The Restore.
     */
    RESTORE(new Potion(new int[] {Items.RESTORE_POTION4_2430, Items.RESTORE_POTION3_127, Items.RESTORE_POTION2_129, Items.RESTORE_POTION1_131}, new RestoreEffect(10.0, 0.3))),
    /**
     * The Sara brew.
     */
    SARA_BREW(new Potion(new int[] {Items.SARADOMIN_BREW4_6685, Items.SARADOMIN_BREW3_6687, Items.SARADOMIN_BREW2_6689, Items.SARADOMIN_BREW1_6691}, new MultiEffect(new PercentHeal(0, 0.15), new SkillEffect(Skills.ATTACK, 0.0, -0.10), new SkillEffect(Skills.STRENGTH, 0.0, -0.10), new SkillEffect(Skills.MAGIC, 0.0, -0.10), new SkillEffect(Skills.RANGE, 0.0, -0.10), new SkillEffect(Skills.DEFENCE, 0.0, 0.25)))),
    /**
     * The Summoning.
     */
    SUMMONING(new Potion(new int[] {Items.SUMMONING_POTION4_12140, Items.SUMMONING_POTION3_12142, Items.SUMMONING_POTION2_12144, Items.SUMMONING_POTION1_12146}, new MultiEffect(new RestoreSummoningSpecial(), new SummoningEffect(7.0, 0.25)))),
    /**
     * The Combat.
     */
    COMBAT(new Potion(new int[] {Items.COMBAT_POTION4_9739, Items.COMBAT_POTION3_9741, Items.COMBAT_POTION2_9743, Items.COMBAT_POTION1_9745}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 3.0, 0.1), new SkillEffect(Skills.ATTACK, 3.0, 0.1)))),
    /**
     * The Energy.
     */
    ENERGY(new Potion(new int[] {Items.ENERGY_POTION4_3008, Items.ENERGY_POTION3_3010, Items.ENERGY_POTION2_3012, Items.ENERGY_POTION1_3014}, new MultiEffect(new EnergyEffect(10)))),
    /**
     * The Fishing.
     */
    FISHING(new Potion(new int[] {Items.FISHING_POTION4_2438, Items.FISHING_POTION3_151, Items.FISHING_POTION2_153, Items.FISHING_POTION1_155}, new SkillEffect(Skills.FISHING, 3.0, 0.0))),
    /**
     * The Prayer.
     */
    PRAYER(new Potion(new int[] {Items.PRAYER_POTION4_2434, Items.PRAYER_POTION3_139, Items.PRAYER_POTION2_141, Items.PRAYER_POTION1_143}, new PrayerEffect(7.0, 0.25))),
    /**
     * The Super resto.
     */
    SUPER_RESTO(new Potion(new int[] {Items.SUPER_RESTORE4_3024, Items.SUPER_RESTORE3_3026, Items.SUPER_RESTORE2_3028, Items.SUPER_RESTORE1_3030}, new MultiEffect(new RestoreEffect(8.0, 0.25, true), new PrayerEffect(8.0, 0.25), new SummoningEffect(8.0, 0.25)))),
    /**
     * The Zammy brew.
     */
    ZAMMY_BREW(new Potion(new int[] {Items.ZAMORAK_BREW4_2450, Items.ZAMORAK_BREW3_189, Items.ZAMORAK_BREW2_191, Items.ZAMORAK_BREW1_193}, new MultiEffect(new DamageEffect(10.0, true), new SkillEffect(Skills.ATTACK, 0.0, 0.25), new SkillEffect(Skills.STRENGTH, 0.0, 0.15), new SkillEffect(Skills.DEFENCE, 0.0, -0.1), new RandomPrayerEffect(0, 10)))),
    /**
     * The Antifire.
     */
    ANTIFIRE(new Potion(new int[] {Items.ANTIFIRE_POTION4_2452, Items.ANTIFIRE_POTION3_2454, Items.ANTIFIRE_POTION2_2456, Items.ANTIFIRE_POTION1_2458}, new AddTimerEffect("dragonfire:immunity", 600, true))),
    /**
     * The Guth rest.
     */
    GUTH_REST(new Potion(new int[] {Items.GUTHIX_REST4_4417, Items.GUTHIX_REST3_4419, Items.GUTHIX_REST2_4421, Items.GUTHIX_REST1_4423}, new GuthixRestEffect())),
    /**
     * The Magic ess.
     */
    MAGIC_ESS(new Potion(new int[] {Items.MAGIC_ESS_MIX1_11491, Items.MAGIC_ESS_MIX2_11489}, new SkillEffect(Skills.MAGIC, 3.0, 0.0))),
    /**
     * The Sanfew.
     */
    SANFEW(new Potion(new int[] {Items.SANFEW_SERUM4_10925, Items.SANFEW_SERUM3_10927, Items.SANFEW_SERUM2_10929, Items.SANFEW_SERUM1_10931}, new MultiEffect(new RestoreEffect(8.0, 0.25, true), new AddTimerEffect("poison:immunity", secondsToTicks(90), new RemoveTimerEffect("disease"))))),
    /**
     * The Super energy.
     */
    SUPER_ENERGY(new Potion(new int[] {Items.SUPER_ENERGY4_3016, Items.SUPER_ENERGY3_3018, Items.SUPER_ENERGY2_3020, Items.SUPER_ENERGY1_3022}, new EnergyEffect(20))),
    /**
     * The Blamish oil.
     */
    BLAMISH_OIL(new FakeConsumable(Items.BLAMISH_OIL_1582, new String[]{"You know... I'd really rather not."})),
    /**
     * The Prayermix.
     */
    PRAYERMIX(new BarbarianMix(new int[] {Items.PRAYER_MIX2_11465, Items.PRAYER_MIX1_11467}, new MultiEffect(new PrayerEffect(7.0, 0.25), new HealingEffect(6)))),
    /**
     * The Zammy mix.
     */
    ZAMMY_MIX(new BarbarianMix(new int[] {Items.ZAMORAK_MIX2_11521, Items.ZAMORAK_MIX1_11523}, new MultiEffect(new DamageEffect(10.0, true), new SkillEffect(Skills.ATTACK, 0.0, 0.15), new SkillEffect(Skills.STRENGTH, 0.0, 0.25), new SkillEffect(Skills.DEFENCE, 0.0, -0.1), new RandomPrayerEffect(0, 10)))),
    /**
     * The Att mix.
     */
    ATT_MIX(new BarbarianMix(new int[] {Items.ATTACK_MIX2_11429, Items.ATTACK_MIX1_11431}, new MultiEffect(new SkillEffect(Skills.ATTACK, 3.0, 0.1), new HealingEffect(3)))),
    /**
     * The Antip mix.
     */
    ANTIP_MIX(new BarbarianMix(new int[] {Items.ANTIPOISON_MIX2_11433, Items.ANTIPOISON_MIX1_11435}, new MultiEffect(new AddTimerEffect("poison:immunity", secondsToTicks(90)), new HealingEffect(3)))),
    /**
     * The Relic mix.
     */
    RELIC_MIX(new BarbarianMix(new int[] {Items.RELICYMS_MIX2_11437, Items.RELICYMS_MIX1_11439}, new MultiEffect(new CureDiseaseEffect(), new HealingEffect(3)))),
    /**
     * The Str mix.
     */
    STR_MIX(new BarbarianMix(new int[] {Items.STRENGTH_MIX2_11443, Items.STRENGTH_MIX1_11441}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 3.0, 0.1), new HealingEffect(3)))),
    /**
     * The Resto mix.
     */
    RESTO_MIX(new BarbarianMix(new int[] {Items.RESTORE_MIX2_11449, Items.RESTORE_MIX1_11451}, new MultiEffect(new RestoreEffect(10.0, 0.3), new HealingEffect(3)))),
    /**
     * The Super resto mix.
     */
    SUPER_RESTO_MIX(new BarbarianMix(new int[] {Items.SUP_RESTORE_MIX2_11493, Items.SUP_RESTORE_MIX1_11495}, new MultiEffect(new RestoreEffect(8.0, 0.25), new PrayerEffect(8.0, 0.25), new SummoningEffect(8.0, 0.25), new HealingEffect(6)))),
    /**
     * The Energy mix.
     */
    ENERGY_MIX(new BarbarianMix(new int[] {Items.ENERGY_MIX2_11453, Items.ENERGY_MIX1_11455}, new MultiEffect(new EnergyEffect(10), new HealingEffect(6)))),
    /**
     * The Def mix.
     */
    DEF_MIX(new BarbarianMix(new int[] {Items.DEFENCE_MIX2_11457, Items.DEFENCE_MIX1_11459}, new MultiEffect(new SkillEffect(Skills.DEFENCE, 3.0, 0.1), new HealingEffect(6)))),
    /**
     * The Agil mix.
     */
    AGIL_MIX(new BarbarianMix(new int[] {Items.AGILITY_MIX2_11461, Items.AGILITY_MIX1_11463}, new MultiEffect(new SkillEffect(Skills.AGILITY, 3.0, 0.0), new HealingEffect(6)))),
    /**
     * The Combat mix.
     */
    COMBAT_MIX(new BarbarianMix(new int[] {Items.COMBAT_MIX2_11445, Items.COMBAT_MIX1_11447}, new MultiEffect(new SkillEffect(Skills.ATTACK, 3.0, 0.1), new SkillEffect(Skills.STRENGTH, 3.0, 0.1), new HealingEffect(6)))),
    /**
     * The Super att mix.
     */
    SUPER_ATT_MIX(new BarbarianMix(new int[] {Items.SUPERATTACK_MIX2_11469, Items.SUPERATTACK_MIX1_11471}, new MultiEffect(new SkillEffect(Skills.ATTACK, 5.0, 0.15), new HealingEffect(6)))),
    /**
     * The Fish mix.
     */
    FISH_MIX(new BarbarianMix(new int[] {Items.FISHING_MIX2_11477, Items.FISHING_MIX1_11479}, new MultiEffect(new SkillEffect(Skills.FISHING, 3.0, 0.0), new HealingEffect(6)))),
    /**
     * The Super energy mix.
     */
    SUPER_ENERGY_MIX(new BarbarianMix(new int[] {Items.SUP_ENERGY_MIX2_11481, Items.SUP_ENERGY_MIX1_11483}, new MultiEffect(new EnergyEffect(20), new HealingEffect(6)))),
    /**
     * The Hunting mix.
     */
    HUNTING_MIX(new BarbarianMix(new int[] {Items.HUNTING_MIX2_11517, Items.HUNTING_MIX1_11519}, new MultiEffect(new SkillEffect(Skills.HUNTER, 3.0, 0.0), new HealingEffect(6)))),
    /**
     * The Super str mix.
     */
    SUPER_STR_MIX(new BarbarianMix(new int[] {Items.SUP_STR_MIX2_11485, Items.SUP_STR_MIX1_11487}, new MultiEffect(new SkillEffect(Skills.STRENGTH, 5.0, 0.15), new HealingEffect(6)))),
    /**
     * The Antidote plus mix.
     */
    ANTIDOTE_PLUS_MIX(new BarbarianMix(new int[] {Items.ANTIDOTE_PLUS_MIX2_11501, Items.ANTIDOTE_PLUS_MIX1_11503}, new MultiEffect(new AddTimerEffect("poison:immunity", minutesToTicks(9)), new RandomHealthEffect(3, 7)))),
    /**
     * The Antifire mix.
     */
    ANTIFIRE_MIX(new BarbarianMix(new int[] {11505, 11507}, new MultiEffect(new AddTimerEffect("dragonfire:immunity", 600, true), new RandomHealthEffect(3, 7)))),
    /**
     * The Antip supermix.
     */
    ANTIP_SUPERMIX(new BarbarianMix(new int[] {Items.ANTI_P_SUPERMIX2_11473, Items.ANTI_P_SUPERMIX1_11475}, new MultiEffect(new AddTimerEffect("poison:immunity", minutesToTicks(6)), new RandomHealthEffect(3, 7)))),
    /**
     * The Sc prayer.
     */
    SC_PRAYER(new Potion(new int[] {Items.PRAYER_POTION5_14207, Items.PRAYER_POTION4_14209, Items.PRAYER_POTION3_14211, Items.PRAYER_POTION2_14213, Items.PRAYER_POTION1_14215}, new PrayerEffect(7.0, 0.25))),
    /**
     * The Sc energy.
     */
    SC_ENERGY(new Potion(new int[] {Items.ENERGY_POTION_5_14217, Items.ENERGY_POTION_4_14219, Items.ENERGY_POTION_3_14221, Items.ENERGY_POTION_2_14223, Items.ENERGY_POTION_1_14225}, new EnergyEffect(20))),
    /**
     * The Sc attack.
     */
    SC_ATTACK(new Potion(new int[] {Items.SUPER_ATTACK5_14227, Items.SUPER_ATTACK4_14229, Items.SUPER_ATTACK3_14231, Items.SUPER_ATTACK2_14233, Items.SUPER_ATTACK1_14235}, new SkillEffect(Skills.ATTACK, 3.0, 0.2))),
    /**
     * The Sc strength.
     */
    SC_STRENGTH(new Potion(new int[]{Items.SUPER_STRENGTH5_14237, Items.SUPER_STRENGTH4_14239, Items.SUPER_STRENGTH3_14241, Items.SUPER_STRENGTH2_14243, Items.SUPER_STRENGTH1_14245}, new SkillEffect(Skills.STRENGTH, 3.0, 0.2))),
    /**
     * The Sc range.
     */
    SC_RANGE(new Potion(new int[]{Items.RANGING_POTION5_14247, Items.RANGING_POTION4_14249, Items.RANGING_POTION3_14251, Items.RANGING_POTION2_14253, Items.RANGING_POTION1_14255}, new SkillEffect(Skills.RANGE, 3.0, 0.1))),
    /**
     * The Sc defence.
     */
    SC_DEFENCE(new Potion(new int[]{Items.DEFENCE_POTION5_14257, Items.DEFENCE_POTION4_14259, Items.DEFENCE_POTION3_14261, Items.DEFENCE_POTION2_14263, Items.DEFENCE_POTION1_14265}, new SkillEffect(Skills.DEFENCE, 3.0, 0.1))),
    /**
     * The Sc magic.
     */
    SC_MAGIC(new Potion(new int[]{Items.MAGIC_POTION5_14267, Items.MAGIC_POTION4_14269, Items.MAGIC_POTION3_14271, Items.MAGIC_POTION2_14273, Items.MAGIC_POTION1_14275}, new SkillEffect(Skills.MAGIC, 3.0, 0.1))),
    /**
     * The Sc summoning.
     */
    SC_SUMMONING(new Potion(new int[]{Items.SUMMONING_POTION5_14277, Items.SUMMONING_POTION4_14279, Items.SUMMONING_POTION3_14281, Items.SUMMONING_POTION2_14283, Items.SUMMONING_POTION1_14285}, new SummoningEffect(7.0, 0.25)))
    ;

    /**
     * The constant consumables.
     */
    public static final Map<Integer, Consumables> consumables = new HashMap<>();
    public static final ArrayList<Integer> potions = new ArrayList<>();

    private final Consumable consumable;
    public boolean isIgnoreMainClock = false;

    Consumables(Consumable consumable) {
        this.consumable = consumable;
    }

    Consumables(Consumable consumable, boolean isIgnoreMainClock) {
        this.consumable = consumable;
        this.isIgnoreMainClock = isIgnoreMainClock;
    }

    public Consumable getConsumable() {
        return consumable;
    }

    public static Consumables getConsumableById(final int itemId) {
        return consumables.get(itemId);
    }

    public static void add(final Consumables consumable) {
        for (int id : consumable.consumable.getIds()) {
            consumables.putIfAbsent(id, consumable);
        }
    }

    static {
        for (Consumables consumable : Consumables.values()) {
            add(consumable);
            if (consumable.consumable instanceof Potion) {
                for (int pot : consumable.consumable.getIds()) {
                    potions.add(pot);
                }
            }
        }
    }
}