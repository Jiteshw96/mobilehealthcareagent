package com.atos.mobilehealthcareagent.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.atos.mobilehealthcareagent.R
import com.atos.mobilehealthcareagent.TAG
import com.atos.mobilehealthcareagent.database.AppDatabase
import com.atos.mobilehealthcareagent.database.UserFitnessData
import com.atos.mobilehealthcareagent.fitnessharedpreferences.LastSyncSharedPreferences
import com.atos.mobilehealthcareagent.fragments.SecondFragment
import com.atos.mobilehealthcareagent.googlefit.GetDateDetailsStartEndTime
import com.atos.mobilehealthcareagent.googlefit.getEndTimeString
import com.atos.mobilehealthcareagent.googlefit.getStartTimeString
import com.atos.mobilehealthcareagent.googlefit.readfitnessapi.ReadFitDataApi
import com.atos.mobilehealthcareagent.presenter.FitnesActivityPresenter
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.data.Field.FIELD_STEPS
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ServiceInputToDB(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    var NotificationChannelID="mobilehealthcareagent"
    var NotificationChannelName="mobilehealthcareagent"
    var NotificationContentTitle="Mobile HealthCare Agent"
    lateinit var db: AppDatabase



    // var mStartDate = "2020-05-11" + " 11:40:01"
   // var mEndDate = "2020-05-19" + " 12:51:59"

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_HYDRATION, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_NUTRITION, FitnessOptions.ACCESS_WRITE)
        .build()



    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    lateinit var readFitDataApi: ReadFitDataApi

    override fun doWork(): Result {
        var lastSyncDate =  LastSyncSharedPreferences().getLastSyncTime(applicationContext)
        var mStartDate = sdf.format(lastSyncDate).toString()
        var timeZone = TimeZone.getDefault()
        var currentTime = Calendar.getInstance(timeZone).getTime();
        var date = sdf.format(currentTime)
        var mSeconds = sdf.parse(date).time
        var mEndDate = sdf.format(mSeconds).toString()
        var mStartTimeInMili = sdf.parse(mStartDate)
        var mEndTimeInMili = sdf.parse(mEndDate)

        db = AppDatabase.getAppDatabase(applicationContext) as AppDatabase

        val data = inputData
        val desc = data.getString(SecondFragment.KEY_TASK_DESC)
        val data1 = Data.Builder()
            .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
            .build()


        readFitDataApi = ReadFitDataApi(applicationContext,fitnessOptions)

        Log.i("DO Work Method","Fun called")


     /*   //GetCalories Func Called
        CoroutineScope(Dispatchers.IO).launch {
            val task:Task<DataReadResponse> =  readFitDataApi.readHistoryDataCalorie()
            task.addOnSuccessListener{
                DataParsing(it,com.google.android.gms.fitness.data.Field.FIELD_CALORIES)
            }
        }
*/

       /* //GetHeartPoints Func Called
        CoroutineScope(Dispatchers.IO).launch {
            val task:Task<DataReadResponse> =  readFitDataApi.readHistoryHeartPoints()
            task.addOnSuccessListener{
                DataParsing(it,com.google.android.gms.fitness.data.Field.FIELD_INTENSITY)
            }
        }*/


     /*    //GetHeartPoints Func Called
      CoroutineScope(Dispatchers.IO).launch {
          val task:Task<DataReadResponse> =  readFitDataApi.readHistoryDatateps()
          task.addOnSuccessListener{
              DataParsing(it,com.google.android.gms.fitness.data.Field.FIELD_STEPS)
          }
      }*/



        //TimeInterval Func Called
        CoroutineScope(Dispatchers.IO).launch {

                val task:Task<DataReadResponse> =  readFitDataApi.getStepsTimeInterval(
                    GetDateDetailsStartEndTime.DateStartEnd(
                        mStartDate, mEndDate,
                        mStartTimeInMili.time, mEndTimeInMili.time
                    )
                )

                task.addOnSuccessListener{
                    CoroutineScope(Dispatchers.IO).launch {
                        DataParsing(task.result,com.google.android.gms.fitness.data.Field.FIELD_STEPS)
                        LastSyncSharedPreferences().setLastSyncTime(mSeconds,applicationContext)

                    }

                }
        }
        return Result.success(data1)
    }



     suspend fun displayNotification(task: String, desc: String?) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationChannelID,
                NotificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val builder =
            NotificationCompat.Builder(applicationContext, NotificationChannelID)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
        manager.notify(1, builder.build())
        Log.e("Time", Calendar.getInstance().getTime().toString())
    }

    companion object {
        const val KEY_TASK_OUTPUT = "key_task_output"
    }


    suspend  fun DataParsing(dataReadResult: DataReadResponse?,fieldName:Field){
        Log.i("Bucket",dataReadResult?.buckets.toString())
        val data = arrayListOf<String>()
        if(dataReadResult!!.buckets.isNotEmpty()){

            for (bucket in dataReadResult.buckets) {
                bucket.dataSets.forEach {
                    if (it.dataPoints.size > 0){
                        data.add(it.dataPoints.get(0).getValue(fieldName).toString())
                    }
                }
            }
            for(element in data){
                Log.i("ArrayData",element.toString())
            }


            if(data.isNotEmpty()){
                displayNotification("Steps", data.get(data.size-1))

            }else{
                displayNotification("Steps", "No Record")
            }
        }

        var user = UserFitnessData()
        user.firstName = "John"
        user.age = 30
        user.lastName = "wick"
        user.timestamp = "2020"
       // user.steps = data?.get(data.size-1) ?: "2200"
        user.steps = "2200"
        user.uid = 11
        db.userDao()?.insertFitnessData(user)
        db.userDao()?.fitnessData?.get(0)?.steps


        Log.i("Database Data" , db.userDao()?.fitnessData?.get(0)?.steps)
        Log.i("Database Data Size" , db.userDao()?.fitnessData?.size.toString())
    }

   /* fun stepsDataParsing(dataReadResult: DataReadResponse?){
        Log.i("Task Result",dataReadResult.toString())
        Log.i("Bucket",dataReadResult?.buckets.toString())
        Log.i("Bucket-1",dataReadResult?.buckets?.get(1).toString())
        Log.i("DataSet",dataReadResult?.buckets?.get(1)?.dataSets?.get(0).toString())
        Log.i("Steps",dataReadResult?.buckets?.get(3)?.dataSets?.get(0)?.dataPoints?.get(0)?.getValue(com.google.android.gms.fitness.data.Field.FIELD_STEPS).toString())
        val data = arrayListOf<String>()

        if(dataReadResult!!.buckets.isNotEmpty()){

            for (bucket in dataReadResult.buckets) {
                bucket.dataSets.forEach {
                    if (it.dataPoints.size > 0){
                        data.add(it.dataPoints.get(0).getValue(com.google.android.gms.fitness.data.Field.FIELD_STEPS).toString())
                    }
                }
            }
            Log.i("StepsArray",data.size.toString())
        }

        var size = dataReadResult?.buckets?.size!! - 1
        var bucket =  dataReadResult?.buckets?.get(3)
        var dataSet = bucket?.dataSets?.get(0)
        var dataPoint  = dataSet?.dataPoints?.get(0)
        var steps = dataPoint?.getValue(com.google.android.gms.fitness.data.Field.FIELD_STEPS).toString()
        displayNotification("Steps",steps)
    }*/
}