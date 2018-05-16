package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.interfaces.RemoveMember;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogRemoveFromSearch extends DialogFragment {

    private mTextView tvDesc;
    private JSONObject params;
    private String alias;
    private onCompleteListener mCompleteListener;

    private RemoveMember removeMember;

    // private ListView lv_mycontacts;
    public static dialogRemoveFromSearch newInstance(Members member, JSONObject params) {

        dialogRemoveFromSearch frag = new dialogRemoveFromSearch();
        Bundle args = new Bundle();
        args.putString("params", params.toString());
        args.putString("alias", member.getAlias());

        frag.setArguments(args);
        return frag;
    }


    public void setListener(RemoveMember listener) {
        removeMember = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        try {
            params = new JSONObject(mArgs.getString("params"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        alias = mArgs.getString("alias");

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            Log.e("getfragment manager", "======" + getTargetFragment());
            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_remove_from_search, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogRemoveFromSearch);
        String text = "Are you sure you want to remove  <font color=#216917>" + alias + "</font> from your search?";
        tvDesc.setText(Html.fromHtml(text));


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonRemoveFromSearch);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

             /*   try {
                  //  params.put("checkedTextView", checkedTextView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                removeMember(params);
            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnRemoveFromSearch);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogRemoveFromSearch.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void removeMember(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params Remove Search", Urls.removeMember + " ===== " + params.toString());
        //      Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removeMember, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  ", response + "");

                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {

                                //  member.set_removed_member(responseid);
                                Toast.makeText(getContext(), "User has been Removed successfully ", Toast.LENGTH_SHORT).show();
                                dialogRemoveFromSearch.this.getDialog().cancel();
                                removeMember.onRemove(true);
                                //mCompleteListener.onComplete("removed");
                                /*  MenuItem menuItem = addRemoveBlockMenu.getMenu().findItem(R.id.menu_up_remove);
                                 */

                            } /*else {
                               // member.set_removed_member(responseid);
                                Toast.makeText(getContext(), "User has been unremoved successfully ", Toast.LENGTH_SHORT).show();
                            *//*    MenuItem menuItem = addRemoveBlockMenu.getMenu().findItem(R.id.menu_up_remove);
                             *//*

                            }*/


                        } catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
                        }


                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.hide();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);
    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }

}


