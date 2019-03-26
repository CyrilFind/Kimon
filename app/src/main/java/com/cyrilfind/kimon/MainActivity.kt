package com.cyrilfind.kimon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cyrilfind.kimon.extensions.flash
import com.cyrilfind.kimon.extensions.unhighlight
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import kotlin.random.Random

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var randomValues: List<Int>
    private lateinit var colorViews: List<View>
    private var responseValues = mutableListOf<Int>()
    private var sequenceSize = START_SEQUENCE_SIZE
    private var canPlay = false
    private var score: Int by Delegates.observable(-1) { _, _, newValue -> score_text_view.text = "Score: $newValue" }
    private var count: Int by Delegates.observable(-1) { _, _, newValue -> count_text_view.text = "Count: $newValue" }

    override fun onStop() {
        super.onStop()
        cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_show.setOnClickListener { showSequence() }
        colorViews = listOf(button_blue, button_red, button_green, button_yellow)
        colorViews.forEach { view -> view.setOnClickListener { onClickColor(view) } }
        score = 0
        count = sequenceSize
        randomize()
    }

    private fun randomize() {
        randomValues = List(sequenceSize) { Random.nextInt(0, colorViews.size) }
    }

    private fun showSequence() {
        reset()
        responseValues.clear()
        canPlay = false
        launch {
            randomValues.forEach {
                count--
                colorViews[it].flash()
                delay(COOLDOWN_INTERVAL)
            }
            canPlay = true
            count = sequenceSize
        }
    }

    private fun onClickColor(view: View) {
        if (canPlay) check(colorViews.indexOf(view))
    }

    private fun check(response: Int) {
        count--
        responseValues.add(response)
        when {
            responseValues == randomValues -> win()
            responseValues.size == sequenceSize -> fail()
            else -> canPlay = true
        }
    }

    private fun win() {
        score++
        sequenceSize++
        randomize()
        count = sequenceSize
        launch { score_text_view.flash() }
        canPlay = false
    }

    private fun fail() {
        reset()
        responseValues.clear()
        canPlay = false
        launch {
            colorViews.map { launch { it.flash() } }.joinAll()
            canPlay = true
        }
    }

    private fun reset() {
        coroutineContext.cancelChildren()
        colorViews.forEach { it.unhighlight() }
        count = sequenceSize
    }

    companion object {
        private const val COOLDOWN_INTERVAL = 200L
        private const val START_SEQUENCE_SIZE = 2
    }
}
