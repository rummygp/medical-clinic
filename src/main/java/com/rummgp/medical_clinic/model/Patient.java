package com.rummgp.medical_clinic.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Patient {
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
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
        return newData;
    }
}
