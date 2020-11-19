package com.example.gerenciadorcontatos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    private static SQLiteDatabase bancoDados = ConexaoBancoDados.getInstance();

    public static void criarTabela() {
        String sql = new StringBuilder("")
                .append("CREATE TABLE IF NOT EXISTS contatos (")
                .append("   id INTEGER PRIMARY KEY,")
                .append("   nome VARCHAR(250) NOT NULL,")
                .append("   email VARCHAR(50) NOT NULL,")
                .append("   telefone VARCHAR(25) NOT NULL")
                .append(")")
                .toString();

        bancoDados.execSQL(sql);
    }

    public static void adicionar(Contato contato) {
        String sql = new StringBuilder("INSERT INTO contatos(nome, email, telefone) VALUES (\"")
                .append(contato.getNome()).append("\", \"")
                .append(contato.getEmail()).append("\", \"")
                .append(contato.getTelefone()).append("\")")
                .toString();

        bancoDados.execSQL(sql);
    }

    public static List<Contato> listar() {
        Cursor cursor = bancoDados.rawQuery("SELECT id, nome, email, telefone FROM contatos", null);
        List<Contato> contatos = new ArrayList<>();

        if(cursor != null && cursor.getCount() > 0) {
            // Índices
            int iId = cursor.getColumnIndex("id");
            int iNome = cursor.getColumnIndex("nome");
            int iEmail = cursor.getColumnIndex("email");
            int iTelefone = cursor.getColumnIndex("telefone");

            while (cursor.moveToNext()) {
                // Pego o valor em cada linha
                int id = cursor.getInt(iId);
                String nome = cursor.getString(iNome);
                String email = cursor.getString(iEmail);
                String telefone = cursor.getString(iTelefone);

                Contato contato = new Contato(id, nome, email, telefone);
                contatos.add(contato);
            }
        }
        return contatos;
    }

    public static void excluir(Contato contato) {
        String sql = new StringBuilder("DELETE FROM contatos WHERE id = ")
                .append(contato.getId())
                .toString();

        bancoDados.execSQL(sql);
    }
}
