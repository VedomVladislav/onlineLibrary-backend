package by.vedom.library.business.repository

import by.vedom.library.business.entity.Stat
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StatRepository: CrudRepository<Stat, Long> {

    fun findByUserEmail(email: String): Stat
}