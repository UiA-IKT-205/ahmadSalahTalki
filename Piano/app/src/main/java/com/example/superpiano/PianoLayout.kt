package com.example.superpiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_halv_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.*

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val halvTones = listOf("C#", "D#","0", "F#", "G#", "A#","0", "C#2", "D#2","0", "F#2", "G#2","A#2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        val fmHalvTones = childFragmentManager
        val ftHalvTons = fmHalvTones.beginTransaction()

        fullTones.forEach {
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)

            fullTonePianoKey.onKeyDown = {
                println("Piano key down $it")
            }
            fullTonePianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            ft.add(view.fullPianoKeys.id,fullTonePianoKey,"note_$it")
        }
        ft.commit()

        halvTones.forEach {
            val halvTonePianoKey = HalvTonePianoKeyFragment.newInstance(it)

            halvTonePianoKey.onKeyDown = {
                println("Piano key down $it")
            }
            halvTonePianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            if(it == "0") {
                ftHalvTons.add(view.unvisiblePianoKey.id, halvTonePianoKey, "note_$it")
            }
            else{
                ftHalvTons.add (view.halvPianoKeys.id, halvTonePianoKey, "note_$it")
            }

        }
        ftHalvTons.commit()

        return view
    }

}