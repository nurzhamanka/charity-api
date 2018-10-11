package kz.peep.api.entities.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kz.peep.api.entities.AppUser
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
@JsonIgnoreProperties(value = ["createdBy", "updatedBy"], allowGetters = true)
abstract class UserDateAudit : DateAudit() {

    @CreatedBy
    @Column(updatable = false)
    private lateinit var createdBy: AppUser

    @LastModifiedBy
    private lateinit var updatedBy: AppUser
}