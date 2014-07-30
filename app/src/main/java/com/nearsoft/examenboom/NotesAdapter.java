package com.nearsoft.examenboom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

import java.util.List;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NotesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Note> mNotes;
    private Context mContext;

    public NotesAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Note)getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_adapter, null);
            holder = new NoteViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.notes_list_title_text);
            holder.text = (TextView) convertView.findViewById(R.id.notes_list_note_text);
            convertView.setTag(holder);
        }else{
            holder = (NoteViewHolder) convertView.getTag();
        }
        holder.title.setText(mNotes.get(position).getTitle());
        holder.text.setText(mNotes.get(position).getText());
        return convertView;
    }

    private class NoteViewHolder {
        TextView title;
        TextView text;
    }
}
