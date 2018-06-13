package pl.setblack.croco

import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.ipc.netty.http.server.HttpServer
import java.util.concurrent.atomic.AtomicReference

class CrocoServer() {
    val gameState = AtomicReference(CrocoGame())

    internal fun prepareRoute() = router {
        accept(MediaType.APPLICATION_JSON).and(POST("/croco") )(postProposal())
    }

    private fun postProposal(): (ServerRequest) -> Mono<ServerResponse> {
        return { request ->
            request.bodyToMono(Proposal::class.java)
                    .flatMap { proposal ->
                        val result = calculateResult(proposal)
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(result!!, Result::class.java))
                               // .body(BodyInserters.fromObject("ok"))
                    }
        }
    }

    private fun calculateResult(proposal: Proposal): Mono<Result>? {
        return Mono.create<Result> { sink ->
            gameState.updateAndGet { prevState ->
                val result = prevState.propose(proposal)
                sink.success(result.result)
                result.newState
            }
        }
    }
}


object CrocoServerStarter {
    fun start() {
        val route = CrocoServer().prepareRoute()

        val httpHandler = RouterFunctions.toHttpHandler(route)
        val adapter = ReactorHttpHandlerAdapter(httpHandler)
        val server = HttpServer.create("localhost", 8080)
        server.startAndAwait(adapter)
    }
}


fun main(args: Array<String>) {
    CrocoServerStarter.start()
}