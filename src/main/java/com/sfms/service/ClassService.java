package com.sfms.service;

import com.sfms.dto.ClassForm;
import com.sfms.model.SchoolClass;
import com.sfms.model.Student;
import com.sfms.repository.EnrollmentRepository;
import com.sfms.repository.SchoolClassRepository;
import com.sfms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassService {
    private final SchoolClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final BalanceService balanceService;

    public ClassService(SchoolClassRepository classRepository, EnrollmentRepository enrollmentRepository,
                        StudentRepository studentRepository, BalanceService balanceService) {
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.balanceService = balanceService;
    }

    public List<SchoolClass> findAll() { return classRepository.findAll(); }
    public SchoolClass findById(Long id) { return classRepository.findById(id).orElseThrow(); }

    @Transactional
    public SchoolClass create(ClassForm form) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(form.getName());
        schoolClass.setYearlyFee(form.getYearlyFee());
        return classRepository.save(schoolClass);
    }

    @Transactional
    public SchoolClass update(Long id, ClassForm form) {
        SchoolClass schoolClass = findById(id);
        schoolClass.setName(form.getName());
        schoolClass.setYearlyFee(form.getYearlyFee());
        return classRepository.save(schoolClass);
    }

    @Transactional
    public void delete(Long id) {
        SchoolClass schoolClass = findById(id);
        if (enrollmentRepository.countBySchoolClass(schoolClass) > 0) {
            boolean blocked = studentRepository.findAll().stream().anyMatch(s -> {
                boolean inClass = s.getCurrentClass() != null && s.getCurrentClass().getId().equals(id);
                return inClass && balanceService.totalRest(s).signum() > 0;
            });
            if (blocked) {
                throw new IllegalStateException("Cannot delete class while enrolled students have arrears.");
            }
        }
        classRepository.delete(schoolClass);
    }
}
