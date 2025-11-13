package com.tec.apptenis.ui.listadoUsuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.apptenis.R;
import com.tec.apptenis.model.Usuario;

import java.util.List;

public class ListadoUsuariosAdapter extends RecyclerView.Adapter<ListadoUsuariosAdapter.ViewHolder> {

    // INTERFAZ MOVIMIENTO: Ahora es pública y estática dentro del adaptador
    public interface OnUsuarioClickListener {
        void onUsuarioClick(Usuario usuario);
    }

    private final Context context;
    private List<Usuario> listaUsuarios;
    private final OnUsuarioClickListener clickListener;

    // Constructor modificado para recibir el Listener
    public ListadoUsuariosAdapter(Context context, List<Usuario> listaUsuarios, OnUsuarioClickListener clickListener) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
        this.clickListener = clickListener;
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

        holder.tvEmailUsuario.setText(usuario.getEmail());
        holder.tvRolUsuario.setText("Rol: " + usuario.getRol());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onUsuarioClick(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void setUsuarios(List<Usuario> nuevosUsuarios) {
        this.listaUsuarios = nuevosUsuarios;
        notifyDataSetChanged();
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