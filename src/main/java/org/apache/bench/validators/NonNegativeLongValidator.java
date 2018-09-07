package org.apache.bench.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NonNegativeLongValidator implements IParameterValidator {
  @Override
  public void validate(String name, String value) throws ParameterException {
    long n;

    try {
      n = Long.parseLong(value);
    } catch (NumberFormatException e) {
      final String message = String.format("The value %s for option %s is not a valid long.", value, name);
      throw new ParameterException(message);
    }

    if (n < 0L) {
      throw new ParameterException("Parameter " + name + " should be positive (found " + value + ")");
    }
  }
}
