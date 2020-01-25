package lwinmoehein.io.myarnetmaung.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.idp.SingleSignInActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import lwinmoehein.io.myarnetmaung.MainActivity;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.acitivity.LoginActivity;
import lwinmoehein.io.myarnetmaung.adapter.LoverAdapter;
import lwinmoehein.io.myarnetmaung.dialog.RelationShipDialog;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class FragmentProfile extends Fragment {
    ImageView imgUserProfile,partnerProfile;
    TextView txtUserName,txtUserStatus,txtPartnerStatus,txtPartnerName;

    Button addRelationship,copyId,logoutUser,btnEndRelationship;

    LinearLayout cardPartner,bottomLayout;

    ViewPager pagerLovers;
    TabLayout tabLover;

    String rsId;
    String loverid;

    public FragmentProfile(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLover=view.findViewById(R.id.tab_lovers);
        pagerLovers=view.findViewById(R.id.pager_lovers);

        setupViewPager(pagerLovers);
        tabLover.setupWithViewPager(pagerLovers);


        imgUserProfile=view.findViewById(R.id.user_profile);
        partnerProfile=view.findViewById(R.id.partner_profile);
        txtPartnerStatus=view.findViewById(R.id.partner_status);
        txtPartnerName=view.findViewById(R.id.partner_name);
        btnEndRelationship=view.findViewById(R.id.btnEndRelationship);
        cardPartner=view.findViewById(R.id.card_partner);

        txtUserName=view.findViewById(R.id.user_name);
        txtUserStatus=view.findViewById(R.id.user_status);

        addRelationship=view.findViewById(R.id.user_add_relationship);
        copyId=view.findViewById(R.id.btn_user_copy_id);
        logoutUser=view.findViewById(R.id.btnUserLogout);
        bottomLayout=view.findViewById(R.id.bottom_layout);




        txtUserName.setText(CurrentUser.currentUser.getDisplayName());
        Glide.with(getContext()).load(CurrentUser.currentUser.getPhotoUrl()).placeholder(R.drawable.img_error).into(imgUserProfile);

        addRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelationShipDialog relationShipDialog=new RelationShipDialog(v.getRootView());
                relationShipDialog.show(getFragmentManager(),"Add");
            }
        });
        copyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)getActivity(). getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", CurrentUser.currentUser.getUid());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(),"Id copied",Toast.LENGTH_LONG).show();
            }
        });

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity());
                FirebaseAuth.getInstance().signOut();
                Snacky.builder().setView(imgUserProfile.getRootView())
                        .setText("Logged out")
                        .setDuration(Snacky.LENGTH_LONG)
                        .build().setBackgroundTint(Color.GREEN)
                        .show();

                Intent intent=new Intent( getActivity(), LoginActivity.class);
                getContext(). startActivity(intent);
                getActivity().finish();
            }
        });

        getUserData();

        btnEndRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   References.rsDatabaseRef.child(rsId).setValue(null);
                   References.loverDatabaseRef.child(loverid).child("rsid").setValue(null);
                   References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).child("rsid").setValue(null);

            }
        });





    }

    private void getUserData() {
        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lover currentlover=dataSnapshot.getValue(Lover.class);
                if(currentlover.getRsid()==null){
                    txtUserStatus.setText("Single");
                    bottomLayout.setVisibility(View.VISIBLE);
                    cardPartner.setVisibility(View.GONE);
                }else{
                    rsId=currentlover.getRsid();
                    txtUserStatus.setText("Relationship");
                    bottomLayout.setVisibility(View.GONE);
                    References.rsDatabaseRef.child(currentlover.getRsid()).child(currentlover.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Lover partner = dataSnapshot.getValue(Lover.class);
                                txtPartnerName.setText(partner.getName());
                                loverid=partner.getUid();
                                Glide.with(getContext()).load(partner.getProfilepic()).placeholder(R.drawable.img_error).into(partnerProfile);
                                txtPartnerStatus.setText(partner.getRsid());
                                cardPartner.setVisibility(View.VISIBLE);
                            }


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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new PendingLoversFragment(), "pending lovers");
        adapter.addFragment(new SendLoversFragment(), "sent lovers");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
