package by.vedom.library.business.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/ping")
    fun ping(): String {
        return "ping"
    }

    @GetMapping("/pong")
    fun pong(): String {
        return "pong"
    }

}