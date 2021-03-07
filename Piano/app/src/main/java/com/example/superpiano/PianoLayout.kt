package com.example.superpiano

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_halv_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream
import com.example.superpiano.data.Note
import com.example.superpiano.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.io.BufferedReader
import java.nio.Buffer
import java.nio.file.Path


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val halvTones = listOf("C#", "D#","0", "F#", "G#", "A#","0", "C#2", "D#2","0", "F#2", "G#2","A#2")

    private var score:MutableList<Note> = mutableListOf<Note>() // Score == Noteark?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullTones.forEach {
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0
            var endPlay:Long = 0

            fullTonePianoKey.onKeyDown = {
                startPlay = System.nanoTime()
                println("Piano key down $it")
            }
            fullTonePianoKey.onKeyUp = {
                endPlay = System.nanoTime()
                val note = Note(it,startPlay,endPlay)
                score.add(note)
                println("Piano key up $note")
            }

            ft.add(view.fullPianoKeys.id,fullTonePianoKey,"note_$it")
        }


        halvTones.forEach {
            val halvTonePianoKey = HalvTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0
            var endPlay:Long = 0

            halvTonePianoKey.onKeyDown = {
                startPlay = System.nanoTime()
                println("Piano key down $it")
            }
            halvTonePianoKey.onKeyUp = {
                endPlay = System.nanoTime()
                val note = Note(it,startPlay,endPlay)
                score.add(note)
                println("Piano key up $note")
            }

            ft.add (view.halvPianoKeys.id, halvTonePianoKey, "note_$it")


        }
        ft.commit()

        view.saveScoreBt.setOnClickListener {

            var fileName = view.fileNameTextEdit.text.toString()
            if(fileName.count() == 0){ // varsler når file navnet er tomt
                val EmptyFileName = AlertDialog.Builder(it.context)
                EmptyFileName.setTitle("Empty File Name")
                EmptyFileName.setMessage("File navnet kan ikke være tomt")
                EmptyFileName.setNeutralButton("OK",{ dialogInterface: DialogInterface, i: Int -> })
                EmptyFileName.show()
            }
            fileName = "$fileName.musikk"
            val path = this.activity?.getExternalFilesDir(null)
            if (File(path,fileName).exists()){ // sjekker om det finnes en file med samme navn fra før . hvis ikke lagere vi den
                val fileExists = AlertDialog.Builder(it.context)
                fileExists.setTitle("File exists")
                fileExists.setMessage("Det finnes en file med samme navn fra før")
                fileExists.setNeutralButton("OK",{ dialogInterface: DialogInterface, i: Int -> })
                fileExists.show()
            }else if(score.count() == 0 ){  // dette er for å skjekke om det er noe note å lagre
                val EmptyTone = androidx.appcompat.app.AlertDialog.Builder(it.context)
                EmptyTone.setTitle("Empty Note")
                EmptyTone.setMessage("Du må spille inn noe før du lagre den")
                EmptyTone.setNeutralButton("OK",{ dialogInterface: android.content.DialogInterface, i: kotlin.Int -> })
                EmptyTone.show()
            }
            else {
                if (path != null) {
                    FileOutputStream(File(path, fileName), true).bufferedWriter().use { writer ->
                        score.forEach {
                            writer.write("${it.toString()}\n")
                        }
                    }
                    score.clear()
                }
            }
        }
        view.loadScoreBt.setOnClickListener{

            getAllFilesInResources()
            }

        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllFilesInResources()
    {
        var text:String = ""
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(projectDirAbsolutePath, "/sdcard/Android/data/com.example.superpiano/files")
        val path = Files.walk(resourcesPath)
            .filter { item -> Files.isRegularFile(item) }
            .filter { item -> item.toString().endsWith(".musikk") }
            .forEach { item -> text += "${item.fileName}\n" + showNote(item) + "**********\n"}
        println(text)
        //view?.fileNameTextEdit?.setText(text)
        val fileExists = AlertDialog.Builder(requireContext())
        fileExists.setTitle("Your note")
        fileExists.setMessage(text)
        fileExists.setNeutralButton("OK",{ dialogInterface: DialogInterface, i: Int -> })
        fileExists.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNote(x:Path): String {
        val bufferedReader: BufferedReader = File("$x").bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        return inputString
    }

}