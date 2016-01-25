
package org.kantega.restmakeover.rest;

import org.kantega.restmakeover.api.dao.BlogDao;
import org.kantega.restmakeover.api.dao.BlogPostCommentDao;
import org.kantega.restmakeover.api.dao.BlogPostDao;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.jaxrsapi.ApplicationBuilder;

import javax.ws.rs.core.Application;

@Plugin
public class RestPlugin  {

    @Export
    private final Application metricsApp;

    public RestPlugin(BlogDao blogDao, BlogPostDao blogPostDao, BlogPostCommentDao blogPostCommentDao, ApplicationBuilder applicationBuilder) {

        metricsApp = applicationBuilder.application()
                .singleton(new BlogsResource(blogDao, blogPostDao, blogPostCommentDao))
                .build();
    }

}
