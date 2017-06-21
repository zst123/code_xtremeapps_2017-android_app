package codextreme.jimmyneutron.interfaces;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

// https://stackoverflow.com/questions/35639454/method-setdrawerlistener-is-deprecated#comment59827411_35643506

public class MaterialActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSupportActionBarHeight;

    protected ActionBarDrawerToggle createDrawerToggle(DrawerLayout mDrawerLayout){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                android.R.string.yes, android.R.string.no);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        return mDrawerToggle;
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mSupportActionBarHeight = toolbar.getMinimumHeight();
    }

    public int getSupportActionBarHeight() {
        return mSupportActionBarHeight;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            //http://stackoverflow.com/questions/19889436/open-navigation-drawer-by-clicking-the-app-icon
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //https://guides.codepath.com/android/Fragment-Navigation-Drawer
    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);

    }
}