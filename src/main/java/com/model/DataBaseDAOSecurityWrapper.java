package com.model;

import com.entity.Ban;
import com.exception.AdminCkeyIsNotFoundException;
import com.exception.CkeyBanInfoIsNotFoundException;
import com.exception.TooManyRequestsPerMinuteException;
import org.apache.log4j.Logger;

import java.util.List;

public class DataBaseDAOSecurityWrapper {
    private static final Logger logger = Logger.getLogger(DataBaseDAOSecurityWrapper.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 30;
    private static final DataBaseDAO dataBaseDAO = DataBaseDAO.getInstance();
    private Updater updater = new Updater();

    private int requestAtCurrentMinute = 0;

    public List<Ban> getAdminBans(String adminCkey, boolean jobBan, Order order) throws AdminCkeyIsNotFoundException, TooManyRequestsPerMinuteException {
        requestAtCurrentMinute++;

        logger.info("Перехват запроса к DataBaseDAO. Текущее количество запросов за минуту - " + requestAtCurrentMinute + ", максимальное количество - " + MAX_REQUESTS_PER_MINUTE);
        if (requestAtCurrentMinute > MAX_REQUESTS_PER_MINUTE)
            throw new TooManyRequestsPerMinuteException();

        try {
            return dataBaseDAO.getAdminBans(adminCkey, jobBan, order);
        } catch (AdminCkeyIsNotFoundException e) {
            logger.info("Сикей админа " + adminCkey + " не был найден в кэше. Данный запрос не будет засчитан.");
            requestAtCurrentMinute--;
            throw e;
        }
    }

    public List<Ban> getBans(String ckey, String adminCkey, boolean jobBan, Order order) throws CkeyBanInfoIsNotFoundException, TooManyRequestsPerMinuteException {
        requestAtCurrentMinute++;

        logger.info("Перехват запроса к DataBaseDAO. Текущее количество запросов за минуту - " + requestAtCurrentMinute + ", максимальное количество - " + MAX_REQUESTS_PER_MINUTE);
        if (requestAtCurrentMinute > MAX_REQUESTS_PER_MINUTE)
            throw new TooManyRequestsPerMinuteException();

        try {
            return dataBaseDAO.getBans(ckey, adminCkey, jobBan, order);
        } catch (CkeyBanInfoIsNotFoundException e) {
            logger.info("Сикей игрока " + ckey + " не был найден в кэше. Данный запрос не будет засчитан.");
            requestAtCurrentMinute--;
            throw e;
        }
    }

    public void updateCash() throws TooManyRequestsPerMinuteException {
        requestAtCurrentMinute++;

        logger.info("Перехват запроса к DataBaseDAO. Текущее количество запросов за минуту - " + requestAtCurrentMinute + ", максимальное количество - " + MAX_REQUESTS_PER_MINUTE);
        if (requestAtCurrentMinute > MAX_REQUESTS_PER_MINUTE)
            throw new TooManyRequestsPerMinuteException();

        dataBaseDAO.updateCash();
    }

    private class Updater implements Runnable {
        private Updater() {
            Thread thread = new Thread(this, "Updater");
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 60);
                    logger.info("Ежеминутный сброс количества запросов.");
                    DataBaseDAOSecurityWrapper.this.requestAtCurrentMinute = 0;
                } catch (InterruptedException e) {
                    logger.error(e);
                    break;
                }
            }
        }
    }
}
