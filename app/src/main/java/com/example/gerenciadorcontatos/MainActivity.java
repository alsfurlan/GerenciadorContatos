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

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> adapter;
    private final String NOME_BANCO_DADOS = "Contatos";
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        criarConexaoBancoDados();
        criarTabelas();
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


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.contatos);
        listView.setAdapter(adapter);
    }

    private void criarConexaoBancoDados() {
        try {
            // Abertura da conexão
            this.bancoDados = this.openOrCreateDatabase(NOME_BANCO_DADOS, MODE_PRIVATE, null);

            // Validação da existência/criação do banco de dados
            File databasePath = getApplicationContext().getDatabasePath(NOME_BANCO_DADOS + ".db");
            if(databasePath.exists()) {
                mensagem("Conectado ao banco com sucesso!");
            } else {
                mensagem("Não foi possível acessar o banco de dados!");
            }
        } catch(Exception e) {
            mensagem("Erro ao conectar no banco de dados!");
            finish();
        }
    }

    private void criarTabelas() {
        String sql = new StringBuilder("")
                .append("CREATE TABLE IF NOT EXISTS contatos (")
                .append("   id INTEGER PRIMARY KEY,")
                .append("   nome VARCHAR(250) NOT NULL,")
                .append("   email VARCHAR(50) NOT NULL,")
                .append("   telefone VARCHAR(25) NOT NULL")
                .append(")")
                .toString();

        this.bancoDados.execSQL(sql);
    }

    private void listarContatos() {
        Cursor cursor = this.bancoDados.rawQuery("SELECT nome, email, telefone FROM contatos", null);

        adapter.clear();
        if(cursor != null && cursor.getCount() > 0) {
            // Índices
            int iNome = cursor.getColumnIndex("nome");
            int iEmail = cursor.getColumnIndex("email");
            int iTelefone = cursor.getColumnIndex("telefone");

            while(cursor.moveToNext()) {
                String nome = cursor.getString(iNome);
                String email = cursor.getString(iEmail);
                String telefone = cursor.getString(iTelefone);

                String contato = new StringBuilder("Nome: ").append(nome).append("\n")
                            .append("E-mail: ").append(email).append("\n")
                            .append("Telefone: ").append(telefone).toString();

                this.adapter.add(contato);

            }
            this.adapter.notifyDataSetChanged();
        }

    }

    private void mensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

    public void salvar(View view) {
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String telefone = campoTelefone.getText().toString();

        String sql = new StringBuilder("INSERT INTO contatos(nome, email, telefone) VALUES (\"")
                .append(nome).append("\", \"")
                .append(email).append("\", \"")
                .append(telefone).append("\")")
                .toString();

        this.bancoDados.execSQL(sql);
        this.limpar();
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