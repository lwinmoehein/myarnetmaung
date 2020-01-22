package lwinmoehein.io.myarnetmaung.adapter;

import android.content.ContentUris;
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

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipAdapter.RelationshipViewHolder>{

    private List<Lover> loverArrayList=new ArrayList<>();
    ;

    public RelationshipAdapter(List<Lover> postArrayList) {
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
                .inflate(R.layout.item_lover, viewGroup, false);
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

           this.btnConfirmRelationship=view.findViewById(R.id.lover_confirm_relationship);




        }

        public void bindPostUi(Lover lover, RelationshipAdapter adapter, List<Lover> lovers){
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

                    References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Lover lover1=dataSnapshot.getValue(Lover.class);
                            References.rsDatabaseRef.child(rsid).child(lover.getUid()).setValue(lover1);
                            References.rsDatabaseRef.child(rsid).child(CurrentUser.currentUser.getUid()).setValue(lover);

                            References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).setValue(lover1);

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