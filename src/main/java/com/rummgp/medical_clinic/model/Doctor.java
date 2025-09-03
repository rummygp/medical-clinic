package com.rummgp.medical_clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private User user;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "institutions_id"))
    private List<Institution> institutions;

    public void edit(Doctor newData) {
        this.firstName = newData.firstName;
        this.lastName = newData.lastName;
        this.specialization = newData.specialization;
    }
}
