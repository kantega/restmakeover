
package org.kantega.restmakeover.dao;

import org.kantega.restmakeover.api.dao.BlogDao;
import org.kantega.restmakeover.api.dao.BlogPostCommentDao;
import org.kantega.restmakeover.api.dao.BlogPostDao;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;

import javax.sql.DataSource;

@Plugin
public class DaoPlugin {

    @Export
    private final BlogDao blogDao;

    @Export
    private final BlogPostDao blogPostDao;

    @Export
    private final BlogPostCommentDao blogPostCommentDao;

    public DaoPlugin(DataSource dataSource) {
        blogDao = new JdbcBlogDao(dataSource);
        blogPostDao = new JdbcBlogPostDao(dataSource);
        blogPostCommentDao = new JdbcBlogPostCommentDao(dataSource);
    }

}
