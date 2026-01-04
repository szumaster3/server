package content.global.travel.balloon.routes.screens.impl

import core.api.sendAngleOnInterface
import core.api.sendAnimationOnInterface
import core.api.sendModelOnInterface
import core.game.node.entity.player.Player

object CraftingGuildRouteScreen {

    fun firstStage(p: Player, c: Int) {
        // Floor
        sendModelOnInterface(p,c,40,19558)
        sendModelOnInterface(p,c,45,19559)
        sendModelOnInterface(p,c,50,19604)
        sendModelOnInterface(p,c,55,19605)

        // Landing base
        sendModelOnInterface(p,c,138,19572)

        // Mountain peak
        sendModelOnInterface(p,c,118,19607)
        sendModelOnInterface(p,c,119,19611)

        // Mountain lower layer
        sendModelOnInterface(p,c,99,19608)
        sendModelOnInterface(p,c,98,19608)
        sendModelOnInterface(p,c,100,19612)

        // Mountain lowest layer
        sendModelOnInterface(p,c,78,19609)
        sendModelOnInterface(p,c,79,19609)
        sendModelOnInterface(p,c,80,19609)
        sendModelOnInterface(p,c,81,19613)

        sendModelOnInterface(p,c,122,19570) // Tree crown
        sendModelOnInterface(p,c,102,19569) // Tree branch
        sendModelOnInterface(p,c,82, 19568) // Tree trunk

        sendModelOnInterface(p,c,103,19521) // Tree top
        sendModelOnInterface(p,c,83,19519)  // Tree base

        sendModelOnInterface(p,c,124,19570) // Tree crown
        sendModelOnInterface(p,c,104,19569) // Tree branch
        sendModelOnInterface(p,c,84, 19568) // Tree trunk


        sendModelOnInterface(p,c,128,19618) // house left
        sendModelOnInterface(p,c,108,19633) // house left
        sendModelOnInterface(p,c,88, 19630) // house left

        sendModelOnInterface(p,c,129,19619) // house center left
        sendModelOnInterface(p,c,109,19603) // house center left
        sendModelOnInterface(p,c,89, 19602) // house center left

        sendModelOnInterface(p,c,130,19620) // house barrel
        sendModelOnInterface(p,c,110,19629) // house barrel
        sendModelOnInterface(p,c,90, 19628) // house barrel

        sendModelOnInterface(p,c,131,19621) // house barrel
        sendModelOnInterface(p,c,111,19632) // house barrel
        sendModelOnInterface(p,c,91, 19631) // house barrel

        sendModelOnInterface(p,c,133,19570) //Tree crown
        sendModelOnInterface(p,c,113,19569) //Tree branch
        sendModelOnInterface(p,c,93,19568)  //Tree trunk

        sendModelOnInterface(p,c,116,19521) // Tree top
        sendModelOnInterface(p,c,96,19519)  // Tree base

        sendModelOnInterface(p,c,137,19570) //Tree crown
        sendModelOnInterface(p,c,117,19569) //Tree branch
        sendModelOnInterface(p,c,97,19568)  //Tree trunk

        // Eagle
        sendModelOnInterface(p,c,220,19780)
        sendAngleOnInterface(p,c,220,2100,300,300)
        sendAnimationOnInterface(p,341,c,220)

        // Eagle
        sendModelOnInterface(p,c,221,19780)
        sendAngleOnInterface(p,c,221,2100,300,300)
        sendAnimationOnInterface(p,341,c,221)

        // Eagle
        sendModelOnInterface(p,c,206,19780)
        sendAngleOnInterface(p,c,206,2100,300,300)
        sendAnimationOnInterface(p,341,c,206)

        // Eagle white
        sendModelOnInterface(p,c,148,19779)
        sendAngleOnInterface(p,c,148,2100,300,300)
        sendAnimationOnInterface(p,341,c,148)

        // Eagle white
        sendModelOnInterface(p,c,175,19780)
        sendAngleOnInterface(p,c,175,2100,300,300)
        sendAnimationOnInterface(p,341,c,175)

        // Eagle white
        sendModelOnInterface(p,c,151,19779)
        sendAngleOnInterface(p,c,151,2100,300,300)
        sendAnimationOnInterface(p,341,c,151)

        // Eagle white
        sendModelOnInterface(p,c,171,19779)
        sendAngleOnInterface(p,c,171,2100,300,300)
        sendAnimationOnInterface(p,341,c,171)

        // Star
        sendModelOnInterface(p,c,183,19781)
        sendAngleOnInterface(p,c,183,2100,0,1500)
        sendAnimationOnInterface(p,373,c,183)

        // Clouds
        sendModelOnInterface(p,c,162,19525) // Left
        sendModelOnInterface(p,c,163,19524) // Center
        sendModelOnInterface(p,c,164,19526) // Right

        sendModelOnInterface(p,c,192,19525) // Left
        sendModelOnInterface(p,c,193,19524) // Center
        sendModelOnInterface(p,c,194,19526) // Right

        sendModelOnInterface(p,c,233,19525) // Left
        sendModelOnInterface(p,c,234,19524) // Center
        sendModelOnInterface(p,c,235,19526) // Right
    }

    fun secondStage(p: Player, c: Int) {
        // Floor.
        sendModelOnInterface(p,c,40,19606)
        sendModelOnInterface(p,c,45,19561)
        sendModelOnInterface(p,c,50,19562)
        sendModelOnInterface(p,c,55,19562)

        sendModelOnInterface(p,c,99,19521)  // Tree top
        sendModelOnInterface(p,c,78,19519)  // Tree base

        sendModelOnInterface(p,c,119,19570) // Tree crown
        sendModelOnInterface(p,c,98,19569)  // Tree branch
        sendModelOnInterface(p,c,79,19568)  // Tree trunk

        sendModelOnInterface(p,c,103,19625) // Ship
        sendModelOnInterface(p,c,83,19623)

        sendModelOnInterface(p,c,124,19626) // Ship
        sendModelOnInterface(p,c,104,19627)
        sendModelOnInterface(p,c,84,19624)

        sendModelOnInterface(p,c,106,19617) // Stone
        sendModelOnInterface(p,c,86,19615)

        sendModelOnInterface(p,c,108,19617) // Stone
        sendModelOnInterface(p,c,88,19615)

        sendModelOnInterface(p,c,111,19617) // Stone
        sendModelOnInterface(p,c,91,19615)

        sendModelOnInterface(p,c,96,19616) // Stone

        sendModelOnInterface(p,c,97,19717) // Shark
    }

    fun thirdStage(p: Player, c: Int) {
        // Floor
        sendModelOnInterface(p,c,40,19595)
        sendModelOnInterface(p,c,45,19596)
        sendModelOnInterface(p,c,50,19597)
        sendModelOnInterface(p,c,55,19598)

        sendModelOnInterface(p,c,97,19567) // Land

        sendModelOnInterface(p,c,78,19717) // Shark

        sendModelOnInterface(p,c,79,19616) // Stone

        sendModelOnInterface(p,c,100,19617) // Stone
        sendModelOnInterface(p,c,80,19615)

        sendModelOnInterface(p,c,123,19601) // Crown
        sendModelOnInterface(p,c,103,19600) // Branch
        sendModelOnInterface(p,c,83,19599)  // Trunk

        sendModelOnInterface(p,c,84,19616) // Stone

        sendModelOnInterface(p,c,105,19521) // Tree top
        sendModelOnInterface(p,c,85,19519)  // Tree base

        sendModelOnInterface(p,c,86,19522)

        sendModelOnInterface(p,c,107,19521) // Tree top
        sendModelOnInterface(p,c,87,19519)  // Tree base

        sendModelOnInterface(p,c,108,19521) // Tree top
        sendModelOnInterface(p,c,88,19519)  // Tree base

        sendModelOnInterface(p,c,89,19622) // Stone

        sendModelOnInterface(p,c,130,19601) // Crown
        sendModelOnInterface(p,c,110,19600) // Branch
        sendModelOnInterface(p,c,90,19599)  // Trunk

        sendModelOnInterface(p,c,198,19526) // Cloud

        // Eagle white
        sendModelOnInterface(p,c,199,19779)
        sendAngleOnInterface(p,c,199,2100,300,300)
        sendAnimationOnInterface(p,341,c,199)

        // Cloud
        sendModelOnInterface(p,c,222,19525) // Left
        sendModelOnInterface(p,c,223,19524) // Center
        sendModelOnInterface(p,c,224,19526) // Right

        sendModelOnInterface(p,c,120,19780)
        sendAngleOnInterface(p,c,120,2100,300,300)
        sendAnimationOnInterface(p,341,c,120)

        // Star
        sendModelOnInterface(p,c,143,19781)
        sendAngleOnInterface(p,c,143,2100,0,1500)
        sendAnimationOnInterface(p,373,c,143)

        // Star
        sendModelOnInterface(p,c,104,19781)
        sendAngleOnInterface(p,c,104,2100,0,1500)
        sendAnimationOnInterface(p,373,c,104)

        // Eagle
        sendModelOnInterface(p,c,163,19780)
        sendAngleOnInterface(p,c,163,2100,300,300)
        sendAnimationOnInterface(p,341,c,163)

        sendModelOnInterface(p,c,166,19525) // Left
        sendModelOnInterface(p,c,167,19524) // Center
        sendModelOnInterface(p,c,168,19526) // Right

        sendModelOnInterface(p,c,173,19525) // Left
        sendModelOnInterface(p,c,174,19524) // Center
        sendModelOnInterface(p,c,175,19526) // Right

        // Cloud
        sendModelOnInterface(p,c,228,19525) // Left
        sendModelOnInterface(p,c,229,19524) // Center
        sendModelOnInterface(p,c,230,19526) // Right

        // Eagle
        sendModelOnInterface(p,c,209,19780)
        sendAngleOnInterface(p,c,209,2100,300,300)
        sendAnimationOnInterface(p,341,c,209)

        // Cloud
        sendModelOnInterface(p,c,210,19525) // Left
        sendModelOnInterface(p,c,211,19524) // Center
        sendModelOnInterface(p,c,212,19526) // Right
    }
}