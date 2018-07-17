package com.servlet;

import com.exception.TooManyRequestsPerMinuteException;
import com.model.DataBaseDAO;
import com.model.DataBaseDAOSecurityWrapper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CashUpdateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CashUpdateServlet.class);
    private static final DataBaseDAOSecurityWrapper DATA_BASE_DAO = new DataBaseDAOSecurityWrapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Получение запроса на обновление кэша DAO от " + req.getRemoteHost());

        String password = req.getParameter("password");

        if (password != null && password.equals("diesiegePassword322")) {
            try {
                DATA_BASE_DAO.updateCash();
                logger.info("Пароль принят. Обновление кэша.");
            } catch (TooManyRequestsPerMinuteException e) {
                logger.warn("Невозможно обновить кэш: исчерпан лимит запросов в минуту.");
            }
        } else {
            logger.info("Неправильный пароль. В доступе отказано.");
        }
    }
}
