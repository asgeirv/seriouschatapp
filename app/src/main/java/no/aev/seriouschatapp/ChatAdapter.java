package no.aev.seriouschatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aev on 11.10.17.
 */

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{

    private List<Conversation> convs = new ArrayList<>();

    private Context context;

    private OnClickListener listener;

    public interface OnClickListener
    {

        void onClick(int position);
    }

    public ChatAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        return new ChatViewHolder(new View(context));
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position)
    {
        Conversation conv = convs.get(position);
    }

    @Override
    public int getItemCount()
    {
        return convs.size();
    }

    public void setConvs(List<Conversation> convs) {
        this.convs = convs;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.listener = listener;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(View view)
        {
            super(view);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (listener != null)
                    {
                        listener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
