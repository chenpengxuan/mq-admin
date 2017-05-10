/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public final static String SESSION_KEY_USER_ID = "session_key_user_id";
    public final static String SESSION_KEY_USER = "session_key_user";
    public final static String SESSION_KEY_PERMIT = "session_key_permit";

    public static <T> T get(String key) {

        HttpServletRequest request = WebUtil.getRequest();

        if (request != null) {
            HttpSession session = request.getSession();

            if (session != null) {
                return (T) session.getAttribute(key);
            }
        }

        return null;
    }

    public static void put(String key, Object value) {

        HttpServletRequest request = WebUtil.getRequest();

        if (request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.setAttribute(key, value);
            }
        }
    }

    public static String sessionId(){
        HttpServletRequest request = WebUtil.getRequest();

        if (request != null) {
            HttpSession session = request.getSession();

            if (session != null) {
                return session.getId();
            }
        }

        return null;
    }

}
