package lwinmoehein.io.myarnetmaung.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.adapter.LoverAdapter;
import lwinmoehein.io.myarnetmaung.dialog.RelationShipDialog;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class FragmentProfile extends Fragment {
    ImageView imgUserProfile;
    TextView txtUserName,txtUserStatus;

    Button addRelationship;

    RecyclerView recyclerLovers;
    LoverAdapter loverAdapter;
    ArrayList<Lover> lovers=new ArrayList<>();
    public FragmentProfile(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgUserProfile=view.findViewById(R.id.user_profile);
        txtUserName=view.findViewById(R.id.user_name);
        txtUserStatus=view.findViewById(R.id.user_status);

        addRelationship=view.findViewById(R.id.user_add_relationship);
        recyclerLovers=view.findViewById(R.id.recycler_lovers);

        loverAdapter=new LoverAdapter(lovers);
        recyclerLovers.setLayoutManager(new LinearLayoutManager(getActivity()));



        txtUserName.setText(CurrentUser.currentUser.getDisplayName());
        Glide.with(getContext()).load(CurrentUser.currentUser.getPhotoUrl()).placeholder(R.drawable.img_error).into(imgUserProfile);

        addRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelationShipDialog relationShipDialog=new RelationShipDialog();
                relationShipDialog.show(getFragmentManager(),"Add");
            }
        });

        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).child("lover").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Toast.makeText(getActivity(),"No Lover found",Toast.LENGTH_LONG).show();
                }else {
                    Lover lover=(Lover)dataSnapshot.getValue(Lover.class);
                    lovers.add(lover);
                    loverAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
