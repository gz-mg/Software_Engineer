package com.edu.hzau.cocs.fe.utils;

import org.springframework.stereotype.Component;


/**
 * @Author yue
 */

@Component
public class DateUtils {
    public double getDate() {
        return System.currentTimeMillis();
    }
}
