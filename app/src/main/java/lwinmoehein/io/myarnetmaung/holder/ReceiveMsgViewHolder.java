package lwinmoehein.io.myarnetmaung.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;


public class ReceiveMsgViewHolder extends RecyclerView.ViewHolder implements ChatBinder{

    public TextView message;
    public ImageView imgProfile;

    public ReceiveMsgViewHolder(@NonNull View itemView) {
        super(itemView);

        message=itemView.findViewById(R.id.txt_send_message);
        imgProfile=itemView.findViewById(R.id.img_send_profile);
    }

    @Override
    public void onBind(ChatMessage chatMessage) {
        this.message.setText(chatMessage.getMessage().toString());
        Glide.with(this.imgProfile.getContext()).load(chatMessage.getSenderprofile())
                .placeholder(R.drawable.img_error).fitCenter()
                .into(this.imgProfile);
    }
}
