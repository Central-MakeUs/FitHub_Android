package com.proteam.fithub.presentation.ui.sign.up.social

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySocialSignUpBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.sign.result.SignUpResultActivity
import com.proteam.fithub.presentation.ui.sign.up.common.agreement.AgreementFragment
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenSingle
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialSignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySocialSignUpBinding
    private val viewModel : SocialSignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_sign_up)

        initBinding()
        initUi()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initUi() {
        initDefaultFragment()
    }

    private fun initDefaultFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.social_sign_up_layout_container, AgreementFragment(), "Social").commit()
    }

    fun changeFragments(fragment : Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(fragment.id.toString()).add(R.id.social_sign_up_layout_container, fragment, "Social").commit()
    }

    fun onBackPress() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction().commit()
        }
    }

    fun requestSocialSignUp() {
        viewModel.requestSocialSignUp(Convert()?.also { viewModel.setPathForDelete(it) }?.getAbsolutePath())
        observeSignUpResult()
    }

    private fun observeSignUpResult() {
        viewModel.signUpState.observe(this) {
            deletePhoto()
            when(it) {
                2000 -> {
                    openSignUpResultActivity()
                }
                else -> ComponentAlertToast().show(supportFragmentManager, "$it")
            }
        }
    }

    private fun openSignUpResultActivity() {
        setResult(RESULT_OK, Intent(this, SocialSignInActivity::class.java).putExtra("state", true))
        startActivity(Intent(this, SignUpResultActivity::class.java).setType(viewModel.userInputNickName.value))
        finish()
    }


    /** Resize Profile Image **/

    private fun Convert() : Uri? {
        val res = viewModel.userInputImage.value?.let { (it.toUri().getAbsolutePath())?.ConvertWhenSingle(this) }
        return if(res == null) null else "content://${res.substring(9)}".toUri()
    }

    private fun Uri?.getAbsolutePath() : String? {
        return if(this == null) {
            null
        } else {
            val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            val c: Cursor = contentResolver.query(this, proj, null, null, null)!!
            val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            c.moveToFirst()
            c.getString(index)
        }
    }

    private fun deletePhoto() {
        viewModel.imagePaths.value?.deletePic(this)
    }
}