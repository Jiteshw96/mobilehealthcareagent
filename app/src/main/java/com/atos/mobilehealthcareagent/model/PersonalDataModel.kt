package com.atos.mobilehealthcareagent.model

import android.util.Log
import com.atos.mobilehealthcareagent.contract.UserBasicDatabaseInterface
import com.atos.mobilehealthcareagent.database.AppDatabase
import com.atos.mobilehealthcareagent.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class PersonalDataModel :
    UserBasicDatabaseInterface.UserBasicDataModelInterface {

    override suspend fun addUserBasicData(db: AppDatabase, user: User):LongArray? {
        var indexNumber_user_basic_info_DB:LongArray?=null

            val user = User()
            user.firstName = "Rajdeep"
            user.lastName = "Sarker"
            user.age = 27

            indexNumber_user_basic_info_DB = db.userDao()?.insertAll(user)
            Log.e("Value", indexNumber_user_basic_info_DB.toString())


        return indexNumber_user_basic_info_DB
    }

    override suspend fun getUserBasicData() {

        //  return ArrayList<User>()
    }

}