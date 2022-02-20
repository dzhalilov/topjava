package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealTestData {
    public static Integer counter = 100003;
    // User's meals
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final Meal meal1 = new Meal(counter++, LocalDateTime.parse("2022-02-18 08:00", formatter), "Завтрак User", 300);
    public static final Meal meal2 = new Meal(counter++, LocalDateTime.parse("2022-02-18 12:30", formatter), "Обед User", 1200);
    public static final Meal meal3 = new Meal(counter++, LocalDateTime.parse("2022-02-18 20:00", formatter), "Ужин User", 600);
    public static final Meal meal4 = new Meal(counter++, LocalDateTime.parse("2022-02-19 07:10", formatter), "Завтрак User", 250);
    public static final Meal meal5 = new Meal(counter++, LocalDateTime.parse("2022-02-19 13:00", formatter), "Обед User", 700);
    public static final Meal meal6 = new Meal(counter++, LocalDateTime.parse("2022-02-19 20:00", formatter), "Ужин User", 1000);
    // Admin's meals
    public static final Meal meal7 = new Meal(counter++, LocalDateTime.parse("2022-02-19 09:00", formatter), "Завтрак Admin", 500);
    public static final Meal meal8 = new Meal(counter++, LocalDateTime.parse("2022-02-19 12:00", formatter), "Обед Admin", 1400);
    public static final Meal meal9 = new Meal(counter++, LocalDateTime.parse("2022-02-19 22:00", formatter), "Ужин Admin", 500);
    // Guest's meals
    public static final Meal meal10 = new Meal(counter++, LocalDateTime.parse("2022-02-19 10:00", formatter), "Завтрак Guest", 600);
    public static final Meal meal11 = new Meal(counter++, LocalDateTime.parse("2022-02-19 14:00", formatter), "Обед Guest", 700);
    public static final Meal meal12 = new Meal(counter++, LocalDateTime.parse("2022-02-19 19:00", formatter), "Ужин Guest", 600);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Tea", 50);
    }
}
