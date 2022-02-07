package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepo;
import ru.javawebinar.topjava.repository.MealStorageable;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    MealStorageable repo;

    @Override
    public void init() {
        repo = new MealRepo();
        repo.saveList();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");
        String action = req.getParameter("action");
        if (action != null && action.equals("delete")) {
            log.debug("deleted: " + repo.findById(Integer.parseInt(req.getParameter("id"))));
            repo.deleteById(Integer.parseInt(req.getParameter("id")));
        } else if (action != null && action.equals("edit")) {
            log.debug("start edit: " + repo.findById(Integer.parseInt(req.getParameter("id"))));
            Meal meal = repo.findById(Integer.parseInt(req.getParameter("id")));
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("meal.jsp").forward(req, resp);
        }
        List<MealTo> list = MealsUtil.fillListMealTo(repo.getMeals(), MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("mealsTo", list);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("delete meal: " + req.getParameter("id"));
        repo.deleteById(Integer.parseInt(req.getParameter("id")));
        doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("doPost Method");
        doGet(req, resp);
    }
}
