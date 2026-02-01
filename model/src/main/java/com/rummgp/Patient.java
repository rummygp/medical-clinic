package com.rummgp;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    private Long id;
    private String firstName;
    private String lastName;
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;
    private User user;
    private List<Appointment> appointments = new ArrayList<>();

    public void edit(Patient newData) {
        this.firstName = newData.getFirstName();
        this.lastName = newData.getLastName();
        this.phoneNumber = newData.getPhoneNumber();
        this.birthday = newData.getBirthday();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", user=" + user +
                ", doctors=" + appointments.stream().map(Appointment::getDoctor).map(Doctor::getId).toList() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
