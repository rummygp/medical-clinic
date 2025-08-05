package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.ChangePasswordCommand;
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
    public List<Patient> getPatients() {
        return patientService.getAll();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
       return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatient(email);
    }

    @PutMapping("/{email}")
    public Patient editPatient(@PathVariable String email, @RequestBody Patient patient) {
        return patientService.editPatient(email, patient);
    }

    @PatchMapping("/email")
    public Patient changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand password) {
        return patientService.changePassword(email, password.getPassword());
    }
}
