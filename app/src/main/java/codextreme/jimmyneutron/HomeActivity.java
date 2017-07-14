package codextreme.jimmyneutron;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import codextreme.jimmyneutron.baseline.BaselineMapFragment;
import codextreme.jimmyneutron.interfaces.MaterialActivity;
import codextreme.jimmyneutron.sidebar.SidebarListAdapter;
import codextreme.jimmyneutron.tradbooking.CheckBookingFragment;
import codextreme.jimmyneutron.wordcloud.WordCloudFragment;

import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_BASE64_IMG;
import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_URL;
import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_USER;


public class HomeActivity extends MaterialActivity {

    public static final String URL_TEST = "https://assets.entrepreneur.com/content/16x9/822/20160118164234-interior-modern-office-desks-space-computers.jpeg";;

    BaselineMapFragment fragmentOfficeView;
    WordCloudFragment fragmentWordCloud;
    CheckBookingFragment fragmentBooking;

    public void initFragments() {
        if (fragmentBooking == null) {
            fragmentBooking = new CheckBookingFragment();
        }

        if (fragmentOfficeView == null) {
            fragmentOfficeView = new BaselineMapFragment();
            Bundle args = new Bundle();
            //args.putString(BUNDLE_URL, URL_TEST);
            args.putString(BUNDLE_BASE64_IMG, Common.URL_BASELINE);
            fragmentOfficeView.setArguments(args);
        }

        if (fragmentWordCloud == null) {
            fragmentWordCloud = new WordCloudFragment();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_home_titlebar);

        // Add drawer toggle (hamburger icon)
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        createDrawerToggle(mDrawerLayout);

        // Add button to close drawer
        final ImageButton buttonCloseDrawer = (ImageButton) findViewById(R.id.button2);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        // Init sidebar list view
        final ListView listView = (ListView) findViewById(R.id.sidebar_listview);
        SidebarListAdapter adapter = new SidebarListAdapter(this)
                .add("Office View", R.drawable.ic_desktop_mac_black_24dp)
                .add("Booking", R.drawable.ic_book_black_24dp)
                .add("Word Cloud", R.drawable.ic_cloud_black_24dp);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mDrawerLayout.closeDrawers();
                Fragment newFragment = null;
                SidebarListAdapter.SidebarListAdapterItem itemValue =
                        (SidebarListAdapter.SidebarListAdapterItem) listView.getItemAtPosition(position);
                switch (itemValue.text) {
                    case "Office View":
                        newFragment = fragmentOfficeView;
                        break;
                    case "Booking":
                        newFragment = fragmentBooking;
                        break;
                    case "Word Cloud":
                        newFragment = fragmentWordCloud;
                        break;
                    default:
                        break;
                }
                if (newFragment != null) {
                    FragmentTransaction transaction = HomeActivity.this.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    // transaction.addToBackStack(null); // Back button to previous fragment
                    transaction.commit();
                } else {
                    Toast.makeText(HomeActivity.this.getApplicationContext(),
                            "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        // init sidebar username
        final TextView tv_user = (TextView) findViewById(R.id.username);
        final TextView tv_email = (TextView) findViewById(R.id.email);
        tv_user.setText(
                "Hello, " + Common.mUsername + "!"
        );
        tv_email.setText("");

        initFragments();

        //http://www.marinabaysands.com/content/dam/singapore/marinabaysands/master/main/home/hotel/web%20redesign%20room%20images/Club%20Room/club-room-garden-view.jpg

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState == null) {
                // Add the fragment to the 'fragment_container' FrameLayout
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragmentOfficeView).commit();

            }
        }
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