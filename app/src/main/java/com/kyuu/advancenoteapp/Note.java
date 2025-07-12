package com.kyuu.advancenoteapp;

public class Note {
    private String noteText;
    private String imageUri;

    public Note() {
        // default constructor
    }

    public Note(String noteText, String imageUri) {
        this.noteText = noteText;
        this.imageUri = imageUri;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
