package com.rummgp.medical_clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "institutions_id"))
    private List<Institution> institutions = new ArrayList<>();
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();

    public void edit(Doctor newData) {
        this.firstName = newData.getFirstName();
        this.lastName = newData.getLastName();
        this.specialization = newData.getSpecialization();
    }
}
