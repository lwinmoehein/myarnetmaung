package lwinmoehein.io.myarnetmaung.adapter;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.fragment.FragmentImageViewer;
import lwinmoehein.io.myarnetmaung.model.Lover;
import lwinmoehein.io.myarnetmaung.model.PrivacyImage;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private List<PrivacyImage> photoList=new ArrayList<>();
    ;

    public PhotoAdapter(List<PrivacyImage> photoList) {
        this.photoList = photoList;
        this.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_photo, viewGroup, false);
        PhotoViewHolder photoViewHolder=new PhotoViewHolder(view);
        return photoViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull PhotoViewHolder loverViewHolder, int i) {
        PrivacyImage photo=photoList.get(i);
        loverViewHolder.bindPostUi(photo);

    }

    @Override public int getItemCount() {
        return photoList.size();

    }

    public void changeIndexData(PrivacyImage privacyImage, int i) {
        notifyDataSetChanged();
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_photo;

        public PhotoViewHolder(View view) {
            super(view);
            this.img_photo=view.findViewById(R.id.img_photo);
        }

        public void bindPostUi(PrivacyImage image) {
            Glide.with(this.img_photo.getContext()).load(image.getImg_url()).fitCenter().placeholder(R.drawable.ic_loading).into(this.img_photo);
            this.img_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("img_url", image.getImg_url());
                            FragmentImageViewer dialog = new FragmentImageViewer();
                            dialog.setArguments(bundle);
                            FragmentTransaction ft =  ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                            dialog.show(ft, "TAG");

                }
            });
        }
    }

}