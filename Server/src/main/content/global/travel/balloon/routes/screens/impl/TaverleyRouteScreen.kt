package content.global.travel.balloon.routes.screens.impl

import core.api.sendAngleOnInterface
import core.api.sendAnimationOnInterface
import core.api.sendModelOnInterface
import core.game.node.entity.player.Player

object TaverleyRouteScreen {

    fun first(p: Player, c: Int) {
        // Floor.
        sendModelOnInterface(p,c,40,19558)
        sendModelOnInterface(p,c,45,19559)
        sendModelOnInterface(p,c,50,19560)
        sendModelOnInterface(p,c,55,19561)

        // Trees.
        sendModelOnInterface(p,c,123,19580) // Tree crown left
        sendModelOnInterface(p,c,124,19578) // Tree crown center
        sendModelOnInterface(p,c,125,19582) // Tree crown right

        sendModelOnInterface(p,c,103,19579) // Tree branch left
        sendModelOnInterface(p,c,104,19576) // Tree branch center
        sendModelOnInterface(p,c,105,19581) // Tree branch right

        sendModelOnInterface(p,c,83,19575) // Tree trunk left
        sendModelOnInterface(p,c,84,19574) // Tree trunk center
        sendModelOnInterface(p,c,85,19577) // Tree trunk right

        // Small trees.
        sendModelOnInterface(p,c,102,19521) // Tree top
        sendModelOnInterface(p,c,82,19519)  // Tree base

        sendModelOnInterface(p,c,107,19521) // Tree top
        sendModelOnInterface(p,c,87,19519)  // Tree base

        sendModelOnInterface(p,c,106,19521) // Tree top
        sendModelOnInterface(p,c,86,19519)  // Tree base

        // Smallest trees.
        sendModelOnInterface(p,c,91,19522) // First
        sendModelOnInterface(p,c,92,19522) // Second

        // Evergreens.
        sendModelOnInterface(p,c,127,19570) //Tree crown
        sendModelOnInterface(p,c,107,19569) //Tree branch
        sendModelOnInterface(p,c,87,19568)  //Tree trunk

        sendModelOnInterface(p,c,128,19570) //Tree crown
        sendModelOnInterface(p,c,108,19569) //Tree branch
        sendModelOnInterface(p,c,88,19568)  //Tree trunk

        // Eagles.
        sendModelOnInterface(p,c,145,19780)
        sendAngleOnInterface(p,c,145,2100,300,300)
        sendAnimationOnInterface(p,341,c,145)

        sendModelOnInterface(p,c,148,19780)
        sendAngleOnInterface(p,c,148,2100,300,300)
        sendAnimationOnInterface(p,341,c,148)

        sendModelOnInterface(p,c,177,19780)
        sendAngleOnInterface(p,c,177,2100,200,300)
        sendAnimationOnInterface(p,341,c,177)

        sendModelOnInterface(p,c,209,19780)
        sendAngleOnInterface(p,c,209,2576,200,300)
        sendAnimationOnInterface(p,341,c,209)

        // Clouds.
        sendModelOnInterface(p,c,155,19525) // Left
        sendModelOnInterface(p,c,156,19526) // Right

        sendModelOnInterface(p,c,210,19525) // Left
        sendModelOnInterface(p,c,211,19524) // Center left
        sendModelOnInterface(p,c,212,19524) // Center
        sendModelOnInterface(p,c,213,19524) // Center right
        sendModelOnInterface(p,c,214,19526) // Right

        sendModelOnInterface(p,c,227,19525) // Left
        sendModelOnInterface(p,c,228,19524) // Center
        sendModelOnInterface(p,c,229,19526) // Right

        sendModelOnInterface(p,c,172,19525) // Left
        sendModelOnInterface(p,c,173,19524) // Center
        sendModelOnInterface(p,c,174,19526) // Right

        // Stars.
        sendModelOnInterface(p,c,175,19781)
        sendAngleOnInterface(p,c,175,2100,0,1500)
        sendAnimationOnInterface(p,373,c,175)

        sendModelOnInterface(p,c,230,19781)
        sendAngleOnInterface(p,c,230,2100,0,1500)
        sendAnimationOnInterface(p,373,c,230)

        // Landing base.
        sendModelOnInterface(p,c,78,19572)
    }

    fun second(p: Player, c: Int) {
        // Floor.
        sendModelOnInterface(p,c,40,19562)
        sendModelOnInterface(p,c,45,19563)
        sendModelOnInterface(p,c,50,19564)
        sendModelOnInterface(p,c,55,19565)

        // Clouds.
        sendModelOnInterface(p,c,158,19526)

        // Island & Palm.
        sendModelOnInterface(p,c,83,19551)  // Island left
        sendModelOnInterface(p,c,84,19553)  // Island right
        sendModelOnInterface(p,c,103,19552) // Palm base
        sendModelOnInterface(p,c,123,19527) // Palm leaves

        // Stone.
        sendModelOnInterface(p,c,90,19616)

        // Dead trees.
        sendModelOnInterface(p,c,111,19530) // Top
        sendModelOnInterface(p,c,91,19523)  // Base
        sendModelOnInterface(p,c,112,19530) // Top
        sendModelOnInterface(p,c,92,19523)  // Base

        // Evergreen.
        sendModelOnInterface(p,c,133,19570) // Crown
        sendModelOnInterface(p,c,113,19569) // Branch
        sendModelOnInterface(p,c,93,19568)  // Trunk

        // Houses.
        sendModelOnInterface(p,c,154,19534)
        sendModelOnInterface(p,c,134,19533)
        sendModelOnInterface(p,c,114,19532)
        sendModelOnInterface(p,c,94,19531)

        sendModelOnInterface(p,c,155,19544)
        sendModelOnInterface(p,c,135,19543)
        sendModelOnInterface(p,c,115,19542)
        sendModelOnInterface(p,c,95,19541)

        sendModelOnInterface(p,c,156,19545)
        sendModelOnInterface(p,c,136,19546)
        sendModelOnInterface(p,c,116,19540)
        sendModelOnInterface(p,c,96,19539)

        sendModelOnInterface(p,c,157,19550)
        sendModelOnInterface(p,c,137,19549)
        sendModelOnInterface(p,c,117,19548)
        sendModelOnInterface(p,c,97,19547)

        // Eagles.
        sendModelOnInterface(p,c,161,19779)
        sendAngleOnInterface(p,c,161,2100,200,300)
        sendAnimationOnInterface(p,341,c,161)

        sendModelOnInterface(p,c,179,19779)
        sendAngleOnInterface(p,c,179,2100,200,300)
        sendAnimationOnInterface(p,341,c,179)

        sendModelOnInterface(p,c,185,19779)
        sendAngleOnInterface(p,c,185,2100,200,300)
        sendAnimationOnInterface(p,341,c,185)

        sendModelOnInterface(p,c,204,19779)
        sendAngleOnInterface(p,c,204,2100,200,300)
        sendAnimationOnInterface(p,341,c,204)

        sendModelOnInterface(p,c,219,19779)
        sendAngleOnInterface(p,c,219,2100,200,300)
        sendAnimationOnInterface(p,373,c,219)

        // Clouds.
        sendModelOnInterface(p,c,180,19525)
        sendModelOnInterface(p,c,181,19526)
        sendModelOnInterface(p,c,201,19525)
        sendModelOnInterface(p,c,202,19524)
        sendModelOnInterface(p,c,203,19526)
        sendModelOnInterface(p,c,186,19525)
        sendModelOnInterface(p,c,187,19524)
        sendModelOnInterface(p,c,188,19526)
    }

    fun third(p: Player, c: Int) {
        // Floor.
        sendModelOnInterface(p,c,40,19566)
        sendModelOnInterface(p,c,45,19554)
        sendModelOnInterface(p,c,50,19555)
        sendModelOnInterface(p,c,55,19556)

        // Lighthouse.
        sendModelOnInterface(p,c,138,19538) // Roof.
        sendModelOnInterface(p,c,118,19537) // Window.
        sendModelOnInterface(p,c, 99,19536) // Wall.
        sendModelOnInterface(p,c, 78,19535) // Base.

        // Trees.
        sendModelOnInterface(p,c,119,19580) // Tree crown left
        sendModelOnInterface(p,c,120,19578) // Tree crown center
        sendModelOnInterface(p,c,121,19582) // Tree crown right

        sendModelOnInterface(p,c,98,19579)  // Tree branch left
        sendModelOnInterface(p,c,100,19576) // Tree branch center
        sendModelOnInterface(p,c,101,19581) // Tree branch right

        sendModelOnInterface(p,c,79,19575) // Tree trunk left
        sendModelOnInterface(p,c,80,19574) // Tree trunk center
        sendModelOnInterface(p,c,81,19577) // Tree trunk right

        // Small tree.
        sendModelOnInterface(p,c,102,19521) // Tree top
        sendModelOnInterface(p,c,82,19519)  // Tree base

        // Small tree.
        sendModelOnInterface(p,c,103,19521) // Tree top
        sendModelOnInterface(p,c,83,19519)  // Tree base

        // Smallest trees.
        sendModelOnInterface(p,c,84,19522) // First
        sendModelOnInterface(p,c,85,19522) // Second

        // Evergreen.
        sendModelOnInterface(p,c,126,19570) // Tree crown
        sendModelOnInterface(p,c,106,19569) // Tree branch
        sendModelOnInterface(p,c,86, 19568) // Tree trunk

        // Smallest tree.
        sendModelOnInterface(p,c,92,19522)

        // Small tree.
        sendModelOnInterface(p,c,113,19521) // Tree top
        sendModelOnInterface(p,c,93,19519)  // Tree base

        // Landing base.
        sendModelOnInterface(p,c,97,19567)

        // Eagles.
        sendModelOnInterface(p,c,146,19780)
        sendAngleOnInterface(p,c,146,2100,200,300)
        sendAnimationOnInterface(p,341,c,146)

        sendModelOnInterface(p,c,151,19780)
        sendAngleOnInterface(p,c,151,2100,200,300)
        sendAnimationOnInterface(p,341,c,151)

        sendModelOnInterface(p,c,192,19780)
        sendAngleOnInterface(p,c,192,2100,200,300)
        sendAnimationOnInterface(p,341,c,192)

        sendModelOnInterface(p,c,207,19780)
        sendModelOnInterface(p,c,207,19780)
        sendAngleOnInterface(p,c,207,2100,200,300)
        sendAnimationOnInterface(p,341,c,207)

        sendModelOnInterface(p,c,208,19780)
        sendAngleOnInterface(p,c,208,2100,200,300)
        sendAnimationOnInterface(p,341,c,208)

        sendModelOnInterface(p,c,215,19780)
        sendAngleOnInterface(p,c,215,2100,200,300)
        sendAnimationOnInterface(p,341,c,215)

        // Clouds.
        sendModelOnInterface(p,c,167,19525) // Left
        sendModelOnInterface(p,c,168,19524) // Center left
        sendModelOnInterface(p,c,169,19524) // Center
        sendModelOnInterface(p,c,170,19524) // Center right
        sendModelOnInterface(p,c,171,19526) // Right

        sendModelOnInterface(p,c,174,19525) // Left
        sendModelOnInterface(p,c,175,19524) // Center
        sendModelOnInterface(p,c,176,19526) // Right

        sendModelOnInterface(p,c,212,19525) // Left
        sendModelOnInterface(p,c,213,19524) // Center
        sendModelOnInterface(p,c,214,19526) // Right

        // Star.
        sendModelOnInterface(p,c,191,19781)
        sendAngleOnInterface(p,c,191,2100,0,1500)
        sendAnimationOnInterface(p,373,c,191)
    }
}