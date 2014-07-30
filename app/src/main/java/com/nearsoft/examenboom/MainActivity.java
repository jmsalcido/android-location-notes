package com.nearsoft.examenboom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.repository.NoteRepository;
import com.nearsoft.examenboom.database.repository.NoteRepositoryImpl;

import java.util.List;

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        initUI();
    }

    private void initUI() {
        mActionBar = getActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initDrawer();

        // add notes fragment always.
        addNotesFragment();
    }

    private void addNotesFragment() {
        NotesFragment fragment = new NotesFragment();
        addFragment(fragment);
        mActionBar.setSubtitle("Love Notes");
    }

    private void initDrawer() {
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        DrawerAdapter adapter = new DrawerAdapter(this, new String[]{"Notes List", "Notes Map"});
        drawerList.setAdapter(adapter);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, 0, 0);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addFragment(new NotesFragment());
                        mActionBar.setSubtitle("Love Notes");
                        break;
                    case 1:
                        addFragment(new NotesMapFragment());
                        mActionBar.setSubtitle("Map of Love");
                        break;
                    default:
                        throw new AssertionError("You should not be able to come here.");
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
