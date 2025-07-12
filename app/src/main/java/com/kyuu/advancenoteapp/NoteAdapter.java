package com.kyuu.advancenoteapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> noteList;
    private final OnNoteClickListener onNoteClickListener;

    // tambahkan konstruktor yang menerima listener
    public NoteAdapter(List<Note> noteList, OnNoteClickListener onNoteClickListener) {
        this.noteList = noteList;
        this.onNoteClickListener = onNoteClickListener;
    }

    // tambahkan interface untuk listener
    public interface OnNoteClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view, onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textViewNote.setText(note.getNoteText());

        if (note.getImageUri() != null && !note.getImageUri().isEmpty()) {
            holder.imageViewNote.setImageURI(Uri.parse(note.getImageUri()));
            holder.imageViewNote.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewNote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNote;
        TextView textViewNote;
        Button btnEdit;
        Button btnDelete;
        OnNoteClickListener onNoteClickListener;

        public NoteViewHolder(@NonNull View itemView, OnNoteClickListener listener) {
            super(itemView);
            imageViewNote = itemView.findViewById(R.id.imageViewNote);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            onNoteClickListener = listener; // inisialisasi onNoteClickListener

            btnEdit.setOnClickListener(view -> {
                if (onNoteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onNoteClickListener.onEditClick(position);
                    }
                }
            });

            btnDelete.setOnClickListener(view -> {
                if (onNoteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onNoteClickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}
