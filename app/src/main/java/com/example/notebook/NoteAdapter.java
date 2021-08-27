package com.example.notebook;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> listNote;
    private OnItemClicked onItemClicked;

    public NoteAdapter(NoteAdapter.OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setData(List<Note> listNote) {
        this.listNote = listNote;
        notifyDataSetChanged();
    }

    public interface OnItemClicked {
        void deleteNote(Note note);
        void updateNote(Note note);
    }

    //store item view
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final Button deleteButton;
        private final Button updateButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            updateButton = itemView.findViewById(R.id.btn_update);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = listNote.get(position);
        if(note == null)
            return;
        holder.title.setText(note.getTitle());

        holder.deleteButton.setOnClickListener(v -> onItemClicked.deleteNote(note));
        holder.updateButton.setOnClickListener(v -> onItemClicked.updateNote(note));
    }

    @Override
    public int getItemCount() {
        return listNote == null? 0:listNote.size();
    }

}
