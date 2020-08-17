package com.example.myapplicationmo1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationmo1.Adapter.NotesAdapter
import com.example.myapplicationmo1.Model.Notes
import com.example.myapplicationmo1.clickListener.ItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
public class MyNotesActivity : AppCompatActivity(){
    lateinit var fullName: String
    lateinit var fabAddNotes:FloatingActionButton
    val TAG = "MyNotesActivity"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerView: RecyclerView
    var notesList = ArrayList<Notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        bindViews()
        setupSharedferences()
        getIntentData()
        supportActionBar?.title = fullName
        fabAddNotes.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                setupDialogBox()
            }

        })
    }

    private fun setupSharedferences() {
    sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun getIntentData() {
       val intent = intent
        if(intent.hasExtra(AppConstant.Full_Name)) {
            fullName = intent.getStringExtra(AppConstant.Full_Name)
        }
        if(fullName.isEmpty()){
            fullName = sharedPreferences.getString(PrefConstant.FULL_NAME,"").toString()
        }
    }

    private fun setupDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog_layout,null)
        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()
        buttonSubmit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
                if(title.isNotEmpty() && description.isNotEmpty()){
                    val notes = Notes(title,description)
                    notesList.add(notes)
                }else{
                    Toast.makeText(this@MyNotesActivity,"Title or Description cannot be empty",Toast.LENGTH_SHORT).show()
                }
                setupRecyclerView()
                dialog.hide()
            }

        })
        dialog.show()
    }

    private fun setupRecyclerView() {
        val itemClickListener = object :ItemClickListener{
            override fun onClick(notes: Notes) {
                val intent =Intent(this@MyNotesActivity,DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE,notes.title)
                intent.putExtra(AppConstant.DESCRIPTION,notes.description)
                startActivity(intent)
            }

        }
        val notesAdapter = NotesAdapter(notesList,itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = notesAdapter
    }

    private fun bindViews() {
       fabAddNotes = findViewById(R.id.fabAddNotes)
        recyclerView = findViewById(R.id.recyclerViewNotes)
    }
}

