package com.proteam.fithub.presentation.ui.main.mypage

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentMypageBinding
import com.proteam.fithub.presentation.util.LoadingDialog
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.alarm_setting.AlarmSettingActivity
import com.proteam.fithub.presentation.ui.changeexercise.ChangeExerciseActivity
import com.proteam.fithub.presentation.ui.main.mypage.adapter.MyPageExerciseAdapter
import com.proteam.fithub.presentation.ui.main.mypage.adapter.MyPageUpperMenuAdapter
import com.proteam.fithub.presentation.ui.main.mypage.viewmodel.MyPageViewModel
import com.proteam.fithub.presentation.ui.manageinfo.ManageMyInfoActivity
import com.proteam.fithub.presentation.ui.managewrite.ManageMyWriteActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenSingle
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private lateinit var binding : FragmentMypageBinding
    private val viewModel : MyPageViewModel by viewModels()
    private val exerciseAdapter by lazy {
        MyPageExerciseAdapter(::onChangeMainExerciseClicked)
    }
    private val upperMenuAdapter by lazy {
        MyPageUpperMenuAdapter(returnUpperMenuTitles(), ::onUpperMenuClicked)
    }

    private lateinit var deletePath : Uri
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        observeMyPageData()
        initBinding()
        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestMyPageData().also { showLoadingDialog() }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initExercisePager()
        initUpperMenuRV()
    }

    private fun initExercisePager() {
        binding.fgMypageVpExercises.apply {
            adapter = exerciseAdapter
            TabLayoutMediator(binding.fgMypageTabExerciseIndicator, this) {tab, _ ->
            }.attach()
        }
    }

    private fun initUpperMenuRV() {
        binding.fgMypageRvUpperMenu.adapter = upperMenuAdapter
    }

    private fun observeMyPageData() {
        viewModel.myPageData.observe(viewLifecycleOwner) {
            dismissLoadingDialog()

            binding.myPageData = it

            binding.fgMypageComponentLevel.getLevel(it.myInfo.mainExerciseInfo.level, it.myInfo.mainExerciseInfo.gradeName)
            binding.fgMypageComponentExercise.getExercise(it.myInfo.mainExerciseInfo.category)

            exerciseAdapter.apply {
                exercises = it.myExerciseList.toMutableList()
                notifyItemRangeChanged(0, it.myExerciseList.size)
            }
        }
    }

    private fun onChangeMainExerciseClicked() {
        startActivity(Intent(requireActivity(), ChangeExerciseActivity::class.java))
    }

    fun onManageMyWriteClicked() {
        startActivity(Intent(requireActivity(), ManageMyWriteActivity::class.java))
    }

    fun onProfileChangeToDefaultClicked() {
        viewModel.requestProfileToDefault()
        observeDeleteProfileStatus()
    }

    private fun observeDeleteProfileStatus() {
        viewModel.deleteProfileStatus.observe(viewLifecycleOwner) {
            if(it == 2000) viewModel.requestMyPageData()
        }
    }

    private fun onUpperMenuClicked(position : Int) {
        when(position) {
            0 -> requestGotoSignIn.launch(Intent(requireActivity(), ManageMyInfoActivity::class.java))
            1 -> startActivity(Intent(requireActivity(), AlarmSettingActivity::class.java))
            2 -> {} //학원 등록 요청
            3 -> {} //약관 및 정책
        }
    }

    private val requestGotoSignIn=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(), SocialSignInActivity::class.java))
                }
            }
        }

    fun onGalleryOpen() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        this.requestGalleryActivity.launch(intent)
    }

    private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data?.data != null) {
                if (it?.data?.clipData == null) {
                    it.data!!.data?.let { it1 ->
                        deletePath = it1.Convert()
                        viewModel.setUserSelectedProfile(deletePath.getAbsolutePath())
                        observeChangeStatus()
                    }
                }
            }
        }

    private fun observeChangeStatus() {
        viewModel.changeProfileStatus.observe(viewLifecycleOwner) {
            if(it == 2000) {
                viewModel.requestMyPageData()
                deletePath.deletePic(requireActivity())
            }
        }
    }

    fun onLogoutClicked() {
        ComponentDialogYesNo(::onLogOut).show(requireActivity().supportFragmentManager, "LOG_OUT")
    }

    private fun onLogOut() {
        viewModel.requestLogOut()
        startActivity(Intent(requireActivity(), SocialSignInActivity::class.java))
        requireActivity().finish()
    }

    private fun Uri.Convert() : Uri {
        val res = (this.getAbsolutePath()).ConvertWhenSingle(requireContext())

        return "content://${res.substring(9)}".toUri()
    }

    private fun Uri.getAbsolutePath() : String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c : Cursor = requireActivity().contentResolver.query(this, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    /** Dummy **/
    private fun returnUpperMenuTitles() : List<String> = listOf("개인 정보 설정", "알림 설정", "학원 등록 요청", "약관 및 정책")


    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(requireActivity().supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}