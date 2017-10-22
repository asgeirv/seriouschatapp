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

class ConvAdapter extends RecyclerView.Adapter<ConvAdapter.ConvViewHolder>
{

    private List<Conversation> convs = new ArrayList<>();

    private Context context;

    private OnClickListener listener;

    public interface OnClickListener
    {
        void onClick(int position);
    }

    public ConvAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public ConvViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation, parent, false);

        return new ConvViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ConvViewHolder holder, int position)
    {
        Conversation conv = convs.get(position);
        String text = context.getString(R.string.chat_id_string) + conv.getId().toString();
        holder.text.setText(text);
    }

    @Override
    public int getItemCount()
    {
        return convs.size();
    }

    public void setConvs(List<Conversation> convs)
    {
        this.convs = convs;
        notifyDataSetChanged();
    }

    public List<Conversation> getConvs()
    {
        return convs;
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.listener = listener;
    }

    public class ConvViewHolder extends RecyclerView.ViewHolder
    {

        public TextView text;

        public ConvViewHolder(View view)
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
