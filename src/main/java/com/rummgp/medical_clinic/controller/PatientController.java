package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> getPatients() {
        return patientService.getAll();
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPatient(@RequestBody Patient patient) {
       patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatient(email);
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public void editPatient(@PathVariable String email, @RequestBody Patient patient) {
        patientService.editPatient(email, patient);
    }
}
