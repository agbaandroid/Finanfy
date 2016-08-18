package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class DropBoxInicioFragment extends Fragment {
    private static final String KEY_CONTENT = "DropBoxFragment:Content";
    private String mContent = "???";

    final static private String APP_KEY = "yty5ua0nmg6nv9a";
    final static private String APP_SECRET = "sn345hty9ytw7v3";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    SharedPreferences prefs;
    TextView buttonAcceder;

    boolean isSinPublicidad;
    boolean isPremium;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            isPremium = bundle.getBoolean("isPremium");
            isSinPublicidad = bundle.getBoolean("isSinPublicidad");
        }

        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dropbox_inicio, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        buttonAcceder = (TextView) getView().findViewById(R.id.buttonAcceder);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        prefs = getActivity().getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        String token = prefs.getString("dropboxToken", null);

        if (token != null) {
            mDBApi.getSession().setOAuth2AccessToken(token);

            if(mDBApi.getSession().isLinked()){
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSinPublicidad", isSinPublicidad);
                bundle.putBoolean("isPremium", isPremium);

                Fragment fragment = new DropBoxFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();
            }
        }

        buttonAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDBApi.getSession().startOAuth2Authentication(getActivity());
            }
        });
    }

    public void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("dropboxToken", accessToken);
                editor.commit();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isSinPublicidad", isSinPublicidad);

                Fragment fragment = new DropBoxFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();


            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
}
