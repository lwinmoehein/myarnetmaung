package lwinmoehein.io.myarnetmaung.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.utils.ImageUtil;

public class FragmentImageViewer extends DialogFragment {
    private ImageView image;
    private ImageView imgSave;
    private String img_url;
    private ImageView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image=view.findViewById(R.id.img_view);
        imgSave=view.findViewById(R.id.img_save);
        back=view.findViewById(R.id.img_back);
        back.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        img_url=getArguments().getString("img_url");



        Glide.with(this.image.getContext()).load(img_url).placeholder(R.drawable.img_error)
                .into(image);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveImageAsync().execute(img_url);


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  closeFragment();
            }
        });


    }

    private void closeFragment() {
         getDialog().dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);



        return view;
    }

    private void cancelUpload() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    //save image async
    public class SaveImageAsync extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                java.net.URL url = new java.net.URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Downloading Image..",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            String path= ImageUtil.saveToInternalStorage(bitmap,image.getContext());
            Toast.makeText(getActivity(),"Image saved to "+path,Toast.LENGTH_SHORT).show();


        }


    }
}
