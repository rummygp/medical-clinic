package com.rummgp;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Doctor {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private User user;
    @Builder.Default
    private List<Institution> institutions = new ArrayList<>();
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    public void edit(Doctor newData) {
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
                ", institutions=" + institutions.stream().map(Institution::getId) +
                ", patients=" + appointments.stream().map(Appointment::getPatient).map(Patient::getId) +
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
