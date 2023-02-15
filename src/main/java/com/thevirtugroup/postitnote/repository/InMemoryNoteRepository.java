package com.thevirtugroup.postitnote.repository;

import com.thevirtugroup.postitnote.model.Note;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryNoteRepository implements NoteRepository {

    Map<Long, List<Note>> inMemoryDB;

    public InMemoryNoteRepository() {
        inMemoryDB = new HashMap<>();
    }

    @Override
    public Note addNote(Note note) {
        Long userId = note.getUserId();
        if (!inMemoryDB.containsKey(userId)) {
            inMemoryDB.put(userId, new ArrayList<>());
        }
        inMemoryDB.get(userId).add(note);
        return note;
    }

    @Override
    public Note updateNote(Note note) {
        Long userId = note.getUserId();
        List<Note> noteList = inMemoryDB.get(userId);
        for (int i = 0; i < noteList.size(); i++) {
            if (noteList.get(i).getNoteId().equals(note.getNoteId())) {
                noteList.set(i, note);
                return note;
            }
        }
        return null;
    }

    @Override
    public boolean deleteNote(String noteId) {
        for (Map.Entry<Long, List<Note>> entry : inMemoryDB.entrySet()) {
            List<Note> noteList = entry.getValue();
            for (int i = 0; i < noteList.size(); i++) {
                if (noteList.get(i).getNoteId().equals(noteId)) {
                    noteList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Note> getAllNotesByUserId(Long userId) {
        return inMemoryDB.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public boolean ifPresentNoteHeader(Long userId, String noteHeader) {
        List<Note> noteList = inMemoryDB.get(userId);
        if (noteList != null) {
            for (int i = 0; i < noteList.size(); i++) {
                if (noteList.get(i).getNoteHeader().equals(noteHeader)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean ifPresentUserId(Long userId) {
        if (inMemoryDB.containsKey(userId)) {
            return true;
        }
        return false;
    }
}
