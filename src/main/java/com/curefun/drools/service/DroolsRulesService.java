package com.curefun.drools.service;

import org.kie.api.runtime.KieSession;

/**
 * Drools 规则引擎接口
 *  (1) 本地文件读取接口
 *  (2) 数据库文件读取接口
 *  (3) 服务内存已加载规则容器接口
 *
 *  后面看能不能提炼成一个简单的工厂方法做这件事
 */
public interface DroolsRulesService {

    KieSession getLocalKieSessionByName(String rule_name) throws Exception;


}
