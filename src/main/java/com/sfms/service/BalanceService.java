package com.sfms.service;

import com.sfms.model.Enrollment;
import com.sfms.model.Payment;
import com.sfms.model.Student;
import com.sfms.repository.EnrollmentRepository;
import com.sfms.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Service
public class BalanceService {
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    public BalanceService(EnrollmentRepository enrollmentRepository, PaymentRepository paymentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    public BigDecimal totalFees(Student student) {
        return enrollmentRepository.findByStudentOrderByAcademicYearAsc(student).stream()
                .map(Enrollment::getFeeSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalPayments(Student student) {
        return paymentRepository.totalPaidByStudent(student);
    }

    public BigDecimal totalRest(Student student) {
        return totalFees(student).subtract(totalPayments(student));
    }

    public boolean hasArrearsFromPreviousYears(Student student) {
        int currentYear = Year.now().getValue();
        List<Enrollment> previousEnrollments = enrollmentRepository.findByStudentOrderByAcademicYearAsc(student)
                .stream()
                .filter(e -> e.getAcademicYear() < currentYear)
                .toList();
        if (previousEnrollments.isEmpty()) {
            return false;
        }
        BigDecimal previousFees = previousEnrollments.stream().map(Enrollment::getFeeSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal previousPayments = paymentRepository.findByStudentOrderByPaymentDateAscIdAsc(student)
                .stream()
                .filter(p -> p.getPaymentDate().getYear() < currentYear)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return previousFees.subtract(previousPayments).compareTo(BigDecimal.ZERO) > 0;
    }
}
