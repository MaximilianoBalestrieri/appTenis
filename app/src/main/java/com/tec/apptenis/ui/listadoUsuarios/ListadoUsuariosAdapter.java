package com.tec.apptenis.ui.listadoUsuarios; // Ajusta el paquete si es necesario

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.apptenis.R;
import com.tec.apptenis.model.Usuario; // Asegúrate de que este paquete sea correcto

import java.util.List;

public class ListadoUsuariosAdapter extends RecyclerView.Adapter<ListadoUsuariosAdapter.ViewHolder> {

    private final Context context;
    private List<Usuario> listaUsuarios;

    public ListadoUsuariosAdapter(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        // Asignamos los datos a los TextViews
        holder.tvEmailUsuario.setText(usuario.getEmail());
        holder.tvRolUsuario.setText("Rol: " + usuario.getRol());

        // Aquí podrías agregar un listener si el usuario hiciera click en la fila
        // holder.itemView.setOnClickListener(...);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    // Método para actualizar la lista de usuarios desde el ViewModel
    public void setUsuarios(List<Usuario> nuevosUsuarios) {
        this.listaUsuarios = nuevosUsuarios;
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvEmailUsuario;
        final TextView tvRolUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmailUsuario = itemView.findViewById(R.id.tvEmailUsuario);
            tvRolUsuario = itemView.findViewById(R.id.tvRolUsuario);
        }
    }
}