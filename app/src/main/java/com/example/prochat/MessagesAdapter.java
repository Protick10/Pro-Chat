package com.example.prochat;

import static com.example.prochat.ChatWindow.reciverIImg;
import static com.example.prochat.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//the messages that will be displayed will be write here
public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessagModelClass> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<MessagModelClass> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagModelClass messagModelClass = messagesAdpterArrayList.get(position);


        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtext.setText(messagModelClass.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);

        }
        else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtext.setText(messagModelClass.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessagModelClass messagModelClass = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messagModelClass.getSenderid())){
            return ITEM_SEND;
        }
        else {
            return ITEM_RECIVE;
        }
    }

    // when message will be sent how the sender and the reciver will see message taht will controled..
    class  senderViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView msgtext;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.senderlayoutimg);
            msgtext = itemView.findViewById(R.id.senderlayouttv);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView msgtext;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.reciverlayoutimg);
            msgtext = itemView.findViewById(R.id.reciverlayouttv);
        }
    }
}
