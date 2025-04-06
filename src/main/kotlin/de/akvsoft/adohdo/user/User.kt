package de.akvsoft.adohdo.user

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var name: String,

    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    val id: UUID? = null,
)
