package com.rummgp.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "CARD_NUMBER_ID", unique = true)
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;

    public Patient edit(Patient newData) {
        this.email = newData.getEmail();
        this.password = newData.getPassword();
        this.idCardNo = newData.getIdCardNo();
        this.firstName = newData.getFirstName();
        this.lastName = newData.getLastName();
        this.phoneNumber = newData.getPhoneNumber();
        this.birthday = newData.getBirthday();
        return this;
    }
}
