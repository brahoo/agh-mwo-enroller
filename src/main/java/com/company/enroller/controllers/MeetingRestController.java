package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;



@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

//    @RequestMapping(value = "", method = RequestMethod.POST)
//    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
//        //Meeting foundMeeting = meetingService.findById(meeting.getId());
//        meetingService.add(meeting);
//        return new ResponseEntity("A meeting with title " + meeting.getTitle() + "has bee added.", HttpStatus.OK);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
//        if (meeting == null) {
//            return new ResponseEntity("Unable to delete. A meeting with id " + meeting.getId() + " doesn't exist.", HttpStatus.NOT_FOUND);
//        }
        meetingService.delete(meeting);
        return new ResponseEntity("A meeting with title " + meeting.getTitle() + " has been deleted.", HttpStatus.OK);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingService.findById(updatedMeeting.getId());
//        if (meeting == null) {
//            return new ResponseEntity("Unable to update. A meeting with id " + meeting.getId() + " doesn't exist.", HttpStatus.NOT_FOUND);
//        }
//        if (meeting.getId() != updatedMeeting.getId()) {
//            return new ResponseEntity("Unable to update. Id can't be changed.", HttpStatus.CONFLICT);
//        }
        meetingService.update(meeting, updatedMeeting);
        return new ResponseEntity("A meeting with title " + meeting.getTitle() + " has been updated.", HttpStatus.OK);
    }

}
