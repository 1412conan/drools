package com.curefun.drools.controller;

import com.curefun.drools.entity.Message;
import com.curefun.drools.entity.Order;
import com.curefun.drools.entity.User;
import com.curefun.drools.model.Address;
import com.curefun.drools.model.fact.AddressCheckResult;
import com.curefun.drools.service.DroolsRulesService;
import com.curefun.drools.service.ReloadDroolsRulesService;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/drools")
@Controller
public class DroolsRulesController {

    @Resource
    private ReloadDroolsRulesService rules;

    @Autowired
    private DroolsRulesService droolsRulesService;


    /**
     * ===========  本地调用  ============
     * 范例1  调用本地维护的hello-world.drl 规则文件
     * META-INF 中的 kmodule.xml 维护对应的规则 hello-worldKS
     * helloWorld
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/helloWorld")
    public String helloWorld() throws Exception {

        KieSession ksession = droolsRulesService.getLocalKieSessionByName("hello-worldKS");
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        ksession.insert(message);//插入
        ksession.fireAllRules();//执行规则
        ksession.dispose();

        return message.getMessage();
    }

    /**
     * 范例2 订单分数 本地规则文件校验方法
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value =  "/orderPoint")
    public void execOrderPointRule()throws Exception{

        KieSession ksession = droolsRulesService.getLocalKieSessionByName("point-rulesKS");

        List<Order> orderList = getInitData();

        for (int i = 0; i < orderList.size(); i++) {
            Order o = orderList.get(i);
            ksession.insert(o);
            ksession.fireAllRules();
            // 执行完规则后, 执行相关的逻辑
            addScore(o);
        }

        ksession.dispose();
    }

    /**
     * ===============  数据库调用  =================
     * 范例 3 通过上面的本地调用范例下面说一下数据库调用
     * 针对 实际生产环境下面数存在对应的规则实时修改的情况下面提供如下解决方案
     *  先调用 chkPostCode -> 调用reload -->  chkPostCode 查看三个阶段的不同返回
     *
     * (1) 规则服务器启动时加载对应的数据库规则到对应的规则目录(下面举例时默认加载还是先做本地往后再优化)
     * (2) 在数据库维护时 调用reload()更新对应规则到本地的服务
     * @param num
     */
    @ResponseBody
    @RequestMapping(value =  "/chkPostCode/{num}")
    public void chkPostCode(@PathVariable int num){
        // 这里偷懒 传个几位数就好 postCode随机生成位数
        Address address = new Address(); // 规则校验对象
        address.setPostcode(generateRandom(num));

        System.out.println(address.getPostcode());
        KieSession ksession = ReloadDroolsRulesService.kieContainer.newKieSession();

        AddressCheckResult result = new AddressCheckResult(); // 用于接收规则返回对象
        ksession.insert(address);
        ksession.insert(result);
        int ruleFiredCount = ksession.fireAllRules();
        ksession.destroy();
        System.out.println("触发了" + ruleFiredCount + "条规则");

        // result 中的 postCodeResult 通过相关规则已经做了处理
        if(result.isPostCodeResult()){
            System.out.println("规则校验通过");
        }
    }

    /**
     * 从数据加载最新规则
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "ok";
    }

    /**
     * 生成随机数
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number=new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number=number.append(chars.charAt(rand));
        }
        return number.toString();
    }

    private static void addScore(Order o){
        System.out.println("用户" + o.getUser().getName() + "享受额外增加积分: " + o.getScore());
    }

    private static List<Order> getInitData() throws Exception {
        List<Order> orderList = new ArrayList<Order>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        {
            Order order = new Order();
            order.setAmout(80);
            order.setBookingDate(df.parse("2015-07-01"));
            User user = new User();
            user.setLevel(1);
            user.setName("Name1");
            order.setUser(user);
            order.setScore(111);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(200);
            order.setBookingDate(df.parse("2015-07-02"));
            User user = new User();
            user.setLevel(2);
            user.setName("Name2");
            order.setUser(user);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(800);
            order.setBookingDate(df.parse("2015-07-03"));
            User user = new User();
            user.setLevel(3);
            user.setName("Name3");
            order.setUser(user);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(1500);
            order.setBookingDate(df.parse("2015-07-04"));
            User user = new User();
            user.setLevel(4);
            user.setName("Name4");
            order.setUser(user);
            orderList.add(order);
        }
        return orderList;
    }

}
