package com.dylanops.review.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.NoCache;

@Path("/api/users")
public class UserResource {

    @GET
    @Path("/me")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public User me() {
        return new User();
    }

    public static class User {

        private final String userName;

        User() {
            this.userName = "Dylan";
        }

        public String getUserName() {
            return userName;
        }
    }
}
