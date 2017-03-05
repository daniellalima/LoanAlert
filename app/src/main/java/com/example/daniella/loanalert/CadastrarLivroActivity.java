package com.example.daniella.loanalert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daniella.loanalert.database.DataBase;
import com.example.daniella.loanalert.dominio.BuscarLivros;
import com.example.daniella.loanalert.dominio.entidades.Livro;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class CadastrarLivroActivity extends AppCompatActivity {



    private EditText txt_titulo;
    private EditText txt_autor;
    private EditText txt_anotacoes;
    private EditText txt_data;


    private DataBase database;
    private SQLiteDatabase conn;
    private BuscarLivros buscarLivros;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_livro);

        txt_titulo = (EditText) findViewById(R.id.txt_titulo);
        txt_autor = (EditText) findViewById(R.id.txt_autor);
        txt_anotacoes = (EditText) findViewById(R.id.txt_anotacoes);
        txt_data = (EditText) findViewById(R.id.txt_data);

        //showDialogOnButtonClick();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //txt_data.setOnClickListener(new );

        ExibirDataListener listener = new ExibirDataListener();
        txt_data.setOnClickListener(listener);
        txt_data.setOnFocusChangeListener(listener);

        livro = new Livro();

        Bundle bundle = getIntent().getExtras();
        if((bundle != null) && (bundle.containsKey("LIVRO"))){
            livro = (Livro)bundle.getSerializable("LIVRO");
            preencherDados();
        }else

        livro = new Livro();

        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            buscarLivros = new BuscarLivros(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder dig = new AlertDialog.Builder(this);
            dig.setMessage("Erro na conexão!" + ex.getMessage());
            dig.setNeutralButton("OK", null);
            dig.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastrar_livro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_acao1:
                if (livro.getId() == 0) {
                    inserir();
                }

                finish();

                break;
            case R.id.menu_acao2:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencherDados(){
        txt_titulo.setText(livro.getLivro());
        txt_autor.setText(livro.getAutor());
        txt_anotacoes.setText(livro.getAnotacoes());

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(livro.getData());

        txt_data.setText(dt);

    }

    private void inserir() {
        try {
            livro.setLivro(txt_titulo.getText().toString());
            livro.setAutor(txt_autor.getText().toString());
            livro.setAnotacoes(txt_anotacoes.getText().toString());

            buscarLivros.inserir(livro);
        } catch (Exception ex) {
        AlertDialog.Builder dig = new AlertDialog.Builder(this);
        dig.setMessage("Erro ao Cadastrar!" + ex.getMessage());
        dig.setNeutralButton("OK", null);
        dig.show();
    }
    }

    private void exibirData(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dig = new DatePickerDialog(this,new selecionaDataListener(),ano,mes,dia);
        dig.show();
    }

    private class ExibirDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        @Override
        public void onClick(View v) {
            exibirData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                exibirData();
            }
        }

    }

    private class selecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);

            Date data = calendar.getTime();
            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(data);
            txt_data.setText(dt);

            livro.setData(data);
        }
    }
}
