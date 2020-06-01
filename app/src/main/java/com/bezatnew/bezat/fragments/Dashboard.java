package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.activities.ForgotPassword;
import com.bezatnew.bezat.activities.LoginActivity;
import com.bezatnew.bezat.adapter.SliderAdapter;
import com.bezatnew.bezat.models.DashBoardItem;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.google.android.material.tabs.TabLayout;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dashboard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    JSONArray jsonArray;
    List<String> iconName;
    RecyclerView recycle;
    ViewPager viewPager;
    TabLayout indicator;
    List<DashBoardItem> dashBoardItem;
    View rootView;
    String lang = "";
    boolean isGuestUser;
    String signOutLabel;

    public Dashboard() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        // Inflate the layout for this fragment
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
        }
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initSignOutLabel();
        initViewPager();
        setDashboardData();
        getProfile();
        return rootView;
    }

    private void initSignOutLabel() {
        if (SharedPrefs.isGuestUser(getContext())) {
            signOutLabel = getString(R.string.sign_up);
        } else {
            signOutLabel = getString(R.string.sign_out);
        }
    }

    private void initViewPager() {

        viewPager = rootView.findViewById(R.id.viewPager);
        indicator = rootView.findViewById(R.id.indicator);

        getUserBanner();
    }

    private void getUserBanner() {

        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        URLS.Companion.getUSER_BANNER(),
                        object,
                        response -> {
                            try {

                                jsonArray = response.getJSONArray("result");
                                viewPager.setAdapter(new SliderAdapter(getActivity(), jsonArray));
                                indicator.setupWithViewPager(viewPager, true);

                                Timer timer = new Timer();
                                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Log.v("error", error.getMessage() + " ");


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

    private void setDashboardData() {

        recycle = rootView.findViewById(R.id.recycle);
        dashBoardItem = dashBoardItem = new ArrayList<>();
        dashBoardItem.add(new DashBoardItem(
                R.drawable.qr_code,
                getString(R.string.get_coupon) + ""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.fav_offers,
                getString(R.string.fav_offers) + ""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.prize,
                getString(R.string.prizes) + ""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.total_coupons,
                getString(R.string.total_coupon) + ""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.partners,
                getString(R.string.partners) + ""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.winners,
                getString(R.string.winners) + ""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.feedback,
                getString(R.string.get_feedback)
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.vip_logo,
                getString(R.string.vip_offers) + ""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.logout,
                signOutLabel
        ));

        PostAdapter postAdapter = new PostAdapter(dashBoardItem, isGuestUser);

        recycle.setNestedScrollingEnabled(false);
        recycle.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen._1sdp);
        recycle.addItemDecoration(itemDecoration);
        recycle.setItemAnimator(new DefaultItemAnimator());
        if (postAdapter != null && postAdapter.getItemCount() > 0) {

            recycle.setAdapter(postAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
                                SharedPrefs.setKey(getActivity(), "userId", response.getString("userID"));
                                JSONObject userInfo = response.getJSONObject("userInfo");
                                String user_code = userInfo.getString("user_code");
                                SharedPrefs.setKey(getActivity(), "user_code", user_code);
                                String user_name = userInfo.getString("user_name");
                                SharedPrefs.setKey(getActivity(), "user_name", user_name);
                                String user_type = userInfo.getString("user_type");
                                SharedPrefs.setKey(getActivity(), "user_type", user_type);
                                String email = userInfo.getString("email");
                                SharedPrefs.setKey(getActivity(), "email", email);
                                String phone_code = userInfo.getString("phone_code");
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
                                String country = userInfo.getString("country" + lang);
                                SharedPrefs.setKey(getActivity(), "country", country);
                                String language_id = userInfo.getString("language_id");
                                SharedPrefs.setKey(getActivity(), "language_id", language_id);
                                String language_name = userInfo.getString("language_name");
                                SharedPrefs.setKey(getActivity(), "language_name", language_name);
                                String country_ar = userInfo.getString("country_ar");
                                SharedPrefs.setKey(getActivity(), "country_ar", country_ar);
                                String gender = userInfo.getString("gender");
                                SharedPrefs.setKey(getActivity(), "gender", gender);
                                String dob = userInfo.getString("dob");
                                SharedPrefs.setKey(getActivity(), "dob", dob);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

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

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < jsonArray.length() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        List<DashBoardItem> dashBoardItems;
        boolean isGuestUser;

        public PostAdapter(List<DashBoardItem> dashBoardItems, boolean isGuestUser) {
            this.dashBoardItems = dashBoardItems;
            this.isGuestUser = isGuestUser;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {

                holder.text.setText(dashBoardItems.get(position).getName() + " ");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                holder.image.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        dashBoardItems.get(position).getDrawable()));

                holder.bind(position);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return dashBoardItems.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView text;
            ImageView image;
            View view;

            public MyViewHolder(View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                image = itemView.findViewById(R.id.image);
                view = itemView;
            }

            private void bind(int position) {

                if (SharedPrefs.isGuestUser(getContext()) &&
                        (dashBoardItem.get(position).getName().equals(getString(R.string.get_coupon)) ||
                                dashBoardItem.get(position).getName().equals(getString(R.string.get_feedback)) ||
                                dashBoardItem.get(position).getName().equals(getString(R.string.fav_offers)) ||
                                dashBoardItem.get(position).getName().equals(getString(R.string.total_coupon)))) {
//                    view.setEnabled(false);
//                    view.setClickable(false);
                    image.setAlpha(0.5f);
                    text.setAlpha(0.5f);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.total_coupon))) {
                                toastMsg(view);

                                getActivity().finish();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.fav_offers))) {
                                toastMsg(view);

                                getActivity().finish();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.get_feedback))) {
                                toastMsg(view);

                                getActivity().finish();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.get_coupon))) {
                                toastMsg(view);
                                getActivity().finish();
                            }
                        }
                    });

                } else {

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.sign_out))) {
                                new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                                        .setMessage(getActivity().getString(R.string.logout_confirm))

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(getString(R.string.yes_label), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                ClientRetrofit retrofit = new ClientRetrofit();
                                                retrofit.logOutAPi(SharedPrefs.getKey(getActivity(), "userId"));
                                                SharedPrefs.deleteSharedPrefs(getActivity());
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                                getActivity().finish();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(R.string.no_label, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.sign_up))) {
                                new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                                        .setMessage(getString(R.string.you_will_take_to_login_screen))
                                        .setPositiveButton(R.string.yes_label, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().finish();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(R.string.no_label, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.total_coupon))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new TotalCoupon());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.prizes))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new Prizes());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.winners))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new BezatWinner());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.fav_offers))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new FavouriteOffer());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.get_coupon))) {

                                if (SharedPrefs.getKey(getActivity(), "user_code").equals("")) {
                                    Toast.makeText(rootView.getContext(), getString(R.string.no_coupons_at_this_moment),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new GetCoupon());
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }
                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.partners))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.container, new Partners());
                                ft.replace(R.id.container, new SearchRetailer());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.vip_offers))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new VIPOffer());
                                ft.addToBackStack(null);
                                ft.commit();
                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.my_profile))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new MyProfile());
                                ft.addToBackStack(null);
                                ft.commit();

                            } else if (dashBoardItems.get(getAdapterPosition())
                                    .getName().equalsIgnoreCase(getString(R.string.get_feedback))) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new Feedback());
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        }
                    });
                }

            }
        }
    }

    private void toastMsg(View view) {
        Toast.makeText(view.getContext(), getString(R.string.sign_in_to_access_this_action),
                Toast.LENGTH_LONG).show();
    }


}
