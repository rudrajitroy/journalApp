package com.Springproject1.springapp.controller;

import com.Springproject1.springapp.entity.JournalEntry;
import com.Springproject1.springapp.entity.User;
import com.Springproject1.springapp.service.JournalEntryService;
import com.Springproject1.springapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        List<?> getJournals = user.getEntries();
        if (getJournals != null && !getJournals.isEmpty()) {
            return new ResponseEntity<>(getJournals, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry, auth.getName());
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        List<JournalEntry> entry = user.getEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toUnmodifiableList());
        if (!entry.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isDeleted = journalEntryService.deleteById(myId, auth.getName());
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId myId, @RequestBody JournalEntry entry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        List<JournalEntry> journalEntryList = user.getEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toUnmodifiableList());
        if (!journalEntryList.isEmpty()) {
            JournalEntry tempEntry = journalEntryService.findById(myId).orElse(null);
            tempEntry.setTitle(entry.getTitle() == null || (entry.getTitle().isEmpty() && entry.getTitle().isBlank()) ? tempEntry.getTitle() : entry.getTitle());
            tempEntry.setContent(entry.getContent() == null || (entry.getContent().isEmpty() && entry.getContent().isBlank()) ? tempEntry.getContent() : entry.getContent());
            tempEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(tempEntry);
            return new ResponseEntity<>(tempEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
