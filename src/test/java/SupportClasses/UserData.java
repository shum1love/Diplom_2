package SupportClasses;

import java.util.Arrays;
import java.util.Collection;

public class UserData {
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"", "123456789", "Ricardo"},
                {"test-ricardo@yandex.ru", "", "Ricardo"},
                {"test-ricardo@yandex.ru", "password44", ""}
        });
    }
}
