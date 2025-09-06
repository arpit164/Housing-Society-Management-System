package com.asworld.hsms_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    
    // Supports international phone numbers with optional country code
    private static final String PHONE_NUMBER_PATTERN = "^\\+?[1-9]\\d{1,14}$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        // Remove all non-digit characters except leading +
        String digitsOnly = phoneNumber.replaceAll("[^0-9+]", "");
        
        // Check if the number has a reasonable length (E.164 standard allows up to 15 digits)
        if (digitsOnly.startsWith("+")) {
            if (digitsOnly.length() > 16 || digitsOnly.length() < 5) { // + and 4-15 digits
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Phone number with country code should be between 5 and 15 digits")
                       .addConstraintViolation();
                return false;
            }
        } else {
            // For numbers without country code, assuming local format (e.g., 10 digits for US/Canada)
            if (digitsOnly.length() != 10) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Local phone number should be 10 digits")
                       .addConstraintViolation();
                return false;
            }
        }

        // Check against the pattern
        if (!Pattern.matches(PHONE_NUMBER_PATTERN, digitsOnly)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid phone number format. Use international format (e.g., +1234567890) or local format (e.g., 1234567890)")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
