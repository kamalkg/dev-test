package com.thevirtugroup.postitnote.rest;

import com.thevirtugroup.postitnote.dto.NoteDTO;
import com.thevirtugroup.postitnote.dto.NoteDTOMapper;
import com.thevirtugroup.postitnote.excpetion.NoteNotFoundException;
import com.thevirtugroup.postitnote.excpetion.NoteObjectMisMatchException;
import com.thevirtugroup.postitnote.excpetion.UserNotfoundException;
import com.thevirtugroup.postitnote.model.Note;
import com.thevirtugroup.postitnote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 */
@RestController
@RequestMapping(path = "api/v1/note")
public class NoteController {

    private final NoteService noteService;
    private final NoteDTOMapper noteDTOMapper;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
        noteDTOMapper = new NoteDTOMapper();
    }

    @GetMapping(path = "user/{userId}")
    public ResponseEntity<List<NoteDTO>> getNoteById(@PathVariable("userId") Long userId) throws
            NoteNotFoundException, NoteObjectMisMatchException, UserNotfoundException {
        List<Note> noteList = noteService.getAllNotesByUserId(userId);
        List<NoteDTO> noteDTOList = noteList.stream().map(noteDTOMapper::apply).collect(Collectors.toList());
        return ResponseEntity.ok(noteDTOList);
    }

    @PostMapping(path = "user/{userId}")
    public ResponseEntity<NoteDTO> createNote(@RequestBody Note note) {
        try {
            Note savedNote = noteService.addNote(note);
            NoteDTO noteDTO = noteDTOMapper.apply(savedNote);
            return ResponseEntity.ok(noteDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating the note");
        }
    }


    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@RequestBody Note note) throws
            NoteNotFoundException, NoteObjectMisMatchException, UserNotfoundException {
        Note updatedNote = noteService.updateNote(note);
        if (updatedNote == null) {
            throw new NoteNotFoundException("Note not found with ID: " + note.getNoteId());
        }
        NoteDTO noteDTO = noteDTOMapper.apply(updatedNote);
        return ResponseEntity.ok(noteDTO);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Boolean> deleteNote(@PathVariable("noteId") String noteId) throws NoteNotFoundException {
        boolean isDeleted = noteService.deleteNote(noteId);
        if (!isDeleted) {
            throw new NoteNotFoundException("Note not found with ID: " + noteId);
        }
        return ResponseEntity.ok(true);
    }
}