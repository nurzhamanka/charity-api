package kz.peep.api.entities.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["createdAt", "updatedAt"], allowGetters = true)
open class DateAudit : Serializable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private lateinit var createdAt : Instant

    @LastModifiedDate
    @Column(nullable = false)
    private lateinit var updatedAt : Instant
}