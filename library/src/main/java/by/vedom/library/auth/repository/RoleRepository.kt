package by.vedom.library.auth.repository

import by.vedom.library.auth.entity.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository: CrudRepository<Role, Long> {

    fun findByName(name: String): Optional<Role>
}