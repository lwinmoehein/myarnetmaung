package lwinmoehein.io.myarnetmaung.acitivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.adapter.PhotoAdapter;
import lwinmoehein.io.myarnetmaung.model.ChatMessage;
import lwinmoehein.io.myarnetmaung.model.MessageType;
import lwinmoehein.io.myarnetmaung.model.PrivacyImage;
import lwinmoehein.io.myarnetmaung.model.PrivacyVideo;
import lwinmoehein.io.myarnetmaung.utils.PermissionHelper;

import static android.view.View.GONE;

public class PrivacyActivity extends AppCompatActivity {
    String rsid=null;
    FloatingActionButton fabAddImage;
    ProgressBar uploadProgress;
    RecyclerView recyclerPhotos;
    PhotoAdapter photoAdapter;
    ArrayList<PrivacyImage> privacyImages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        rsid=getIntent().getStringExtra("rsid");

        fabAddImage=findViewById(R.id.fab_add_image);
        uploadProgress=findViewById(R.id.progressUpload);
        recyclerPhotos=findViewById(R.id.recycler_photos);

        listenFloatingButtons();

        photoAdapter=new PhotoAdapter(privacyImages);
        recyclerPhotos.setLayoutManager(new GridLayoutManager(this,3));

        recyclerPhotos.setAdapter(photoAdapter);


        References.privacyPhotoRef.child(rsid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PrivacyImage privacyImage=dataSnapshot.getValue(PrivacyImage.class);
                privacyImages.add(privacyImage);
                photoAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void listenFloatingButtons() {

        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionHelper.checkImgPickPermission(PrivacyActivity.this);
                    }
                    TedBottomPicker.with(PrivacyActivity.this)
                            .setPeekHeight(1200)
                            .setTitle("select an image")
                            .showTitle(true)
                            .setGalleryTileBackgroundResId(R.color.colorPrimaryText)
                            .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    uploadImage(uri);
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Cannot Pick Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage(Uri choosedUri) {
        Toasty.info(getApplicationContext(),"Uploading ,please wait").show();
        StorageReference childRef = References.privacyPhotoStorage.child(UUID.randomUUID().toString()+".jpg");
        childRef.putFile(choosedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String id=References.privacyPhotoRef.child(rsid).push().getKey();
                        PrivacyImage image=new PrivacyImage(id,CurrentUser.currentUser.getUid(),uri.toString(),ServerValue.TIMESTAMP);
                         References.privacyPhotoRef.child(rsid).child(id).setValue(image).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                uploadProgress.setVisibility(GONE);
                                Toasty.success(getApplicationContext(),"Done uploading").show();

                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                uploadProgress.setVisibility(View.VISIBLE);

                double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                Double.isNaN(bytesTransferred);
                bytesTransferred *= 100.0d;
                double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                bytesTransferred /= totalByteCount;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((int) bytesTransferred);
            }
        });
    }

    public Uri getImageUriFromVideo(Uri video){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(video, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return Uri.parse(picturePath);
    }

}
