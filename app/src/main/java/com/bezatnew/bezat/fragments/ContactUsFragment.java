package com.bezatnew.bezat.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.api.contactusResponse.ContactUsRequest;
import com.bezatnew.bezat.api.contactusResponse.ContactUsResponse;
import com.bezatnew.bezat.interfaces.ContactUsSuccessResponse;
import com.bezatnew.bezat.utils.SharedPrefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {
    ImageView imgBack;
    private EditText name, email, comments;
    private Button sendBtn;
    String lang="";
    private ImageView insta,youtube,twitter,whatsapp,dialer;


    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (SharedPrefs.getKey(getActivity(),"selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang="_ar";
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/tajawal_regular");
//            name.setTypeface(typeface);
//            email.setTypeface(typeface);
//            comments.setTypeface(typeface);
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang="";
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/muli_regular");
//            name.setTypeface(typeface);
//            email.setTypeface(typeface);
//            comments.setTypeface(typeface);
        }
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        addViews(view);
        onClickSendButton();
        onClickBackButton(view);
        return view;
    }

    private void onClickSendButton() {
        sendBtn.setOnClickListener(v -> {
            ContactUsRequest request = new ContactUsRequest();
            request.setName(name.getText().toString());
            request.setEmail(email.getText().toString());
            request.setComments(comments.getText().toString());
            sendDataToServer(request);
        });
    }

    private void sendDataToServer(ContactUsRequest request) {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.SendDataViaApi(request, new ContactUsSuccessResponse() {
            @Override
            public void onSuccess(ContactUsResponse response) {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });

    }

    private void addViews(View view) {
        name = view.findViewById(R.id.contact_name_edit_text);
        email = view.findViewById(R.id.contact_email_edit_text);
        insta = view.findViewById(R.id.contact_insta);
        youtube = view.findViewById(R.id.contact_browser);
        twitter = view.findViewById(R.id.contact_twitter);
        dialer = view.findViewById(R.id.contact_dialer);
        whatsapp = view.findViewById(R.id.contact_whattsApp);
        comments = view.findViewById(R.id.contact_comments_edit_text);
        sendBtn = view.findViewById(R.id.contact_button);
        imgBack = view.findViewById(R.id.img_back);

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://instagram.com/bezat.bh?igshid=11q4aefhaxp98")));
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://m.youtube.com/watch?v=PH8T9aZrRqg")));
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://twitter.com/bezatapp?s=08")));
            }
        });
            dialer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+97313399993"));
                    startActivity(intent);
                }
            });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + "+97313399993");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, "+97313399993"));
            }
        });

    }

    private void onClickBackButton(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(view1 -> getActivity().onBackPressed());
    }

}
