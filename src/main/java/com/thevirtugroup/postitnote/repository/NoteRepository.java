package com.thevirtugroup.postitnote.repository;

import com.thevirtugroup.postitnote.model.Note;

import java.util.List;


public interface NoteRepository {

    Note addNote(Note note);

    Note updateNote(Note note);

    boolean deleteNote(String noteId);

    List<Note> getAllNotesByUserId(Long userId);

    boolean ifPresentNoteHeader(Long userId, String noteHeader);

    boolean ifPresentUserId(Long userId);
}
