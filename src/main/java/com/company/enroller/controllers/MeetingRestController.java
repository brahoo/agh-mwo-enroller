package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;



@RestController
@RequestMapping("/meetings")
public class MeetingRestController {


    private MeetingService meetingService;
    private ParticipantService participantService;

    @Autowired
    public MeetingRestController (MeetingService meetingService, ParticipantService participantService) {
        this.meetingService = meetingService;
        this.participantService = participantService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
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

    @RequestMapping(value ="/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @RequestBody Participant participant) {
        Meeting meeting = meetingService.findById(id);
        Participant foundedParticipant = participantService.findByLogin(participant.getLogin());
//        if (meeting == null) {
//            return new ResponseEntity("Unable to update. A meeting with id " + meeting.getId() + " doesn't exist.", HttpStatus.NOT_FOUND);
//        }
        if (foundedParticipant == null) {
            return new ResponseEntity("Unable to add participant to meeting. Participant doesn't exist.", HttpStatus.NOT_FOUND);
        }
        if (meeting.getParticipants().contains(foundedParticipant)) {
            return new ResponseEntity("Unable to add participant to meeting. Participant is already added to this meeting.", HttpStatus.CONFLICT);
        }
        meetingService.addParticipant(meeting, foundedParticipant);
        return new ResponseEntity("A participant with login " + foundedParticipant.getLogin() + " has been added to meeting with title " + meeting.getTitle() + ".", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        Collection<Participant> participants = meeting.getParticipants();
//        if (meeting == null) {
//            return new ResponseEntity("Meeting not found.", HttpStatus.NOT_FOUND);
//        }
        if (participants.size() < 1) {
            return new ResponseEntity<>("Meeting has no participant.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findById(id);
        Participant participant = participantService.findByLogin(login);
        //        if (meeting == null) {
//            return new ResponseEntity("Meeting not found.", HttpStatus.NOT_FOUND);
//        }
        if (!meeting.getParticipants().contains(participant)) {
            return new ResponseEntity("Unable to remove participant from this meeting. Participant isn't already added to this meeting.", HttpStatus.NOT_FOUND);
        }
        meetingService.removeParticipant(meeting, participant);
        return new ResponseEntity("A participant with login " + participant.getLogin() + " has been removed from meeting with title " + meeting.getTitle() + ".", HttpStatus.OK);
    }

}
