package by.vedom.library.business.controller

import by.vedom.library.business.entity.Priority
import by.vedom.library.business.search.PrioritySearchValues
import by.vedom.library.business.service.PriorityService
import by.vedom.library.business.util.Logger
import by.vedom.library.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/priority")
class PriorityController(
    private val priorityService: PriorityService
) {

    @PostMapping("/all")
    fun findAll(@RequestBody email: String): ResponseEntity<MutableList<Priority>> {
        Logger.debugMethodName("------------ PriorityController.findAll() ")
        return ResponseEntity.ok(priorityService.findAll(email))
    }

    @PutMapping("/add")
    fun addPriority(@RequestBody priority: Priority): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(priorityService.add(priority))
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @PatchMapping("/update")
    fun updatePriority(@RequestBody priority: Priority): ResponseEntity<*> {
        return try {
            priorityService.update(priority)
            ResponseEntity("", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity("", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @DeleteMapping("/delete")
    fun deletePriority(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            priorityService.delete(id);
            ResponseEntity("Priority with id = $id was successfully deleted", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/search")
    fun searchCategories(
        @RequestBody prioritySearchValues: PrioritySearchValues
    ): ResponseEntity<MutableList<Priority>> {
         return ResponseEntity.ok(priorityService.findPrioritiesByTitleAndUserEmail(prioritySearchValues))
    }

    @PostMapping("/id")
    fun findPriorityById(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(priorityService.findById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity("id = $id not found", HttpStatus.NOT_ACCEPTABLE)
        }
    }

}