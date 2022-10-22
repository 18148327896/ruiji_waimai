package com.rdq.ruiji.config;

import com.rdq.ruiji.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //使用registry设置映射的访问路径，映射到那些资源文件
        //addResourceHandler资源处理器，addResourceLocations
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }


    /*
    * 扩展mvc的消息转换器
    *
    * */

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("执行扩展的消息转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器底层使用jackson将Java转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器追加到mvc的消息转换器集合中,设置下标为0优先使用我们的专户暗器
        converters.add(0,messageConverter);
    }
}
