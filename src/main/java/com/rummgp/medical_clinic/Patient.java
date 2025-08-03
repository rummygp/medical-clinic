package com.rummgp.medical_clinic;

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
    private int idCardNo;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private LocalDate birthday;
}
