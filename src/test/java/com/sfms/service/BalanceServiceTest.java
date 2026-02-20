package com.sfms.service;

import com.sfms.model.Enrollment;
import com.sfms.model.Payment;
import com.sfms.model.Student;
import com.sfms.repository.EnrollmentRepository;
import com.sfms.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BalanceServiceTest {

    @Test
    void computesRestAcrossYears() {
        EnrollmentRepository enrollmentRepository = Mockito.mock(EnrollmentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        BalanceService service = new BalanceService(enrollmentRepository, paymentRepository);

        Student student = new Student();
        Enrollment e1 = new Enrollment(); e1.setAcademicYear(2024); e1.setFeeSnapshot(new BigDecimal("1000"));
        Enrollment e2 = new Enrollment(); e2.setAcademicYear(2025); e2.setFeeSnapshot(new BigDecimal("1200"));
        Mockito.when(enrollmentRepository.findByStudentOrderByAcademicYearAsc(student)).thenReturn(List.of(e1, e2));
        Payment p1 = new Payment(); p1.setAmount(new BigDecimal("500")); p1.setPaymentDate(LocalDate.of(2024, 3, 1));
        Payment p2 = new Payment(); p2.setAmount(new BigDecimal("300")); p2.setPaymentDate(LocalDate.of(2025, 3, 1));
        Mockito.when(paymentRepository.findByStudentOrderByPaymentDateAscIdAsc(student)).thenReturn(List.of(p1, p2));

        assertEquals(new BigDecimal("1400"), service.totalRest(student));
        assertTrue(service.hasArrearsFromPreviousYears(student));
    }
}
