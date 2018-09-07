package org.apache.bench.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class PositiveIntegerValidator implements IParameterValidator {
  @Override
  public void validate(String name, String value) throws ParameterException {
    int n;

    try {
      n = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      final String message = String.format("The value %s for option %s is not a valid integer.", value, name);
      throw new ParameterException(message);
    }

    if (n <= 0) {
      throw new ParameterException("Parameter " + name + " should be positive (found " + value + ")");
    }
  }
}
