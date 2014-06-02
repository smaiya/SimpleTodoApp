package com.codepath.starter.simpletodoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	private EditText etText;
	private ListView lvItems;
	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;

	protected final int EDIT_ITEM_REQUEST_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Take the view and convert it into real Java object.
		setContentView(R.layout.activity_todo);
		//Access to the EditText
		etText = (EditText) findViewById(R.id.etNewItem);
		//Access to the List View
		lvItems = (ListView) findViewById(R.id.lvItems);
		//Create the array list to locally store the list view content
		items = new ArrayList<String>();
		//Read the content from the file
		readToDoItems();
		//Adapter used to convert between list view and array list.
		itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		//Associate/attach the adapter to the list view
		lvItems.setAdapter(itemsAdapter);
		//Set the listener to the list view to handle long click.
		setTodoListLongClickListener();
	}
	/*
	 * Handle the onClick event of Add button. Refer to activity-todo.xml
	 */
	public void addItemToList(View v) {
		//1. Update the List view through list adapter
		itemsAdapter.add(etText.getText().toString());
		//2. Reset the user input in the text view
		etText.setText("");
		//3. Save the list content to the file for the persistence.
		saveToDoItems();
	}
	
	/*
	 * This is to handle Long click inside the list view.
	 * The requirement is to delete the selected item on which long click is performed.
	 */
	private void setTodoListLongClickListener(){
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item, int pos, long id ){
				items.remove(pos);
				itemsAdapter.notifyDataSetInvalidated();
				saveToDoItems();
				return true;
			}
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
				editToDoItem(pos);
			}
		});
	}
	
	/*
	 * Utility used to read the file content to the list view.
	 */
	private void readToDoItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			//Using the FileUtils from Apache commons-io-2.4.jar
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Utility used to write the list view contents to a file.
	 */
	private void saveToDoItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			//Using the FileUtils from Apache commons-io-2.4.jar
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

	/*
	 * Action bar item click handler 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		// 
		int id = mi.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(mi);
	}

	/*
	 * Utility to handle editing selected ToDo item using an "Intent"
	 */
	protected void editToDoItem(int pos) {
		Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
		i.putExtra("text", items.get(pos));
		i.putExtra("pos", pos);
		startActivityForResult(i, EDIT_ITEM_REQUEST_CODE);
	}

	/*
	 * Handle the return data after editing.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
			String text = data.getExtras().getString("text");
			int position = data.getExtras().getInt("pos");
			items.set(position, text);
			itemsAdapter.notifyDataSetChanged();
			saveToDoItems();
		}
	}
	
}
