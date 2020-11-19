package com.example.gerenciadorcontatos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ContatoAdapter adapter;
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        criarConexaoBancoDados();
        listarContatos();
    }

    @Override
    protected void onDestroy() {
        if(this.bancoDados != null) {
            this.bancoDados.close();
        }
        super.onDestroy();
    }

    private void inicializarComponentes() {
        campoNome = findViewById(R.id.nome);
        campoEmail = findViewById(R.id.email);
        campoTelefone = findViewById(R.id.telefone);

        adapter = new ContatoAdapter(this);
        ListView listView = findViewById(R.id.contatos);
        listView.setAdapter(adapter);
    }

    private void criarConexaoBancoDados() {
        try {
            // Abertura da conexão
            bancoDados = this.openOrCreateDatabase(ConexaoBancoDados.NOME_BANCO_DADOS, MODE_PRIVATE, null);
            ConexaoBancoDados conexao = new ConexaoBancoDados(bancoDados);

            ContatoDAO.criarTabela();
        } catch(Exception e) {
            mensagem("Erro ao conectar no banco de dados!");
            finish();
        }
    }

    public void listarContatos() {
        // Limpar a lista
        adapter.clear();

        // Pego os contato do banco
        List<Contato> contatos = ContatoDAO.listar();

        // Adicionar os contatos ao adapter
        this.adapter.addAll(contatos);

        // Notificar as mudanças de dados para o ListView
        this.adapter.notifyDataSetChanged();
    }

    private void mensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

    public void salvar(View view) {
        // Pegar os dados preenchidos nos campos
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String telefone = campoTelefone.getText().toString();

        // Criar objeto contato para salvar
        Contato contato = new Contato(nome, email, telefone);

        // Salvar dados no banco
        ContatoDAO.adicionar(contato);

        // Limpar os campos para permitir novos cadastros
        this.limpar();

        // Atualizar a lista de contatos
        this.listarContatos();
    }

    public void cancelar(View view) {
        this.limpar();
    }

    public void limpar() {
        campoNome.setText("");
        campoEmail.setText("");
        campoTelefone.setText("");
    }
}