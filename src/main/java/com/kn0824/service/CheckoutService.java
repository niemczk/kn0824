package com.kn0824.service;

import com.kn0824.core.agreement.RentalAgreement;
import com.kn0824.core.tools.Tool;
import com.kn0824.core.tools.ToolType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CheckoutService {

    // Note: Normally I would not implement in this manner. The tools and the tool types would be better served being
    // stored in a database so that they could be added, modified, and removed without requiring code updates.
    // For the sake of the scope of this demonstration, we are just going to generate the data internally.
    private Map<String, Tool> toolMapping;

    public CheckoutService() {
        initialize();
    }

    public RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDays,
                                    int discount) throws IllegalArgumentException {
        StringBuilder errMessage = new StringBuilder();
        Tool toolBeingRequested = toolMapping.get(toolCode);
        if (rentalDays <= 0) {
            errMessage.append("The number of days chosen for rental must be at least 1 day. ");
        }
        if (discount < 0 || discount > 100) {
            errMessage.append("The discount must be a value between 0 and 100. ");
        }
        if (toolBeingRequested == null) {
            errMessage.append("Unknown tool code is being requested. Please confirm the code and try again. ");
        }
        if (!errMessage.isEmpty()) {
            throw new IllegalArgumentException(errMessage.toString());
        }

        LocalDate dueDate =  checkoutDate.plusDays(rentalDays);
        int chargableDays = calculateChargeableDays(toolBeingRequested, checkoutDate, rentalDays);
        BigDecimal preDiscountCharge = toolBeingRequested.getToolType().getDailyCharge().multiply(new BigDecimal(chargableDays).setScale(2, RoundingMode.CEILING));
        BigDecimal discountAmount = preDiscountCharge.multiply(new BigDecimal(discount/100.0)).setScale(2, RoundingMode.CEILING);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(toolBeingRequested, rentalDays, checkoutDate, dueDate,
                toolBeingRequested.getToolType().getDailyCharge(), chargableDays, preDiscountCharge, discount, discountAmount, finalCharge);
    }

    /*
    Method calculates the number of days we can actually charge the user based on the Tool settings (holidays, weekends, etc.)
    The starting checkoutDate is NOT inclusive.
     */
    private int calculateChargeableDays(Tool toolBeingRequested, LocalDate startDate, int remainingRentalDays) {
        int chargeableDays = 0;
        Set<LocalDate> holidays = getHolidays(startDate.getYear());

        for (int i = 0; i < remainingRentalDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);

            if (isChargeableDay(currentDate, toolBeingRequested.getToolType().isWeekdayCharge(),
                    toolBeingRequested.getToolType().isWeekendCharge(), toolBeingRequested.getToolType().isHolidayCharge(), holidays)) {
                chargeableDays++;
            }
        }

        return chargeableDays;
    }

    private static boolean isChargeableDay(LocalDate date, boolean chargeOnWeekday, boolean chargeOnWeekend, boolean chargeOnHoliday, Set<LocalDate> holidays) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

        if (chargeOnHoliday && holidays.contains(date)) {
            return true;
        }

        if (isWeekend && chargeOnWeekend) {
            return true;
        }

        if (!isWeekend && chargeOnWeekday) {
            return true;
        }

        return false;
    }

    private static Set<LocalDate> getHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();

        // July 4th
        LocalDate july4 = LocalDate.of(year, Month.JULY, 4);
        holidays.add(adjustHolidayIfWeekend(july4));

        // Labor Day (first Monday in September)
        LocalDate laborDay = LocalDate.of(year, Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        holidays.add(adjustHolidayIfWeekend(laborDay));

        return holidays;
    }

    private static LocalDate adjustHolidayIfWeekend(LocalDate holiday) {
        DayOfWeek dayOfWeek = holiday.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return holiday.minusDays(1);  // Observed on Friday
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return holiday.plusDays(1);  // Observed on Monday
        }
        return holiday;
    }

    private void initialize() {
        Map<String, ToolType> types = createToolTypes();
        this.toolMapping = new HashMap<>();
        this.toolMapping.put("CHNS", new Tool("CHNS", types.get("Chainsaw"), "Stihl"));
        this.toolMapping.put("LADW", new Tool("LADW", types.get("Ladder"), "Werner"));
        this.toolMapping.put("JAKD", new Tool("JAKD", types.get("Jackhammer"), "DeWalt"));
        this.toolMapping.put("JAKR", new Tool("JAKR", types.get("Jackhammer"), "Ridgid"));
    }

    private Map<String, ToolType> createToolTypes() {
        Map<String, ToolType> types = new HashMap<>();
        types.put("Ladder", new ToolType("Ladder", new BigDecimal("1.99"), true, true, false));
        types.put("Chainsaw", new ToolType("Chainsaw", new BigDecimal("1.49"), true, false, true));
        types.put("Jackhammer", new ToolType("JackHammer", new BigDecimal("2.99"), true, false, false));
        return types;
    }
}
