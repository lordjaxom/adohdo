package de.akvsoft.adohdo.user

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_email", columnList = "email", unique = true),
    ]
)
class User(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var name: String,

    @Id
    val id: UUID = UUID.randomUUID(),
)
