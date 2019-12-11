package com.curefun.drools.service;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service("droolsRulesService")
public class DroolsRulesServiceImpl implements DroolsRulesService{

    public KieSession getLocalKieSessionByName(String rule_name) throws Exception {

        // KieServices is the factory for all KIE services
        KieServices ks = KieServices.Factory.get();

        // From the kie services, a container is created from the classpath
        KieContainer kc = ks.getKieClasspathContainer();

        KieSession ksession = kc.newKieSession(rule_name);

        return ksession;
    }
}
