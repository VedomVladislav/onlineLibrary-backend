package by.vedom.library.auth.repository

import by.vedom.library.auth.entity.Activity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface ActivityRepository: CrudRepository<Activity, Long> {

    @Modifying // if request is changing data it desirable to add this annotation
    @Transactional
    @Query("UPDATE Activity a SET a.activated = :active WHERE a.uuid = :uuid")
    fun changeActivated(@Param("uuid") uuid: String, @Param("active") active: Boolean): Int

    fun findByUserId(id: Long): Optional<Activity>

    fun findByUuid(uuid: String): Optional<Activity>
}