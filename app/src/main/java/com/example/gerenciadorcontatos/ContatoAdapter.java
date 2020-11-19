package com.example.gerenciadorcontatos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    public ContatoAdapter(@NonNull Context context) {
        super(context, R.layout.contato, new ArrayList<>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = layoutInflater.inflate(R.layout.contato, parent, false);

        Contato contato = getItem(position);

        TextView nome = view.findViewById(R.id.nome);
        TextView email = view.findViewById(R.id.email);
        TextView telefone = view.findViewById(R.id.telefone);

        ImageButton excluir = view.findViewById(R.id.excluir);
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmação")
                        .setMessage("Deseja excluir o contato?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Exclui do banco de dados
                                ContatoDAO.excluir(contato);

                                // Lista os contatos atualizados
                                MainActivity activity = (MainActivity) getContext();
                                activity.listarContatos();
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        nome.setText(contato.getNome());
        email.setText(contato.getEmail());
        telefone.setText(contato.getTelefone());

        return view;
    }
}
