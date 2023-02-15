package com.thevirtugroup.postitnote.model;

import java.util.Date;

public class Note {

    private String noteId;
    private Long userId;
    private Date createdDate;
    private Date updatedDate;
    private String noteHeader;
    private String content;

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }



    public Note(Long userId, String noteHeader, String content) {
        this.userId = userId;
        this.noteHeader = noteHeader;
        this.content = content;
    }

    public Note() {
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
