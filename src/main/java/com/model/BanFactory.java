package com.model;

import com.entity.Ban;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

public abstract class BanFactory {
    public static Ban getJobBan(String ckey, String adminCkey, String job, String reason, String adminWho, Date banTime, int durationTime, Date expirationTime) {
        List<String> adminsWasOnline = new ArrayList<>(Arrays.asList(adminWho.split(", ")));

        return new Ban(ckey, adminCkey, job, reason, banTime, durationTime, expirationTime, adminsWasOnline);
    }

    public static Ban getTempBan(String ckey, String adminCkey, String reason, String adminWho, Date banTime, int durationTime, Date expirationTime) {
        List<String> adminsWasOnline = new ArrayList<>(Arrays.asList(adminWho.split(", ")));

        return new Ban(ckey, adminCkey, reason, banTime, durationTime, expirationTime, adminsWasOnline);
    }

    public static Ban getPermaJobBan(String ckey, String adminCkey, String job, String reason, String adminWho, Date banTime) {
        List<String> adminsWasOnline = new ArrayList<>(Arrays.asList(adminWho.split(", ")));

        return new Ban(ckey, adminCkey, job, reason, banTime, adminsWasOnline);
    }

    public static Ban getPermaBan(String ckey, String adminCkey, String reason, String adminWho, Date banTime) {
        List<String> adminsWasOnline = new ArrayList<>(Arrays.asList(adminWho.split(", ")));

        return new Ban(ckey, adminCkey, reason, banTime, adminsWasOnline);
    }
}
