package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    // User's meals
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final Meal meal1 = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 8, 0), "Завтрак User", 300);
    public static final Meal meal2 = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 12, 30), "Обед User", 1200);
    public static final Meal meal3 = new Meal(counter++, LocalDateTime.of(2022, 2, 18, 20, 0), "Ужин User", 600);
    public static final Meal meal4 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 7, 10), "Завтрак User", 250);
    public static final Meal meal5 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 12, 0), "Обед User", 700);
    public static final Meal meal6 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 22, 0), "Ужин User", 1000);
    // Admin's meals
    public static final Meal meal7 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 9, 0), "Завтрак Admin", 500);
    public static final Meal meal8 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 12, 0), "Обед Admin", 1400);
    public static final Meal meal9 = new Meal(counter++, LocalDateTime.of(2022, 2, 19, 22, 0), "Ужин Admin", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, 2, 20, 0, 0), "Tea", 50);
    }
}
