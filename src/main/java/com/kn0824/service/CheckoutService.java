package com.kn0824.service;

import com.kn0824.core.agreement.RentalAgreement;
import com.kn0824.core.tools.Tool;
import com.kn0824.core.tools.ToolType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class CheckoutService {

    // Note: Normally I would not implement in this manner. The tools and the tool types would be better served being
    // stored in a database so that they could be added, modified, and removed without requiring code updates.
    // For the sake of the scope of this demonstration, we are just going to read the file from stored csv files
    private static final String TOOLTYPE_CSV_FILE = "/tooltype.csv";
    private static final String TOOLS_CSV_FILE = "/tools.csv";
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
    The starting checkoutDate is NOT inclusive. In other words, with a checkout date of Aug 1, and remaining rental days is 3,
    we will consider the date range as Aug 2, Aug 3, Aug 4.
     */
    private int calculateChargeableDays(Tool toolBeingRequested, LocalDate checkoutDate, int remainingRentalDays) {
        int chargeableDays = 0;
        LocalDate startingDate = checkoutDate.plusDays(1);
        LocalDate endDate = checkoutDate.plusDays(remainingRentalDays);
        boolean chargeOnHolidays = toolBeingRequested.getToolType().isHolidayCharge();
        boolean chargeOnWeekends = toolBeingRequested.getToolType().isWeekendCharge();
        boolean chargeOnWeekdays = toolBeingRequested.getToolType().isWeekdayCharge();
        for (LocalDate date = startingDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (chargeOnWeekdays && !isWeekend(date)) {
                if (isHoliday(date) && !chargeOnHolidays) {
                    continue;
                } else {
                    chargeableDays++;
                }
            }

            if (isWeekend(date) && chargeOnWeekends) {
                chargeableDays++;
            }

            if (isHoliday(date)) {
                if (isHolidayObservedOnWeekday(date) && !chargeOnHolidays) {
                    // In the case where we are a holiday that is on a weekend but we observe it on a weekday,
                    // Just deduct the number of chargeable days if we don't charge on the holiday.
                    chargeableDays--;
                }
            }
        }
        return chargeableDays;
    }

    private boolean isHoliday(LocalDate date) {
        return date.equals(LocalDate.of(date.getYear(), Month.JULY, 4))
                || isLaborDay(date);
    }

    private boolean isHolidayObservedOnWeekday(LocalDate date) {
        // If the holiday falls on a weekend, it will be observed on the nearest weekday
        return (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4
                && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY))
                || (isLaborDay(date) && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY));
    }

    private boolean isLaborDay(LocalDate date) {
        return date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY
                && date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).getDayOfMonth() <= 7;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private void initialize() {
        Map<String, ToolType> types = createToolTypes();
        this.toolMapping = new HashMap<>();
        try (InputStream inputStream = CheckoutService.class.getResourceAsStream(TOOLS_CSV_FILE);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                toolMapping.put(row[0], new Tool(row[0], types.get(row[1]), row[2]));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private Map<String, ToolType> createToolTypes() {
        Map<String, ToolType> types = new HashMap<>();
        try (InputStream inputStream = CheckoutService.class.getResourceAsStream(TOOLTYPE_CSV_FILE);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                types.put(row[0], new ToolType(row[0], new BigDecimal(row[1]), Boolean.parseBoolean(row[2]),
                        Boolean.parseBoolean(row[3]), Boolean.parseBoolean(row[4])));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return types;
    }
}
