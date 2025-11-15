package com.tec.apptenis.ui.alumnos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.apptenis.R;
import com.tec.apptenis.model.Alumno;

import java.util.List;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.AlumnoViewHolder> {

    private List<Alumno> listaAlumnos;
    // 1. Declarar la interfaz para el clic
    private final OnAlumnoClickListener listener;

    // Constructor que acepta el listener
    public AlumnosAdapter(List<Alumno> listaAlumnos, OnAlumnoClickListener listener) {
        this.listaAlumnos = listaAlumnos;
        this.listener = listener;
    }

    // El constructor anterior (sin listener) debe ser eliminado o corregido:
    /* public AlumnosAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    } */

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);

        // Asignamos los campos del modelo
        holder.tvNombreAlumno.setText(alumno.getNombre());
        holder.tvTelefonoAlumno.setText("Teléfono: " + alumno.getTelefono());
        holder.tvNivelAlumno.setText("Nivel: " + alumno.getNivel());

        // 3. Implementar el listener de clic
        // Llamamos al método bind que contiene la lógica del clic
        holder.bind(alumno, listener);
    }

    @Override
    public int getItemCount() {
        return listaAlumnos != null ? listaAlumnos.size() : 0;
    }

    public void setAlumnos(List<Alumno> nuevosAlumnos) {
        this.listaAlumnos = nuevosAlumnos;
        notifyDataSetChanged();
    }

    // --- 2. Interfaz de Callback ---
    // El Fragment (AlumnosFragment) implementará esta interfaz
    public interface OnAlumnoClickListener {
        void onAlumnoClick(Alumno alumno);
    }

    // --- ViewHolder Modificado ---
    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombreAlumno;
        final TextView tvTelefonoAlumno;
        final TextView tvNivelAlumno;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlumno = itemView.findViewById(R.id.tvNombreAlumno);
            tvTelefonoAlumno = itemView.findViewById(R.id.tvTelefonoAlumno);
            tvNivelAlumno = itemView.findViewById(R.id.tvNivelAlumno);
        }

        // 4. Nuevo método 'bind' que maneja el clic
        public void bind(final Alumno alumno, final OnAlumnoClickListener listener) {

            // La asignación de texto ya se hizo en onBindViewHolder,
            // aquí solo nos enfocamos en el clic:

            itemView.setOnClickListener(v -> {
                // Verificar que el listener no sea null y ejecutar el callback
                if (listener != null) {
                    listener.onAlumnoClick(alumno);
                }
            });
        }
    }
}