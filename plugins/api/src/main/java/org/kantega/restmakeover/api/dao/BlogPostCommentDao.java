package org.kantega.restmakeover.api.dao;


import org.kantega.restmakeover.api.BlogPost;
import org.kantega.restmakeover.api.BlogPostComment;

import java.util.List;

/**
 * Data Access Object for Blog Post Comments.
 */
public interface BlogPostCommentDao {

    /**
     * Saves a blog comment.
     * 
     * @param comment blog comment to save to the database
     */
    void saveOrUpdate(BlogPostComment comment);

    /**
     * Return a list of comments for a given blog post.
     * 
     * @param post The blog post to read comments from
     * @return List of comments for this blog post.
     */
    List<BlogPostComment> getComments(final BlogPost post);

}