package com.example.daniella.loanalert;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.database.sqlite.*;
import android.database.*;

import com.example.daniella.loanalert.database.DataBase;
import com.example.daniella.loanalert.dominio.BuscarLivros;
import com.example.daniella.loanalert.dominio.entidades.Livro;

import java.io.Serializable;

public class LivrosActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton btn_adicionar;
    private EditText txt_pesquisa;
    private ListView lst_livros;
    private ArrayAdapter<Livro>adpLivros;

    private DataBase database;
    private SQLiteDatabase conn;

    private BuscarLivros buscarLivros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros);

        btn_adicionar = (ImageButton)findViewById(R.id.btn_adicionar);
        txt_pesquisa = (EditText) findViewById(R.id.txt_pesquisa);
        lst_livros = (ListView) findViewById(R.id.lst_livros);

        btn_adicionar.setOnClickListener(this);

        lst_livros.setOnItemClickListener(this);//cast errado

        try{
            database = new DataBase(this);
            conn = database.getWritableDatabase();

            buscarLivros = new BuscarLivros(conn);
            adpLivros = buscarLivros.buscaLivros(this);
            lst_livros.setAdapter(adpLivros);

        }catch (SQLException ex){
            AlertDialog.Builder dig = new AlertDialog.Builder(this);
            dig.setMessage("Erro na conexão!" + ex.getMessage());
            dig.setNeutralButton("OK",null);
            dig.show();
        }

    }


    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, CadastrarLivroActivity.class);
        startActivityForResult(it,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adpLivros = buscarLivros.buscaLivros(this);
        lst_livros.setAdapter(adpLivros);
    }


    //SPINNER?
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Livro livro = adpLivros.getItem(position);
        Intent it = new Intent(this,CadastrarLivroActivity.class);
        it.putExtra("LIVRO", livro);
        startActivityForResult(it,0);
    }
}
