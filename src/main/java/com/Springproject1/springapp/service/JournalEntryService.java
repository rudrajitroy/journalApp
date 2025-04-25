package com.Springproject1.springapp.service;

import com.Springproject1.springapp.entity.JournalEntry;
import com.Springproject1.springapp.entity.User;
import com.Springproject1.springapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry entry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            JournalEntry saved = journalEntryRepository.save(entry);
            user.getEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            logger.error("Error while saving journal entry"+e,e);
            throw new RuntimeException(e);
        }
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        try {
            User user = userService.findByUserName(userName);
            boolean remove = user.getEntries().removeIf(x -> x.getId().equals(id));
            if (remove) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the entries",e);
        }
    }
}
