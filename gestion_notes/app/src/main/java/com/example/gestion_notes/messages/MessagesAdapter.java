package com.example.gestion_notes.messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gestion_notes.R;
import com.example.gestion_notes.activity_messenger;
import com.example.gestion_notes.chat.Chat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
  private  List<MessagesList> messagesLists;
  private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,null));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2 = messagesLists.get(position);
        if (list2.getProfilePic() != null && !list2.getProfilePic().isEmpty()) {
            Glide.with(context)
                    .load(list2.getProfilePic())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profilePic);
        } else {
            Log.d("AdapterData", "URL de l'image est vide ou null.");
        }
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());
        if (list2.getUnseenMesages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMesages()+"");
            holder.lastMessage.setTextColor(ContextCompat.getColor(context, R.color.blue));

        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, Chat.class);
                intent.putExtra("tel",list2.getMobile());
                intent.putExtra("nom",list2.getName());
                intent.putExtra("image",list2.getProfilePic());
                intent.putExtra("chat_key",list2.getChatKey());
                context.startActivity(intent);
            }
        });
    }
public void updateData(List<MessagesList>messagesLists){
        this.messagesLists=messagesLists;
        notifyDataSetChanged();
}

    @Override
    public int getItemCount() {
        return messagesLists.size() ;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;
        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profilePic);
            name= itemView.findViewById(R.id.name);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            unseenMessages=itemView.findViewById(R.id.unseenMessages);
            rootLayout=itemView.findViewById(R.id.rootLayout);
        }
    }
}
