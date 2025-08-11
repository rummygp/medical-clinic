package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.ChangePasswordCommand;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.mapper.PatientMapper;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getAll().stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return PatientMapper.toDto(patientService.getPatient(email));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody Patient patient) {
       return PatientMapper.toDto(patientService.addPatient(patient));
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @RequestBody Patient patient) {
        return PatientMapper.toDto(patientService.editPatient(email, patient));
    }

    @PatchMapping("/{email}")
    public PatientDto changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand password) {
        return PatientMapper.toDto(patientService.changePassword(email, password.getPassword()));
    }
}
