package me.mikasa.doubandemo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.mikasa.doubandemo.R;
import me.mikasa.doubandemo.base.BaseToolbarActivity;
import me.mikasa.doubandemo.fragment.MovieFragment;
import me.mikasa.doubandemo.fragment.TheaterFragment;

public class DoubanActivity extends BaseToolbarActivity {
    private DrawerLayout drawerLayout;
    private static long lastBackPressed=0;
    private TheaterFragment theaterFragment;
    private MovieFragment movieFragment;
    @Override
    protected int setLayoutResId() {
        return R.layout.activity_douban;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //mTitle.setText("豆瓣电影");
    }

    @Override
    protected void initData() {
        theaterFragment =TheaterFragment.getInstance();
        movieFragment=MovieFragment.getInstance();
    }

    @Override
    protected void initView() {
        initDrawer();
        mTitle.setText("豆瓣热映");
        showFragment(theaterFragment);//
    }

    @Override
    protected void initListener() {
        NavigationView navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_theater:
                        mTitle.setText("豆瓣热映");
                        showFragment(theaterFragment);
                        hideDrawer();
                        return true;
                    case R.id.menu_movie:
                        mTitle.setText("豆瓣电影");
                        showFragment(movieFragment);
                        hideDrawer();
                        return true;
                }
                return false;
            }
        });
    }
    private void initDrawer(){
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,
                R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
    }
    private void showFragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.container_layout,fragment);
        transaction.commit();
    }

    private void hideDrawer(){
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void onBackPressed() {
        hideDrawer();
        long current=System.currentTimeMillis();
        if ((current-lastBackPressed)>2000){
            lastBackPressed=current;
            showToast("再点击一次退出程序");
        }else {
            finish();
        }
    }

}
