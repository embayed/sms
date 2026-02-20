package com.sfms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "academicYear"}))
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @Column(nullable = false)
    private Integer academicYear;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal feeSnapshot;

    @Column(nullable = false)
    private LocalDate enrolledAt = LocalDate.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
    public BigDecimal getFeeSnapshot() { return feeSnapshot; }
    public void setFeeSnapshot(BigDecimal feeSnapshot) { this.feeSnapshot = feeSnapshot; }
    public LocalDate getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDate enrolledAt) { this.enrolledAt = enrolledAt; }
}
