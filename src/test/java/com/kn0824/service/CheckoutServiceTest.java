package com.kn0824.service;

import com.kn0824.core.agreement.RentalAgreement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class CheckoutServiceTest {

    private static CheckoutService checkoutService;

    @BeforeAll
    public static void setup() {
        checkoutService = new CheckoutService();
    }

    /**
     * Testing for a bad discount value
     */
    @Test
    public void test1() {
        try {
            checkoutService.checkout("JAKR", LocalDate.of(2015, 9, 3), 5, 101);
            // This should throw an illegal argument exception
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("The discount must be a value between 0 and 100. ", e.getMessage());
        }
    }

    @Test
    public void test2() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("LADW", LocalDate.of(2020, 7, 2), 3, 10);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test2Output.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test3() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("CHNS", LocalDate.of(2015, 7, 2), 5, 25);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test3Output.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test3_withJuly4thOnMonday() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("CHNS", LocalDate.of(2016, 7, 2), 5, 25);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test3Output_withJuly4thOnMonday.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test3_withJuly4thOnSunday() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("CHNS", LocalDate.of(2021, 7, 2), 5, 25);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test3Output_withJuly4thOnSunday.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test4() throws IOException {

        RentalAgreement agreement = checkoutService.checkout("JAKD", LocalDate.of(2015, 9, 3), 6, 0);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test4Output.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test5() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("JAKR", LocalDate.of(2015, 7, 2), 9, 0);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test5Output.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void test6() throws IOException {
        RentalAgreement agreement = checkoutService.checkout("JAKR", LocalDate.of(2020, 7, 2), 4, 50);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/test6Output.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }

    @Test
    public void holidayEdgeCase() throws IOException{
        RentalAgreement agreement = checkoutService.checkout("CHNS", LocalDate.of(2021, 7, 3), 1, 0);
        String expectedResults = readExpectedResultsFromFile("com/kn0824/service/testOutput_holidayEdgeCase.txt");
        Assertions.assertEquals(expectedResults, agreement.prettyPrint());
    }


    @Test
    public void testForBadToolCode() {
        try {
            checkoutService.checkout("FOOBAR", LocalDate.of(2015, 9, 3), 5, 0);
            // This should throw an illegal argument exception
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Unknown tool code is being requested. Please confirm the code and try again. ", e.getMessage());
        }
    }

    @Test
    public void testForBadRentalDaysValue() {
        try {
            checkoutService.checkout("JAKR", LocalDate.of(2015, 9, 3), 0, 0);
            // This should throw an illegal argument exception
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("The number of days chosen for rental must be at least 1 day. ", e.getMessage());
        }
    }

    @Test
    public void testForMultipleBadArgs() {
        try {
            checkoutService.checkout("FOOBAR", LocalDate.of(2015, 9, 3), 0, 200);
            // This should throw an illegal argument exception
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("The number of days chosen for rental must be at least 1 day. " +
                    "The discount must be a value between 0 and 100. Unknown tool code is being requested. " +
                    "Please confirm the code and try again. ", e.getMessage());
        }
    }


    private String readExpectedResultsFromFile(String fileName) throws IOException {
        // Use class loader to get resource as stream
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        }
    }
}
