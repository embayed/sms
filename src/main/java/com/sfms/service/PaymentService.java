package com.sfms.service;

import com.sfms.dto.PaymentForm;
import com.sfms.model.Payment;
import com.sfms.model.PaymentAudit;
import com.sfms.model.Student;
import com.sfms.repository.PaymentAuditRepository;
import com.sfms.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentAuditRepository paymentAuditRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentAuditRepository paymentAuditRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentAuditRepository = paymentAuditRepository;
    }

    public List<Payment> findByStudent(Student student) {
        return paymentRepository.findByStudentOrderByPaymentDateAscIdAsc(student);
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Transactional
    public Payment create(Student student, PaymentForm form, String actor) {
        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setAmount(form.getAmount());
        payment.setPaymentDate(form.getPaymentDate());
        payment.setModifiedBy(actor);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment edit(Long paymentId, PaymentForm form, String actor) {
        Payment payment = findById(paymentId);
        PaymentAudit audit = new PaymentAudit();
        audit.setPayment(payment);
        audit.setOldAmount(payment.getAmount());
        audit.setOldDate(payment.getPaymentDate());
        audit.setNewAmount(form.getAmount());
        audit.setNewDate(form.getPaymentDate());
        audit.setEditedBy(actor);
        paymentAuditRepository.save(audit);

        payment.setAmount(form.getAmount());
        payment.setPaymentDate(form.getPaymentDate());
        payment.setModifiedBy(actor);
        return paymentRepository.save(payment);
    }

    public BigDecimal totalPaid(Student student) {
        return paymentRepository.totalPaidByStudent(student);
    }
}
