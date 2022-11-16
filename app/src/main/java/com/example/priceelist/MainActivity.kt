package com.example.priceelist

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.priceelist.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbarTop)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.savedListsFragment, R.id.invoicesListFragment, R.id.receiptsListFragment,
                /*R.id.accountFragment,*/ R.id.clientsListFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.makeListFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.addClientFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.invoiceClientInfoFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.invoicePriceInfoFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.invoicePreviewFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.canvas)
                }
                R.id.previewFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.canvas)
                }
                R.id.accountFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.canvas)
                }
                R.id.helpFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.canvas)
                }
                R.id.createAccountFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                R.id.accountInformationFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                R.id.statsFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                R.id.savedClientsMenuDialog -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    ////window.statusBarColor = getColor(R.color.white)
                }
                R.id.invoiceMenuDialogFragment -> {
                    //binding.appBarMain.toolbarTop.visibility = View.GONE
                    ////window.statusBarColor = getColor(R.color.white)
                }
                R.id.previewMenuDialogFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.invoiceNewClientFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
                R.id.shareDownloadListFragment -> {
                    binding.appBarMain.toolbarTop.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.canvas)
                }
                else -> {
                    binding.appBarMain.toolbarTop.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    //window.statusBarColor = getColor(R.color.white)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}