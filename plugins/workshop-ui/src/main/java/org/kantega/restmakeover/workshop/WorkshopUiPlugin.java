
package org.kantega.restmakeover.workshop;

import org.kantega.restmakeover.api.templates.TemplateRenderer;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.FilterPhase;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.api.ServletBuilder;
import org.kantega.reststop.webjars.WebjarsVersions;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Plugin
public class WorkshopUiPlugin {

    @Export
    private final Collection<Filter> servlets = new ArrayList<>();

    private ServletBuilder servletBuilder;
    private WebjarsVersions webjarsVersions;
    private TemplateRenderer templateRenderer;

    public WorkshopUiPlugin(final ServletBuilder servletBuilder, WebjarsVersions webjarsVersions, TemplateRenderer templateRenderer) {
        this.servletBuilder = servletBuilder;
        this.webjarsVersions = webjarsVersions;
        this.templateRenderer = templateRenderer;

        templateServlet("index.html", "/workshop-ui/");
        templateServlet("gettingstarted.html", "/workshop-ui/gettingstarted");
        templateServlet("warmup.html", "/workshop-ui/warmup");
        templateServlet("notfound.html", "/workshop-ui/notfound");
        templateServlet("caching.html", "/workshop-ui/caching");
        templateServlet("etags.html", "/workshop-ui/etags");
        templateServlet("enemy-of-the-state.html", "/workshop-ui/enemy-of-the-state");
        templateServlet("pagination.html", "/workshop-ui/pagination");

        servlets.add(servletBuilder.filter(new SessionAttributeTrackingFilter(), "/*", FilterPhase.PRE_USER));
    }

    private void templateServlet(String file, String path) {
        TemplateServlet templateServlet = new TemplateServlet(file);
        servlets.add(servletBuilder.servlet(templateServlet, path));
    }

    class TemplateServlet extends HttpServlet {

        private final String file;

        TemplateServlet(String file) {
            this.file = file;
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            final Map<String, String> versions = webjarsVersions.getVersions();
            resp.setContentType("text/html");
            TemplateRenderer.Renderer renderer = templateRenderer
                    .template("org/kantega/restmakeover/workshop/" + file, Charset.forName("utf-8"));
            versions.entrySet().forEach((e) -> renderer.addAttribute(e.getKey().replace('.', '_'), e.getValue()));
            renderer.render(resp.getWriter());

        }
    }
}
