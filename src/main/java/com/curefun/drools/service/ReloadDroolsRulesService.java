package com.curefun.drools.service;

import com.curefun.drools.model.Rule;
import com.curefun.drools.repository.RuleRepository;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by neo on 17/7/31.
 */

@Service
public class ReloadDroolsRulesService {

    public static KieContainer kieContainer;

    private static final String RULES_PATH = "rules/";

    @Autowired
    private RuleRepository ruleRepository;

    public  void reload() throws IOException{
        KieContainer kieContainer=loadContainerFromString(loadRules());
        this.kieContainer=kieContainer;
    }

    private List<Rule>  loadRules(){
        List<Rule> rules=ruleRepository.findAll();
//        System.out.println(rules.toString());
        return rules;
    }

    private  KieContainer loadContainerFromString(List<Rule> rules) throws IOException{
        long startTime = System.currentTimeMillis();
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        KieFileSystem kfs = ks.newKieFileSystem();

        Map<String,Object> ruleMap = new HashMap<String,Object>();

        for (Rule rule:rules) {
            String  drl=rule.getContent();
            kfs.write("src/main/resources/rules/" + drl.hashCode() + ".drl", drl);
            ruleMap.put(rule.getRuleKey(),rule);
        }

        for (Resource file : getRuleFiles()) {
            if(ruleMap.get(file.getFilename().substring(0,file.getFilename().lastIndexOf(".")))==null){
                System.out.println(file.getFilename().substring(0,file.getFilename().lastIndexOf(".")));
                kfs.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
            }
        }

        KieBuilder kb = ks.newKieBuilder(kfs);

        kb.buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time to build rules : " + (endTime - startTime)  + " ms" );
        startTime = System.currentTimeMillis();
        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
        endTime = System.currentTimeMillis();
        System.out.println("Time to load container: " + (endTime - startTime)  + " ms" );
        return kContainer;
    }

    private Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
    }

}
