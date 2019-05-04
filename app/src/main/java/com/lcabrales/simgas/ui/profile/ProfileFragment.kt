package com.lcabrales.simgas.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.BaseFragment
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.FragmentProfileBinding
import com.lcabrales.simgas.di.ViewModelFactory
import com.lcabrales.simgas.model.session.EditUserRequest
import com.lcabrales.simgas.model.session.User

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity!!))
            .get(ProfileViewModel::class.java)

        setOnClickListeners()
        setImeOptions()
        subscribe()
        viewModel.fetchUser()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer(this::populateUserInfo))
        viewModel.enableSubmitButtonLiveData.observe(viewLifecycleOwner,
            Observer(this::enableSubmitButton))
    }

    private fun setOnClickListeners() {
        binding.btnSubmit.setOnClickListener { performEdit() }
    }

    private fun setImeOptions() {
        binding.etEmail.setOnEditorActionListener { _, action, _ ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_SEND) {
                performEdit()
                handled = true
            }
            handled
        }
    }

    private fun performEdit() {
        hideKeyboard()

        val request = EditUserRequest(
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            email = binding.etEmail.text.toString()
        )
        viewModel.sendEditRequest(request)
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun populateUserInfo(user: User) {
        binding.etUsername.setText(user.username)
        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etEmail.setText(user.email)
    }

    @UiThread
    private fun enableSubmitButton(enable: Boolean) {
        binding.btnSubmit.isEnabled = enable
    }
}