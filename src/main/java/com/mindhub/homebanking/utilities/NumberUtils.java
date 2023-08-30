package com.mindhub.homebanking.utilities;

import org.springframework.context.annotation.Bean;

/**
 * This class provides utility methods for numeric operations.
 * It can be used to perform manipulations and verifications of numbers.
 *
 * @since 1.0
 */
public class NumberUtils {

    // Check if the amount is valid (and greater than 0) and is not a NaN value
    public static boolean isValidAmount(double amount) {
        return !Double.isNaN(amount) && amount >= 0;
    }
}
