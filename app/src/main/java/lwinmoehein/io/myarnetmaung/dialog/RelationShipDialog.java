package lwinmoehein.io.myarnetmaung.dialog;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.time.Duration;

import de.mateware.snacky.Snacky;
import lwinmoehein.io.myarnetmaung.MainActivity;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class RelationShipDialog extends DialogFragment {
    View parentView;
    public RelationShipDialog(View view){
        this.parentView=view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add_rs, null))
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.edt_lover_id);
                        String strLoverId=editText.getText().toString();
                        if(strLoverId.equals("")){
                            Snacky.builder().setView(parentView)
                                    .setText("Pelase fill the id")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .build().setBackgroundTint(Color.RED)
                                    .show();
                        }else if(strLoverId.equals(CurrentUser.currentUser.getUid())){
                            Snacky.builder().setView(parentView)
                                    .setText("You can't love yourself!!")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .build().setBackgroundTint(Color.RED)
                                    .show();
                        }

                        else {
                            References.loverDatabaseRef.child(strLoverId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                       //get current user inifo
                                        Lover otherlover=dataSnapshot.getValue(Lover.class);
                                        if(!(otherlover.getRsid()==null)){
                                            Snacky.builder().setView(parentView)
                                                    .setText("already in a relationship")
                                                    .setDuration(Snacky.LENGTH_LONG)
                                                    .build().setBackgroundTint(Color.RED)
                                                    .show();

                                        }else {

                                            References.pendingloverDb.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Snacky.builder().setView(parentView)
                                                                .setText("This acc already sent you a request,please click confirm!!")
                                                                .setDuration(Snacky.LENGTH_LONG)
                                                                .build().setBackgroundTint(Color.RED)
                                                                .show();
                                                    } else {
                                                        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Lover lover = dataSnapshot.getValue(Lover.class);
                                                                //insert to pending lovers

                                                                References.pendingloverDb.child(strLoverId).child(lover.getUid()).setValue(lover)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                References.sentLovers.child(CurrentUser.currentUser.getUid()).child(otherlover.getUid()).setValue(otherlover).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Snacky.builder().setView(parentView)
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
                                                                        Snacky.builder().setView(parentView)
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
                                        Snacky.builder().setView(parentView)
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
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RelationShipDialog.this.getDialog().cancel();

                    }
                });
        return builder.create();
    }
}
