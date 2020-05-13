package com.atos.mobilehealthcareagent.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.atos.mobilehealthcareagent.R
import com.atos.mobilehealthcareagent.contract.UserBasicDatabaseInterface
import com.atos.mobilehealthcareagent.database.AppDatabase
import com.atos.mobilehealthcareagent.database.User
import com.atos.mobilehealthcareagent.databinding.FragmentRegistrationBasicInfoBinding
import com.atos.mobilehealthcareagent.presenter.PersonalDataPresenter


/**
 * A simple [Fragment] subclass.
 */
class RegistrationBasicInfo : Fragment(),
    UserBasicDatabaseInterface.UserBasicDataBaseViewInterface {

    lateinit var mPersonalDataPresenter: PersonalDataPresenter
    lateinit var db: AppDatabase

    /**
     * Call after on attach method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View =
            inflater.inflate(R.layout.fragment_registration_basic_info, container, false)

        val binding: FragmentRegistrationBasicInfoBinding? = DataBindingUtil.bind(view)

        binding?.registrationBasicInfoFragment = this

        binding?.view = view

        return view

    }

    /**
     * Call after activity created
     * @param savedInstanceState Bundle object containing the fragment's previously saved state. If the fragment has never existed before, the value of the Bundle object is null
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPersonalDataPresenter = PersonalDataPresenter(this)
    }

    override fun initUserFitnessDatabase() {

        db = AppDatabase.getAppDatabase(activity!!.applicationContext) as AppDatabase

        Log.e("Database Created", "Ready to Read/Write")
    }


    /**
     * On click Button
     * @param view object of android.view.View
     */
    fun onClickSaveButton(view: View) {
        mPersonalDataPresenter.buttonClickAddBasecDataToDatabase(db, User())
    }


}
