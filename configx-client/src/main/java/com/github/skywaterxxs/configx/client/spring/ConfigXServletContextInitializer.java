package com.github.skywaterxxs.configx.client.spring;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Map;

/**
 * @author xuxiaoshuo 2018/4/10
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class ConfigXServletContextInitializer implements ServletContextInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConfigXServletContextInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {


        FilterRegistration.Dynamic servletWatcherFilter = servletContext.addFilter("test", new ConfigXFilter());
        servletWatcherFilter.setAsyncSupported(true);
        servletWatcherFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC,
                DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/configX/update/v1");

    }

    public class ConfigXFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            //http://127.0.0.1:8080/configX/update/v1

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader br = request.getReader();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            PrintWriter out = response.getWriter();

            String body = stringBuilder.toString();

            Map<String, String> config = JsonUtil.ofMap(body, String.class, String.class);

            if (config == null || config.isEmpty()) {
                out.write("没有设置");
                return;
            }

            Object bean = ConfigXStore.store.get("configXBean");
            Class<?> clazz = bean.getClass();

            try {
                StringBuilder outSb = new StringBuilder();
                for (Field field : clazz.getDeclaredFields()) {

                    String configKey = field.getName();
                    String configValue = config.get(configKey);

                    if (StringUtils.isBlank(configValue)) {
                        continue;
                    }

                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, bean, configValue);
                    logger.info("[成功更新]key={},value={}", configKey, configValue);
                    outSb.append("[成功更新]key=").append(configKey).append(",value=").append(configValue).append("\n");
                }
                out.write(outSb.toString());

            } catch (Exception e) {
                out.write("设置发送错误:" + e.getMessage());
            }
        }

        @Override
        public void destroy() {

        }
    }
}
