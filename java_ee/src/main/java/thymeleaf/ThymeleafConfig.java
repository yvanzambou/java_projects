package thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafConfig implements ServletContextListener {

    private static TemplateEngine engine;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        engine = templateEngine(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}

    public static TemplateEngine getTemplateEngine() {
        return engine;
    }

    private TemplateEngine templateEngine(ServletContext servletContext) {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver(servletContext));
        return engine;
    }

    private ITemplateResolver templateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}
