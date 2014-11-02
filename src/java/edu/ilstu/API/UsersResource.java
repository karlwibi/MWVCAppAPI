/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ilstu.API;

import edu.ilstu.dao.UserDAO;
import edu.ilstu.dao.UserDAOImpl;
import edu.ilstu.model.StudentModel;
import edu.ilstu.model.TeacherModel;
import edu.ilstu.model.UserModel;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author kawibi
 */
@Path("/users")
public class UsersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    /**
     * Retrieves representation of an instance of edu.ilstu.API.UsersResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        return getAllUsersJSON();
    }

    /**
     * PUT method for updating or creating an instance of UsersResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
     public String getAllUsersJSON() {

        String JSON = null;
        ArrayList<UserModel> list=null;
        JSONObject obj = new JSONObject();
        JSONArray jlist = new JSONArray();
        
        UserDAO dAO=new UserDAOImpl();
        
        list=dAO.getAllUsers();
        
        for(Iterator<UserModel> i=list.iterator();i.hasNext();){
            
            jlist.add(i.next().userToJSONString());
            
        }
        
        obj.put("users", jlist);

        JSON=obj.toJSONString();
       

        return JSON;
    }
}
