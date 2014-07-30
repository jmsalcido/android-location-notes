package com.nearsoft.examenboom.database.repository;

import com.nearsoft.examenboom.common.Note;

import java.util.List;

/**
 * Created by jsalcido on 7/27/14.
 */
public interface NoteRepository {

    public void saveNote(Note note);
    public void deleteNote(Note note);
    public void updateNote(Note note);
    public List<Note> allNotes();
    public Note getNoteById(int id);

}
