package com.yskj.acdr.common.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;

public class TableToEntityConstructor {
    public static final String BASE_PATH = System.getProperty("user.dir"); // 修改为你的项目路径

    // 新版本的代码生成器
    public static void AutoTable(String moduleName, String entityPackage, String mapperPackage, String servicePackage, String... tables) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/acdr?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false",
                        "root",
                        "root")
                .globalConfig(builder -> {
                    builder.author("林河") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()
                            .commentDate("yyyy-MM-dd")
                            // 指定输出目录
                            .outputDir(BASE_PATH + "/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.yskj.acdr.master") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    BASE_PATH + "/src/main/java/com/yskj/acdr/master/" + moduleName + "/mapper/xml"))
                            .entity(entityPackage.isEmpty() ? "entity" : entityPackage) // 设置 entity 包名
                            .controller("controller") // 设置 controller 包名
                            .mapper(mapperPackage.isEmpty() ? "mapper" : mapperPackage) // 设置 mapper 包名
                            .service(servicePackage.isEmpty() ? "service" : servicePackage) // 设置 service 包名
                            .serviceImpl(servicePackage.isEmpty() ? "service.impl" : servicePackage + ".impl"); // 设置 service impl 包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_", "ua_", "us_", "acdr_") // 设置过滤表前缀
                            .entityBuilder()
                            .enableChainModel()
                            // 设置采用Lombok策略
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .logicDeletePropertyName("deleted")
                             .idType(IdType.ASSIGN_ID)
                            .enableFileOverride()
                            .addTableFills(
                                    new Property("updateTime", FieldFill.INSERT_UPDATE),
                                    new Property("createTime", FieldFill.INSERT),
                                    new Property("createUser", FieldFill.INSERT),
                                    new Property("userId", FieldFill.INSERT),
                                    new Property("updateUser", FieldFill.INSERT_UPDATE)
                            )
                            .build()
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .mapperBuilder()
                            .mapperAnnotation(Mapper.class)
                            .build();
                }).strategyConfig(builder -> {
                    builder.entityBuilder()
                            .javaTemplate("ftl/entity")
                            .controllerBuilder()
                            .template("ftl/controller");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    public static void main(String[] args) {
        AutoTable("order", "", "", "",
                 "acdr_order");
    }
}
