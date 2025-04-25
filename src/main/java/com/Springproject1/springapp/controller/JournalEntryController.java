package com.Springproject1.springapp.controller;

import com.Springproject1.springapp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    private Map<ObjectId, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping//("/getJournal")
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping//("/getJournal")
    public boolean createEntry(@RequestBody JournalEntry entry){
        journalEntries.put(entry.getId(), entry);
        return true;
    }

    @GetMapping("id/{myId}")
    public JournalEntry getById(@PathVariable Long myId){
        return journalEntries.get(myId);
    }

    @DeleteMapping("id/{myId}")
    public JournalEntry deleteById(@PathVariable String myId){
        return journalEntries.remove(myId);
    }


    @PutMapping("id/{myId}")
    public JournalEntry updateById(@PathVariable ObjectId myId , @RequestBody JournalEntry entry){
        return journalEntries.put(myId,entry);
    }
}
