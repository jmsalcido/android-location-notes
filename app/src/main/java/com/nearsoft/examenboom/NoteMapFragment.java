package com.nearsoft.examenboom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.NotesSQLite;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NoteMapFragment extends MapFragment {

    private NoteRepository mNotesRepository;
    private Note mNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotesRepository = new NoteRepositoryImpl(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int noteId = getArguments().getInt(NotesSQLite.COLUMN_NOTE_ID);
        mNote = mNotesRepository.getNoteById(noteId);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MarkerOptions options = new MarkerOptions();
        options.title(mNote.getTitle());
        options.snippet(mNote.getText());
        LatLng latLn = new LatLng(mNote.getLatitude(), mNote.getLongitude());
        options.position(latLn);
        Marker marker = getMap().addMarker(options);
        marker.showInfoWindow();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLn,14);
        getMap().animateCamera(update);
    }
}
