package kz.peep.api.entities

import kz.peep.api.entities.audit.UserDateAudit
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "ORGANIZATION")
data class Organization (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,

        @NotBlank
        @Size(max = 50)
        var name: String,

        @Column(length = 3000)
        var description: String? = null,

        @OneToMany(mappedBy = "organization",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true)
        val efforts: List<Effort> = ArrayList(),

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "ORGANIZATION_EFFORT_TYPE",
                joinColumns = [JoinColumn(name = "ORGANIZATION_ID")],
                inverseJoinColumns = [JoinColumn(name = "EFFORT_TYPE_ID")])
        var donationTypes: MutableSet<EffortType> = HashSet()
) : UserDateAudit()