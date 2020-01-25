package lwinmoehein.io.myarnetmaung.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.User;

import java.util.ArrayList;
import java.util.List;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.holder.ReceiveImgMsgViewHolder;
import lwinmoehein.io.myarnetmaung.holder.ReceiveMsgViewHolder;
import lwinmoehein.io.myarnetmaung.holder.SendImgViewHolder;
import lwinmoehein.io.myarnetmaung.holder.SendMsgViewHolder;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;
import lwinmoehein.io.myarnetmaung.model.MessageType;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> chatMessages=new ArrayList<>();
    private Context context;

    public MessageAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder=null;
        View view=null;
        switch (i){
            case 1:
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.send_msg_vh,viewGroup,false);
                viewHolder=new SendMsgViewHolder(view);
                break;
            case 2:
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.send_msg_img_vh,viewGroup,false);
                viewHolder=new SendImgViewHolder(view);
                break;
            case 3:
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receive_msg_vh,viewGroup,false);
                viewHolder=new ReceiveMsgViewHolder(view);
                break;
            case 4:
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receive_img_msg_vh,viewGroup,false);
                viewHolder=new ReceiveImgMsgViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatMessage chatMessage = chatMessages.get(i);

        if(chatMessage.getSender_id().equals(CurrentUser.currentUser.getUid())){
            //send message
            if(chatMessage.getType()== MessageType.TEXT_MESSAGE){
                SendMsgViewHolder sendMessageViewHolder=(SendMsgViewHolder) viewHolder;
                sendMessageViewHolder.onBind(chatMessage);
            }else {
                SendImgViewHolder sendMessageImageViewHolder=(SendImgViewHolder) viewHolder ;
                sendMessageImageViewHolder.onBind(chatMessage);
            }
        }else {            //receive message
            if(chatMessage.getType()==MessageType.TEXT_MESSAGE){
                ReceiveMsgViewHolder receiveMessageViewHolder=(ReceiveMsgViewHolder) viewHolder;
                receiveMessageViewHolder.onBind(chatMessage);
            }else {
                 ReceiveImgMsgViewHolder receiveMessageImageViewHolder=(ReceiveImgMsgViewHolder)viewHolder;
                 receiveMessageImageViewHolder.onBind(chatMessage);
            }
        }

    }
    public void addToTop(ChatMessage chatMessage){
        this.chatMessages.add(0, chatMessage);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage=chatMessages.get(position);
        int type=0;
        if (chatMessage.getSender_id().equals(CurrentUser.currentUser.getUid())){
            //sending message
            if(chatMessage.getType()==MessageType.TEXT_MESSAGE){
                type=1;
            }else {
            type=2;
            }
        }else {
            //sending message
            if(chatMessage.getType()==MessageType.TEXT_MESSAGE){
                type=3;
            }else {
                type=4;
            }
        }

        return  type;
    }

    @Override
    public long getItemId(int position) {
        ChatMessage chatMessage=chatMessages.get(position);
        return chatMessage.getMessage_id().hashCode();
    }
}
