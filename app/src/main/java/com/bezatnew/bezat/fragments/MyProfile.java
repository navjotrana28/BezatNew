package com.bezatnew.bezat.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.bezatnew.bezat.utils.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfile extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String encoded = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    private OnFragmentInteractionListener mListener;
    ImageView imgProfile;
    EditText etName, etEmail, etAddress;
    Loader loader;
    TextView etCountry, etDob, etGender,etPhone;
    TextView txtSave;
    String phone_code;
    String lang = "";
    private int mYear, mMonth, mDay;
    PostAdapter postAdapter;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    public MyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfile newInstance(String param1, String param2) {
        MyProfile fragment = new MyProfile();
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
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
        }
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fargment_profile, container, false);

        if (SharedPrefs.isGuestUser(getActivity().getBaseContext())) {
            toastMsgHere();
            return rootView;
        }

        loader = new Loader(getContext());
        imgProfile = rootView.findViewById(R.id.imgProfile);
        etName = rootView.findViewById(R.id.etName);
        etName.setGravity(Gravity.START);
        etEmail = rootView.findViewById(R.id.etEmail);
        etCountry = rootView.findViewById(R.id.etCountry);
        etAddress = rootView.findViewById(R.id.etAddress);
        etGender = rootView.findViewById(R.id.etGender);
        etDob = rootView.findViewById(R.id.etDob);
        etPhone = rootView.findViewById(R.id.etPhone);
        txtSave = rootView.findViewById(R.id.txtSave);
        getProfile();
        ConstraintLayout editButtonLayout = rootView.findViewById(R.id.edit_profile);
        etDob.setOnClickListener(this);
        etCountry.setOnClickListener(this);


        if (SharedPrefs.getKey(getActivity(), "gender").equalsIgnoreCase("Male")) {
            imgProfile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.profile));
            imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imgProfile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_femaleicon));
            imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        etGender.setOnClickListener(this);
        etPhone.setOnClickListener(this);
        txtSave.setOnClickListener(this);

        loader = new Loader(getContext());

        getCountryList();
//        loader= new Loader(getContext());
//        loader.show();
//        getProfile();

        onCLickEditImage(editButtonLayout);
        return rootView;
    }

    private void toastMsgHere() {
        Toast.makeText(getActivity().getBaseContext(), getString(R.string.sign_in_to_access_this_action),
                Toast.LENGTH_LONG).show();
    }


    private void onCLickEditImage(ConstraintLayout editButtonLayout) {
        editButtonLayout.setOnClickListener(v -> checkPermission());

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            checkPermission();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void selectImage() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("Select Option:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                SharedPrefs.setKey(getActivity(), "image", "data:image/jpeg;base64," + encoded);
                Log.e("Activity", "Pick from Camera::>>> ");
                imgProfile.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Bitmap bitmap1 = rotateImageIfRequired(getContext(), bitmap, selectedImage);
                imgProfile.setImageBitmap(bitmap1);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                SharedPrefs.setKey(getActivity(), "image", "data:image/jpeg;base64," + encoded);
                Log.e("Activity", "Pick from Gallery::>>> ");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            ei = new ExifInterface(input);
        } else {
            ei = new ExifInterface(selectedImage.getPath());
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private void getCountryList() {
        loader.show();
        JSONObject object = new JSONObject();
        String Url = URLS.Companion.getGET_COUNTRY();

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                postAdapter = new PostAdapter(response.getJSONArray("result"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            loader.dismiss();
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("apikey", "12345678");
                        return headers;
                    }
                };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getProfile() {

        JSONObject object = new JSONObject();
        String vipUrl = URLS.Companion.getUSER_PROFILE()
                + "userId=" + SharedPrefs.getKey(getActivity(), "userId");
        Log.v("profile", vipUrl + " ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        vipUrl,
                        object,
                        response -> {
                            Log.v("NotificationResponse", response + " ");
                            try {
                                JSONObject userInfo = response.getJSONObject("userInfo");
                                String user_code = userInfo.getString("user_code");
                                SharedPrefs.setKey(getActivity(), "user_code", user_code);
                                String user_name = userInfo.getString("user_name");
                                SharedPrefs.setKey(getActivity(), "user_name", user_name);
                                String user_type = userInfo.getString("user_type");
                                SharedPrefs.setKey(getActivity(), "user_type", user_type);
                                String email = userInfo.getString("email");
                                SharedPrefs.setKey(getActivity(), "email", email);
                                phone_code = userInfo.getString("phone_code");
                                SharedPrefs.setKey(getActivity(), "phone_code", phone_code);
                                String phone = userInfo.getString("phone");
                                SharedPrefs.setKey(getActivity(), "phone", phone);
                                String push_notification_status = userInfo.getString("push_notification_status");
                                SharedPrefs.setKey(getActivity(), "push_notification_status", push_notification_status);
                                String image = userInfo.getString("image");
                                SharedPrefs.setKey(getActivity(), "image", image);
                                String address = userInfo.getString("address");
                                SharedPrefs.setKey(getActivity(), "address", address);
                                String country_id = userInfo.getString("country_id");
                                SharedPrefs.setKey(getActivity(), "country_id", country_id);
                                String country = userInfo.getString("country");
                                SharedPrefs.setKey(getActivity(), "country", country);
                                String language_id = userInfo.getString("language_id");
                                SharedPrefs.setKey(getActivity(), "language_id", language_id);
                                String language_name = userInfo.getString("language_name");
                                SharedPrefs.setKey(getActivity(), "language_name", language_name);
                                String gender = userInfo.getString("gender");
                                SharedPrefs.setKey(getActivity(), "gender", gender);
                                String dob = userInfo.getString("dob");
                                SharedPrefs.setKey(getActivity(), "dob", dob);
                                phone_code = SharedPrefs.getKey(getActivity(), "phone_code");
                                etName.setText(SharedPrefs.getKey(getActivity(), "user_name"));
                                etEmail.setText(SharedPrefs.getKey(getActivity(), "email"));
                                etCountry.setText(SharedPrefs.getKey(getActivity(), "country" + lang));
                                etAddress.setText(SharedPrefs.getKey(getActivity(), "address"));
                                etGender.setText(SharedPrefs.getKey(getActivity(), "gender"));
                                etDob.setText(SharedPrefs.getKey(getActivity(), "dob"));
                                etPhone.setText(SharedPrefs.getKey(getActivity(), "phone"));
                                String path = SharedPrefs.getKey(getActivity(), "image");
                                if (path.equals("")) {
                                    Picasso.get().load(R.drawable.maleicon).into(imgProfile);

                                } else {
                                    Picasso.get().load(path).into(imgProfile);
                                }
                                SharedPrefs.setKey(getActivity(), "image", "");
                                loader.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            loader.dismiss();
                            Log.v("NotificationError", error.toString() + " ");
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("apikey", "12345678");
                        return headers;
                    }
                };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
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
        if (view.getId() == R.id.txtSave) {
            loader.show();
            String image = SharedPrefs.getKey(getActivity(), "image").replaceAll
                    ("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
            updateProfile(SharedPrefs.getKey(getActivity(), "userId"),
                    etName.getText().toString(),
                    phone_code,
                    etPhone.getText().toString(),
                    etEmail.getText().toString(),
                    etGender.getText().toString(),
                    etDob.getText().toString(),
                    SharedPrefs.getKey(getActivity(), "country_id"),
                    image
            );
        }
        if (view.getId()==R.id.etPhone){
            etPhone.setRawInputType(InputType.TYPE_NULL);
            etPhone.setFocusable(false);

            openPhoneDialog();
            etPhone.setInputType(InputType.TYPE_NULL);
        }
        if (view.getId() == R.id.etDob) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.datepicker,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if ((monthOfYear + 1) < 10) {
                                etDob.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
                            } else if (dayOfMonth < 10) {
                                etDob.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
                            } else {
                                etDob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }

                        }
                    }, mYear, mMonth, mDay);
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.YEAR,-17);
            datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
            datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            datePickerDialog.show();
        }
        if (view.getId() == R.id.etGender) {
            openDialog();
//            if (etGender.getText().equals("Female") || etGender.getText().equals("Male")) {
//                if (ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toLanguageTags().equals("ar")) {
//                    Toast.makeText(getActivity(), "يرجى الاتصال بالمسؤول لتغيير جنسك", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(), "Contact admin to change your gender",
//                            Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog);
//                dialog.setContentView(R.layout.gender_dialog);
//                dialog.show();
//                TextView txtFemale = (TextView) dialog.findViewById(R.id.txtFemale);
//                TextView txtMale = (TextView) dialog.findViewById(R.id.txtMale);
//                txtFemale.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        etGender.setText("Female");
//                        dialog.dismiss();
//                    }
//                });
//                txtMale.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        etGender.setText("Male");
//                        dialog.dismiss();
//                    }
//                });
//            }

        }
        if (view.getId() == R.id.etCountry) {
            countryDialog = new Dialog(getActivity());
            countryDialog.setContentView(R.layout.country_dialog);
            countryDialog.show();
            RecyclerView recCountry = countryDialog.findViewById(R.id.recCountry);
            StaggeredGridLayoutManager layoutManager =
                    new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recCountry.setLayoutManager(layoutManager);
            recCountry.setItemAnimator(new DefaultItemAnimator());
            if (postAdapter != null && postAdapter.getItemCount() > 0) {
                recCountry.setAdapter(postAdapter);
            }
        }
    }

    private void openPhoneDialog() {
        PhoneDialog phoneDialog=new PhoneDialog();
        phoneDialog.show(getFragmentManager(),"any");
    }

    private void openDialog() {
        GenderDialog genderDialog = new GenderDialog();
        genderDialog.show(getFragmentManager(), "any");
    }

    Dialog countryDialog;

    private void updateProfile(String userId, String user_name,
                               String phone_code, String phone,
                               String email, String gender,
                               String dob, String country_id, String image) {

        String saveddata = "{" + "\"phone_code\":" + "\"" + phone_code + "\","
                + "\"user_name\":" + "\"" + user_name + "\"," +
                "\"email\":" + "\"" + email + "\"," +
                "\"dob\":" + "\"" + dob + "\"," +
                "\"phone\":" + "\"" + phone + "\"," +
                "\"country_id\":" + "\"" + country_id + "\"," +
                "\"userId\":" + "\"" + userId + "\"," +
                "\"gender\":" + "\"" + gender + "\"," +
                "\"image\":" + "\"" + image + "\"" + "}";
        Log.v("savedData", saveddata + "");


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getPROFILE_Edit(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                Toast toast = Toast.makeText(getContext(), "Your profile has been updated successfully", Toast.LENGTH_SHORT);
//the default toast view group is a relativelayout
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(14);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.v("response", response.data + "");
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("phone", phone);
//                params.put("addres", addres);
//                params.put("gender", gender);
//                params.put("dob", dob);
//                params.put("country_id", country_id);
//                System.out.println("object" + params + " ");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return saveddata == null ? null : saveddata.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "12345678");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(volleyMultipartRequest);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.txtCountry.setText(jsonArray.getJSONObject(position).getString("name"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtCountry;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtCountry = itemView.findViewById(R.id.txtCountry);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            phone_code = jsonArray.getJSONObject(getAdapterPosition()).getString("phone_code");
                            etCountry.setText(jsonArray.getJSONObject(getAdapterPosition()).getString("name"));
                            SharedPrefs.setKey(getActivity(), "country_id",
                                    jsonArray.getJSONObject(getAdapterPosition()).getString("country_id"));
                            countryDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }
}
