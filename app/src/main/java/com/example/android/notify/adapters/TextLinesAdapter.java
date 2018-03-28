package com.example.android.notify.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.notify.R;

import java.util.Arrays;
import java.util.List;

public class TextLinesAdapter extends RecyclerView.Adapter<TextLinesAdapter.TextLinesViewHolder> {

    private List<CharSequence> mTextLinesList;

    public TextLinesAdapter(CharSequence[] textLines) {
        mTextLinesList = Arrays.asList(textLines);
    }

    @NonNull
    @Override
    public TextLinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextLinesViewHolder(LayoutInflater.from(parent.getContext())
                                                     .inflate(R.layout.text_line_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextLinesViewHolder holder, int position) {
        holder.mTextLineTextView.setText(mTextLinesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTextLinesList == null ? 0 : mTextLinesList.size();
    }

    class TextLinesViewHolder extends RecyclerView.ViewHolder {

        TextView mTextLineTextView;

        TextLinesViewHolder(View itemView) {
            super(itemView);
            mTextLineTextView = itemView.findViewById(R.id.notification_text_line);
        }
    }
}
