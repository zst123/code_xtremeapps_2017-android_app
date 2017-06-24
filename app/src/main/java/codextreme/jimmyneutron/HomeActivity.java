package codextreme.jimmyneutron;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import codextreme.jimmyneutron.baseline.BaselineMapFragment;
import codextreme.jimmyneutron.interfaces.MaterialActivity;
import codextreme.jimmyneutron.sidebar.SidebarListAdapter;

import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_URL;
import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_USER;


public class HomeActivity extends MaterialActivity {

    public static final String URL_TEST =
            "https://assets.entrepreneur.com/content/16x9/822/20160118164234-interior-modern-office-desks-space-computers.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add drawer toggle (hamburger icon)
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        createDrawerToggle(mDrawerLayout);

        // Init sidebar list view
        ListView listView = (ListView) findViewById(R.id.sidebar_listview);
        SidebarListAdapter adapter = new SidebarListAdapter(this)
                .add("List-item A")
                .add("List-item B")
                .add("List-item C")
                .add("List-item D")
                .add("List-item E")
                .add("List-item F")
                .add("List-item G")
                .add("List-item H")
                .add("List-item I")
                .add("List-item J")
                .add("List-item K")
                .add("List-item L");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String itemValue = (String) listView.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(),
                    "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                    .show();
        });


        //http://www.marinabaysands.com/content/dam/singapore/marinabaysands/master/main/home/hotel/web%20redesign%20room%20images/Club%20Room/club-room-garden-view.jpg

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.

            if (savedInstanceState == null) {
                // Create a new Fragment to be placed in the activity layout
                BaselineMapFragment firstFragment = new BaselineMapFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                Bundle args = new Bundle();
                args.putString(BUNDLE_URL, URL_TEST);
                firstFragment.setArguments(args);

                // Add the fragment to the 'fragment_container' FrameLayout
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, firstFragment).commit();

            }
        }
/*
        findViewById(R.id.toggleButton2).setOnClickListener(view -> {
            Testfrag newFragment = new Testfrag();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });*/
    }



public static class Testfrag extends android.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);
        return view;
    }
}

}