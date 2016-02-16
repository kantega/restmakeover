package org.kantega.restmakeover.rest.users;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 */
@Path("r/user")
public class UserInfoResource {


    // We should repond to GET requests on path {username} and return application/json
    @GET
    @Path("{username}")
    @Produces("application/json")
    public User getUserByUsername(@PathParam("username") String username) {
        return new User(username, " John Doe");
    }
}
