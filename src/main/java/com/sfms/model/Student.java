package com.sfms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String fatherName;

    @NotBlank
    @Column(nullable = false)
    private String motherName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_class_id")
    private SchoolClass currentClass;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public SchoolClass getCurrentClass() { return currentClass; }
    public void setCurrentClass(SchoolClass currentClass) { this.currentClass = currentClass; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Payment> getPayments() { return payments; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
