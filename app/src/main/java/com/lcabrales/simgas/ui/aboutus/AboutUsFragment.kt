package com.lcabrales.simgas.ui.aboutus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.BaseFragment
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.FragmentAboutUsBinding

class AboutUsFragment : BaseFragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var viewModel: AboutUsViewModel
    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)

        subscribe()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }
}