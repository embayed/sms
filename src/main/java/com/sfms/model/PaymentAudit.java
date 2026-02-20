package com.sfms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_audits")
public class PaymentAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal oldAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal newAmount;

    @Column(nullable = false)
    private LocalDate oldDate;

    @Column(nullable = false)
    private LocalDate newDate;

    @Column(nullable = false)
    private String editedBy;

    @Column(nullable = false)
    private LocalDateTime editedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public BigDecimal getOldAmount() { return oldAmount; }
    public void setOldAmount(BigDecimal oldAmount) { this.oldAmount = oldAmount; }
    public BigDecimal getNewAmount() { return newAmount; }
    public void setNewAmount(BigDecimal newAmount) { this.newAmount = newAmount; }
    public LocalDate getOldDate() { return oldDate; }
    public void setOldDate(LocalDate oldDate) { this.oldDate = oldDate; }
    public LocalDate getNewDate() { return newDate; }
    public void setNewDate(LocalDate newDate) { this.newDate = newDate; }
    public String getEditedBy() { return editedBy; }
    public void setEditedBy(String editedBy) { this.editedBy = editedBy; }
    public LocalDateTime getEditedAt() { return editedAt; }
    public void setEditedAt(LocalDateTime editedAt) { this.editedAt = editedAt; }
}
