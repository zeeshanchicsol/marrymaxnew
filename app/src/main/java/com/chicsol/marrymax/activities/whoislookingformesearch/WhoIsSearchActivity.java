package com.chicsol.marrymax.activities.whoislookingformesearch;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterWhoIsLookingForMe;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.modal.WebCSCWithList;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class WhoIsSearchActivity extends AppCompatActivity {

    private
    List<WebCSCWithList> dataList;
    public static JSONObject paramsa;
    private
    RecyclerView recyclerView;


    private RecyclerViewAdapterWhoIsLookingForMe
            recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_is_search);


        initialize();
        setListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {
        dataList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewWhoIsSearch);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        recyclerAdapter = new RecyclerViewAdapterWhoIsLookingForMe(new ArrayList<WebCSCWithList>(), getApplicationContext());

        recyclerView.setAdapter(recyclerAdapter);
        //  swipeRefresh.setOnRefreshListener(this);

        //  btSearch = (AppCompatButton) findViewById(R.id.ButtonMyContatsSearch);


        getSupportActionBar().setTitle("Be More Specific");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getRequest();


        Members memberSearchObj = defaultSelectionsObj;

        if (memberSearchObj != null) {
            ///  memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

            // memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
            //  memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
            // memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
            //page and type
            memberSearchObj.setPage_no(1);
            memberSearchObj.setType("");

            //Log.e("gender",memberSearchObj.getGender());

            Gson gson = new Gson();


            try {
                paramsa = new JSONObject("" + gson.toJson(memberSearchObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    private void setListeners() {

    }


    private void getRequest() {


        final ProgressDialog pDialog = new ProgressDialog(WhoIsSearchActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
      // Log.e("getLfmLists", "" + Urls.getLfmLists + " ====  ");

        JsonArrayRequest req = new JsonArrayRequest(Urls.getLfmLists,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("getLfmLists", response.toString());
                        try {


                            //Log.e("getLfmLists", "" + response.length());

                            for (int i = 0; i < response.length(); i++) {

                                WebCSCWithList cscWithList = new WebCSCWithList();

                                JSONObject jsonObj = response.getJSONObject(i);

                                //Log.e("getLfmLists", "" + jsonObj.get("name"));

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebCSC>>() {
                                }.getType();

                                List<WebCSC> objList = (List<WebCSC>) gsonc.fromJson(jsonObj.get("list").toString(), listType);

                                cscWithList.setId(jsonObj.get("id").toString());
                                cscWithList.setName(jsonObj.get("name").toString());
                                cscWithList.setList(objList);

                                dataList.add(cscWithList);

                            }
                            recyclerAdapter.addAll(dataList);

                            //Log.e("getLfmLists", "" + dataList.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_apply:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
