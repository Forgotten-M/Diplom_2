package utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import model.User;
import model.UserEdit;

public class Generator {

    static Faker faker = new Faker();

    public static User generateUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6, 10);
        String name = faker.name().fullName();
        return new User(email, password, name);
    }

    public static UserEdit generateUserEditedData() {
        String newEmail = faker.internet().emailAddress();
        String newPassword = faker.internet().password(6, 10);
        String newName = faker.name().fullName();
        return new UserEdit(newEmail, newPassword, newName);
    }

    public static int generateSizeForIngredientSublist(int max) {
        int sublistIndex = RandomUtils.nextInt(1, max);
        return sublistIndex;
   }
}