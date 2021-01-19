package com.taxilik.driver.home.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.taxilik.Data;
import com.taxilik.R;
import com.taxilik.User;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.client.home.offre.ClientOffreFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class DriverRequestFragment extends Fragment {

    ProgressDialog pdDialog;
    FirebaseFirestore db ;

    ListView listViewRequests ;
    Map<Integer,User> clientsMap ;
    ArrayList<User> clients ;
    RequestAdapter requestAdapter ;
    String ids = "" ;

    String URL_GET_USER = "https://omega-store.000webhostapp.com/getUsers.php";

    public DriverRequestFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_driver_request, container, false);

        listViewRequests = root.findViewById(R.id.list_requests);
        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();

        return root ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        clients = new ArrayList<>();
        clientsMap = new HashMap<>();
        requestAdapter = new RequestAdapter(getContext(),clients);
        listViewRequests.setAdapter(requestAdapter);
        pdDialog.show();
        getRequests();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        void messageFromChildFragment(Uri uri);
    }

    private void getRequests() {

        db.collection("OnlineOrder").whereEqualTo("CarID", 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                ids="";
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    int cd = Integer.parseInt(dc.getDocument().getLong("ClientID").toString());
                    switch (dc.getType()) {
                        case ADDED:
                            if(!clientsMap.containsKey(cd))ids=ids.concat(""+cd+",");
                            break;
                        case MODIFIED:
                            //Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            clients.remove(clientsMap.get(cd));
                            clientsMap.remove(cd);
                            break;
                    }
                }

                if(!ids.equals("")){
                    ids = ids.substring(0,ids.length()-1);
                    loadRequests();
                }
                else {
                    requestAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void loadRequests() {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray users = jsonObject.getJSONArray("users");

                            if(success.equals("0")){
                                for (int i=0;i<users.length();i++){
                                    JSONObject user = users.getJSONObject(i);

                                    int id = user.getInt("id") ;
                                    String firstName = user.getString("first_name");
                                    String lastName = user.getString("last_name");
                                    String phone = user.getString("phone");
                                    String image = user.getString("image");

                                    User u = new User(id,firstName,lastName,phone,image,0,"");
                                    clients.add(u);
                                    clientsMap.put(u.getId(),u);
                                }
                                requestAdapter.notifyDataSetChanged();
                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("users_ids",ids);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}