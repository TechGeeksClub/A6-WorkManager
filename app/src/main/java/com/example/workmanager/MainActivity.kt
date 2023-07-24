package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.sql.Ref
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = Data.Builder().putInt("intKey",1).build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /*val myWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()*/

        val myWorkRequest : WorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        //WorkManager.getInstance(this).cancelAllWork()

        /*val myWorkRequests : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()*/

        /*WorkManager.getInstance(this).beginWith(myWorkRequests)
            .then(myWorkRequests)
            .then(myWorkRequests)
            .enqueue()*/

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.RUNNING){
                    println("running")
                }
                else if(it.state == WorkInfo.State.FAILED){
                    println("failed")
                }
                else if(it.state == WorkInfo.State.SUCCEEDED){
                    println("succeeded")
                }
                else if(it.state == WorkInfo.State.ENQUEUED){
                    println("enqueued")
                }
            })


    }
}