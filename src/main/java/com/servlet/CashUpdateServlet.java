package com.servlet;

import com.model.DataBaseDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CashUpdateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CashUpdateServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Получение запроса на обновление кэша DAO от " + req.getRemoteHost());

        String password = req.getParameter("password");

        if (password != null && password.equals("diesiegePassword322")) {
            logger.info("Пароль принят. Обновление кэша.");
            DataBaseDAO.getDataBaseDAO().updateCash();
        } else {
            logger.info("Неправильный пароль. В доступе отказано.");
        }
    }
}
