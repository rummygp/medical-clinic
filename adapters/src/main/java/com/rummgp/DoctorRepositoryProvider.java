package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DoctorRepositoryProvider implements DoctorRepositoryPort {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PageMapper pageMapper;

    @Override
    public PagePojo<Doctor> findAll(DoctorFindCommand doctorFindCommand) {
        Specification<DoctorEntity> spec = DoctorSpecification.filter(doctorFindCommand);
        Pageable pageable = PageRequest.of(doctorFindCommand.pageNumber(), doctorFindCommand.pageSize());
        Page<DoctorEntity> page = doctorRepository.findAll(spec, pageable);
        return pageMapper.toPojo(page, doctorMapper::toPojo);
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id)
                .map(doctorMapper::toPojo);
    }

    @Override
    @Transactional
    public Doctor save(Doctor doctor) {
        DoctorEntity doctorEntity = doctorMapper.toEntity(doctor);
        return doctorMapper.toPojo(doctorRepository.save(doctorEntity));
    }

    @Override
    @Transactional
    public void delete(Doctor doctor) {
        doctorRepository.delete(doctorMapper.toEntity(doctor));
    }
}
