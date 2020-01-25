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
import java.util.List;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.adapter.UserListAdapter;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class FragmentUsers extends Fragment {
    RecyclerView recycler_user_list;
    List<Lover> lovers=new ArrayList<>();
    UserListAdapter listAdapter;
    private List<String> loverids=new ArrayList<>();

    public FragmentUsers(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler_user_list=view.findViewById(R.id.recycler_user_list);
        listAdapter=new UserListAdapter(lovers);

        recycler_user_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_user_list.setAdapter(listAdapter);

        References.currentLovers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Lover lover=dataSnapshot.getValue(Lover.class);
                if(!(lover==null)){
                    if(!loverids.contains(lover.getUid())) {
                        lovers.add(lover);
                        loverids.add(lover.getUid());
                        listAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
