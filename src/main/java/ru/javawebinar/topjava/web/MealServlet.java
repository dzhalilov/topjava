package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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
    MealStorageable repo = new MealRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");
        if (req.getParameter("id") != null) {
            repo.deleteById(Integer.parseInt(req.getParameter("id")));
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
        log.debug("forward to doGet");
        doGet(req, resp);
    }
}
