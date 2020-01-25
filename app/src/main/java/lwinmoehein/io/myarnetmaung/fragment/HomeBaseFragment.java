package lwinmoehein.io.myarnetmaung.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class HomeBaseFragment extends Fragment {
    //provide necessary views
    public abstract View provideView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = provideView(inflater,container,savedInstanceState);
        return view;
    }
}
