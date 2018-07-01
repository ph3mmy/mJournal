package com.oluwafemi.mjournal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oluwafemi.mjournal.R;
import com.oluwafemi.mjournal.databinding.ActivityAddJournalBinding;
import com.oluwafemi.mjournal.helper.PrefUtils;
import com.oluwafemi.mjournal.helper.UIUtils;
import com.oluwafemi.mjournal.model.Journal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.oluwafemi.mjournal.helper.Constants.DB_JOURNALS;


/**
 * Class gets user location, date and time
 */

public class AddJournalActivity extends AppCompatActivity {

    private static final String TAG = "AddJournalActivity";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    ActivityAddJournalBinding binding;
    String locationStr;
    double locationLat, locationLong;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        UIUtils.setupToolbar(this, getString(R.string.add_new_journal));

        setUpJournalTime();
        checkLocationPermission();
        setupJournalLocation();

        // set onclick to location edittext to enable editting
        binding.tieLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditLocationDialog();
            }
        });

    }

    /**
     * A custom alertDialog to edit location
     */
    private void showEditLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Location");
        final EditText editText = new EditText(this);
        editText.setText(locationStr);
        builder.setView(editText);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(R.string.change_location, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newLocation = editText.getText().toString();
                if (TextUtils.isEmpty(newLocation)) {
                    editText.setError("Invalid Location");
                } else {
                    locationStr = newLocation;
                    binding.tieLocation.setText(locationStr);
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        if (!TextUtils.isEmpty(locationStr)) { // Edit alertDialog should ONLY be shown if Location is valid
            alertDialog.show();
        }
    }

    @SuppressLint("MissingPermission")
    private void setupJournalLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            locationLat = location.getLatitude();
                            locationLong = location.getLongitude();
                            new LocationAddressAsyncTask(getApplicationContext()).execute(location);
//                            locationStr = location.getProvider();
                            Log.e(TAG, "onSuccess: gotten lat == " + locationLat + " longi == " + locationLong );
                        }
                    }
                });
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddJournalActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } /*else {
            return ;
        }

        setupJournalLocation();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    setupJournalLocation();
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Snackbar.make(binding.getRoot(), "Location Permission denied, No location will be added to your Journal", Snackbar.LENGTH_SHORT).show();
                locationLat = 0;
                locationLong = 0;
                locationStr = "Not Available";

            }
            return;
        }

    }

    /**
     * gets current system time, format it to date and set to textview
     */
    private void setUpJournalTime() {
        long timeInMillis = System.currentTimeMillis();
        String formatedDate = UIUtils.formatDateNoTime(timeInMillis);
        binding.tvDateTime.setText(formatedDate);
    }

    /**
     * set toolbar title and back arrow
     *//*
    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_new_journal);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                validateForm();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void validateForm() {

        String title = binding.tieJournalTitle.getText().toString();
        String text = binding.tieJournalText.getText().toString();

        if (TextUtils.isEmpty(text)) {
            binding.tieJournalText.setError("Text is required");
            Toast.makeText(this, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (TextUtils.isEmpty(title)) {
                title = null;
            }
            long timeInLong = System.currentTimeMillis();
            String userId = PrefUtils.getLoggedUserId(this);

            Journal journal = new Journal();
            journal.setTitle(title);
            journal.setText(text);
            journal.setLocationText(locationStr);
            journal.setLatitude(locationLat);
            journal.setLongitude(locationLong);
            journal.setTime(timeInLong);
            Log.e(TAG, "validateForm: user od = "+userId );
            journal.setCreatorId(userId);

            addJournalToFirebase(journal);
        }

    }

    private void addJournalToFirebase(Journal journal) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference journalRef = database.getReference(DB_JOURNALS);

        String key = journalRef.push().getKey();
        Log.e(TAG, "addJournalToFirebase: fireKey == " + key );
        if (key != null) {
            journal.setId(key);
            journalRef.child(key).setValue(journal);
            finish();
                    /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddJournalActivity.this, R.string.journal_saved, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });*/
        }
    }

    class LocationAddressAsyncTask extends AsyncTask<Location, Void, String> {

        private Context mContext;

        public LocationAddressAsyncTask(Context applicationContext) {
            this.mContext = applicationContext;
        }

        @Override
        protected String doInBackground(Location... locations) {

            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            Location location = locations[0];
            List<Address> addresses = null;
            String resultStr = "";

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                resultStr = e.getMessage();
                e.printStackTrace();
            }

            if (addresses == null || addresses.size() == 0) {
                resultStr = getString(R.string.no_address_found);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }
                resultStr = TextUtils.join("\n", addressParts);
            }
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            locationStr = s;
            binding.tieLocation.setText(locationStr);
            super.onPostExecute(s);
        }
    }
}
