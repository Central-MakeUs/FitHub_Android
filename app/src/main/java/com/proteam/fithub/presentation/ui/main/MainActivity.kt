package com.proteam.fithub.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityMainBinding
import com.proteam.fithub.presentation.ui.bookmark.BookMarkActivity
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.main.community.CommunityFragment
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import com.proteam.fithub.presentation.ui.main.home.HomeFragment
import com.proteam.fithub.presentation.ui.mylevel.MyLevelActivity
import com.proteam.fithub.presentation.ui.search.SearchActivity
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    private val communityViewModel : CommunityViewModel by viewModels()

    var needNotifyCertificate : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBinding()
        initUi()

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
                    R.id.main_bottom_home -> changeFragments(HomeFragment(), "HOME")
                    R.id.main_bottom_community -> changeFragments(CommunityFragment(), "COMMUNITY")
                }
                return@setOnItemSelectedListener true
            }

         selectedItemId = R.id.main_bottom_home
        }
    }

    private fun changeFragments(fragment : Fragment, tag : String) {
        viewModel.setFragmentTag(tag)
        supportFragmentManager.beginTransaction().replace(R.id.main_layout_container, fragment).commit()
    }

    fun openWriteOrModifyCertificate(tag : String) {
        startActivity(Intent(this, WriteOrModifyCertificateActivity::class.java).setType(tag))
    }

    fun openCertificateDetailActivity(index : Int) {
        startActivity(Intent(this, ExerciseCertificateDetailActivity::class.java).setType(index.toString()))
    }

    fun openWriteOrModifyBoard(tag : String) {
        startActivity(Intent(this, WriteOrModifyBoardActivity::class.java).setType(tag))
    }

    fun openBoardDetailActivity(index : Int) {
        startActivity(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    fun openSearchActivity() {
        startActivity(Intent(this, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
    }

    fun openMyLevelActivity() {
        startActivity(Intent(this, MyLevelActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
    }

    fun openBookmarkActivity() {
        requestProcessFinished.launch(Intent(this, BookMarkActivity::class.java))
        //startActivity(Intent(this, BookMarkActivity::class.java))
    }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode) {
                1001 -> {
                    //북마크 -> 커뮤니티
                    changeFragments(CommunityFragment(), "COMMUNITY")
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


}