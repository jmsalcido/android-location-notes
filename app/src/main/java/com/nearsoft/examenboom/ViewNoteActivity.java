package com.nearsoft.examenboom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.NotesSQLite;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

public class ViewNoteActivity extends Activity {

    private boolean mMapVisible; // the last hack

    private ActionBar mActionBar;
    private EditText mNoteTitle;
    private EditText mNoteText;
    private FrameLayout mMapLayout;

    private Location mLocation;
    private LocationManager mLocationManager;
    private String mBestLocationProvider;

    private NoteRepository mNoteRepository;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mActionBar = getActionBar();
        mNoteRepository = new NoteRepositoryImpl(this);
        if (getIntent().getExtras() != null) {
            initUI();

            int noteId = getIntent().getExtras().getInt(NotesSQLite.COLUMN_NOTE_ID);
            mNote = mNoteRepository.getNoteById(noteId);
            mNoteTitle.setText(mNote.getTitle());
            mNoteText.setText(mNote.getText());
        } else {
            finish();
        }
        obtainUserLocation();
    }

    private void initUI() {
        mActionBar.setSubtitle("Please don't");
        RelativeLayout latLong = (RelativeLayout) findViewById(R.id.new_note_lat_lon_view);
        latLong.setVisibility(View.GONE);

        mMapLayout = (FrameLayout) findViewById(R.id.map_fragment);
        mNoteTitle = (EditText) findViewById(R.id.note_title_edit_text);
        mNoteText = (EditText) findViewById(R.id.note_note_edit_text);
        mMapLayout.setVisibility(View.GONE);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateNote(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void obtainUserLocation() {
        if(mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            mBestLocationProvider = mLocationManager.getBestProvider(criteria, true);
        }

        if (checkLocationAviability()) {
            mLocation = mLocationManager.getLastKnownLocation(mBestLocationProvider);
            if (mLocation != null) {
                updateNote(mLocation);
            } else {
                mLocationManager.requestLocationUpdates(mBestLocationProvider, NewNoteActivity.MIN_TIME, NewNoteActivity.MIN_DISTANCE, mLocationListener);
            }
        }
    }

    private boolean checkLocationAviability() {
        boolean gps_enabled = false, network_enabled = false;
        if (mLocationManager == null)
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            // pos ah
        }
        try {
            network_enabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            // dunnoderino
        }

        if (!network_enabled) {
            showLocationGpsDialog("Location not Available, please allow your device to access your location",
                    "Edit",
                    "Cancel", new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            );
            return false;
        } else if (!gps_enabled) {
            showLocationGpsDialog(
                    "GPS is not available, please activate it to improve location accuracy",
                    "Edit",
                    "Cancel",
                    new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            );
        }
        return true;
    }

    private void showLocationGpsDialog(String message, String okString,
                                       String cancelString, final Intent intent) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton(okString, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent);
            }
        });

        dialog.setNegativeButton(cancelString,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface,
                                        int paramInt) {
                    }
                }
        );
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_note_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.view_note_location_action) {
            if(mMapVisible) {
                mMapLayout.setVisibility(View.GONE);
                mMapVisible = false;
            } else {
                NoteMapFragment mapFragment = new NoteMapFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(NotesSQLite.COLUMN_NOTE_ID, mNote.getId());
                mapFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
                mMapLayout.setVisibility(View.VISIBLE);
                mMapVisible = true;
            }
            return true;
        }
        if (id == R.id.view_note_update_action) {
            saveNote();
            finish();
            return true;
        }
        if (id == R.id.view_note_delete_action) {
            mNoteRepository.deleteNote(mNote);
            finish();
            return true;
        }
        if (id == R.id.view_note_cancel_action) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        RelativeLayout progressBarLayout = (RelativeLayout) findViewById(R.id.note_layout_progress_bar);
        TextView progressBarText = (TextView) findViewById(R.id.note_progress_bar_text);
        progressBarText.setText("Saving Note...");
        progressBarLayout.setVisibility(View.VISIBLE);
        updateNote(mLocation);
        mNoteRepository.updateNote(mNote);
        mNoteRepository.saveNote(mNote);
        progressBarLayout.setVisibility(View.GONE);
    }

    private void updateNote(Location location) {
        mNote.setTitle(mNoteTitle.getText().toString());
        mNote.setText(mNoteText.getText().toString());
        mNote.setLatitude(location.getLatitude());
        mNote.setLongitude(location.getLongitude());
    }
}
