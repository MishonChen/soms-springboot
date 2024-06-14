package xyz.ontip.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {
    @Bean
    public Snowflake snowflake() {
        // 初始化 Snowflake 实例
        long workerId = 0L;
        long datacenterId = 0L;
        return new Snowflake(workerId, datacenterId);
    }
}
