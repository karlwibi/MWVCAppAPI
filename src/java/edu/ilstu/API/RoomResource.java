/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ilstu.API;

import edu.ilstu.model.OnlineClassModel;
import edu.ilstu.model.RoomParticipantModel;
import edu.ilstu.model.ScheduleClassModel;
import edu.ilstu.model.StudentModel;
import edu.ilstu.model.TeacherModel;
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
@Path("/room/{roomId}")
public class RoomResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RoomResource
     */
    public RoomResource() {
    }

    /**
     * Retrieves representation of an instance of edu.ilstu.API.RoomResource
     *
     * @return an instance of java.lang.String
     */
    
    /**
     * Retrieves representation of an instance of edu.ilstu.API.RoomResource
     * @param roomId
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getRoomInfo(@PathParam("roomId") int roomId) {
        
        return buildInfo(roomId);
        
    }

    /**
     * PUT method for updating or creating an instance of RoomResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    private String buildInfo(int roomid) {

        OnlineClassModel ocm = new OnlineClassModel();
        //ScheduleClassModel scm=new ScheduleClassModel();
        RoomParticipantModel rpm = new RoomParticipantModel();
        JSONObject obj = new JSONObject();
        JSONArray jlist = new JSONArray();
        TeacherModel teacherModel = new TeacherModel();
        StudentModel studentModel = new StudentModel();
        ScheduleClassModel scheduleClassModel = new ScheduleClassModel();

        ArrayList<RoomParticipantModel> listOfParticipant = null;

        ocm.setRoomid(roomid);
        ocm = ocm.getClassByRoomId();
        scheduleClassModel.setOnlineClassId(ocm.getOnlineClassId());
        scheduleClassModel=scheduleClassModel.getScheduleByOnlineClassID();
        System.out.println(scheduleClassModel.ToJSONString());
        teacherModel=teacherModel.getTeacherById(ocm.getTeacherId());
        //scm.setOnlineClassId(ocm.getOnlineClassId());
        rpm.setOnlineClassId(ocm.getOnlineClassId());

        listOfParticipant = rpm.getAllParticipantForRoom();

        for (RoomParticipantModel Participant : listOfParticipant) {
            studentModel = studentModel.getStudentById(Participant.getStudentId());
            jlist.add(studentModel.studentToJSONString());
        }
        
        obj.put("onlineClassid",ocm.getOnlineClassId() );
        obj.put("roomParticipants", jlist);
        obj.put("teacherInfo",teacherModel.teacherToJSONString());
        //obj.put(, obj)
        
        
        return obj.toJSONString();
    }
}
