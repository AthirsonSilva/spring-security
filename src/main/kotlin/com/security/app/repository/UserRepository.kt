package com.security.app.repository

import com.security.app.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User?, String?> {
    fun findByEmail(email: String?): Optional<User?>
}
