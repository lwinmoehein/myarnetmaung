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

public class SendImgViewHolder extends RecyclerView.ViewHolder implements ChatBinder{

    public ImageView img;

    public SendImgViewHolder(@NonNull View itemView) {
        super(itemView);

        img=itemView.findViewById(R.id.img_receive);
    }

    @Override
    public void onBind(ChatMessage chatMessage) {
        Glide.with(this.img.getContext()).load(chatMessage.getImgurl())
                .placeholder(R.drawable.ic_loading).fitCenter()
                .into(this.img);
        this.img.setOnClickListener(new View.OnClickListener() {
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
