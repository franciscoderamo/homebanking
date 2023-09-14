package com.mindhub.homebanking.models;

import org.junit.jupiter.api.Test;

import static com.mindhub.homebanking.models.Client.passwordValidator;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void passwordValidatorCapitalLetter() {
        assertThrowsExactly(IllegalArgumentException.class, () -> passwordValidator("abcd1234#"), "Invalid password: missing capital letter");
    }
    @Test
    void passwordValidatorLowerCase() {
        assertThrowsExactly(IllegalArgumentException.class, () -> passwordValidator("ABCD1234#"), "Invalid password: missing lowercase");
    }
    @Test
    void passwordValidatorSpecialCharacter() {
        assertThrowsExactly(IllegalArgumentException.class, () -> passwordValidator("Abcd1234"), "Invalid password: missing special character");
    }
    @Test
    void passwordValidatorNumber() {
        assertThrowsExactly(IllegalArgumentException.class, () -> passwordValidator("Abcdefgh#"), "Invalid password: missing number");
    }
    @Test
    void passwordValidatorMinimumCharacters() {
        assertThrowsExactly(IllegalArgumentException.class, () -> passwordValidator("Ab12#"), "Invalid password: 8 minimum characters required");
    }
}