package lwinmoehein.io.myarnetmaung.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.adapter.LoverAdapter;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class PendingLoversFragment  extends Fragment {
    RecyclerView recyclerLovers;
    LoverAdapter loverAdapter;
    ArrayList<Lover> lovers=new ArrayList<>();
    ArrayList<String> pendingloverids=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_pending_lovers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerLovers=view.findViewById(R.id.recycler_lovers);

        loverAdapter=new LoverAdapter(lovers);
        recyclerLovers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLovers.setAdapter(loverAdapter);

        References.pendingloverDb.child(CurrentUser.currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    Lover lover=dataSnapshot.getValue(Lover.class);
                    lovers.add(lover);
                    loverAdapter.notifyDataSetChanged();
                    pendingloverids.add(lover.getUid());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Lover lover=dataSnapshot.getValue(Lover.class);
                int index=pendingloverids.indexOf(lover.getUid());
                lovers.remove(index);
                loverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
