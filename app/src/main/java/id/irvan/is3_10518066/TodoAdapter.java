package id.irvan.is3_10518066;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.irvan.is3_10518066.model.NoteModel;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    Context activity;
    RecyclerListener listener;
    List<NoteModel> notes;

    public TodoAdapter(Context activity, RecyclerListener listener, List<NoteModel> notes) {
        this.activity = activity;
        this.listener = listener;
        this.notes = notes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView createdAt;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            createdAt = view.findViewById(R.id.tv_time);
        }
    }

    public void updateData(List<NoteModel> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.list_recy, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TodoAdapter.ViewHolder holder, int position) {
        NoteModel note = notes.get(position);
        holder.title.setText(note.title);
        holder.createdAt.setText(note.dateTime);
        holder.itemView.setOnClickListener(v -> {
            listener.onClicked(note);
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
}