package pl.setblack.croco

import io.kotlintest.specs.BehaviorSpec
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

internal class CrocoServerTest : BehaviorSpec ({
 given ("Croco server") {
     val webTest = WebTestClient.bindToRouterFunction ( CrocoServer().prepareRoute() )
             .build()
     `when` ("first proposal posted")  {
         then ("should give WAIT") {
              webTest.post().uri("/croco")
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .syncBody("{\"playerName\":\"John\", \"value\":\"100\"}")
                     .exchange()
                     .expectBody(String::class.java)
                     .isEqualTo("\"WAIT\"")


         }

     }

 }

})