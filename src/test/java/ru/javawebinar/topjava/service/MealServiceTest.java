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
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(meal1User.getId(), USER_ID);
        assertMatch(meal, meal1User);
    }

    @Test
    public void getWithWrongId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(meal7Admin.getId(), USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(meal1User.getId(), USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(meal1User.getId(), USER_ID));
    }

    @Test
    public void deleteWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(meal1User.getId(), ADMIN_ID));
        assertMatch(mealService.get(meal1User.getId(), USER_ID), meal1User);
    }

    @Test
    public void deleteWithWrongId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusiveStartDateNull() {
        List<Meal> meals = mealService.getBetweenInclusive(startDate, null, USER_ID);
        assertMatch(meals, meal6User, meal5User, meal4User);
    }

    @Test
    public void getBetweenInclusiveAfterAddMealStartDateEndDate() {
        mealService.create(getNew(), USER_ID);
        List<Meal> meals = mealService.getBetweenInclusive(startDate, startDate, USER_ID);
        assertMatch(meals, meal6User, meal5User, meal4User);
    }

    @Test
    public void getBetweenInclusiveNullNull() {
        List<Meal> meals = mealService.getBetweenInclusive(null, null, USER_ID);
        assertMatch(meals, meal6User, meal5User, meal4User, meal3User, meal2User, meal1User);
    }

    @Test
    public void getBetweenInclusiveStartDateEndDate() {
        List<Meal> meals = mealService.getBetweenInclusive(startDate, startDate, USER_ID);
        assertMatch(meals, meal6User, meal5User, meal4User);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        assertMatch(meals, meal9Admin, meal8Admin, meal7Admin);
    }

    @Test
    public void update() {
        Meal meal = new Meal(meal1User.getId(), NEW_DATE_TIME, NEW_DESCRIPTION, NEW_CALORIES);
        Meal actual = new Meal(meal1User.getId(), NEW_DATE_TIME, NEW_DESCRIPTION, NEW_CALORIES);
        mealService.update(meal, USER_ID);
        Meal mealResult = mealService.get(meal1User.getId(), USER_ID);
        assertMatch(actual, mealResult);
    }

    @Test
    public void updateWithWrongId() {
        Meal meal = new Meal(NOT_FOUND, meal1User.getDateTime(), meal1User.getDescription(), meal1User.getCalories());
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(meal, USER_ID));
    }

    @Test
    public void updateWithWrongUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(meal1User, NOT_FOUND));
        assertMatch(mealService.get(meal1User.getId(), USER_ID), meal1User);
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), ADMIN_ID);
        Meal actual = getNew();
        actual.setId(counter);
        assertMatch(actual, created);
        assertMatch(actual, mealService.get(counter, ADMIN_ID));
    }

    @Test
    public void createWithDuplicateDate() {
        Meal meal = new Meal(null, meal8Admin.getDateTime(), NEW_DESCRIPTION, NEW_CALORIES);
        Assert.assertThrows(DuplicateKeyException.class, () -> mealService.create(meal, ADMIN_ID));
    }
}