package com.example.gerenciadorcontatos;

import android.database.sqlite.SQLiteDatabase;

public class ConexaoBancoDados {

    public final static String NOME_BANCO_DADOS = "Contatos";
    private static SQLiteDatabase bancoDados;

    public ConexaoBancoDados(SQLiteDatabase bancoDados) {
        this.bancoDados = bancoDados;
    }

    public static SQLiteDatabase getInstance() {
        if(bancoDados != null) {
            return bancoDados;
        }
        return null;
    }
}
