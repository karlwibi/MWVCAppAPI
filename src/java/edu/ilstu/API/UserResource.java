/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ilstu.API;

import edu.ilstu.model.StudentModel;
import edu.ilstu.model.TeacherModel;
import edu.ilstu.model.UserModel;
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
@Path("/user/{userid}")
public class UserResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    /**
     * Retrieves representation of an instance of edu.ilstu.API.UserResource
     *
     * @param userId
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJSON(@PathParam("userid") int userId) {

        String json = getUserJSON(userId);

        return json;
    }

    /**
     * PUT method for updating or creating an instance of UserResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    public String getUserJSON(int userID) {

        String JSON = null;

        TeacherModel teacherModel = new TeacherModel();
        StudentModel studentModel = new StudentModel();

        if (teacherModel.getTeacherById(userID) != null) {
            teacherModel = teacherModel.getTeacherById(userID);
            
            JSON = teacherModel.teacherToJSONString();

        } else {

            studentModel = studentModel.getStudentById(userID);
            
            
            JSON = studentModel.studentToJSONString();

        }

        return JSON;
    }

}
