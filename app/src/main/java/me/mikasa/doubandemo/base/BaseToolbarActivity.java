package me.mikasa.doubandemo.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.mikasa.doubandemo.R;


/**
 * Created by mikasacos on 2018/9/7.
 */

public abstract class BaseToolbarActivity extends BaseActivity {
    protected Toolbar mToolbar;//toolbar可继承
    protected TextView mTitle;//title可继承

    @Override
    public void setContentView(@LayoutRes int layoutResID) {//setContentView()
        super.setContentView(layoutResID);
        initToolbar();
    }

    protected void initToolbar(){
        mToolbar=findViewById(R.id.toolbar_include);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        mTitle=findViewById(R.id.toolbar_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
