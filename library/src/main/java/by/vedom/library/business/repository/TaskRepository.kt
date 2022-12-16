package by.vedom.library.business.repository

import by.vedom.library.business.entity.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository: JpaRepository<Task, Long> {

    @Query(
        "SELECT t FROM Task t where " +
                "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title,'%'))) and" +
                "(:completed is null or t.completed=:completed) and " +
                "(:priorityId is null or t.priority.id=:priorityId) and " +
                "(:categoryId is null or t.category.id=:categoryId) and " +
                "(" +
                "(cast(:dateFrom as timestamp) is null or t.taskDate>=:dateFrom) and " +
                "(cast(:dateTo as timestamp) is null or t.taskDate<=:dateTo)" +
                ") and " +
                "(t.user.email=:email)"
    )
    fun find(
        @Param("title") title: String?,
        @Param("completed") completed: Int?,
        @Param("priorityId") priorityId: Long?,
        @Param("categoryId") categoryId: Long?,
        @Param("email") email: String?,
        @Param("dateFrom") dateFrom: Date?,
        @Param("dateTo") dateTo: Date?,
        pageable: Pageable?
    ): Page<Task>

    fun findByUserEmailOrderByTitleAsc(email: String): List<Task>
}