package com.codepath.starter.simpletodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	protected EditText etEditItem;
	protected int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		String text = getIntent().getStringExtra("text");
		etEditItem = (EditText)findViewById(R.id.etEditItem);
		etEditItem.setText(text);

		position = getIntent().getIntExtra("pos", -1);
	}

	/*
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	/*
	 * ActionBar click handler
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) { 
		int id = mi.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(mi);
	}

	/*
	 * On click handler for the save button. Save the data and return to the caller
	 * through an "Intent".
	 */
	public void saveToDoItem(View v) {
		Intent data = new Intent();
		data.putExtra("pos", position);
		data.putExtra("text", etEditItem.getText().toString());
		//Set the result and return to the caller
		setResult(RESULT_OK, data);
		//Activity is done, close it.
		finish();
	}
}
