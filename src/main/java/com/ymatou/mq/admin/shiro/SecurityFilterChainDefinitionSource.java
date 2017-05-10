/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.shiro;

import org.apache.shiro.config.Ini;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.FactoryBean;


public class SecurityFilterChainDefinitionSource implements FactoryBean<Ini.Section> {
    @Override
    public Ini.Section getObject() throws Exception {
        return loadSection();
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private Ini.Section loadSection() {
        Ini ini = Ini.fromResourcePath("classpath:shiro.ini");
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        return section;
    }
}
