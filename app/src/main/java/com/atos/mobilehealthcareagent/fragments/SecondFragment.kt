package com.atos.mobilehealthcareagent.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.work.*
import com.atos.mobilehealthcareagent.R
import com.atos.mobilehealthcareagent.contract.UserFitnessDatabaseInterface
import com.atos.mobilehealthcareagent.contract.UserFitnessDatabaseInterface.UserFitnessActivityViewInterface
import com.atos.mobilehealthcareagent.googlefit.BackgroundTask
import com.atos.mobilehealthcareagent.googlefit.readfitnessapi.ReadFitDataApi
import com.atos.mobilehealthcareagent.presenter.FitnesActivityPresenter
import com.atos.mobilehealthcareagent.service.ServiceInputToDB
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class SecondFragment : Fragment(), UserFitnessActivityViewInterface{

    companion object {
        const val KEY_TASK_DESC = "key_task_desc"
    }
   /* private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_HYDRATION, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_NUTRITION, FitnessOptions.ACCESS_WRITE)
        .build()
         lateinit var readFitDataApi:ReadFitDataApi

   */
    lateinit var mFitnesActivityPresenter: FitnesActivityPresenter
    lateinit var mview: View
    lateinit var task:BackgroundTask
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mview = inflater.inflate(R.layout.fragment_workmanager, container, false)
        var btn = mview?.findViewById<Button>(R.id.button1)
        btn?.setOnClickListener{
           CoroutineScope(Dispatchers.IO).launch {
               task?.getData()
           }
    }
        // Inflate the layout for this fragment
        return mview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFitnesActivityPresenter=FitnesActivityPresenter(this)

       /* readFitDataApi = ReadFitDataApi(this.activity?.applicationContext!!,fitnessOptions)
        readFitDataApi.readDataDaily()*/
    }


    override fun initLongRunningService() {
        val data = Data.Builder()
            .putString(KEY_TASK_DESC, "Hey "+ Calendar.getInstance().getTime().toString())
            .build()

        val constraints = Constraints.Builder()

            .build()

        val request = PeriodicWorkRequestBuilder<ServiceInputToDB>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag("mobilehealthcareagent")
                .build()


        WorkManager.getInstance().enqueueUniquePeriodicWork("MobileHealthCareAgent",ExistingPeriodicWorkPolicy.KEEP,request);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
       task = BackgroundTask(context.applicationContext)
    }
}
