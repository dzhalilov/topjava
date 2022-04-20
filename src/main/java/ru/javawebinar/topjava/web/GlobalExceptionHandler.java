package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.getRootCause(e);
        UserTo userTo = new UserTo();
        for (Map.Entry<String, String[]> pair : req.getParameterMap().entrySet()) {
            if ("name".contains(pair.getKey()) && pair.getValue().length > 0) {
                userTo.setName(pair.getValue()[0]);
            }
            if ("email".contains(pair.getKey()) && pair.getValue().length > 0) {
                userTo.setEmail(pair.getValue()[0]);
            }
            if ("caloriesPerDay".contains(pair.getKey()) && pair.getValue().length > 0) {
                userTo.setCaloriesPerDay(Integer.valueOf(pair.getValue()[0]));
            }
        }
        ErrorInfo errorInfo = ExceptionInfoHandler.handleUniqEmailError(req, e);
        HttpStatus httpStatus = HttpStatus.CONFLICT;
//        ModelAndView mav = new ModelAndView("profile", Map.of("exception", rootCause, "message", "User with this email already exists", "status", httpStatus));
        ModelAndView mav = new ModelAndView("profile",
                Map.of("exception", rootCause, "userTo", userTo, "register", true, "error", "User with this email already exists", "message", errorInfo));
        return addUserToAndStatus(mav, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.getRootCause(e);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", rootCause.toString(), "status", httpStatus));
        return addUserToAndStatus(mav, httpStatus);
    }

    private static ModelAndView addUserToAndStatus(ModelAndView mav, HttpStatus httpStatus) {
        mav.setStatus(httpStatus);
        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }
}
