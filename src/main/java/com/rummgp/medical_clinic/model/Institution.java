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
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNo;
    @ManyToMany(mappedBy = "institutions")
    private List<Doctor> doctors;

    public void edit(Institution newData) {
        this.name = newData.getName();
        this.city = newData.getCity();
        this.postalCode = newData.getPostalCode();
        this.street = newData.getCity();
        this.buildingNo = newData.getBuildingNo();
    }
}
