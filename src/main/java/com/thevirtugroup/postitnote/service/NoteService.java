package com.thevirtugroup.postitnote.service;

import com.thevirtugroup.postitnote.excpetion.NoteObjectMisMatchException;
import com.thevirtugroup.postitnote.excpetion.UserNotfoundException;
import com.thevirtugroup.postitnote.model.Note;
import com.thevirtugroup.postitnote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note addNote(Note note) throws NoteObjectMisMatchException, UserNotfoundException {
        validateUserID(note.getUserId());
        validateNoteHeader(note.getUserId(), note.getNoteHeader());
        note.setNoteId(UUID.randomUUID().toString());
        note.setCreatedDate(new Date());
        note.setUpdatedDate(new Date());
        return noteRepository.addNote(note);
    }

    public Note updateNote(Note note) throws NoteObjectMisMatchException, UserNotfoundException {
        validateUserID(note.getUserId());
        note.setUpdatedDate(new Date());
        return noteRepository.updateNote(note);
    }

    public boolean deleteNote(String noteId) {
        return noteRepository.deleteNote(noteId);
    }

    public List<Note> getAllNotesByUserId(Long userId) throws NoteObjectMisMatchException, UserNotfoundException {
        validateUserID(userId);
        return noteRepository.getAllNotesByUserId(userId);
    }

    private void  validateUserID(Long userId) throws NoteObjectMisMatchException, UserNotfoundException {
        if (userId == null) {
            throw new NoteObjectMisMatchException("The user id for the note is missing");
        }
    }

    private void validateNoteHeader(Long userId, String noteHeader) throws NoteObjectMisMatchException {
        if(noteRepository.ifPresentNoteHeader(userId, noteHeader)) {
            throw new NoteObjectMisMatchException("The Note header already exists for " + userId);
        }
    }
}
