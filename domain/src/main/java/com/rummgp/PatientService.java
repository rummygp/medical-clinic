package com.rummgp;

import com.rummgp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PatientService {
    private final PatientRepositoryPort patientRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public PagePojo<Patient> findAll(PatientFindCommand patientFindCommand) {
        return patientRepositoryPort.findAll(patientFindCommand);
    }

    public Patient find(Long id) {
        return patientRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient", id));
    }

    public Patient add(Patient patient) {
        PatientValidator.validatePatientCreate(patient);
        assignUserToPatient(patient);
        return patientRepositoryPort.save(patient);
    }

    public void delete(Long id) {
        Patient patient = patientRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient",id));
        patientRepositoryPort.delete(patient);
    }

    public Patient edit(Long id, Patient updatedpatient) {
        Patient patient = patientRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient",id));
        PatientValidator.validatePatientEdit(patient, updatedpatient);
        patient.edit(updatedpatient);
        return patientRepositoryPort.save(patient);
    }

    private void assignUserToPatient(Patient patient) {
        if (patient.getUser().getId() != null) {
            patient.setUser(userRepositoryPort.findById(patient.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("User", patient.getUser().getId())));
        }
    }
}
