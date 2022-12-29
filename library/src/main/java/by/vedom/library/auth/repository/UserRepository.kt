package by.vedom.library.auth.repository

import by.vedom.library.auth.entity.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, Long> {

    @Query("SELECT case when count(u) > 0 then true else false end FROM User u where lower(u.email) = lower(:email)")
    fun existsByEmail(@Param("email") email: String): Boolean

    @Query("SELECT case when count(u) > 0 then true else false end FROM User u where lower(u.username) = lower(:username)")
    fun existsByUsername(@Param("username") userName: String): Boolean

    fun findByUsername(name: String): Optional<User>

    fun findByEmail(email: String): Optional<User>

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    fun updateUserPassword(@Param("password") password: String, @Param("email") email: String): Int
}