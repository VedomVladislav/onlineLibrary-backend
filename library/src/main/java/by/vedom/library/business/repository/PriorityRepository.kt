package by.vedom.library.business.repository

import by.vedom.library.business.entity.Priority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PriorityRepository: JpaRepository<Priority, Long> {

    @Query(
        "SELECT p FROM Priority p where " +
                "(:title is null or :title='' " +
                " or lower(p.title) like lower(concat('%', :title,'%'))) " +
                " and p.user.email=:email " +
                "order by p.title asc"
    )
    fun findPrioritiesByTitleAndUserEmail(@Param("title") title: String?, @Param("email") email: String): List<Priority>

    fun findByUserEmailOrderByIdAsc(text: String): List<Priority>
}