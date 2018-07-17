package com.model;

import com.entity.Ban;
import com.exception.CanNotGetConnectionException;
import com.exception.CkeyBanInfoIsNotFoundException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DataBaseDAO {
    private static final Logger logger = Logger.getLogger(DataBaseDAO.class);
    private static DataBaseDAO dataBaseDAO;
    private static final Properties properties = new Properties();

    // Кэш и кэш-апдейтер. Кэш-апдейтер обновляет кэш каждый час.
    private CacheUpdater cacheUpdater = new CacheUpdater();
    private Set<String> availableCkeySet = new HashSet<>();

    static {
        try {
            logger.info("Поиск JDBC драйвера для MySQL.");
            Class.forName("com.mysql.jdbc.Driver");
            logger.info("Загрузка JDBC драйвера для MySQL успешно завершена.");
        } catch (ClassNotFoundException e) {
            logger.error("JDBC драйвер для MySQL не был найден. Корректная работа программы невозможна без подключения к БД.", e);
        }

        try (InputStream inputStream = Files.newInputStream(Paths.get(DataBaseDAO.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("%20", " ") + "database.properties"))) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Файл database.properties не был найден. Пожалуйста, проверьте, что файл database.properties действительно существует в директории src, после чего перезапустите программу.", e);
            throw new CanNotGetConnectionException(e);
        }
    }

    /**
     * Возвращение DAO-объекта. lazy initialization. Во время инициализации заполняет кэш, что единожды может вызвать нагрузку.
     *
     * @return DAO
     */
    public static DataBaseDAO getDataBaseDAO() {
        if (dataBaseDAO == null) {
            logger.info("Инициализация DAO...");
            dataBaseDAO = new DataBaseDAO();

            try (Connection connection = dataBaseDAO.getConnection();
                 Statement statement = connection.createStatement()) {

                logger.info("Загрузка в кэш уникальных ckey-значений из БД...");

                ResultSet results = statement.executeQuery("SELECT DISTINCT ckey FROM " + properties.get("ban_table"));

                while (results.next())
                    dataBaseDAO.availableCkeySet.add(results.getString("ckey"));

                logger.info("Загрузка кэша успешно завершена. Всего загружено сикеев: " + dataBaseDAO.availableCkeySet.size());
            } catch (SQLException e) {
                logger.fatal("Ошибка загрузки кэша. Проверьте доступ к БД. Проверьте настройки в файле database.properties. Корректная работа программы невозможна без правильной инициализации кэша. Завершение программы...", e);
                System.exit(1);
            } catch (CanNotGetConnectionException e) {
                logger.fatal("Ошибка загрузки кэша. Не удалось соединиться с БД. Корректная работа программы невозможна без правильной инициализации кэша. Завершение программы...", e);
                System.exit(1);
            }
        }

        return dataBaseDAO;
    }

    // ### Job Bans Without Admin Ckey ### \\
    public List<Ban> getJobBans(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.NO_ORDER);
    }

    public List<Ban> getJobBansOrderedByAdminCkeyASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.ADMIN_CKEY_ASC);
    }

    public List<Ban> getJobBansOrderedByAdminCkeyDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.ADMIN_CKEY_DESC);
    }

    public List<Ban> getJobBansOrderedByBantimeASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.BANTIME_ASC);
    }

    public List<Ban> getJobBansOrderedByBantimeDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.BANTIME_DESC);
    }

    public List<Ban> getJobBansOrderedByDurationASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.DURATION_ASC);
    }

    public List<Ban> getJobBansOrderedByDurationDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.DURATION_DESC);
    }

    public List<Ban> getJobBansOrderedByJobASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.JOB_ASC);
    }

    public List<Ban> getJobBansOrderedByJobDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, true, Order.JOB_DESC);
    }

    // ### Job Bans By Admin Ckey ### \\
    public List<Ban> getJobBansByAdminCkey(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.NO_ORDER);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByAdminCkeyASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.ADMIN_CKEY_ASC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByAdminCkeyDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.ADMIN_CKEY_DESC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByBantimeASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.BANTIME_ASC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByBantimeDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.BANTIME_DESC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByDurationASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.DURATION_ASC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByDurationDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.DURATION_DESC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByJobASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.JOB_ASC);
    }

    public List<Ban> getJobBansByAdminCkeyOrderedByJobDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, true, Order.JOB_DESC);
    }

    // ### Bans Without Admin Ckey ### \\
    public List<Ban> getBans(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.NO_ORDER);
    }

    public List<Ban> getBansOrderedByAdminCkeyASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.ADMIN_CKEY_ASC);
    }

    public List<Ban> getBansOrderedByAdminCkeyDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.ADMIN_CKEY_DESC);
    }

    public List<Ban> getBansOrderedByBantimeASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.BANTIME_ASC);
    }

    public List<Ban> getBansOrderedByBantimeDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.BANTIME_DESC);
    }

    public List<Ban> getBansOrderedByDurationASC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.DURATION_ASC);
    }

    public List<Ban> getBansOrderedByDurationDESC(String ckey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, null, false, Order.DURATION_DESC);
    }

    // ### Bans By Admin Ckey ### \\
    public List<Ban> getBansByAdminCkey(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.NO_ORDER);
    }

    public List<Ban> getBansByAdminCkeyOrderedByAdminCkeyASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.ADMIN_CKEY_ASC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByAdminCkeyDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.ADMIN_CKEY_DESC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByBantimeASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.BANTIME_ASC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByBantimeDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.BANTIME_DESC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByDurationASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.DURATION_ASC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByDurationDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.DURATION_DESC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByJobASC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.JOB_ASC);
    }

    public List<Ban> getBansByAdminCkeyOrderedByJobDESC(String ckey, String adminCkey) throws CkeyBanInfoIsNotFoundException {
        return getBans(ckey, adminCkey, false, Order.JOB_DESC);
    }

    /**
     * Метод для извлечения списка банов из БД. Конструрирует запрос на лету в зависимости от переданных параметров.
     * @param ckey Сикей игрока.
     * @param adminCkey Сикей администратора.
     * @param jobBan true - jobban, false - ban.
     * @param order Порядок, в котором будет проводиться сортировка.
     * @return Список извлеченных банов.
     * @throws CkeyBanInfoIsNotFoundException Игрока нет в кэше.
     */
    public List<Ban> getBans(String ckey, String adminCkey, boolean jobBan, Order order) throws CkeyBanInfoIsNotFoundException {
        if (isCkeyNotExisting(ckey))
            throw new CkeyBanInfoIsNotFoundException(ckey);

        String query = String.format("SELECT bantime, %sreason, duration, expiration_time, ckey, a_ckey, adminwho, bantype FROM %s WHERE ckey=?", jobBan ? "job, " : "", properties.get("ban_table"));
        String log = "Извлечение ";

        if (jobBan) {
            log += "джоббанов игрока " + ckey + ".";
            query += " AND (bantype='JOB_TEMPBAN' OR bantype='JOB_PERMABAN')";
        } else {
            log += "банов игрока " + ckey + ".";
            query += " AND (bantype='TEMPBAN' OR bantype='PERMABAN')";
        }

        if (adminCkey != null && !adminCkey.isEmpty()) {
            log += " Администратор, выдавший бан: " + adminCkey + ".";
            query += " AND a_ckey=?";
        }

        if (order != Order.NO_ORDER) {
            log += " Сортировка: " + order.getOrderQueryValue();
            query += " ORDER BY " + order.getOrderQueryValue();
        }

        logger.info(log);

        List<Ban> resultList = new ArrayList<>();

        try (Connection connection = getDataBaseDAO().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ckey);

            if (adminCkey != null && !adminCkey.isEmpty())
                statement.setString(2, adminCkey);

            ResultSet resultSet = statement.executeQuery();
            retrieveBansInfo(resultList, resultSet);
        } catch (SQLException e) {
            logger.error("Ошибка извлечения списка банов игрока " + ckey + ". Возможные (но не все) причины: изменение структуры БД или отказ в доступе к БД.", e);
        }

        return resultList;
    }

    public void updateCash() {
        try (Connection connection = dataBaseDAO.getConnection();
             Statement statement = connection.createStatement()) {

            logger.info("Обновление кэша...");

            ResultSet results = statement.executeQuery("SELECT DISTINCT ckey FROM " + properties.get("ban_table"));

            while (results.next())
                dataBaseDAO.availableCkeySet.add(results.getString("ckey"));

            logger.info("Обновление кэша завершено. Всего загружено сикеев: " + dataBaseDAO.availableCkeySet.size());
        } catch (SQLException | CanNotGetConnectionException e) {
            logger.error("Ошибка обновления кэша.", e);
        }
    }
    /**
     * Метод для установления соединения с базой данных.
     *
     * @return Инкапсуляция соединения с базой данных.
     */
    private Connection getConnection() throws CanNotGetConnectionException {
        try {
            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных.", e);
            throw new CanNotGetConnectionException(e);
        }
    }

    /**
     * Проверка, что есть хотя бы один бан у переданного сикея.
     *
     * @param ckey Сикей игрока.
     * @return true - у игрока есть хотя бы один бан, false - игрок не существует или ранее не был забанен.
     */
    private boolean isCkeyNotExisting(String ckey) {
        return !availableCkeySet.contains(ckey);
    }

    private void retrieveBansInfo(List<Ban> toRetrieve, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            switch (resultSet.getString("bantype")) {
                case "PERMABAN": {
                    try {
                        String reason = new String(resultSet.getBytes("reason"), "windows-1251");

                        toRetrieve.add(BanFactory.getPermaBan(resultSet.getString("ckey"), resultSet.getString("a_ckey"), reason,
                                resultSet.getString("adminwho"), new Date(resultSet.getTimestamp("bantime").getTime())));
                    } catch (UnsupportedEncodingException e) {
                        // It can't be.
                    }
                }
                break;
                case "TEMPBAN": {
                    try {
                        String reason = new String(resultSet.getBytes("reason"), "windows-1251");

                        toRetrieve.add(BanFactory.getTempBan(resultSet.getString("ckey"), resultSet.getString("a_ckey"), reason,
                                resultSet.getString("adminwho"), new Date(resultSet.getTimestamp("bantime").getTime()), resultSet.getInt("duration"), new Date(resultSet.getTimestamp("expiration_time").getTime())));
                    } catch (UnsupportedEncodingException e) {
                        // It can't be.
                    }
                }
                break;
                case "JOB_PERMABAN": {
                    try {
                        String reason = new String(resultSet.getBytes("reason"), "windows-1251");

                        toRetrieve.add(BanFactory.getPermaJobBan(resultSet.getString("ckey"), resultSet.getString("a_ckey"), resultSet.getString("job"),
                                reason, resultSet.getString("adminwho"), new Date(resultSet.getTimestamp("bantime").getTime())));
                    } catch (UnsupportedEncodingException e) {
                        // It can't be.
                    }
                }
                break;
                case "JOB_TEMPBAN": {
                    try {
                        String reason = new String(resultSet.getBytes("reason"), "windows-1251");

                        toRetrieve.add(BanFactory.getJobBan(resultSet.getString("ckey"), resultSet.getString("a_ckey"), resultSet.getString("job"),
                                reason, resultSet.getString("adminwho"), new Date(resultSet.getTimestamp("bantime").getTime()),
                                resultSet.getInt("duration"), new Date(resultSet.getTimestamp("expiration_time").getTime())));
                    } catch (UnsupportedEncodingException e) {
                        // It can't be.
                    }
                }
                break;
            }
        }
    }

    private static class CacheUpdater implements Runnable {
        private static final Logger logger = Logger.getLogger(CacheUpdater.class);

        private CacheUpdater() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    logger.info("Обновление кэша запланировано через один час!");
                    // Один час задержка.
                    Thread.sleep(60*60*1000);
                    getDataBaseDAO().updateCash();
                    logger.info("Произведено обновление кэша!");
                } catch (InterruptedException e) {
                    logger.error("Остановка кэш-апдейтера...", e);
                    break;
                }
            }
        }
    }
    // singleton
    private DataBaseDAO() {

    }
}