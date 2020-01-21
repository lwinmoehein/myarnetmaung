package lwinmoehein.io.myarnetmaung.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class LoverAdapter extends RecyclerView.Adapter<LoverAdapter.LoverViewHolder>{

    private List<Lover> loverArrayList=new ArrayList<>();
    ;

    public LoverAdapter(List<Lover> postArrayList) {
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
    @Override public LoverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lover, viewGroup, false);
        LoverViewHolder loverViewHolder=new LoverViewHolder(view);
        return loverViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull LoverViewHolder loverViewHolder, int i) {
        Lover post=loverArrayList.get(i);
        loverViewHolder.bindPostUi(post,this,loverArrayList);
    }

    @Override public int getItemCount() {
        return loverArrayList.size();

    }

    public void changeIndexData(Lover post, int i) {
        notifyDataSetChanged();
    }


    static class LoverViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLoverName,txtLoverStatus;
        private Button btnConfirmRelationship;
        private ImageView imgLoverProfile;

        public LoverViewHolder(View view) {
            super(view);
           this.txtLoverName=view.findViewById(R.id.lover_name);
           this.txtLoverStatus=view.findViewById(R.id.lover_status);
           this.imgLoverProfile=view.findViewById(R.id.lover_profile);

           this.btnConfirmRelationship=view.findViewById(R.id.lover_confirm_relationship);




        }

        public void bindPostUi(Lover lover, LoverAdapter adapter, List<Lover> lovers){
            this.txtLoverName.setText(lover.getName());
            if(!(lover.getRsid()==null)) {
                if (lover.getRsid().equals("")) {
                    this.txtLoverStatus.setText("Single");
                } else {
                    this.txtLoverStatus.setText("Has Relationship");
                }
            }

            Glide.with(this.txtLoverName.getContext()).load(lover.getProfilepic()).placeholder(R.drawable.img_error).into(this.imgLoverProfile);
            this.btnConfirmRelationship.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rsid= References.rsDatabaseRef.push().getKey();
                    lover.setRsid(rsid);
                    References.pendingloverDb.child(CurrentUser.currentUser.getUid()).setValue(null);

                    References.loverDatabaseRef.child(lover.getUid()).setValue(lover);
                    References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).child("rsid").setValue(rsid);

                    References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Lover lover1=dataSnapshot.getValue(Lover.class);
                            References.rsDatabaseRef.child(rsid).child(lover.getUid()).setValue(lover1);
                            References.rsDatabaseRef.child(CurrentUser.currentUser.getUid()).setValue(lover);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });

        }
    }

}