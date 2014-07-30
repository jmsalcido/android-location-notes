package com.nearsoft.examenboom;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

import java.util.List;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NotesMapFragment extends MapFragment {

    private NoteRepository mNotesRepository;
    private List<Note> mNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mNotesRepository = new NoteRepositoryImpl(getActivity());
        mNotes = mNotesRepository.allNotes();
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Note note : mNotes) {
            MarkerOptions options = new MarkerOptions();
            LatLng latLng = new LatLng(note.getLatitude(), note.getLongitude());
            options.position(latLng);
            options.title(note.getTitle());
            options.snippet(note.getText());
            Marker marker = getMap().addMarker(options);
        }
    }
}
