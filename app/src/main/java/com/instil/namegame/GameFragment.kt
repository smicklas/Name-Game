package com.instil.namegame

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class GameFragment : Fragment() {
    //stores data
    private var sourceProfiles = MutableLiveData<List<Profile>>()

    private var currentQuestion = 0;

    //progress bar
    private lateinit var progressSpinner : ProgressBar
    //get a random
    //rxview
    
    //define lifecycle of the coroutine scope
    //if children fail, parent is notified and it will cancel the other children
    private val viewModelJob = Job()
    //coroutine scope -> define in the thread our coroutine runs in
    private val coroutineScope = CoroutineScope( viewModelJob + Dispatchers.Main )

    private lateinit var layoutView : View

    private val viewModel: DialogViewModel by activityViewModels()

    //to do make this less stupipid?
    private lateinit var currentQuestionText : TextView

    var userSelectionID = ""
    var userSelectionName = ""

    lateinit var feedbackBackground : ImageView
    lateinit var feedbackText : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutView = inflater.inflate(R.layout.game_fragment, container, false);
        currentQuestionText = layoutView.findViewById<TextView>(R.id.questionNumber)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressSpinner = layoutView.findViewById<ProgressBar>(R.id.progressBar)
        feedbackBackground = layoutView.findViewById<ImageView>(R.id.questionBackgroundMask)
        if(viewModel.getSelectedNumber() == null){
            //to do make this a const
            viewModel.setItem("5")
        }
        currentQuestionText.text = "$currentQuestion / ${viewModel.getSelectedNumber()}"
        //to do change this fun name
        getAdviceProperty()
    }

    companion object {
        fun newInstance(): GameFragment = GameFragment()
    }

    private fun getAdviceProperty(){
        coroutineScope.launch {
            var getProperty = ServiceBuilder.NameGameAPI.retrofitService.getProperty()
            try {
                sourceProfiles.value =  getProperty.await()
                progressSpinner.visibility = View.GONE
                //create card
                //add loader animation
                createQuestion()
            }catch (e: Exception){
                println("EROR ${e}");
            }
        }
    }

    fun checkAnswer(correctAnswer: String, view: ImageView){
        layoutView.clearAnimation()

        if(userSelectionID == correctAnswer) {
            var colorTo = resources.getColor(R.color.transparent)
            var colorFrom = resources.getColor(R.color.green)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 500; // milliseconds
            colorAnimation.addUpdateListener { animator -> view.setColorFilter(animator.animatedValue as Int) }
            colorAnimation.start()
            colorAnimation.doOnEnd {
                if(currentQuestion < viewModel.getSelectedNumber()!!){
                    createQuestion()
                }else{
                    //show end dialog
                    var fragmentManager = (activity as FragmentActivity).supportFragmentManager
                    EndGameDialogFragment().show(fragmentManager, "EndGameDialogFragment")
                }
            }
        }else{
            //todo - do this once?
            var colorTo = resources.getColor(R.color.transparent)
            var colorFrom = resources.getColor(R.color.red)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 500; // milliseconds
            colorAnimation.addUpdateListener { animator -> view.setColorFilter(animator.animatedValue as Int) }
            colorAnimation.start()
        }
    }
    fun createQuestion(){
        currentQuestion++
        currentQuestionText.text = "$currentQuestion / ${viewModel.getSelectedNumber()}"
        layoutView.clearAnimation()
        var questionNameLabel= layoutView.findViewById<TextView>(R.id.questionTextView)
        var gameDataSet = mutableListOf<Profile>()
        var duplicateSourceData = ArrayList<Profile>()
        duplicateSourceData = sourceProfiles.value as ArrayList<Profile>

        //get 6 random people for the buttons
        repeat(6){
            //get a random profile, add to array
            var randomNum = (0..(duplicateSourceData?.size-1)!!).random()
            gameDataSet.add(duplicateSourceData.removeAt(randomNum))
        }

        //choose a correct answer
        var randomNum = (0..5).random()

        var selectedProfile = gameDataSet[randomNum]
        var selectedProfileFirstName = gameDataSet[randomNum].firstName
        var selectedProfileLastName = gameDataSet[randomNum].lastName
        questionNameLabel.text = "Who is $selectedProfileFirstName $selectedProfileLastName?"

        //fill the image buttons with random from the data set
        var duplicateGameDataSet = gameDataSet

        var questionButtons = arrayListOf(
                layoutView.findViewById<ImageButton>(R.id.imageButton1),
                layoutView.findViewById<ImageButton>(R.id.imageButton2),
                layoutView.findViewById<ImageButton>(R.id.imageButton3),
                layoutView.findViewById<ImageButton>(R.id.imageButton4),
                layoutView.findViewById<ImageButton>(R.id.imageButton5),
                layoutView.findViewById<ImageButton>(R.id.imageButton6),
            )

        for (questionButton in questionButtons) {
            var randomNum = (0 until duplicateGameDataSet.size).random()
            var tempProfile = duplicateGameDataSet.removeAt(randomNum)
            Picasso.with(context).load(tempProfile.headshot.url).fit().centerCrop().into(questionButton);
            questionButton.setOnClickListener{
                userSelectionID = tempProfile.id
                userSelectionName = "${tempProfile.firstName} ${tempProfile.lastName}"
                checkAnswer(selectedProfile.id, questionButton)
            }
        }


    }
    //do this if using view models
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }

}