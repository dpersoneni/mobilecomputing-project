package com.example.accordo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.accordo.database.AppDatabase;
import com.example.accordo.database.ImageContent;
import com.example.accordo.post.PostAdapter;
import com.example.accordo.post.PostImage;
import com.example.accordo.post.PostLocation;
import com.example.accordo.post.PostText;
import com.example.accordo.database.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ChannelActivity extends AppCompatActivity implements View.OnClickListener {
    CommunicationController cc;

    AppDatabase db;

    RecyclerView recyclerView;
    PostAdapter adapter;


    EditText insertTextPost;
    Button addTextPost;
    Button openAddImagePost;
    Button openAddLocationPost;

    LinearLayout layoutImage;
    ImageView imageToPost;
    Button addImagePost;

    LinearLayout layoutLocation;
    TextView locationToPost;
    Button addLocationPost;

    String ctitle;
    String imageString;

    //questa costante intera mi serve a capire quale permesso voglio ottenere
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;

    //per ultima posizione nota
    private FusedLocationProviderClient fusedLocationClient;

    //per aggiornamenti di posizione
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;

    private Double latitude;
    private Double longitude;

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final int MAX_IMAGE_SIZE = 137000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        cc = new CommunicationController(this);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db_users").build();

        insertTextPost = findViewById(R.id.insertTextPost);
        addTextPost = findViewById(R.id.addTextPost);
        openAddImagePost = findViewById(R.id.openAddImagePost);
        openAddLocationPost = findViewById(R.id.openAddLocationPost);
        layoutImage = findViewById(R.id.layoutImage);
        imageToPost = findViewById(R.id.imageToPost);
        addImagePost = findViewById(R.id.addImagePost);

        layoutLocation = findViewById(R.id.layoutLocation);
        locationToPost = findViewById(R.id.locationToPost);
        addLocationPost = findViewById(R.id.addLocationPost);

        layoutImage.setVisibility(View.GONE);
        layoutLocation.setVisibility(View.GONE);
        addImagePost.setVisibility(View.GONE);
        imageToPost.setVisibility(View.GONE);

        openAddImagePost.setOnClickListener(this);
        openAddLocationPost.setOnClickListener(this);
        addTextPost.setOnClickListener(this);
        addImagePost.setOnClickListener(this);
        addLocationPost.setOnClickListener(this);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                //aggiorno i dati nelle textView con le informazioni della posizione aggiornata

                for (Location location : locationResult.getLocations()) {
                    Log.d("Danilo", "New Location received: " + location.toString());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
            };
        };


        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new PostAdapter(getApplicationContext());
        Intent intent = getIntent();

        if (intent.hasExtra("ctitle")) {
            ctitle = intent.getStringExtra("ctitle");
            cc.getChannel(ctitle, this::ShowPosts, this::logError);
        }
    }


    private void ShowPosts(JSONObject response) {
        Model.getInstance().clearChannel();
        Log.d("Danilo", "" + response.toString());

        try {
            HashMap<String, String> userToCheck = new HashMap<>();
            JSONArray arrayPosts = response.getJSONArray("posts");

            for (int i = 0; i < arrayPosts.length(); i++) {
                JSONObject obj = arrayPosts.getJSONObject(i);
                userToCheck.put(obj.getString("uid"), obj.getString("pversion"));

                //        Post post = new Post(obj.getString("pid"), obj.getString("uid"), obj.getString("name"));
                switch (obj.getString("type")) {
                    case "t":
                        PostText postText = new PostText(obj.getString("pid"), obj.getString("uid"), obj.getString("name"), obj.getString("type"), obj.getString("content"));
                        Model.getInstance().addPostToModel(postText);
                        break;
                    case "i":
                        PostImage postImage = new PostImage(obj.getString("pid"), obj.getString("uid"), obj.getString("name"), obj.getString("type"));
                        Model.getInstance().addPostToModel(postImage);

                        break;
                    case "l":
                        double lat = Double.parseDouble(obj.getString("lat"));
                        double lon = Double.parseDouble(obj.getString("lon"));
                        PostLocation postLocation = new PostLocation(obj.getString("pid"), obj.getString("uid"), obj.getString("name"), obj.getString("type"), lat, lon);

                        Model.getInstance().addPostToModel(postLocation);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + obj.getString("type"));
                }
            }

            checkUserProfile(userToCheck);

        } catch (JSONException e) {
            Log.d("Danilo JSON Error", "JSON Error", e);
        }

        for (int i = 0; i < Model.getInstance().getPostsSize(); i++) {
            if (Model.getInstance().getPost(i).getType().equals("i")) {
                Log.d("DaniloImage", "Checkpoint0");
                checkImage(Model.getInstance().getPost(i).getPid(), i);
            }
        }
        recyclerView.setAdapter(adapter);
    }


    private void checkUserProfile(HashMap<String, String> users) {
        Model m = Model.getInstance();
        for (String uid : users.keySet()) {
            User u = m.getUser(uid);
            if (u != null && u.samePVersion(users.get(uid))) {
                Log.d("Danilo", "Exists in model");
                continue;
            }
            if (u != null && !(u.samePVersion(users.get(uid)))) {
                Log.d("Danilo", "pversion OLD");
                cc.getUserPicture(uid, this::setUserInDB, error -> Log.d("Danilo", error.toString()));
                continue;
            }

            if (u == null) {
                Log.d("Danilo", "user non in model");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User userDB = db.userDao().getUser(uid);
                        if (userDB == null || !userDB.samePVersion(users.get(uid))) {
                            cc.getUserPicture(uid, response -> setUserInDB(response), error -> logError(error));
                            return;
                        }
                        if (userDB.samePVersion(users.get(uid))) {
                            Model.getInstance().setUser(userDB);
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        }

                    }

                }).start();
                adapter.notifyDataSetChanged();
            }
        }
    }


    public void setUserInDB(JSONObject response) {
        try {
            User user = new User(response.getString("uid"), response.getString("pversion"), response.getString("picture"));
            Model.getInstance().setUser(user);
            //adapter

            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.userDao().insertUser(user);

                }
            }).start();

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void logError(VolleyError error) {
        Log.d("Danilo", "Error" + error.toString());
        Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_LONG).show();
    }


    private void checkImage(String pid, int i) {
        ImageContent image = Model.getInstance().getImageContent(pid);

        if (image != null) {
            Log.d("DaniloImage", "checkpoint1 presente nel model");
            Model.getInstance().setImageToPost(image.getContent(), i);
            adapter.notifyItemChanged(i);
            return;
        }

        Log.d("DaniloImage", "checkpoint1 non presente nel model");
        setImageInDB(pid, i);
    }

    private void setImageInDB(String pid, int position) {
        Log.d("DaniloImage", "set image in  db");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("DaniloImage", "entrato nel run setimageINDB");
                ImageContent imageDB = db.imageContentDao().getImageContent(pid);
                if (imageDB != null) {
                    Log.d("DaniloImage", "checkpoint2  presente nel db");

                    Model.getInstance().setImageContent(imageDB);       // prendiamo la lista dei post
                    Model.getInstance().setImageToPost(imageDB.getContent(), position); //prendiamo l'immagine e l'associamo al post
                    runOnUiThread(() -> adapter.notifyItemChanged(position));
                    return;
                }
                Log.d("DaniloImage", "checkpoint2 non presente nel db");
                cc.getPostImage(pid, response -> receivedContentImage(response, position), error -> logError(error));

            }
        }).start();

    }


    private void receivedContentImage(JSONObject response2, int finalI) {
        try {
            Log.d("DaniloImage", "ricevuta immagine");

            ImageContent image = new ImageContent(response2.getString("pid"), response2.getString("content"));
            Model.getInstance().setImageToPost(image.getContent(), finalI);
            Model.getInstance().setImageContent(image);
            adapter.notifyItemChanged(finalI);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("DaniloImage", "setto immagine nel db");

                    db.imageContentDao().insertImageContent(image);
                }
            }).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.openAddImagePost):
                Log.d("Danilo", "Click open Image");
                layoutImage.setVisibility(View.VISIBLE);
                layoutLocation.setVisibility(View.GONE);


              //  Intent intent = new Intent(Intent.ACTION_PICK);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Log.d("Danilo", "click su image update1");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
            case (R.id.openAddLocationPost):
                Log.d("Danilo", "Click open Location");
                layoutLocation.setVisibility(View.VISIBLE);
                layoutImage.setVisibility(View.GONE);

                if (checkLocationPermission()) {
                    Log.d("Danilo", "checkLocationPermission");
                    locationToPost.setText("Your position has been stored!");
                    startLocationUpdates();

                }
                break;

            case (R.id.addTextPost):
                String text = insertTextPost.getText().toString().trim();
                if (text.length() > 99) {
                    Toast.makeText(getApplicationContext(), "Error: message must be shorter than 100 letters", Toast.LENGTH_LONG).show();
                    break;
                } else if (text.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Error: message can't be blank", Toast.LENGTH_LONG).show();
                    break;
                }
                cc.addPost(ctitle, "t", text, response -> addingTextPost(), this::logError);
                Log.d("Danilo", insertTextPost.getText().toString().trim());
                break;

            case (R.id.addImagePost):
                //   openGallery.setVisibility(View.VISIBLE);
                layoutImage.setVisibility(View.GONE);
             //   addImagePost.setVisibility(View.GONE);

                cc.addPost(ctitle, "i", imageString, response -> addingImagePost(), this::logError);

                break;

            case (R.id.addLocationPost):
                layoutLocation.setVisibility(View.GONE);
               // addLocationPost.setVisibility(View.GONE);

                fusedLocationClient.removeLocationUpdates(locationCallback);
                requestingLocationUpdates = false;

                Log.d("Danilo", "location al click" + latitude + "  " + longitude);

                String latitudeToPost = latitude.toString();
                String longitudeToPost = longitude.toString();


                cc.addPost(ctitle, "l", latitudeToPost, longitudeToPost, response -> addingImagePost(), this::logError);
                break;

        }

    }


    private void addingImagePost() {
        cc.getChannel(ctitle, this::ShowPosts, this::logError);
    }


    private void addingTextPost() {
        insertTextPost.getText().clear();
        cc.getChannel(ctitle, this::ShowPosts, this::logError);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Uri imageUri = data.getData();
                Log.d("Danilo", imageUri.toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Log.d("Danilo", bitmap.toString());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (encoded.length() > MAX_IMAGE_SIZE) {

                    imageToPost.setVisibility(View.GONE);
                    addImagePost.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Error: picture is too big.", Toast.LENGTH_LONG).show();
                    return;
                }
                imageToPost.setVisibility(View.VISIBLE);
                addImagePost.setVisibility(View.VISIBLE);
                imageToPost.setImageBitmap(decodedByte);
                imageString = encoded;


                //    Log.d("Danilo - B64 img", encoded);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //ho i permessi, scrivo nella casella di testo che va tutto bene
            Log.d("Danilo", "The permission is granted");

            return true;
        } else {
            //non ho i permessi, devo richiederli
            Log.d("Danilo", "Ask for permission");

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d("Danilo", "Now the permission is granted");

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    startLocationUpdates();
                } else {
                    // permission was not granted
                    Log.d("Danilo", "Permission still not granted");

                }
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //se non voglio ottenere aggiornamenti di posizione in background, mi devo ricordare di sospenderli
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;

    }


    //per chiedere la posizione aggiornata
    private void startLocationUpdates() {
        Log.d("Danilo", "startLocationUpdates");
        if (checkLocationPermission()) {

            //inizio a chiedere gli aggiornamenti di posizione
            requestingLocationUpdates = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000); //in ms.
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        }
    }


}


// default pk.eyJ1IjoiZHBlcnNvbmVuaSIsImEiOiJja2hha3dhb2owanQ0MzFwam44MWx3M2p1In0.j2kboFTenc7rda5RURJJWQ
// token  sk.eyJ1IjoiZHBlcnNvbmVuaSIsImEiOiJja2hhbGJsM3EwbDNvMzRsaGJxcDdzcWZoIn0.KNnGaA6uCy6SUkPH1VE8EQ
// name android-mapbox