package org.kantega.restmakeover.api.dao;



import org.kantega.restmakeover.api.Blog;
import org.kantega.restmakeover.api.BlogPost;

import java.util.List;

/**
 * Data Access Object for blog posts.
 */
public interface BlogPostDao {

    /**
     * Saves a blog post.
     * 
     * @param post the blog post to save to the database 
     */
    void saveOrUpdate(BlogPost post);

    /**
     * Return all blog posts for a given blog.
     * 
     * @param blog The blog to read all blog post for
     * @return List of blog posts in this blog
     */
    List<BlogPost> getBlogPosts(Blog blog);

    /**
     * Return a blog post given an id of the blog post.
     * 
     * @param blogPostId The id of the blog post
     * @return The blog post with this ID
     */
    BlogPost getBlogPost(long blogPostId);

    /**
     * Return a blog post given the blog and the name of the post.
     * 
     * @param blog The blog this post belongs to
     * @param postTitle The name of the blog post
     * @return The blog post if found
     */
    BlogPost getBlogPost(Blog blog, String postTitle);

    /**
     * Deletes the blog post
     *
     * @param blogPost The blog post to delete
     */
    void delete(BlogPost blogPost);
}
