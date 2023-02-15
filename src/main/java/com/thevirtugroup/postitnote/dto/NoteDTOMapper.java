package com.thevirtugroup.postitnote.dto;

import com.thevirtugroup.postitnote.model.Note;

import java.util.function.Function;

public class NoteDTOMapper implements Function<Note, NoteDTO> {

    @Override
    public NoteDTO apply(Note note) {
        return new NoteDTO(note.getNoteId(), note.getUserId(), note.getCreatedDate(), note.getUpdatedDate(),
                note.getNoteHeader(), note.getContent());
    }
}
