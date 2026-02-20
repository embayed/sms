package com.sfms.repository;

import com.sfms.model.Enrollment;
import com.sfms.model.SchoolClass;
import com.sfms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentOrderByAcademicYearAsc(Student student);
    Optional<Enrollment> findByStudentAndAcademicYear(Student student, Integer academicYear);
    long countBySchoolClass(SchoolClass schoolClass);
}
