package by.vedom.library.business.controller

import by.vedom.library.business.entity.Stat
import by.vedom.library.business.service.StatService
import by.vedom.library.business.util.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StatController(
    private val statService: StatService
) {

    @PostMapping("/stat")
    fun findByEmail(@RequestBody email: String?): ResponseEntity<Stat> {
        Logger.debugMethodName("StatController: findById() ---------------------------------------------------------- ")
        return ResponseEntity.ok(statService.findStat(email))
    }

}