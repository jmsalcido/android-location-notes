package com.nearsoft.examenboom;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.NotesSQLite;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

import java.util.List;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NotesFragment extends Fragment {

    private NoteRepository mNotesRepository;

    private ListView mListView;
    private TextView mEmptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_layout, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view) {
        Context context = getActivity();
        mListView = (ListView) view.findViewById(R.id.notes_list_view);
        mEmptyText = (TextView) view.findViewById(R.id.empty_notes_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillListView();
    }

    private void fillListView() {
        Context context = getActivity();
        mNotesRepository = new NoteRepositoryImpl(context);
        final List<Note> notes = mNotesRepository.allNotes();
        NotesAdapter adapter = new NotesAdapter(context, notes);
        mListView.setAdapter(adapter);
        displayListViewOrEmptyText(notes.size());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
                intent.putExtra(NotesSQLite.COLUMN_NOTE_ID, notes.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void displayListViewOrEmptyText(int itemsCount) {
        if (itemsCount == 0) {
            mListView.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.notes_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_note_action) {
            Intent intent = new Intent(getActivity(), NewNoteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
