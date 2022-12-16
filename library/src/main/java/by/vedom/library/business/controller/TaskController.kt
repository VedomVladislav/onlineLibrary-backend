package by.vedom.library.business.controller

import by.vedom.library.business.entity.Task
import by.vedom.library.business.search.TaskSearchValues
import by.vedom.library.business.service.TaskService
import by.vedom.library.business.util.Logger
import by.vedom.library.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping("/all")
    fun findAll(@RequestBody email: String): ResponseEntity<*> {
        Logger.debugMethodName("------------ TaskController.findAll() ")
        return ResponseEntity.ok(taskService.findAll(email))
    }

    @PutMapping("/add")
    fun addTask(@RequestBody task: Task): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(taskService.add(task))
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @PatchMapping("/update")
    fun updateTask(@RequestBody task: Task): ResponseEntity<*> {
        return try {
            taskService.update(task)
            ResponseEntity("", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity("", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @DeleteMapping("/delete")
    fun deleteTask(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            taskService.delete(id);
            ResponseEntity("Task with id = $id was successfully deleted", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/id")
    fun findTaskById(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(taskService.findById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity("id = $id not found", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @PostMapping("/search")
    fun search(@RequestBody taskSearchValues: TaskSearchValues): ResponseEntity<*> {
        return try {
            return ResponseEntity.ok(taskService.find(taskSearchValues))
        } catch (e: Exception) {
            ResponseEntity("Any params is absent, check your call", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}