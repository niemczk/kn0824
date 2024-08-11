package com.kn0824.core.agreement;

import com.kn0824.core.tools.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RentalAgreement {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    private Tool tool;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal dailyRentalCharge;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

    public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, LocalDate dueDate, BigDecimal dailyRentalCharge,
                           int chargeDays, BigDecimal preDiscountCharge, int discountPercent,
                           BigDecimal discountAmount, BigDecimal finalAmount) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public void setDailyRentalCharge(BigDecimal dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public void setPreDiscountCharge(BigDecimal preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String prettyPrint() {
        return String.format("Tool code: %s\n", tool.getToolCode()) +
                String.format("Tool type: %s\n", tool.getToolType().getType()) +
                String.format("Tool brand: %s\n", tool.getBrand()) +
                String.format("Rental days: %d\n", rentalDays) +
                String.format("Check out date: %s\n", checkoutDate.format(DATE_FORMAT)) +
                String.format("Due date: %s\n", dueDate.format(DATE_FORMAT)) +
                String.format("Daily rental charge: %s\n", CURRENCY_FORMAT.format(dailyRentalCharge.setScale(2, RoundingMode.CEILING))) +
                String.format("Charge days: %d\n", chargeDays) +
                String.format("Pre-discount charge: %s\n", CURRENCY_FORMAT.format(preDiscountCharge.setScale(2, RoundingMode.CEILING))) +
                String.format("Discount percent: %d%%\n", discountPercent) +
                String.format("Discount amount: %s\n", CURRENCY_FORMAT.format(discountAmount.setScale(2, RoundingMode.CEILING))) +
                String.format("Final charge: %s", CURRENCY_FORMAT.format(finalAmount.setScale(2, RoundingMode.CEILING)));
    }
}
