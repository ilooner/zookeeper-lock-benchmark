package org.apache.bench.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationValidator implements IParameterValidator {
  @Override
  public void validate(String name, String value) throws ParameterException {
    try {
      Duration.parse(value);
    } catch (DateTimeParseException e) {
      String message = String.format("The given duration (%s) is not valid. Error at index %d.",
        value, e.getErrorIndex());
      message += "\nPlease see how the duration format works here https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html";
      throw new ParameterException(message);
    }
  }
}
