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

    public AlumnosAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

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
    }

    @Override
    public int getItemCount() {
        return listaAlumnos != null ? listaAlumnos.size() : 0;
    }

    // Método para actualizar la lista de alumnos desde el ViewModel
    public void setAlumnos(List<Alumno> nuevosAlumnos) {
        this.listaAlumnos = nuevosAlumnos;
        notifyDataSetChanged();
    }

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombreAlumno;
        final TextView tvTelefonoAlumno;
        final TextView tvNivelAlumno; // Nuevo TextView

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Enlazamos los nuevos IDs del item_alumno.xml
            tvNombreAlumno = itemView.findViewById(R.id.tvNombreAlumno);
            tvTelefonoAlumno = itemView.findViewById(R.id.tvTelefonoAlumno);
            tvNivelAlumno = itemView.findViewById(R.id.tvNivelAlumno);
        }
    }
}