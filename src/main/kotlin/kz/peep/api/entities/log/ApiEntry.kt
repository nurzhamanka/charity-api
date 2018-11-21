package kz.peep.api.entities.log

import javax.persistence.*

@Entity
@Table(name = "API_ENTRY")
class ApiEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,

        message: String,
        override val flag: String = "API"
) : LogEntry(message)