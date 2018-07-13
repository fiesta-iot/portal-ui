package eu.fiestaiot.portal.ui.config;

import com.codahale.metrics.MetricRegistry;
import eu.fiestaiot.portal.ui.web.filter.CachingHttpHeadersFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.*;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */

