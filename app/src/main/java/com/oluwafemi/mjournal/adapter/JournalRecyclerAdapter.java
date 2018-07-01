package com.oluwafemi.mjournal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oluwafemi.mjournal.R;
import com.oluwafemi.mjournal.activity.JournalDetailActivity;
import com.oluwafemi.mjournal.helper.UIUtils;
import com.oluwafemi.mjournal.model.Journal;

import java.util.List;

import static com.oluwafemi.mjournal.activity.JournalDetailActivity.JOURNAL_KEY;

/**
 * Created by femi on 7/1/18.
 */

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.JournalViewHolder> {

    private static final String TAG = "JournalRecyclerAdapter";
    private List<Journal> journals;
    private Context context;

    public JournalRecyclerAdapter(List<Journal> journals, Context context) {
        this.journals = journals;
        this.context = context;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_journal, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        final Journal journal = journals.get(position);
        holder.tvDate.setText(UIUtils.formatDateNoTime(journal.getTime()));
        holder.tvTitle.setText(journal.getTitle());
        holder.tvText.setText(journal.getText());
        holder.tvTime.setText(UIUtils.formatTimeFromLong(journal.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String journalStr = gson.toJson(journal);

                Intent detailIntent = new Intent(context, JournalDetailActivity.class);
                detailIntent.putExtra(JOURNAL_KEY, journalStr);
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }


    class JournalViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvTime, tvTitle, tvText;

        public JournalViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_journal_date);
            tvTime = itemView.findViewById(R.id.tv_jounal_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvText = itemView.findViewById(R.id.tv_journal_text);
        }
    }

}
