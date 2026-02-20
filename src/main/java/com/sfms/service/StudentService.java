package com.sfms.service;

import com.sfms.dto.StudentForm;
import com.sfms.model.Enrollment;
import com.sfms.model.SchoolClass;
import com.sfms.model.Student;
import com.sfms.repository.EnrollmentRepository;
import com.sfms.repository.PaymentRepository;
import com.sfms.repository.SchoolClassRepository;
import com.sfms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final BalanceService balanceService;

    public StudentService(StudentRepository studentRepository, SchoolClassRepository classRepository,
                          EnrollmentRepository enrollmentRepository, PaymentRepository paymentRepository,
                          BalanceService balanceService) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
        this.balanceService = balanceService;
    }

    public List<Student> findAll() { return studentRepository.findAll(); }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

    @Transactional
    public Student create(StudentForm form) {
        Student student = new Student();
        applyForm(student, form);
        studentRepository.save(student);
        enroll(student, form.getClassId(), form.getAcademicYear());
        return student;
    }

    @Transactional
    public Student update(Long id, StudentForm form) {
        Student student = findById(id);
        applyForm(student, form);
        studentRepository.save(student);
        enroll(student, form.getClassId(), form.getAcademicYear());
        return student;
    }

    @Transactional
    public void enroll(Student student, Long classId, Integer year) {
        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        Enrollment enrollment = enrollmentRepository.findByStudentAndAcademicYear(student, year)
                .orElseGet(Enrollment::new);
        enrollment.setStudent(student);
        enrollment.setAcademicYear(year);
        enrollment.setSchoolClass(schoolClass);
        enrollment.setFeeSnapshot(schoolClass.getYearlyFee());
        enrollmentRepository.save(enrollment);
        student.setCurrentClass(schoolClass);
    }

    @Transactional
    public void delete(Long id) {
        Student student = findById(id);
        if (paymentRepository.countByStudent(student) > 0 || balanceService.totalRest(student).compareTo(java.math.BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot delete a student with payment history or unpaid balances.");
        }
        studentRepository.delete(student);
    }

    private void applyForm(Student student, StudentForm form) {
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setFatherName(form.getFatherName());
        student.setMotherName(form.getMotherName());
    }
}
