package lwinmoehein.io.myarnetmaung.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import lwinmoehein.io.myarnetmaung.model.Lover;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.RelationshipViewHolder>{

    private List<Lover> loverArrayList=new ArrayList<>();
    boolean showConnect=false;
    ;

    public UserListAdapter(List<Lover> postArrayList) {
        this.loverArrayList = postArrayList;
        this.setHasStableIds(true);

        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue(Lover.class).getRsid()==null){
                        showConnect=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                .inflate(R.layout.current_user_item, viewGroup, false);
        RelationshipViewHolder loverViewHolder=new RelationshipViewHolder(view);
        return loverViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull RelationshipViewHolder loverViewHolder, int i) {
        Lover lov=loverArrayList.get(i);
        loverViewHolder.bindPostUi(lov,this,loverArrayList);
        if(showConnect){
            if(!(lov.getUid().equals(CurrentUser.currentUser.getUid()))) {
                loverViewHolder.btnConnect.setVisibility(View.VISIBLE);
            }else {
                loverViewHolder.btnConnect.setVisibility(View.GONE);
            }


        }else {
            loverViewHolder.btnConnect.setVisibility(View.GONE);
        }
    }

    @Override public int getItemCount() {
        return loverArrayList.size();

    }

    public void changeIndexData(Lover post, int i) {
        notifyDataSetChanged();
    }


    static class RelationshipViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLoverName,txtLoverStatus;
        private Button btnConnect;
        private ImageView imgLoverProfile;

        public RelationshipViewHolder(View view) {
            super(view);
            this.txtLoverName=view.findViewById(R.id.lover_name);
            this.txtLoverStatus=view.findViewById(R.id.lover_status);
            this.imgLoverProfile=view.findViewById(R.id.lover_profile);

            this.btnConnect=view.findViewById(R.id.current_user_connect);




        }

        public void bindPostUi(Lover lover, UserListAdapter adapter, List<Lover> lovers){

            if(lover.getUid().equals(CurrentUser.currentUser.getUid())){
                btnConnect.setVisibility(View.GONE);
            }

            this.txtLoverName.setText(lover.getName());
            if(lover.getRsid()==null){
                this.txtLoverStatus.setText("Single");
            }else {
                References.rsDatabaseRef.child(lover.getRsid()).child(lover.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Lover lover1 = dataSnapshot.getValue(Lover.class);
                            if (lover1.getUid().equals(CurrentUser.currentUser.getUid())) {
                                txtLoverStatus.setText("You are in love with " + lover.getName());
                                btnConnect.setVisibility(View.GONE);
                            } else {
                                txtLoverStatus.setText("Relationship");
                                btnConnect.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            Glide.with(this.txtLoverName.getContext()).load(lover.getProfilepic()).placeholder(R.drawable.img_error).into(this.imgLoverProfile);
            this.btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Snacky.builder().setView(v.getRootView())
                            .setText("Cancled request")
                            .setDuration(Snacky.LENGTH_LONG)
                            .build().setBackgroundTint(Color.GREEN)
                            .show();*/
                    addpending(lover,v);

                }
            });

        }

        private void addpending(Lover lov,View v) {
            {
                References.loverDatabaseRef.child(lov.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //get current user inifo
                            Lover otherlover=dataSnapshot.getValue(Lover.class);
                            if(!(otherlover.getRsid()==null)){
                                Toast.makeText(v.getContext(),"Already in a relationship",Toast.LENGTH_LONG).show();

                            }else {

                                References.pendingloverDb.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(v.getContext(),"Already has one request",Toast.LENGTH_LONG).show();

                                        } else {
                                            References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Lover lover = dataSnapshot.getValue(Lover.class);
                                                    //insert to pending lovers

                                                    References.pendingloverDb.child(lov.getUid()).child(lover.getUid()).setValue(lover)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    References.sentLovers.child(CurrentUser.currentUser.getUid()).child(otherlover.getUid()).setValue(otherlover).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Snacky.builder().setView(v.getRootView())
                                                                                    .setText("Ask your lover to confirm this request")
                                                                                    .setDuration(Snacky.LENGTH_LONG)
                                                                                    .build().setBackgroundTint(Color.GREEN)
                                                                                    .show();
                                                                        }
                                                                    });

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Snacky.builder().setView(v.getRootView())
                                                                    .setText("Something wrong sending lover request")
                                                                    .setDuration(Snacky.LENGTH_LONG)
                                                                    .build().setBackgroundTint(Color.RED)
                                                                    .show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        } else {
                            Snacky.builder().setView(v.getRootView())
                                    .setText("No one found with this id")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .build().setBackgroundTint(Color.RED)
                                    .show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

}