package net.status.util;

import org.springframework.http.HttpStatus;
import java.util.Random;

public class StatusGenerator {
    public static HttpStatus generatePaymentStatus() {
        HttpStatus[] randomStatus = {
                HttpStatus.NOT_FOUND,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.PAYMENT_REQUIRED
        };
        return randomStatus[new Random().nextInt(11)];
    }

    public static HttpStatus generateAccountStatus() {
        HttpStatus[] randomStatus = {
                HttpStatus.TEMPORARY_REDIRECT,
                HttpStatus.NOT_FOUND,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.OK,
                HttpStatus.NOT_ACCEPTABLE
        };
        return randomStatus[new Random().nextInt(11)];
    }
}