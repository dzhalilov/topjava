package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static int counter = AbstractBaseEntity.START_SEQ + 3;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;
    public static final String NEW_DESCRIPTION = "Обновлённая еда";
    public static final LocalDateTime NEW_DATE_TIME = LocalDateTime.of(2022, 2, 21, 15, 0);
    public static final int NEW_CALORIES = 550;
    public static final LocalDate startDate = LocalDate.of(2022, 2, 19);
    // User's meals
    public static final Meal meal1User = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 8, 0), "Завтрак User", 300);
    public static final Meal meal2User = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 12, 30), "Обед User", 1200);
    public static final Meal meal3User = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 20, 0), "Ужин User", 600);
    public static final Meal meal4User = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 7, 10), "Завтрак User", 250);
    public static final Meal meal5User = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 13, 0), "Обед User", 700);
    public static final Meal meal6User = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 20, 0), "Ужин User", 1000);
    // Admin's meals
    public static final Meal meal7Admin = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 9, 0), "Завтрак Admin", 50);
    public static final Meal meal8Admin = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 12, 0), "Обед Admin", 1400);
    public static final Meal meal9Admin = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 22, 0), "Ужин Admin", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, 2, 20, 0, 0), "Tea", 50);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
