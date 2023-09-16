package com.proteam.fithub.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.ActivityMainBinding
import com.proteam.fithub.presentation.ui.alarm.AlarmActivity
import com.proteam.fithub.presentation.ui.bookmark.BookMarkActivity
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.main.arounds.AroundsFragment
import com.proteam.fithub.presentation.ui.main.arounds.viewmodel.AroundsViewModel
import com.proteam.fithub.presentation.ui.main.community.CommunityFragment
import com.proteam.fithub.presentation.ui.main.home.HomeFragment
import com.proteam.fithub.presentation.ui.main.mypage.MyPageFragment
import com.proteam.fithub.presentation.ui.manageinfo.ManageMyInfoActivity
import com.proteam.fithub.presentation.ui.mylevel.MyLevelActivity
import com.proteam.fithub.presentation.ui.search.arounds.SearchAroundsActivity
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private val aroundsViewModel : AroundsViewModel by viewModels()

    private val aroundsFragment by lazy {
        AroundsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

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
        binding.aroundViewModel = aroundsViewModel
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
                    R.id.main_bottom_community -> changeFragments(CommunityFragment(), "SEARCH_COMMUNITY")
                    R.id.main_bottom_around -> changeFragments(AroundsFragment(), "SEARCH_AROUND")
                    R.id.main_bottom_my -> changeFragments(MyPageFragment(), "LOGO")
                }
                return@setOnItemSelectedListener true
            }

            selectedItemId = R.id.main_bottom_home

            setOnItemReselectedListener { return@setOnItemReselectedListener }
        }
    }

    private fun changeFragments(fragment : Fragment, tag : String) {
        binding.mainTvSearchHint.setTextColor(resources.getColor(R.color.text_info, null))

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

    fun openSearchActivity(isCommunity : Boolean) {
        if(isCommunity) startActivity(Intent(this, SearchActivity::class.java))
        else requestSearchAround.launch(Intent(this, SearchAroundsActivity::class.java)
            .putExtra("Latitude", aroundsViewModel.currentLocation.value?.latitude)
            .putExtra("Longitude", aroundsViewModel.currentLocation.value?.longitude)
        )
    }

    private val requestSearchAround =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {

                val result = it.data!!.extras?.getSerializable("list")
                aroundsViewModel.setCurrentMarkerItems(result as List<ResponseLocationData.LocationItems>)

                val state = it.data!!.extras?.getString("keyword")
                state?.let {
                    aroundsViewModel.setUserInputKeyword(it)
                    binding.mainTvSearchHint.apply {
                        setTextColor(resources.getColor(R.color.text_default, null))
                        text = it
                    }
                }
            }
        }

    fun initAroundKeyword() {
        aroundsViewModel.initKeyword()
        binding.mainTvSearchHint.apply {
            setTextColor(resources.getColor(R.color.text_info, null))
            text = resources.getString(R.string.around_hint_toolbar_edt)
        }
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

    fun openAroundFragmentFromHome(index : Int) {
        binding.mainLayoutBottomNavigation.selectedItemId = R.id.main_bottom_around
        initMapWithFilter(index)
    }

    private fun initMapWithFilter(index : Int) {
        aroundsViewModel.apply {
            initTargetLocation()
            setSelectedFilter(index)
        }
    }
}