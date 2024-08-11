package com.kn0824.service;


import com.kn0824.core.agreement.RentalAgreement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CheckoutServiceTest {

    private static CheckoutService checkoutService;

    @BeforeAll
    public static void setup(){
        checkoutService = new CheckoutService();
    }

    @Test
    public void test1() {
        try {
            RentalAgreement agreement = checkoutService.checkout("JAKR",
                    LocalDate.of(2015, 9, 3), 5, 101);
            // This should throw an illegal argument exception
            Assertions.fail();
        }
        catch (IllegalArgumentException e) {
            Assertions.assertEquals("The discount must be a value between 0 and 100. ", e.getMessage());
        }
    }

    @Test
    public void test2() {
        try {
            RentalAgreement agreement = checkoutService.checkout("LADW",
                    LocalDate.of(2020, 7, 2), 3, 10);
            System.out.println(agreement.prettyPrint());

        }
        catch (IllegalArgumentException e) {
            Assertions.fail();
        }
    }

    @Test
    public void test3() {
        try {
            RentalAgreement agreement = checkoutService.checkout("CHNS",
                    LocalDate.of(2015, 7, 2), 5, 25);
            System.out.println(agreement.prettyPrint());

        }
        catch (IllegalArgumentException e) {
            Assertions.fail();
        }
    }

    @Test
    public void test4() {
        try {
            RentalAgreement agreement = checkoutService.checkout("JAKD",
                    LocalDate.of(2015, 9, 3), 6, 0);
            System.out.println(agreement.prettyPrint());

        }
        catch (IllegalArgumentException e) {
            Assertions.fail();
        }
    }

    @Test
    public void test5() {
        try {
            RentalAgreement agreement = checkoutService.checkout("JAKR",
                    LocalDate.of(2015, 7, 2), 9, 0);
            System.out.println(agreement.prettyPrint());

        }
        catch (IllegalArgumentException e) {
            Assertions.fail();
        }
    }

    @Test
    public void test6() {
        try {
            RentalAgreement agreement = checkoutService.checkout("JAKR",
                    LocalDate.of(2020, 7, 2), 4, 50);
            System.out.println(agreement.prettyPrint());

        }
        catch (IllegalArgumentException e) {
            Assertions.fail();
        }
    }
}
