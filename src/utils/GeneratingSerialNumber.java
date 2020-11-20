package utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class GeneratingSerialNumber {
    public static final int getNbUpper = 9;
    public static final int nbDigits = 10;
    public static String generateSerialNumber()
    {
        return new SecureRandom().ints(0,36)
                .mapToObj(i -> Integer.toString(i, 36))
                .map(String::toUpperCase).distinct().limit(16).collect(Collectors.joining())
                .replaceAll("([A-Z0-9]{4})", "$1-").substring(0,19);
    }
}
