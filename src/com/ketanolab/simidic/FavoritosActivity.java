package com.ketanolab.simidic;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ketanolab.simidic.adapters.AdaptadorLista;
import com.ketanolab.simidic.db.FavoritosDbAdapter;
import com.ketanolab.simidic.util.Constants;
import com.ketanolab.simidic.util.Util;

public class FavoritosActivity extends SherlockActivity implements OnItemClickListener {

	private ListView lista;
	private AdaptadorLista adaptadorLista;
	private ArrayList<String> paths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favoritos);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		lista = (ListView) findViewById(R.id.listaFavoritos);
		adaptadorLista = new AdaptadorLista(this);
		lista.setAdapter(adaptadorLista);
		lista.setOnItemClickListener(this);

		paths = new ArrayList<String>();

	}

	@Override
	protected void onStart() {
		super.onStart();
		paths.clear();
		adaptadorLista.eliminarTodo();
		cargarFavoritos();
	}

	private void cargarFavoritos() {
		FavoritosDbAdapter db = new FavoritosDbAdapter(this);
		db.abrir();
		Cursor cursor = db.obtenerTodosFavoritos();
		if (cursor.moveToFirst()) {
			do {
				String word = cursor.getString(1);
				String pathDictionary = cursor.getString(2);
				paths.add(pathDictionary);
				// *****
				String name = "";
				String author = "";
				SQLiteDatabase dbWords = SQLiteDatabase.openOrCreateDatabase(pathDictionary, null);
				try {
					String[] nameAndAuthor = Util.getNameAndAuthorDictionary(dbWords);
					name = nameAndAuthor[0];
					author = nameAndAuthor[1];
				} catch (Exception ex) {
					Log.e(Constants.DEBUG, "Error en la consulta a la base de datos.");
				}
				dbWords.close();
				// ****
				adaptadorLista.adicionarItem(R.drawable.ic_menu_star, word, name + "\n" + author);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.cerrar();
		adaptadorLista.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return true;
	}

	public void onItemClick(AdapterView<?> arg0, View v, int posicion, long arg3) {
		TextView textViewPalabra = (TextView) v.findViewById(R.id.titulo_item);
		String word = textViewPalabra.getText().toString();
		Intent intent = new Intent(this, PalabraActivity.class);
		intent.putExtra("path", paths.get(posicion));
		intent.putExtra("word", word);
		startActivity(intent);
	}

}
