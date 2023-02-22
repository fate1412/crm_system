package com.fate1412.crmSystem.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;


public class MysqlGenerator {


    public static void main(String[] args) {

        AutoGenerator mpg = new AutoGenerator();
        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");  //生成路径(一般都是生成在此项目的src/main/java下面)
        gc.setAuthor("fate1412"); //设置作者
        gc.setOpen(false);
        gc.setFileOverride(true); //第二次生成会把第一次生成的覆盖掉
        gc.setBaseResultMap(true); //生成resultMap
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        dsc.setUrl("jdbc:mysql://localhost:3306/crm");
        dsc.setUsername("root");
        dsc.setPassword("a136936858");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.fate1412.mybatis");
        pc.setEntity("pojo");
//        pc.setMapper("mapper.oa");
//        pc.setXml("mapper.oa");
//        pc.setService("manager.oa");
//        pc.setServiceImpl("manager.impl");

        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); //使用lombok
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setInclude("sys_user","sys_role","sys_user_role","sys_role_permission","sys_permission");  // 逆向工程使用的表   如果要生成多个,这里可以传入String[]
        mpg.setStrategy(strategy);

        //5、执行
        mpg.execute();

    }
}
