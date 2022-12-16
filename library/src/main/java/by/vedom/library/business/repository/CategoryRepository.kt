package by.vedom.library.business.repository

import by.vedom.library.business.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface CategoryRepository: JpaRepository<Category, Long> {

    @Query("SELECT c " +
            "FROM " +
            "   Category c " +
            "WHERE " +
            "   (:title IS NULL OR :title='' " +
            "   OR lower(c.title) like lower(concat('%', :title, '%'))) " +
            "   AND c.user.email=:email " +
            "ORDER BY c.title ASC")
    fun findByTitleAndEmail(@Param("title") title: String, @Param("email") email: String): MutableList<Category>

    fun findByUserEmailOrderByIdAsc(text: String): List<Category>

}