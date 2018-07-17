package com.servlet;

import com.entity.Ban;
import com.exception.CkeyBanInfoIsNotFoundException;
import com.exception.TooManyRequestsPerMinuteException;
import com.model.DataBaseDAO;
import com.model.DataBaseDAOSecurityWrapper;
import com.model.Order;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.StringJoiner;

public class BanListInterfaceServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CashUpdateServlet.class);
    private static final DataBaseDAOSecurityWrapper DATA_BASE_DAO = new DataBaseDAOSecurityWrapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Принят запрос от " + req.getRemoteAddr());

        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        PrintWriter writer = resp.getWriter();

        RequestDispatcher headerRequestDispatcher = req.getRequestDispatcher("header.html");
        headerRequestDispatcher.include(req, resp);

        String ckeyParam = req.getParameter("ckey");
        String adminCkeyParam = req.getParameter("a_ckey");
        String banTypeParam = req.getParameter("bantype");
        String sortingTypeParam = req.getParameter("sorting_type");
        String orderParam = req.getParameter("order");

        logger.info(req.getRemoteAddr() + ": запрашивание информации об игроке " + ckeyParam + "...");
        try {
            boolean jobban = banTypeParam.equals("jobban");
            Order order = Order.NO_ORDER;

            for (Order o : Order.values())
                if (o.getOrderQueryValue().startsWith(sortingTypeParam) && o.name().endsWith(orderParam.toLowerCase()))
                    order = o;

            if (order == Order.NO_ORDER) {
                switch (sortingTypeParam) {
                    case "datetime": {
                        order = Order.BANTIME_DESC;
                    }
                    break;
                    case "job": {
                        order = Order.JOB_ASC;
                    }
                    break;
                    case "duration": {
                        order = Order.DURATION_DESC;
                    }
                    break;
                    case "a_ckey": {
                        order = Order.ADMIN_CKEY_ASC;
                    }
                    break;
                    case "standard_order": {
                        order = Order.BANTIME_DESC;
                    }
                }
            }

            List<Ban> bans = DATA_BASE_DAO.getBans(ckeyParam, adminCkeyParam, jobban, order);

            if (bans.size() == 0) {
                writer.println("Результаты отсутствуют!");
            } else {
                if (jobban) {
                    writer.println("<table align=\"center\" cellpadding=6px>" +
                            "<tr><th>CKEY</th><th>ADMIN_CKEY</th><th>JOB</th><th>REASON</th><th>DURATION</th><th>BANTIME</th><th>EXPIRATION TIME</th><th>ADMINWHO</th><th>BANTYPE</th></tr>");
                } else {
                    writer.println("<table align=\"center\" cellpadding=6px>" +
                            "<tr><th>CKEY</th><th>ADMIN_CKEY</th><th>REASON</th><th>DURATION</th><th>BANTIME</th><th>EXPIRATION TIME</th><th>ADMINWHO</th><th>BANTYPE</th></tr>");
                }

                for (Ban ban : bans) {
                    String ckey = ban.getCkey();
                    String adminCkey = ban.getAdminCkey();
                    String reason = ban.getReason();
                    Date banTime = ban.getBanTime();
                    Date banExpiration = ban.getExpirationTime();
                    int duration = ban.getDurationTime();
                    List<String> adminsWasOnline = ban.getAdminsWasOnline();
                    StringJoiner adminWho = new StringJoiner(", ");
                    adminsWasOnline.forEach(adminWho::add);
                    String banType = ban.getBanType().name();

                    if (jobban) {
                        writer.println(String.format("<tr><td>%1$s</td><td>%2$s</td><td>%9$s</td><td>%3$s</td><td>%4$s</td><td>%5$tD %5$tT</td><td>%6$tD %6$tT</td><td>%7$s</td><td>%8$s</td></tr>",
                                ckey, adminCkey, reason, duration, banTime, banExpiration, adminWho, banType, ban.getJob()));
                    } else {
                        writer.println(String.format("<tr><td>%1$s</td><td>%2$s</td><td>%3$s</td><td>%4$s</td><td>%5$tD %5$tT</td><td>%6$tD %6$tT</td><td>%7$s</td><td>%8$s</td></tr>",
                                ckey, adminCkey, reason, duration, banTime, banExpiration, adminWho, banType));
                    }
                }

                writer.println("</table>");
                logger.info("Информация об игроке " + ckeyParam + " успешно извлечена и отправлена на адрес " + req.getRemoteAddr());
            }
        } catch (CkeyBanInfoIsNotFoundException e) {
            writer.println("<h1>Информация об игроке " + ckeyParam + " не обнаружена!");
            logger.info("Не удалось извлечь информацию об игроке " + ckeyParam + " для адреса " + req.getRemoteAddr());
        } catch (TooManyRequestsPerMinuteException e) {
            writer.println("<h1>Был исчерпан лимит на количество запросов в минуту. Повторите запрос через минуту.</h1>");
            logger.warn("Обнаружена угроза спама запросами. Возможный источник: " + req.getRemoteAddr());
        }

        RequestDispatcher footerRequestDispatcher = req.getRequestDispatcher("footer.html");
        footerRequestDispatcher.include(req, resp);
    }
}