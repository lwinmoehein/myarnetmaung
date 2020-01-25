package lwinmoehein.io.myarnetmaung.holder;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.fragment.FragmentImageViewer;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;


public class ReceiveImgMsgViewHolder extends RecyclerView.ViewHolder implements ChatBinder{

    public ImageView image,profileImage;

    public ReceiveImgMsgViewHolder(@NonNull View itemView) {
        super(itemView);

        image=itemView.findViewById(R.id.img_send);
        profileImage=itemView.findViewById(R.id.img_send_profile);
    }

    @Override
    public void onBind(ChatMessage chatMessage) {
        Glide.with(this.image.getContext()).load(chatMessage.getImgurl())
                .placeholder(R.drawable.ic_loading).fitCenter()
                .into(this.image);
        Glide.with(this.profileImage.getContext()).load(chatMessage.getSenderprofile())
                .placeholder(R.drawable.img_error).fitCenter()
                .into(this.profileImage);

        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("img_url", chatMessage.getImgurl());
                FragmentImageViewer dialog = new FragmentImageViewer();
                dialog.setArguments(bundle);
                FragmentTransaction ft =  ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                dialog.show(ft, "TAG");
            }
        });
    }
}
