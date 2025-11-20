package com.tec.apptenis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tec.apptenis.databinding.ActivityMainBinding;
import com.tec.apptenis.ui.login.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


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
        binding.appBarMainActivityMenu.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show();
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // --------- HEADER DEL NAV DRAWER ----------
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvUsuarioLogueado);

        SharedPreferences prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String emailUsuario = prefs.getString("email", "Usuario Invitado");
        String rolUsuario = prefs.getString("rol", "");    // <-- IMPORTANTE: LEER ROL DEL LOGIN

        if (tvNombre != null) {
            tvNombre.setText("Usuario: " + emailUsuario);
        }

        // --------- MOSTRAR / OCULTAR OPCIONES SEGÚN ROL ----------
        Menu menuNav = navigationView.getMenu();

        if (rolUsuario.equalsIgnoreCase("Alumno")) {

            // Mostrar solo lo permitido
            menuNav.findItem(R.id.nav_alumnos).setVisible(true);
            menuNav.findItem(R.id.nav_listado_clases).setVisible(true);

            // Ocultar lo demás
            menuNav.findItem(R.id.nav_listadousuarios).setVisible(false);
            menuNav.findItem(R.id.nav_crear_clase).setVisible(false);
            menuNav.findItem(R.id.nav_perfil).setVisible(false);

        }

        // --------- NAVIGATION COMPONENT ----------
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_listadousuarios,
                R.id.nav_alumnos,
                R.id.nav_listado_clases,
                R.id.nav_crear_clase)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, FRAGMENT_CONTAINER_ID);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_cerrar_sesion) {

                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Seguro que querés salir?")
                        .setPositiveButton("Aceptar", (dialog, which) -> {

                            binding.drawerLayout.closeDrawers();

                            SharedPreferences prefis = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                            prefis.edit().clear().apply();

                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            binding.drawerLayout.closeDrawers();
                            dialog.dismiss();
                        })
                        .show();

                return true;
            }

            // Cerrar drawer después de navegación normal
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                binding.drawerLayout.closeDrawers();
            }

            return handled;
        });


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

        fragmentTransaction.replace(FRAGMENT_CONTAINER_ID, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if (title != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
