import java.util.Random;

public class GenerateRandomString {
    public String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // Метод для генерации случайной строки
    private String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString();
    }
    public String randomEmail = generateRandomString(10) + "@mail.com";
    public String randomPassword = generateRandomString(8);

}
