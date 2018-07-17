package com.entity;

import com.model.BanType;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Ban {
    private Date banTime, expirationTime;
    private String reason, ckey, adminCkey, job;
    private List<String> adminsWasOnline;
    private BanType banType;
    private int durationTime;

    // JOB_BAN
    public Ban(String ckey, String adminCkey, String job, String reason, Date banTime, int durationTime, Date expirationTime, List<String> adminsWasOnline) {
        this.ckey = ckey;
        this.adminCkey = adminCkey;
        this.job = job;
        this.reason = reason;
        this.banTime = banTime;
        this.durationTime = durationTime;
        this.expirationTime = expirationTime;
        this.adminsWasOnline = adminsWasOnline;
        this.banType = BanType.JOB_BAN;
    }

    // TEMPBAN
    public Ban(String ckey, String adminCkey, String reason, Date banTime, int durationTime, Date expirationTime, List<String> adminsWasOnline) {
        this.ckey = ckey;
        this.adminCkey = adminCkey;
        this.reason = reason;
        this.banTime = banTime;
        this.durationTime = durationTime;
        this.expirationTime = expirationTime;
        this.adminsWasOnline = adminsWasOnline;
        this.banType = BanType.TEMPBAN;
    }

    // PERMABAN
    public Ban(String ckey, String adminCkey, String reason, Date banTime, List<String> adminsWasOnline) {
        this.banTime = banTime;
        this.reason = reason;
        this.ckey = ckey;
        this.adminCkey = adminCkey;
        this.adminsWasOnline = adminsWasOnline;
        this.expirationTime = banTime;
        this.banType = BanType.PERMABAN;
    }

    // JOB_PERMABAN
    public Ban(String ckey, String adminCkey, String job, String reason, Date banTime, List<String> adminsWasOnline) {
        this.ckey = ckey;
        this.adminCkey = adminCkey;
        this.job = job;
        this.reason = reason;
        this.banTime = banTime;
        this.adminsWasOnline = adminsWasOnline;
        this.expirationTime = banTime;
        this.banType = BanType.JOB_PERMABAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ban ban = (Ban) o;
        return Objects.equals(banTime, ban.banTime) &&
                Objects.equals(durationTime, ban.durationTime) &&
                Objects.equals(expirationTime, ban.expirationTime) &&
                Objects.equals(reason, ban.reason) &&
                Objects.equals(ckey, ban.ckey) &&
                Objects.equals(adminCkey, ban.adminCkey) &&
                Objects.equals(job, ban.job) &&
                Objects.equals(adminsWasOnline, ban.adminsWasOnline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banTime, durationTime, expirationTime, reason, ckey, adminCkey, job, adminsWasOnline);
    }

    public Date getBanTime() {
        return banTime;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public String getReason() {
        return reason;
    }

    public String getCkey() {
        return ckey;
    }

    public String getAdminCkey() {
        return adminCkey;
    }

    public String getJob() {
        return job;
    }

    public List<String> getAdminsWasOnline() {
        return adminsWasOnline;
    }

    public BanType getBanType() {
        return banType;
    }
}
