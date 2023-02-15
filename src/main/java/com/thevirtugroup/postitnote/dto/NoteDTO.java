package com.thevirtugroup.postitnote.dto;

import java.util.Date;

public class NoteDTO {

    private String noteId;
    private Long userId;
    private Date createdDate;
    private Date updatedDate;
    private String noteHeader;
    private String content;

    public NoteDTO(String noteId, Long userId, Date createdDate, Date updatedDate, String noteHeader, String content) {
        this.noteId = noteId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.noteHeader = noteHeader;
        this.content = content;
    }

    public String getNoteId() {
        return noteId;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public String getNoteHeader() {
        return noteHeader;
    }

    public String getContent() {
        return content;
    }
}
