package com.seinksansdoozebank.fr.jcommander.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class QuickValueValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 6 || intValue > 8) {
                throw new ParameterException("Value for " + name + " must be between 6 and 8");
            }
        } catch (NumberFormatException e) {
            throw new ParameterException("Invalid value for " + name + ": " + value);
        }
    }
}