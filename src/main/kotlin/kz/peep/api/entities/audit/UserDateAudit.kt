package kz.peep.api.entities.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
@JsonIgnoreProperties(value = ["createdBy", "updatedBy"], allowGetters = true)
abstract class UserDateAudit : DateAudit() {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: String = ""

    @LastModifiedBy
    @Column(nullable = false)
    var updatedBy: String = ""
}