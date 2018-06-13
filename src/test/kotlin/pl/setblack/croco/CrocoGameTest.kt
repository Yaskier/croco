package pl.setblack.croco

import io.kotlintest.be
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import io.vavr.control.Option
import java.math.BigDecimal

internal class CrocoGameTest : BehaviorSpec({
    given ("empty game") {
        val game = CrocoGame()
        `when` ("player2 proposes higher ") {
            val proposal1 = Proposal("player1", BigDecimal.ONE)
            val proposal2 = Proposal("player2", BigDecimal.TEN)
            val game1 = game.propose(proposal1).newState
            val game2 = game1.propose(proposal2)
            then ("player2 wins") {
                game2.result should be (Result.WIN)
            }
        }
    }


})