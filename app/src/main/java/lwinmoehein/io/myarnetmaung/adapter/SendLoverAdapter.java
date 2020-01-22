package lwinmoehein.io.myarnetmaung.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class SendLoverAdapter extends RecyclerView.Adapter<SendLoverAdapter.RelationshipViewHolder>{

    private List<Lover> loverArrayList=new ArrayList<>();
    ;

    public SendLoverAdapter(List<Lover> postArrayList) {
        this.loverArrayList = postArrayList;
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
    @Override public RelationshipViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sent_lover_item, viewGroup, false);
        RelationshipViewHolder loverViewHolder=new RelationshipViewHolder(view);
        return loverViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull RelationshipViewHolder loverViewHolder, int i) {
        Lover post=loverArrayList.get(i);
        loverViewHolder.bindPostUi(post,this,loverArrayList);
    }

    @Override public int getItemCount() {
        return loverArrayList.size();

    }

    public void changeIndexData(Lover post, int i) {
        notifyDataSetChanged();
    }


    static class RelationshipViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLoverName,txtLoverStatus;
        private Button btnConfirmRelationship;
        private ImageView imgLoverProfile;

        public RelationshipViewHolder(View view) {
            super(view);
           this.txtLoverName=view.findViewById(R.id.lover_name);
           this.txtLoverStatus=view.findViewById(R.id.lover_status);
           this.imgLoverProfile=view.findViewById(R.id.lover_profile);

           this.btnConfirmRelationship=view.findViewById(R.id.lover_cancel_relationship);




        }

        public void bindPostUi(Lover lover, SendLoverAdapter adapter, List<Lover> lovers){
            this.txtLoverName.setText(lover.getName());
            if(lover.getRsid()==null){
                this.txtLoverStatus.setText("Single");
            }else {
                this.txtLoverStatus.setText("In a relationship");

            }

            Glide.with(this.txtLoverName.getContext()).load(lover.getProfilepic()).placeholder(R.drawable.img_error).into(this.imgLoverProfile);
            this.btnConfirmRelationship.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   References.pendingloverDb.child(lover.getUid()).setValue(null);
                   References.sentLovers.child(CurrentUser.currentUser.getUid()).setValue(null);
                    Snacky.builder().setView(v.getRootView())
                            .setText("Cancled request")
                            .setDuration(Snacky.LENGTH_LONG)
                            .build().setBackgroundTint(Color.GREEN)
                            .show();

                }
            });

        }
    }

}