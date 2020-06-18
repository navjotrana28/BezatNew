package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bezatnew.bezat.R;
import com.bezatnew.bezat.activities.Homepage;
import com.bezatnew.bezat.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeLanguage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeLanguage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeLanguage extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View rootView;
    EditText etLanguage;
    TextView txtEnglish, txtArabic;
    ImageView imgBack;
    String lang = "";

    public ChangeLanguage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeLanguage.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeLanguage newInstance(String param1, String param2) {
        ChangeLanguage fragment = new ChangeLanguage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.setClickable(true);

        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";

        }

        rootView = inflater.inflate(R.layout.fragment_change_language, container, false);
        etLanguage = rootView.findViewById(R.id.etLanguage);
        txtArabic = rootView.findViewById(R.id.txtArabic);
        txtEnglish = rootView.findViewById(R.id.txtEnglish);
        imgBack = rootView.findViewById(R.id.imgBack);
        if (lang.equals("_ar")) {
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        txtEnglish.setOnClickListener(this);
        txtArabic.setOnClickListener(this);

        List<String> l = new ArrayList<>();
        l.add("");
        l.add("عربى");
        l.add("ENGLISH");


        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_language);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.spinner_item_language,
                R.id.tv_language,
                l) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                LayoutInflater inflater = ChangeLanguage.this.getLayoutInflater();

                if(convertView == null){
                    convertView = inflater.inflate(R.layout.spinner_item_language,parent, false);
                }
                TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_language);
                txtTitle.setText(l.get(position));
                ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_language);
                if(position==1){
                    convertView.setBackgroundColor(0xFFF7F7F7);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.arabic_icon));
                }else if(position==2){
                    convertView.setBackgroundColor(0xFFF7F7F7);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.english_icon));
                }
                else{
                    convertView.setBackgroundColor(0xFFFFFFFF);
                    imageView.setImageDrawable(null);
                }

                return convertView;
            }
        };
        /*{
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        }*/
        ;
        adapter.setDropDownViewResource(R.layout.spinner_item_language);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Change language");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {
                    setLocale("ar");

                } else {
                    setLocale("en");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtEnglish) {
            setLocale("en");
        } else if (view.getId() == R.id.txtArabic) {
            setLocale("ar");
        }
    }

    public void setLocale(String lang) {
        SharedPrefs.setKey(getActivity(), "selectedlanguage", lang);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        startActivity(new Intent(getActivity(), Homepage.class));
        getActivity().finish();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
