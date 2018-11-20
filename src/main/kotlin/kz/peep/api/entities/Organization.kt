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

        var description: String? = null,

        @Embedded
        @AttributeOverrides(
                AttributeOverride(name = "longitude", column = Column(name = "locationLon")),
                AttributeOverride(name = "latitude", column = Column(name = "locationLat"))
        )
        var location: Location? = null,

        @OneToMany(mappedBy = "organization",
                cascade = [CascadeType.ALL],
                fetch = FetchType.EAGER,
                orphanRemoval = true)
        val efforts: Set<Effort> = HashSet(),

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "ORGANIZATION_EFFORT_TYPE",
                joinColumns = [JoinColumn(name = "ORGANIZATION_ID")],
                inverseJoinColumns = [JoinColumn(name = "EFFORT_TYPE_ID")])
        var donationTypes: Set<EffortType> = HashSet()
) : UserDateAudit()