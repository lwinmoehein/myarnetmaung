package lwinmoehein.io.myarnetmaung.Singleton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class References {
    public static DatabaseReference loverDatabaseRef= FirebaseDatabase.getInstance().getReference("lovers");
    public static StorageReference  loverStorageRef= FirebaseStorage.getInstance().getReference("lovers");

    public static DatabaseReference chatDatabaseRef= FirebaseDatabase.getInstance().getReference("chats");
    public static StorageReference  chatStorageRef= FirebaseStorage.getInstance().getReference("chats");

    public static DatabaseReference rsDatabaseRef= FirebaseDatabase.getInstance().getReference("relationships");
    public static StorageReference  rsStorageRef= FirebaseStorage.getInstance().getReference("relationships");


}
