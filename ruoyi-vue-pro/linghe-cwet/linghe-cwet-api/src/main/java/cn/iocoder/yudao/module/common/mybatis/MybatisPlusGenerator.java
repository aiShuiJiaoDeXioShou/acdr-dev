package cn.iocoder.yudao.module.common.mybatis;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.TypeConverts;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * MP代码生成器
 *
 * @author YQK
 * @since 2022-06-20
 */
@Slf4j
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        try {
            Dict dict = YamlUtil.loadByPath("application-devp.yml");
            // 注册数据库驱动
            Class.forName(dict.getByPath("spring.datasource.driver-class-name").toString());

            // 配置数据源参数
            DataSourceConfig.Builder dbBuilder = new DataSourceConfig.Builder(
                    dict.getByPath("spring.datasource.url").toString(),
                    dict.getByPath("spring.datasource.username").toString(),
                    dict.getByPath("spring.datasource.password").toString())
                    .dbQuery(new MySqlQuery())
                    .typeConvert(TypeConverts.getTypeConvert(DbType.MYSQL));

            // 执行代码生成程序
            FastAutoGenerator.create(dbBuilder)
                    .globalConfig(c -> c
                            .author(System.getenv("USERNAME"))// 作者名
                            .outputDir(System.getProperty("user.dir") + "/src/main/java/")// 总输出目录
                            .disableOpenDir()// 不打开资源管理器
                            .build())
                    .packageConfig(c -> c
                            .parent("com.qirenqx.gzgzgs.master.music")// 四层结构生成目录
                            .build())
                    .strategyConfig(c -> c
                            .addInclude("data_grid_day_desc")// 包含的表名
                            .addTablePrefix("base_", "data_", "user_")// 忽略的前缀
                            .entityBuilder()
                            .enableLombok()
                            .disableSerialVersionUID()// 关闭实体类生成序列化ID
                            .enableChainModel()// 开启链式模型
                            .enableLombok()// 开启Lombok
                            .enableTableFieldAnnotation()// 开启生成字段注解
                            .mapperBuilder()
                            .mapperAnnotation(Mapper.class)// 开启生成@Mapper注解
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .controllerBuilder()
                            .enableRestStyle()// 开启生成@RestController控制器
                            .build())
                    .templateEngine(new FreemarkerTemplateEngine())
                    .execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
