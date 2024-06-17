package xyz.ontip.util;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
@Component
public class TimeUtils {

    /**
     * 获取当前时间
     *
     * @param format 规则 如 yyyy-MM--dd
     * @return 格式化后的时间字符串
     */
    public String getStringNowTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取前day天的日期
     *
     * @param format 规则 如 yyyy-MM--dd
     * @param day    前几天
     * @return 格式化后的时间字符串
     */
    public String getStringTimeByDay(String format, Integer day) {
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 获取前day天的日期
        Date date = new Date(System.currentTimeMillis() - day * 24 * 60 * 60 * 1000);
        return sdf.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 转换后的Date对象
     */
    public Date getDateNowTime() {
        return new Date();
    }

    /**
     * @param day 前几天
     * @return 转换后的Date对象
     */
    public Date getDateBeforeTimeByDay(Integer day) {
        Calendar calendar = Calendar.getInstance();
        // 获取当前时间
        calendar.setTime(new Date());
        // 将日历向后推day天
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        // 获取day天之前的时间
        return calendar.getTime();
    }

    /**
     * @param nowTime 要转换的时间字符串
     * @param format  时间格式 如 yyyy-MM--dd
     * @return 转换后的Date对象
     */
    public Date stringToDate(String nowTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(nowTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String TimestampToDate(Long timeStamp) {

        // 将时间戳转换为 Instant 对象
        Instant instant = Instant.ofEpochSecond(timeStamp);

        // 将 Instant 对象转换为 LocalDateTime 对象
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }
}

