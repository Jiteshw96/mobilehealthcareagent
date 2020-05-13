package com.atos.mobilehealthcareagent.contract

import com.atos.mobilehealthcareagent.database.AppDatabase
import com.atos.mobilehealthcareagent.database.User


interface UserBasicDatabaseInterface {
    interface UserBasicDataBaseViewInterface {
        fun initUserFitnessDatabase()

    }

    interface UserBasicDataPresenterInterface {
        fun buttonClickAddBasecDataToDatabase(db: AppDatabase, user: User)
    }


    interface UserBasicDataModelInterface {
        suspend fun getUserBasicData()
        suspend fun addUserBasicData(db:AppDatabase,user: User):LongArray?
    }

}