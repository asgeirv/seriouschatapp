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

class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder>
{

    private List<Message> msgs = new ArrayList<>();

    private Context context;

    private OnClickListener listener;

    public interface OnClickListener
    {

        void onClick(int position);
    }

    public MsgAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);

        return new MsgViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position)
    {
        Message msg = msgs.get(position);
        String text = msg.getUser() + "\n"
                + msg.getText();
        System.out.println(text);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount()
    {
        return msgs.size();
    }

    public void setMsgs(List<Message> msgs)
    {
        this.msgs = msgs;
        notifyDataSetChanged();
    }

    public List<Message> getMsgs()
    {
        return msgs;
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.listener = listener;
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder
    {

        public TextView text;

        public MsgViewHolder(View view)
        {
            super(view);
            this.text = view.findViewById(R.id.textView);

            view.setOnClickListener(new View.OnClickListener()
            {
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
