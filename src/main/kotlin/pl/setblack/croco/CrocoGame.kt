package pl.setblack.croco

import io.vavr.control.Option
import io.vavr.kotlin.none
import io.vavr.kotlin.some
import java.math.BigDecimal

data class CrocoGame(private val proposed : Option<Proposal>) {

    constructor() : this(none<Proposal>())  {}

    fun propose(proposal: Proposal): GameResult =
        proposed.map {existing ->
            GameResult(
                    when( existing.value.compareTo(proposal.value)) {
                -1 -> Result.WIN
                1  -> Result.LOST
                else -> Result.DRAW
            }, CrocoGame(Option.none()))
        }.getOrElse(GameResult(Result.WAIT, CrocoGame(some(proposal))))

}

enum class Result {
    WIN,LOST, DRAW, WAIT
}

data class GameResult( val result :Result, val newState : CrocoGame  )

data class Proposal ( val playerName : String, val value : BigDecimal)


