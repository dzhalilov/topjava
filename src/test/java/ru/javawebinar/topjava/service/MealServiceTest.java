package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.service.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;
    public static final int NO_FOUND_ID = 999999;
    public static final String NEW_DESCRIPTION = "Обновлённая еда";

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(meal1.getId(), USER_ID);
        Assert.assertEquals(meal1, meal);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getNoFoundId() {
        Meal meal = mealService.get(NO_FOUND_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWithWrongUserId() {
        mealService.get(meal7.getId(), USER_ID);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void delete() {
        mealService.delete(meal1.getId(), USER_ID);
        mealService.get(meal1.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWithWrongUserId() {
        mealService.delete(meal1.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2022, 2, 19);
        List<Meal> meals = mealService.getBetweenInclusive(startDate, null, USER_ID);
        List<Meal> expected = Arrays.asList(meal6, meal5, meal4);
        Assert.assertEquals(expected, meals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        List<Meal> expected = Arrays.asList(meal9, meal8, meal7);
        Assert.assertEquals(expected, meals);
    }

    @Test
    public void update() {
        Meal meal = new Meal(meal1.getId(), meal1.getDateTime(),
                meal1.getDescription(), meal1.getCalories());
        meal.setDescription(NEW_DESCRIPTION);
        Meal mealResult = mealService.update(meal, USER_ID);
        Assert.assertEquals(meal, mealResult);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void updateWithWrongId() {
        meal1.setId(NO_FOUND_ID);
        mealService.update(meal1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateWithWrongUserId() {
        mealService.update(meal1, NO_FOUND_ID);
    }

    @Test
    public void create() {
        Meal meal = mealService.create(getNew(), ADMIN_ID);
        Assert.assertEquals("Tea", meal.getDescription());
    }

    @Test(expected = DuplicateKeyException.class)
    public void createWithDuplicateDate() {
        Meal meal = new Meal(null, meal8.getDateTime(), NEW_DESCRIPTION, 500);
        mealService.create(meal, ADMIN_ID);
    }
}