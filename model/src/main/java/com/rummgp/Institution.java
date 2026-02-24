package com.rummgp;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Institution {
    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNo;
    private List<Doctor> doctors = new ArrayList<>();

    public void edit(Institution newData) {
        this.name = newData.getName();
        this.city = newData.getCity();
        this.postalCode = newData.getPostalCode();
        this.street = newData.getStreet();
        this.buildingNo = newData.getBuildingNo();
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", doctors=" + doctors.stream().map(Doctor::getId) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institution other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
