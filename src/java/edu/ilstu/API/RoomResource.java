/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ilstu.API;

import edu.ilstu.dao.RessourceDAOImpl;
import edu.ilstu.helper.AlphanumComparator;
import edu.ilstu.model.ClassSessionModel;
import edu.ilstu.model.ContentModel;
import edu.ilstu.model.OnlineClassModel;
import edu.ilstu.model.PreziContentModel;
import edu.ilstu.model.RevealContentModel;
import edu.ilstu.model.RoomParticipantModel;
import edu.ilstu.model.ScheduleClassModel;
import edu.ilstu.model.SessionResourceModel;
import edu.ilstu.model.StudentModel;
import edu.ilstu.model.StudyToolModel;
import edu.ilstu.model.TeacherModel;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
     *
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
        String presentationType = null;
        //ScheduleClassModel scm=new ScheduleClassModel();
        RoomParticipantModel rpm = new RoomParticipantModel();
        ClassSessionModel sessionModel = new ClassSessionModel();
        PreziContentModel preziResource = null;
        RevealContentModel revealResource = null;
        ContentModel slideContent = null;
        JSONObject obj = new JSONObject();
        JSONArray jlist = new JSONArray();
        JSONArray jSlideList = new JSONArray();
        JSONArray jStudyList = new JSONArray();
        TeacherModel teacherModel = new TeacherModel();
        StudentModel studentModel = new StudentModel();
        ScheduleClassModel scheduleClassModel = new ScheduleClassModel();

        List<SessionResourceModel> listSessionRessource = null;
        List<ContentModel> listContent = null;
        List<RoomParticipantModel> listOfParticipant = null;
        List<ClassSessionModel> listSessionModel = null;
        List<String> slideList = new ArrayList();

        ocm.setRoomid(roomid);
        ocm = ocm.getClassByRoomId();
        //getting the schedule for the class
        scheduleClassModel.setOnlineClassId(ocm.getOnlineClassId());
        scheduleClassModel = scheduleClassModel.getScheduleByOnlineClassID();
        //getting the session

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        listSessionModel = sessionModel.findClassSessionByDate(dateHelper(date));

        //find the session for the schedule
        if (!listSessionModel.isEmpty()) {

            for (ClassSessionModel aSession : listSessionModel) {

                if (aSession.getScheduleClassId() == scheduleClassModel.getScheduleClassId()) {
                    sessionModel = aSession;
                }
            }

            //checking the presentation Id
            if (RessourceDAOImpl.checkResourceType(sessionModel.getPresentationId()).equals("Prezi")) {

                presentationType = "prezi";
                preziResource = new PreziContentModel(sessionModel.getPresentationId());
                preziResource = preziResource.findPreziRessourceById();

            } else {

                presentationType = "slide";
                revealResource = new RevealContentModel(sessionModel.getPresentationId());
                revealResource = revealResource.findRevealRessourceById();

                slideContent = new ContentModel();
                slideContent.setRevealId(revealResource.getRevealId());
                listContent = slideContent.findContentByRevealID();

            }

            //getting the study resource 
            SessionResourceModel srm = new SessionResourceModel();
            srm.setSessionId(sessionModel.getSessionId());
            listSessionRessource = srm.getSessionRessoureForASession();

        }

        if (revealResource != null && listContent != null) {

            for (ContentModel slide : listContent) {

                String path = slide.getContentText();
                String fileName = getSlideName(path);
                System.out.println("Slide Filename: " + fileName);
                slideList.add(fileName);
            }
            Collections.sort(slideList, new AlphanumComparator());
            //creating the sorted list slide 
            for (String slide : slideList) {
                jSlideList.add(slide);
            }
        }

        System.out.println(scheduleClassModel.ToJSONString());

        teacherModel = teacherModel.getTeacherById(ocm.getTeacherId());
        //scm.setOnlineClassId(ocm.getOnlineClassId());
        rpm.setOnlineClassId(ocm.getOnlineClassId());

        listOfParticipant = rpm.getAllParticipantForRoom();

        //getting the roomparticipant and create the jason array
        if (listOfParticipant != null) {
            for (RoomParticipantModel Participant : listOfParticipant) {
                studentModel = studentModel.getStudentById(Participant.getStudentId());
                jlist.add(studentModel.studentToJSONString());
            }
        }

        if (listSessionRessource != null) {
            for (SessionResourceModel srm : listSessionRessource) {
                StudyToolModel stm = new StudyToolModel(srm.getResourceId());
                stm = stm.findStudyToolById();
                jStudyList.add(stm.getArticleLink());
            }
        }

        obj.put("onlineClassid", ocm.getOnlineClassId());
        obj.put("sessionId", sessionModel.getSessionId());
        obj.put("roomParticipants", jlist);
        obj.put("teacherInfo", teacherModel.teacherToJSONString());
        obj.put("presentationType", presentationType);

        if (preziResource != null) {
            obj.put("preziResource", preziResource.PreziToJSONString());
        } else {
            obj.put("preziResource", null);
        }

        if (revealResource != null) {
            obj.put("revealSlide", jSlideList);
        } else {
            obj.put("revealSlide", null);
        }

        if (listSessionRessource != null) {
            obj.put("studyTool", null);
        } else {
            obj.put("studyTool", jStudyList);
        }
        //obj.put(, obj)

        return obj.toJSONString();
    }

    private java.sql.Date dateHelper(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;

    }

    private String getSlideName(String path) {

        File file = new File(path);
        String slideName = file.getName();

        return slideName;

    }
}
