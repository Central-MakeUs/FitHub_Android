package com.proteam.fithub.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityMainBinding
import com.proteam.fithub.presentation.ui.alarm.AlarmActivity
import com.proteam.fithub.presentation.ui.bookmark.BookMarkActivity
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.main.comming_soon.FragmentCommingSoon
import com.proteam.fithub.presentation.ui.main.community.CommunityFragment
import com.proteam.fithub.presentation.ui.main.home.HomeFragment
import com.proteam.fithub.presentation.ui.main.mypage.MyPageFragment
import com.proteam.fithub.presentation.ui.manageinfo.ManageMyInfoActivity
import com.proteam.fithub.presentation.ui.mylevel.MyLevelActivity
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkIntents()

        initBinding()
        initUi()

        checkType()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestCheckAlarmAvailable()
    }

    private fun checkType() {
        if(intent.type == "MY_PAGE") openMyPageFragment()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.mainLayoutBottomNavigation.apply {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.main_bottom_home -> changeFragments(HomeFragment(), "LOGO")
                    R.id.main_bottom_community -> changeFragments(CommunityFragment(), "COMMUNITY")
                    R.id.main_bottom_around -> changeFragments(FragmentCommingSoon(), "AROUND")
                    R.id.main_bottom_my -> changeFragments(MyPageFragment(), "LOGO")
                }
                return@setOnItemSelectedListener true
            }

            selectedItemId = R.id.main_bottom_home

            setOnItemReselectedListener { return@setOnItemReselectedListener }
        }
    }

    private fun changeFragments(fragment : Fragment, tag : String) {
        viewModel.setFragmentTag(tag)
        viewModel.requestCheckAlarmAvailable()
        supportFragmentManager.beginTransaction().replace(R.id.main_layout_container, fragment)
            .commit()
    }

    fun openWriteOrModifyCertificate(tag : String) {
        startActivity(Intent(this, WriteOrModifyCertificateActivity::class.java).setType(tag))
    }

    private fun openCertificateDetailActivity(index : Int) {
        requestOpenMyPage.launch(Intent(this, ExerciseCertificateDetailActivity::class.java).setType(index.toString()))
    }

    fun openWriteOrModifyBoard(tag : String) {
        startActivity(Intent(this, WriteOrModifyBoardActivity::class.java).setType(tag))
    }

    fun openBoardDetailActivity(index : Int) {
        requestOpenMyPage.launch(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    fun openSearchActivity() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    fun openMyLevelActivity() {
        startActivity(Intent(this, MyLevelActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
    }

    fun openBookmarkActivity() {
        requestProcessFinished.launch(Intent(this, BookMarkActivity::class.java))
    }

    fun openAlarmActivity() {
        startActivity(Intent(this, AlarmActivity::class.java))
    }

    fun openMyInfoActivity() {
        requestGotoSignIn.launch(Intent(this, ManageMyInfoActivity::class.java))
    }

    private val requestOpenMyPage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if (state == true) {
                    openMyPageFragment()
                }
            }
        }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode) {
                1001 -> {
                    changeFragments(CommunityFragment(), "SEARCH")
                    binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_community
                }
            }
            if (it.resultCode == 1001) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    finish()
                }
            }
        }

    private val requestGotoSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    finish()
                    startActivity(Intent(this, SocialSignInActivity::class.java))
                }
            }
        }

    private fun checkIntents() {
        intent.extras?.let {
            when(it.getString("View")) {
                "ARTICLE" -> {
                    viewModel.requestAlarmRead(it.getString("AlarmPK")!!.toInt())
                    openBoardDetailActivity(it.getString("PK")!!.toInt())
                    binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_community
                }
                "RECORD" -> {
                    viewModel.requestAlarmRead(it.getString("AlarmPK")!!.toInt())
                    openCertificateDetailActivity(it.getString("PK")!!.toInt())
                    binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_community
                }
            }
        }
    }

    fun openMyPageFragment() {
        binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_my
    }

    fun openAroundFragment() {
        binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_around
    }
}