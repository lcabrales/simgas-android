package com.lcabrales.simgas.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.MainActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.model.session.User

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: com.lcabrales.simgas.databinding.ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        setOnClickListeners()
        subscribe()
    }

    private fun subscribe() {
        viewModel.getObservableShowLoading().observe(this, Observer(this::showLoading))
        viewModel.getObservableLoginCompleted().observe(this, Observer(this::completeLogin))
    }

    private fun setOnClickListeners(){
        binding.btnLogin.setOnClickListener { performLogin() }
    }

    private fun performLogin(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.sendLoginRequest(username, password)
    }

    @UiThread
    private fun showLoading(show: Boolean) {
//        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun completeLogin(user: User){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
