package org.kantega.restmakeover.rest;

import org.kantega.restmakeover.api.Blog;
import org.kantega.restmakeover.api.BlogPost;
import org.kantega.restmakeover.api.BlogPostComment;
import org.kantega.restmakeover.api.dao.BlogDao;
import org.kantega.restmakeover.api.dao.BlogPostCommentDao;
import org.kantega.restmakeover.api.dao.BlogPostDao;
import org.kantega.restmakeover.rest.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Path("r/blogs")
public class BlogsResource {

    private final BlogDao blogDao;
    private final BlogPostDao blogPostDao;
    private final BlogPostCommentDao blogPostCommentDao;

    public BlogsResource(BlogDao blogDao, BlogPostDao blogPostDao, BlogPostCommentDao blogPostCommentDao) {
        this.blogDao = blogDao;
        this.blogPostDao = blogPostDao;
        this.blogPostCommentDao = blogPostCommentDao;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listBlogs(@Context Request request) {


        return Response.ok(blogDao.getAllBlogs())
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBlog(Blog input) {
        Blog blog = new Blog();
        blog.setName(input.getName());
        blog.setColor(input.getColor());
        blogDao.saveOrUpdate(blog);

        return Response.ok().build();
    }

    @GET
    @Path("{blogName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlog(@PathParam("blogName")String blogName) {

        return Response.ok(blogDao.getBlogByName(blogName))
                .build();
    }

    @GET
    @Path("{blogName}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostSummary> getBlogPosts(@PathParam("blogName")String blogName) {

        return blogPostDao.getBlogPosts(blogDao.getBlogByName(blogName)).stream()
                .map(p -> new PostSummary(p.getTitle(), p.getContent(), new Date(p.getPublishDate().toEpochMilli()), p.getCommentCount()))
                .collect(Collectors.toList());
    }

    @POST
    @Path("{blogName}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBlogPost(@PathParam("blogName") String blogName, NewPost newPost) {
        Blog blog = blogDao.getBlogByName(blogName);
        BlogPost post = new BlogPost(blog);
        post.setTitle(newPost.getTitle());
        post.setContent(newPost.getContent());
        post.setPublishDate(new Timestamp(System.currentTimeMillis()));
        blogPostDao.saveOrUpdate(post);
        return Response.ok().build();
    }

    @GET
    @Path("{blogName}/{postTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlogPost(@PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle) {
        Blog blog = blogDao.getBlogByName(blogName);
        BlogPost blogPost = blogPostDao.getBlogPost(blog, postTitle);

        return Response.ok(new Post(blogPost))
                .cacheControl(new CacheControl())
                .build();
    }

    @GET
    @Path("{blogName}/{postTitle}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getCommments(@PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle) {
        List<BlogPostComment> comments = blogPostCommentDao.getComments(blogPostDao.getBlogPost(blogDao.getBlogByName(blogName), postTitle));
        return comments.stream()
                .map(c -> new Comment(c)).collect(Collectors.toList());
    }

    @POST
    @Path("{blogName}/{postTitle}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommments(@PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle, NewComment comment) {
        BlogPost blogPost = blogPostDao.getBlogPost(blogDao.getBlogByName(blogName), postTitle);

        BlogPostComment c = new BlogPostComment(blogPost);
        c.setAuthor(comment.getAuthor());
        c.setContent(comment.getContent());
        c.setPublishDate(new Timestamp(System.currentTimeMillis()));
        blogPostCommentDao.saveOrUpdate(c);
        return Response.ok().build();
    }
}
