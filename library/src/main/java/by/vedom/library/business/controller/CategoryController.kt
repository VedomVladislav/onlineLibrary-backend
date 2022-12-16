package by.vedom.library.business.controller

import by.vedom.library.business.entity.Category
import by.vedom.library.business.search.CategorySearchValues
import by.vedom.library.business.service.CategoryService
import by.vedom.library.business.util.Logger
import by.vedom.library.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.StringBuilder

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping("/all")
    fun findAll(@RequestBody email: String): ResponseEntity<MutableList<Category>> {
        Logger.debugMethodName("------------ CategoryController.findAll() ")
        return ResponseEntity.ok(categoryService.findByUserEmail(email))
    }

    @PutMapping("/add")
    fun addCategory(@RequestBody category: Category): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(categoryService.add(category))
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @PatchMapping("/update")
    fun updateCategory(@RequestBody category: Category): ResponseEntity<*> {
        return try {
            categoryService.update(category)
            ResponseEntity("", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity("", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @DeleteMapping("/delete")
    fun deleteCategory(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            categoryService.delete(id);
            ResponseEntity("Category with id = $id was successfully deleted", HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/search")
    fun searchCategories(
        @RequestBody categorySearchValues: CategorySearchValues
    ): ResponseEntity<MutableList<Category>> {
        return ResponseEntity.ok(categoryService.searchCategoriesByTitleAndEmail(categorySearchValues))
    }

    @PostMapping("/id")
    fun findCategoryById(@RequestBody id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(categoryService.findById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity("id = $id not found", HttpStatus.NOT_ACCEPTABLE)
        }
    }
}