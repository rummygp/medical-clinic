package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
