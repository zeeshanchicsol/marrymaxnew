/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mRequestPermission;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterRequestPermissions extends RecyclerView.Adapter<RecyclerViewAdapterRequestPermissions.ViewHolder> {
    public OnUpdateListener onUpdateListener;
    Context context;
    String userPath;
    boolean permissioncheck;
    private List<mRequestPermission> items;
    Activity activity;
    String alias;
    Members members;
//    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapterRequestPermissions(List<mRequestPermission> items, final Context context, boolean permissioncheck, String userPath, Activity activity, OnUpdateListener onUpdateListener, String alias, Members members) {
        this.items = items;
        this.context = context;
        this.permissioncheck = permissioncheck;
        this.userPath = userPath;
        this.activity = activity;
        this.onUpdateListener = onUpdateListener;
        this.alias = alias;
        this.members = members;

    }

    public interface OnUpdateListener {

        void onUpdate(String msg);
    }
/*    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_request_permission, parent, false);
        return new ViewHolder(v);
        //  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final mRequestPermission obj = items.get(position);


        if (permissioncheck) {
            if (position == 0) {
                if (members.get_image_count() == 0 && obj.getName().equals("PRIVATE")) {

                    /// holder.image.setImageResource(R.drawable.ic_photo_rp_large_black_24dp);

                    obj.setType("Upload Pictures");
                    obj.setPrivilege_type_id(1);
                    if (members.get_photo_upload_request_id() > 0) {

                        obj.setRequest_id(members.get_photo_upload_request_id());
                        obj.setMember_status(1);
                        obj.setSelf(1);


                    }
                }
            }
        }

        holder.name.setText(obj.getName());
        holder.type.setText(obj.getType());
        //   Log.e("Name", "" + obj.getName());

        if (obj.getName().equals("PUBLIC")) {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorDefaultGreen));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorTextRed));

        }

        holder.actionSwitch.setChecked(false);


       /* if (obj.getName().equals("PUBLIC")) {
            holder.status.setText("Available to View");
            holder.actionSwitch.setVisibility(View.GONE);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorDefaultGreen));


        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorTextRed));
            if (obj.request_id == 0) {
                holder.actionSwitch.setChecked(false);

                if (permissioncheck) {
                    holder.status.setText("Give Permission");
                } else {
                    holder.status.setText("Ask for Permission");
                }


            } else {
                holder.actionSwitch.setChecked(true);
                holder.status.setText("Deny");

            }


        }*/


//================================================================
        holder.llNormal.setVisibility(View.VISIBLE);
        holder.llAcceptDecline.setVisibility(View.GONE);
        holder.llDenyPermission.setVisibility(View.GONE);
        holder.llWithdrawRequest.setVisibility(View.GONE);






        if (!permissioncheck) {

            if (SharedPreferenceManager.getUserObject(context).get_member_status() != 4) {
                holder.actionSwitch.setEnabled(false);
            } else {
                holder.actionSwitch.setEnabled(true);
            }


            if (obj.getName().equals("PUBLIC")) {
                holder.status.setText("Available to View");
                holder.actionSwitch.setVisibility(View.GONE);
            } else if (!obj.getName().equals("PUBLIC")) {
                if (obj.getMember_status() == 0) {
                    holder.status.setText("Grant Permission");
                    holder.actionSwitch.setVisibility(View.VISIBLE);
                } else if (obj.getMember_status() == 1) {
                    if (obj.getSelf() == 1) {
                        // holder.status.setText("Deny Permisssion");

                        holder.llNormal.setVisibility(View.GONE);

                        holder.llDenyPermission.setVisibility(View.VISIBLE);


                    } else if (obj.getSelf() == 0) {

                        holder.llNormal.setVisibility(View.GONE);
                        holder.llAcceptDecline.setVisibility(View.VISIBLE);

                        holder.status.setText("Accept / Decline");
                    }
                } else if (obj.getMember_status() == 2) {
                    if (obj.getSelf() == 1) {

                        //  holder.status.setText("Deny Permisssion");


                        holder.llNormal.setVisibility(View.GONE);

                        holder.llDenyPermission.setVisibility(View.VISIBLE);

                    } else if (obj.getSelf() == 0) {

                        holder.status.setText("Granted");
                    }
                }
            }

        } else {
//ask for permissions


//0 check

            //   else {


            if (obj.getName().equals("PUBLIC")) {
                holder.status.setText("Available to View");
                holder.actionSwitch.setVisibility(View.GONE);
                holder.name.setTextColor(context.getResources().getColor(R.color.colorDefaultGreen));
            } else if (!obj.getName().equals("PUBLIC")) {
                if (obj.getMember_status() == 0) {


                    if (obj.getPrivilege_type_id() == 1) {
                        holder.status.setText("Ask for Pictures");
                    } else {
                        holder.status.setText("Ask for Permission");
                    }
                } else if (obj.getMember_status() == 1) {
                    if (obj.getSelf() == 1) {

                        //   holder.status.setText("Withdraw Request");
                        holder.llNormal.setVisibility(View.GONE);
                        holder.llWithdrawRequest.setVisibility(View.VISIBLE);

                    } else if (obj.getSelf() == 0) {

                        holder.status.setText("Ask for Permission");
                    }
                } else if (obj.getMember_status() == 2) {
                    if (obj.getSelf() == 0) {

                        holder.status.setText("Permission Granted");
                        holder.actionSwitch.setVisibility(View.GONE);
                    }
                }
            }


        }


        if (obj.privilege_type_id == 2) {
            holder.image.setImageResource(R.drawable.ic_photo_rp_large_black_24dp);

        } else if (obj.privilege_type_id == 3) {

            holder.image.setImageResource(R.drawable.ic_profile_rp_black_24dp);


        } else if (obj.privilege_type_id == 5) {
            holder.image.setImageResource(R.drawable.ic_phone_rp_black_24dp);
        }


        holder.btAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "";
                if (obj.privilege_type_id == 2) {
                    type = "2";
                } else if (obj.privilege_type_id == 3) {
                    type = "3";
                } else if (obj.privilege_type_id == 5) {
                    type = "5";
                }


                JSONObject params = new JSONObject();
                try {

                    params.put("request_response_id", "3");
                    params.put("request_id", obj.getRequest_id());
                    params.put("type", type);
                    params.put("userpath", userPath);
                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("params", Urls.responseInterest + "   " + params);
                responseOnInterest(params);


            }
        });
        holder.btDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "";
                if (obj.privilege_type_id == 2) {
                    type = "2";
                } else if (obj.privilege_type_id == 3) {
                    type = "3";
                } else if (obj.privilege_type_id == 5) {
                    type = "5";
                }


                JSONObject params = new JSONObject();
                try {

                    params.put("request_response_id", "4");
                    params.put("request_id", obj.getRequest_id());
                    params.put("type", type);
                    params.put("userpath", userPath);
                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("params", Urls.responseInterest + "  " + params);
                responseOnInterest(params);


            }
        });


        holder.btDenyPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject params = new JSONObject();
                try {

                    params.put("request_id", obj.getRequest_id());
                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("params", "" + params);
                withdrawPermisssion(params);


            }
        });


        holder.actionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String type = "";

                if (obj.privilege_type_id == 1) {

                    type = "1";
                } else if (obj.privilege_type_id == 2) {
                    type = "2";
                } else if (obj.privilege_type_id == 3) {
                    type = "3";
                } else if (obj.privilege_type_id == 5) {
                    type = "5";
                }


                if (isChecked) {

                    if (!permissioncheck) {
//requests
                        JSONObject params = new JSONObject();
                        try {


                            params.put("privilege_type_id", obj.getPrivilege_type_id());
                            params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
                            params.put("userpath", userPath);
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                            params.put("status", SharedPreferenceManager.getUserObject(context).get_member_status());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("params", "" + params);
                        grantPermisssion(params, buttonView);

                    } else {
                        JSONObject params = new JSONObject();
                        try {

                            params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
                            params.put("type", type);
                            params.put("userpath", userPath);
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        submitRequest(params, buttonView, obj);
                    }


                }

            }
        });

        holder.btWithdrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject params = new JSONObject();
                try {
                    params.put("alias", alias);
                    params.put("interested_id", obj.getRequest_id());
                    params.put("userpath", userPath);
                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("params", "" + params);
                withdrawInterest(params, obj);
            }
        });


        holder.itemView.setTag(obj);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name, type, status;
        public SwitchCompat actionSwitch;
        public LinearLayout llAcceptDecline, llDenyPermission, llNormal, llWithdrawRequest;
        private Button btAcept, btDeny, btDenyPermission, btWithdrawRequest;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ImageViewRequestPermissionItemImage);
            name = (TextView) itemView.findViewById(R.id.TextViewRequestPermissionItemName);
            type = (TextView) itemView.findViewById(R.id.TextViewRequestPermissionItemType);
            actionSwitch = (SwitchCompat) itemView.findViewById(R.id.SwitchCompatRequestPermissionItemImage);
            status = (TextView) itemView.findViewById(R.id.TextViewRequestPermissionItemStatus);

            llAcceptDecline = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRequestPermissionAcceptDecline);

            llNormal = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRequestPermissionNormal);

            llDenyPermission = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRequestPermissionDenyPermission);

            llWithdrawRequest = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRequestPermissionWithdrawRequest);


            btAcept = (Button) itemView.findViewById(R.id.ButtonDialogRequestAccept);

            btWithdrawRequest = (Button) itemView.findViewById(R.id.ButtonDialogRequestPermissionWithDrawRequest);

            btDeny = (Button) itemView.findViewById(R.id.ButtonDialogRequestDeny);
            btDenyPermission = (Button) itemView.findViewById(R.id.ButtonDialogRequestDenyPermission);

        }
    }

    public void addAll(List<mRequestPermission> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

    }



   /* JSONObject params = new JSONObject();
                                                        try {

        params.put("userpath", member.getUserpath());
        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
        params.put("interested_id", member.get_photo_upload_request_id());
    } catch (JSONException e) {
        e.printStackTrace();
    }
    desc = "Are you sure you are not interested in viewing photos of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";

    withdrawInterest(params, "Withdraw Photo Request", desc);


*/


    private void withdrawInterest(JSONObject params, final mRequestPermission obj) {

        // Log.e("www params", params.toString() + "");

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.withdrawInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  wwww interest", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {
                                Toast.makeText(context, "Withdraw request has been completed successfully", Toast.LENGTH_LONG).show();
                                onUpdateListener.onUpdate("");

                                if (obj.getPrivilege_type_id() == 1) {
                                    members.set_photo_upload_request_id(0);

                                }


                            } else {
                                onUpdateListener.onUpdate("");

                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();

                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Error. Try Again", Toast.LENGTH_SHORT).show();

                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }

    private void responseOnInterest(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.responseInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  responseOnInterest", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                onUpdateListener.onUpdate("");
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }

    private void withdrawPermisssion(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.e("withdrawPermisssion", Urls.withdrawPermission + "=======================" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.withdrawPermission, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  update interest", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {
                                Toast.makeText(context, "Withdraw permission is done successfully", Toast.LENGTH_SHORT).show();
                                onUpdateListener.onUpdate("");
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }

    private void grantPermisssion(JSONObject params, final CompoundButton compoundButton) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.e("g permission", Urls.grantPermission + "=======================" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.grantPermission, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response grant   ", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid >= 1) {
                                Toast.makeText(context, "Permission has been granted successfully", Toast.LENGTH_SHORT).show();

                            }
                            onUpdateListener.onUpdate("");

                        } catch (JSONException e) {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }


    private void submitRequest(JSONObject params, final CompoundButton compoundButton, final mRequestPermission obj) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //    Log.e("params", "" + params);
        //   Log.e("url", "" + Urls.submitRequest);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.submitRequest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  update interest", response + "");

                        try {
                            int responseid = response.getInt("id");


                          /*  if (responseid == 1) {

                                onUpdateListener.onUpdate("");
                            } else */

                            if (responseid == -1) {

                                compoundButton.setChecked(false);

                                String desctxt = "";

                                if (SharedPreferenceManager.getUserObject(context).get_member_status() == 3) {

                                    desctxt = "<ul><li> Daily sent limit is reached.</li>" +
                                            "<br><li> Please wait 24 hours before you can contact new members.</li>" +
                                            "</ul>";


                                } else if (SharedPreferenceManager.getUserObject(context).get_member_status() == 4) {

                                    desctxt = "<ul><li>You have reached the contact limit.</li>" +
                                            "<br><li>Please wait 24 hours to send new request.</li>" +
                                            "</ul>";


                                }

                                if (Build.VERSION.SDK_INT >= 24) {
                                    // for 24 api and more

                                    Toast.makeText(context, Html.fromHtml(desctxt, Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(context, Html.fromHtml(desctxt), Toast.LENGTH_LONG).show();

                                }


                            } else {
                                if (obj.getPrivilege_type_id() == 1) {
                                    if (responseid > 0) {
                                        members.set_photo_upload_request_id(responseid);
                                    }

                                }
                                compoundButton.setChecked(false);
                                onUpdateListener.onUpdate("");
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }


}
