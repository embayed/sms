package com.sfms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class ClassForm {
    @NotBlank
    private String name;
    @DecimalMin("0.0")
    private BigDecimal yearlyFee;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getYearlyFee() { return yearlyFee; }
    public void setYearlyFee(BigDecimal yearlyFee) { this.yearlyFee = yearlyFee; }
}
