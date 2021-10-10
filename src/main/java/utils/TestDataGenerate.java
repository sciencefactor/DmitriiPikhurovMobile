package utils;

import entities.User;
import java.util.Random;

public class TestDataGenerate {

    public static User user() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                                       .limit(targetStringLength)
                                       .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                       .toString();

        return new User(generatedString, generatedString + "@gmail.com", generatedString);
    }

}
