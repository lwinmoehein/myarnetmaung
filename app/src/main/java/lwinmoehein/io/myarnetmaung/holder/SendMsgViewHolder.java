package lwinmoehein.io.myarnetmaung.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;


public class SendMsgViewHolder extends RecyclerView.ViewHolder implements ChatBinder{

    public TextView message;

    public SendMsgViewHolder(@NonNull View itemView) {
        super(itemView);

        message=itemView.findViewById(R.id.txt_receive);

    }

    @Override
    public void onBind(ChatMessage chatMessage) {
        this.message.setText(chatMessage.getMessage());

    }
}
