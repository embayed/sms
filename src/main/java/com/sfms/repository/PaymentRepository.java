package com.sfms.repository;

import com.sfms.model.Payment;
import com.sfms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentOrderByPaymentDateAscIdAsc(Student student);
    long countByStudent(Student student);
    default BigDecimal totalPaidByStudent(Student student) {
        return findByStudentOrderByPaymentDateAscIdAsc(student).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
