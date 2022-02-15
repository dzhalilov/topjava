package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            SecurityUtil.setAuthUserId(1);
            System.out.println(mealRestController.get(3));
            mealRestController.delete(3);
            System.out.println(mealRestController.create(new Meal(LocalDateTime.now(), "Tea", 50)));

            int changingIdOfMeal = 1;
            Meal oldMeal = mealRestController.get(changingIdOfMeal);
            Meal newMeal = new Meal(changingIdOfMeal, oldMeal.getDateTime(), oldMeal.getDescription(), oldMeal.getCalories() + 100);

            System.out.println(mealRestController.update(newMeal, changingIdOfMeal));
            mealRestController.getAll().forEach(System.out::println);
        }
    }
}
