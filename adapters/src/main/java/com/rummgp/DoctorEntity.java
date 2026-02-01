package com.rummgp;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "institutions_id"))
    private List<InstitutionEntity> institutions = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public void edit(DoctorEntity newData) {
        this.firstName = newData.getFirstName();
        this.lastName = newData.getLastName();
        this.specialization = newData.getSpecialization();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", user=" + user +
                ", institutions=" + institutions.stream().map(InstitutionEntity::getId) +
                ", patients=" + appointments.stream().map(AppointmentEntity::getPatient).map(PatientEntity::getId) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
