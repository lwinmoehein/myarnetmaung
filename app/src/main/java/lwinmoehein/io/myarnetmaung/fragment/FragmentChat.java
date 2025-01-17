package lwinmoehein.io.myarnetmaung.fragment;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import lwinmoehein.io.myarnetmaung.MainActivity;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.adapter.MessageAdapter;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;
import lwinmoehein.io.myarnetmaung.model.Lover;
import lwinmoehein.io.myarnetmaung.model.MessageType;
import lwinmoehein.io.myarnetmaung.utils.PermissionHelper;

import static android.view.View.GONE;

public class FragmentChat extends HomeBaseFragment {


    private static final int ITEM_COUNT =10 ;

    private DatabaseReference chatReference= References.chatDatabaseRef;
    private StorageReference photoRef=References.chatStorageRef;
    private LinearLayout stickerLayout;

    private RecyclerView recyclermessages;

    private List<ChatMessage> chatMessages=new ArrayList<>();
    private List<String> chatIds=new ArrayList<>();
    private MessageAdapter messageAdapter;

    //send message
    private ImageView imageBtn,sendBtn;
    private EditText edtMessage;
    private SwipeRefreshLayout refreshChat,noRefreshchat;
    RelativeLayout noLover;
    private int REQUEST_CODE_CHOOSE=1119;

    private   Uri selectedUri=null;
    private int itempos=0;
    private String lastitem="";
    private boolean moreItem=true;

    private DonutProgress donutProgress;




    public FragmentChat() {
        // Required empty public constructor
    }

    @Override    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //buttons
        imageBtn=view.findViewById(R.id.img_btn_send_image);
        sendBtn=view.findViewById(R.id.img_btn_send_message);

        //message edittext
        edtMessage=(EditText)view.findViewById(R.id.edt_message);


        stickerLayout=view.findViewById(R.id.sticker_layout);
        recyclermessages=view.findViewById(R.id.recycler_message);
        noLover=view.findViewById(R.id.no_lover_found);

        refreshChat=view.findViewById(R.id.refresh_chat);
        noRefreshchat=view.findViewById(R.id.no_chat_refresh);
        messageAdapter=new MessageAdapter(chatMessages);
        recyclermessages.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclermessages.setAdapter(messageAdapter);

        donutProgress=view.findViewById(R.id.donut_progress);

        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if((dataSnapshot.getValue(Lover.class).getRsid()!=null)){
                        chatReference=null;
                        chatReference= References.chatDatabaseRef.child(dataSnapshot.getValue(Lover.class).getRsid());
                        getData();
                        noRefreshchat.setVisibility(GONE);

                        noRefreshchat.setEnabled(false);

                    }else {
                        noRefreshchat.setVisibility(View.VISIBLE);
                        noRefreshchat.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        noRefreshchat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            noRefreshchat.setRefreshing(false);
                            if((dataSnapshot.getValue(Lover.class).getRsid()!=null)){
                                chatReference=null;
                                chatReference=chatReference.child(dataSnapshot.getValue(Lover.class).getRsid());
                                //getData();
                                noRefreshchat.setVisibility(GONE);
                                noRefreshchat.setEnabled(false);

                            }else {
                                noRefreshchat.setVisibility(View.VISIBLE);
                                noRefreshchat.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        ///////////
        sendBtn.setOnClickListener( new OnSendBtnListener());
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionHelper.checkImgPickPermission(getActivity());
                    }
                    TedBottomPicker.with(getActivity())
                            .setPeekHeight(1200)
                            .setTitle("select an image")
                            .showTitle(true)
                            .setGalleryTileBackgroundResId(R.color.colorPrimaryText)
                            .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    uploadImage(uri);
                                    if(!(chatMessages.size()==0))
                                        recyclermessages.smoothScrollToPosition(chatMessages.size()-1);

                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Cannot Pick Image", Toast.LENGTH_SHORT).show();
                }

                //////////////////
            }
        });


        refreshChat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               itempos=0;
            }
        });




    }




    private void uploadImage(Uri choosedUri) {
        StorageReference childRef = photoRef.child(UUID.randomUUID().toString()+".jpg");
        childRef.putFile(choosedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String id=chatReference.push().getKey();
                        ChatMessage chatMessage=new ChatMessage(id,false,ServerValue.TIMESTAMP, MessageType.IMAGE_MESSAGE, CurrentUser.currentUser.getUid(),"",uri.toString(),CurrentUser.currentUser.getDisplayName(), CurrentUser.currentUser.getPhotoUrl().toString());
                        chatReference.child(id).setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                donutProgress.setVisibility(GONE);

                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                donutProgress.setVisibility(View.VISIBLE);
                double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                Double.isNaN(bytesTransferred);
                bytesTransferred *= 100.0d;
                double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                bytesTransferred /= totalByteCount;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((int) bytesTransferred);
                donutProgress.setDonut_progress(stringBuilder.toString());
              }
        });
    }

    @Override
    public View provideView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, parent, false);


    }

    public  class OnSendBtnListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String message=edtMessage.getText().toString();
            String id=chatReference.push().getKey();

            //get count
            ChatMessage chatMessage=new ChatMessage(id,false,ServerValue.TIMESTAMP,MessageType.TEXT_MESSAGE, CurrentUser.currentUser.getUid(),message,"ui",CurrentUser.currentUser.getDisplayName(),CurrentUser.currentUser.getPhotoUrl().toString());
            chatReference.child(id).setValue(chatMessage);
            edtMessage.setText("");
            if(!(chatMessages.size()==0))
                recyclermessages.smoothScrollToPosition(chatMessages.size()-1);
        }
    }


//load more
private void getData(){

      chatReference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ChatMessage mg=dataSnapshot.getValue(ChatMessage.class);
            if(!chatIds.contains(dataSnapshot.getKey())) {
                chatMessages.add(mg);
                chatIds.add(dataSnapshot.getKey());
                messageAdapter.notifyItemInserted(chatMessages.size() - 1);
                refreshChat.setRefreshing(false);
                recyclermessages.scrollToPosition(chatMessages.size() - 1);
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


//load more data
//To load more chat data From cloud firebase


}