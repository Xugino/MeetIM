package com.xieyangzhe.meetim.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xieyangzhe.meetim.Activities.LoginActivity;
import com.xieyangzhe.meetim.Activities.MainActivity;
import com.xieyangzhe.meetim.Activities.TestActivity;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Services.XMPPService;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Button buttonLogout;
    private TextView textViewMe;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonLogout = view.findViewById(R.id.button_logout);
        textViewMe = view.findViewById(R.id.me_name);
        textViewMe.setText(XMPPTool.getCurrentUserName());
        buttonLogout.setOnClickListener(view -> {
            getActivity().stopService(new Intent(getContext(), XMPPService.class));
            PreferencesUtils.getInstance().saveData("username", "");
            PreferencesUtils.getInstance().saveData("password", "");
            getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        return view;

    }
}
