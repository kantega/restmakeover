
package org.kantega.restmakeover.template;

import org.kantega.restmakeover.api.templates.TemplateRenderer;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;

@Plugin
public class TemplatesPlugin {


    @Export
    private final TemplateRenderer templateRenderer;

    public TemplatesPlugin() {
        templateRenderer = new VelicityTemplateRenderer();
    }

}
