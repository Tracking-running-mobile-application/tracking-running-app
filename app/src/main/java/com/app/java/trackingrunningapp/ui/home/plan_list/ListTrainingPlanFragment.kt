package com.app.java.trackingrunningapp.ui.home.plan_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentListTrainingPlanBinding
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory

class ListTrainingPlanFragment : Fragment() {
    private lateinit var binding: FragmentListTrainingPlanBinding
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val planFactory = TrainingPlanViewModelFactory(InitDatabase.trainingPlanRepository, InitDatabase.notificationRepository, InitDatabase.runSessionRepository)
        trainingPlanViewModel = ViewModelProvider(this, planFactory).get(TrainingPlanViewModel::class.java)
        // fetch data
        trainingPlanViewModel.fetchRecommendedPlans()
        binding = FragmentListTrainingPlanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(EXTRA_TITLE_PLAN)
        if(title == "Beginner Run"){
            binding.imgPlanTitle.setImageResource(R.drawable.img_begginer_list)
            // TODO: do smt with beginner
            trainingPlanViewModel.recommendedPlansBeginner.observe(viewLifecycleOwner) {
                    trainingPlans ->
                if (trainingPlans.isNotEmpty()) {
                    for (plan in trainingPlans) {
                        Log.d(
                            "Training Plan Log",
                            "${plan.planId}, ${plan.imagePath}, ${plan.title}, ${plan.description}, ${plan.difficulty}"
                        )
                    }
                } else {
                    Log.e("Error LOL", "No training plan exist re-install the app or clean project before call me please")
                }
            }
        }else if(title == "Intermediate Run"){
            binding.imgPlanTitle.setImageResource(R.drawable.img_intermediate_list)
        }else if(title == "Advanced Run"){
            binding.imgPlanTitle.setImageResource(R.drawable.img_advance_list)
        }
    }

    companion object{
        const val EXTRA_TITLE_PLAN = "EXTRA_TITLE_PLAN"
    }
}