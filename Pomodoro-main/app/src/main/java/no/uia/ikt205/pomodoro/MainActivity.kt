package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var sett30mintButton:Button
    lateinit var sett60mintButton:Button
    lateinit var sett90mintButton:Button
    lateinit var sett120mintButton:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
            startButton.isEnabled = false;
        }
        sett30mintButton = findViewById<Button>(R.id.sett30mintButton)
        sett30mintButton.setOnClickListener(){
            timeToCountDownInMs = timeTicks * 60 * 30
        }

        sett60mintButton = findViewById<Button>(R.id.sett60mintButton)
        sett60mintButton.setOnClickListener(){
            timeToCountDownInMs = timeTicks * 60 * 60
        }
        sett90mintButton = findViewById<Button>(R.id.sett90mintButton)
        sett90mintButton.setOnClickListener(){
            timeToCountDownInMs = timeTicks * 60 * 90
        }
        sett120mintButton = findViewById<Button>(R.id.sett120mintButton)
        sett120mintButton.setOnClickListener(){
            timeToCountDownInMs = timeTicks * 60 * 120
        }

        coutdownDisplay = findViewById<TextView>(R.id.countDownView)
    }



    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }



}
