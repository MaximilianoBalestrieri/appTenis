package com.tec.apptenis;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; // Necesario para el método de navegación
import androidx.fragment.app.FragmentManager; // Necesario
import androidx.fragment.app.FragmentTransaction; // Necesario

import com.tec.apptenis.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    private static final int FRAGMENT_CONTAINER_ID = R.id.nav_host_fragment_content_main_activity_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainActivityMenu.toolbar);
        binding.appBarMainActivityMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configuración del encabezado de navegación
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvUsuarioLogueado);
        SharedPreferences prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String emailUsuario = prefs.getString("email", "Usuario Invitado");
        if (tvNombre != null) {
            tvNombre.setText("Usuario: " + emailUsuario);
        }

        // Configuración del Navigation Component
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_listadousuarios, R.id.nav_alumnos)
                .setOpenableLayout(drawer)
                .build();

        // Usamos el ID del NavHostFragment que definiste
        NavController navController = Navigation.findNavController(this, FRAGMENT_CONTAINER_ID);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, FRAGMENT_CONTAINER_ID);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void navigateToFragment(Fragment fragment, String title, Bundle args) {

        if (args != null) {
            fragment.setArguments(args);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplazamos el contenido en el contenedor del NavHostFragment
        fragmentTransaction.replace(FRAGMENT_CONTAINER_ID, fragment);

        // Importante: Agregar a la pila de regreso para poder usar el botón 'Atrás'
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        if (title != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}