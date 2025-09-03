package com.rummgp.medical_clinic.command;

public record InstitutionCreateCommand(String name, String city, String postalCode, String street, String buildingNo) {
}
