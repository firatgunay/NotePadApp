package com.firatgunay.notepad

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.firatgunay.notepad.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: ArrayAdapter<String>
    private var notes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        binding.listViewNotes.adapter = noteAdapter

        binding.editTextNote.doOnTextChanged { text, _, _, _ ->
            binding.buttonAddNote.isEnabled = !text.isNullOrEmpty()
        }

        binding.buttonAddNote.setOnClickListener {
            val note = binding.editTextNote.text.toString()
            notes.add(note)
            noteAdapter.notifyDataSetChanged()
            binding.editTextNote.text.clear()
            saveNotes()
        }

        binding.listViewNotes.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val note = notes[position]
                Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
            }

        loadNotes()
    }

    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("notes", notes.toSet())
        editor.apply()
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val savedNotes =
            sharedPreferences.getStringSet("notes", setOf())?.toMutableList() ?: mutableListOf()
        notes.addAll(savedNotes)
        noteAdapter.notifyDataSetChanged()
    }
}
