package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(meal1.getId(), USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotFoundId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(meal7.getId(), USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(meal1.getId(), USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(meal1.getId(), USER_ID));
    }

    @Test
    public void deleteWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(meal1.getId(), ADMIN_ID));
        assertMatch(mealService.get(meal1.getId(), USER_ID), meal1);
    }

    @Test
    public void deleteWithWrongId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusiveStartDateNull() {
        List<Meal> meals = mealService.getBetweenInclusive(startDate, null, USER_ID);
        assertMatch(meals, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenInclusiveAfterAddMealStartDateEndDate() {
        mealService.create(getNew(), USER_ID);
        List<Meal> meals = mealService.getBetweenInclusive(startDate, startDate, USER_ID);
        assertMatch(meals, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenInclusiveNullNull() {
        List<Meal> meals = mealService.getBetweenInclusive(null, null, USER_ID);
        assertMatch(meals, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenInclusiveStartDateEndDate() {
        List<Meal> meals = mealService.getBetweenInclusive(startDate, startDate, USER_ID);
        assertMatch(meals, meal6, meal5, meal4);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        assertMatch(meals, meal9, meal8, meal7);
    }

    @Test
    public void update() {
        Meal meal = new Meal(meal1.getId(), NEW_DATE_TIME,
                NEW_DESCRIPTION, NEW_CALORIES);
        mealService.update(meal, USER_ID);
        Meal mealResult = mealService.get(meal1.getId(), USER_ID);
        assertMatch(meal, mealResult);
    }

    @Test
    public void updateWithWrongId() {
        Meal meal = new Meal(NOT_FOUND, meal1.getDateTime(), meal1.getDescription(), meal1.getCalories());
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(meal, USER_ID));
    }

    @Test
    public void updateWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(meal1, NOT_FOUND));
        assertMatch(mealService.get(meal1.getId(), USER_ID), meal1);
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), ADMIN_ID);
        Meal actual = getNew();
        actual.setId(counter++);
        assertMatch(actual, created);
    }

    @Test
    public void createWithDuplicateDate() {
        Meal meal = new Meal(null, meal8.getDateTime(), NEW_DESCRIPTION, NEW_CALORIES);
        Assert.assertThrows(DuplicateKeyException.class, () -> mealService.create(meal, ADMIN_ID));
    }
}