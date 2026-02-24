package com.rummgp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    AppointmentService appointmentService(AppointmentRepositoryPort appointmentRepositoryPort,
                                          DoctorRepositoryPort doctorRepositoryPort,
                                          PatientRepositoryPort patientRepositoryPort) {
        return new AppointmentService(appointmentRepositoryPort, doctorRepositoryPort, patientRepositoryPort);
    }

    @Bean
    DoctorService doctorService(DoctorRepositoryPort doctorRepositoryPort,
                                UserRepositoryPort userRepositoryPort,
                                InstitutionRepositoryPort institutionRepositoryPort) {
        return new DoctorService(doctorRepositoryPort, userRepositoryPort, institutionRepositoryPort);
    }

    @Bean
    PatientService patientService(PatientRepositoryPort patientRepositoryPort,
                                  UserRepositoryPort userRepositoryPort) {
        return new PatientService(patientRepositoryPort, userRepositoryPort);
    }

    @Bean
    UserService userService(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);
    }

    @Bean
    InstitutionService institutionService(InstitutionRepositoryPort institutionRepositoryPort) {
        return new InstitutionService(institutionRepositoryPort);
    }
}
