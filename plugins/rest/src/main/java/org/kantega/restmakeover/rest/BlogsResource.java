package org.kantega.restmakeover.rest;

import org.kantega.restmakeover.api.Blog;
import org.kantega.restmakeover.api.BlogPost;
import org.kantega.restmakeover.api.BlogPostComment;
import org.kantega.restmakeover.api.dao.BlogDao;
import org.kantega.restmakeover.api.dao.BlogPostCommentDao;
import org.kantega.restmakeover.api.dao.BlogPostDao;
import org.kantega.restmakeover.rest.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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


        List<Blog> allBlogs = blogDao.getAllBlogs();

        return Response.ok(allBlogs)
                .build();
    }


    /**
     * Returns a String representation of the hashcode of blog list
     */
    private String etag(List<Blog> blogs) {
        StringBuilder content = new StringBuilder();
        for (Blog blog : blogs) {
            content.append(etag(blog));
        }
        return Integer.toString(content.toString().hashCode());
    }

    /**
     * Returns a String representation of the hashcode of blog content
     */
    private String etag(Blog blog) {
        String content = blog.getColor() +
                blog.getName() +
                blog.getId();
        return Integer.toString(content.hashCode());
    }

    /**
     * Returns a String representation of the hashcode of blog post content
     */
    private String etag(BlogPost blog) {
        String content = blog.getTitle() + blog.getContent();
        return Integer.toString(content.hashCode());
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


        Blog blogByName = blogDao.getBlogByName(blogName);

        return Response.ok(blogByName)
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
    public Response getBlogPost(@PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle, @Context HttpServletRequest request, @QueryParam("delete") boolean delete) {
        Blog blog = blogDao.getBlogByName(blogName);
        BlogPost blogPost = blogPostDao.getBlogPost(blog, postTitle);

        request.getSession().setAttribute("lastViewedBlogPost", blogPost);

        if(delete) {
            blogPostDao.delete(blogPost);
            return Response.ok().entity("Deleted").build();
        }

        return Response.ok(new Post(blogPost))
                .cacheControl(new CacheControl())
                .build();
    }



    @PUT
    @Path("{blogName}/{postTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBlogPost(@Context Request request, @PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle, NewPost newPostData) {
        Blog blog = blogDao.getBlogByName(blogName);
        BlogPost post = blogPostDao.getBlogPost(blog, postTitle);

        post.setContent(newPostData.getContent());
        post.setPublishDate(new Timestamp(System.currentTimeMillis()));
        blogPostDao.saveOrUpdate(post);
        return Response.ok().build();
    }

    @GET
    @Path("{blogName}/{postTitle}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommments(@PathParam("blogName") String blogName,
                                 @PathParam("postTitle") String postTitle,
                                 @Context UriInfo uriInfo,
                                 @QueryParam("skip") int skip,
                                 @QueryParam("limit") int limit) {



        List<Comment> comments = getComments(blogName, postTitle, 0, 999);


        return Response.ok(comments).build();
    }

    private List<Comment> getComments(String blogName, String postTitle, int skip, int limit) {
        return blogPostCommentDao.getComments(blogPostDao.getBlogPost(blogDao.getBlogByName(blogName), postTitle), skip, limit)
                .stream().map(Comment::new).collect(Collectors.toList());
    }

    @POST
    @Path("{blogName}/{postTitle}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("blogName") String blogName, @PathParam("postTitle") String postTitle, NewComment comment, @Context HttpServletRequest req) {

        Blog blog = blogDao.getBlogByName(blogName);

        BlogPost blogPost = (BlogPost) req.getSession().getAttribute("lastViewedBlogPost");



        BlogPostComment c = new BlogPostComment(blogPost);
        c.setAuthor(comment.getAuthor());
        c.setContent(comment.getContent());
        c.setPublishDate(new Timestamp(System.currentTimeMillis()));
        blogPostCommentDao.saveOrUpdate(c);
        return Response.ok().build();
    }
}
