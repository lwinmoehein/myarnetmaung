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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    ImageView imgUserProfile;
    TextView txtUserName,txtUserStatus;

    Button addRelationship,copyId,logoutUser;



    ViewPager pagerLovers;
    TabLayout tabLover;

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
        txtUserName=view.findViewById(R.id.user_name);
        txtUserStatus=view.findViewById(R.id.user_status);

        addRelationship=view.findViewById(R.id.user_add_relationship);
        copyId=view.findViewById(R.id.btn_user_copy_id);
        logoutUser=view.findViewById(R.id.btnUserLogout);




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
