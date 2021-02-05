package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime


public class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var Pausetimer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutDownDisplay:TextView
    lateinit var seekBar:SeekBar
    lateinit var PauseSeekBar:SeekBar
    lateinit var PauseDisplay:TextView
    lateinit var Repeat:EditText

    var PauseToCountDownTnMs = 5000L
    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L
    var AntallRepit = 2   // default repaet times

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Repeat = findViewById<EditText>(R.id.repeat)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            val text = Repeat.text.toString() // sparer verdien fra edittext i en variable
            AntallRepit = text.toInt()         // bytter til int selv om den er int men appen crasher uten
            startCountDown(timeToCountDownInMs) // ved trykk på start knappen bygenner tid å telle ned
            startButton.isEnabled = false;
        }
        seekBar = findViewById<SeekBar>(R.id.seekbar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                timeToCountDownInMs = i * 60 * timeTicks
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                updateCountDownDisplay(timeToCountDownInMs)
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                updateCountDownDisplay(timeToCountDownInMs)
            }
        })
        PauseSeekBar = findViewById<SeekBar>(R.id.seekbarPause)
        PauseSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                PauseToCountDownTnMs = i * 60 * timeTicks
                updatePauseDisplay(PauseToCountDownTnMs)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                updatePauseDisplay(PauseToCountDownTnMs)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                updatePauseDisplay(PauseToCountDownTnMs)
            }
        })
        coutDownDisplay = findViewById<TextView>(R.id.countDownView)
        PauseDisplay = findViewById<TextView>(R.id.PauseDisplay)

    }

    fun startCountDown(v: Long){
        timer = object : CountDownTimer(v,timeTicks) {
            override fun onFinish() {
                startCountDownPause(PauseToCountDownTnMs) // når tid er ferdig bygenner pasue tid funksjon å telle ned
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }

    fun startCountDownPause(v: Long){
        Pausetimer = object : CountDownTimer(v,timeTicks) {
            override fun onFinish() {
                repeat() // når pause tid er ferdige også så kjører repeat funksjonen
            }

            override fun onTick(millisUntilFinished: Long) {
                updatePauseDisplay(millisUntilFinished)
            }
        }

        Pausetimer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutDownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun updatePauseDisplay(timeInMs:Long){
        PauseDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun repeat() {
        // etter at både tid å pause tid er ferdige kjører denne funksjonen
        // den kjører so lenge Antall Repitisjoner er ikke 0
        // hver gang denne funksjonen kjøres så antalle går ned med 1
        while (AntallRepit != 0)
        {
            AntallRepit -=1
            startCountDown(timeToCountDownInMs)
        }
    }
}

