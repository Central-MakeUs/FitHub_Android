package com.proteam.fithub.presentation.ui.write.certificate

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityWriteModifyCertificateBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.write.certificate.adapter.WriteOrModifyCertificateExerciseAdapter
import com.proteam.fithub.presentation.ui.write.certificate.viewmodel.WriteOrModifyCertificateViewModel
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenSingle
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteOrModifyCertificateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteModifyCertificateBinding
    private val viewModel: WriteOrModifyCertificateViewModel by viewModels()

    private val exerciseAdapter by lazy {
        WriteOrModifyCertificateExerciseAdapter(::onExerciseClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_modify_certificate)

        initBinding()
        initType()
        initUi()
        initBackPressed()

        observeTagInserted()
        observeExercises()
        observeUserInputContent()
    }

    private fun initBackPressed() {
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPress()
        }
    }

    fun onBackPress() {
        ComponentDialogYesNo(::finishActivity).show(supportFragmentManager, "BACK_PRESSED_WHILE_WRITE")
    }

    private fun initType() {
        intent.type?.let { viewModel.setType(it) }
        validateType()
    }


    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        binding.writeModifyCertificateRvSelectExercise.apply {
            itemAnimator = null
            adapter = exerciseAdapter
        }
    }

    private fun validateType() {
        viewModel.type.observe(this) {
            if (it == "Write") openGallery() else requestLegacyData()
            binding.writeModifyCertificateTvToolbar.text =
                if (it == "Write") resources.getString(R.string.write_certificate) else resources.getString(
                    R.string.modify_certificate
                )
        }
    }

    private fun requestLegacyData() {
        viewModel.requestLegacyData()
    }

    fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        this.requestGalleryActivity.launch(intent)
    }

    private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data?.data != null) {
                val clipData = it?.data?.clipData
                if (clipData == null) {
                    it.data!!.data?.let { it1 ->
                        viewModel.setSelectedImage(it1)
                    }
                }
            }
        }

    private fun observeTagInserted() {
        setTag()
    }

    private fun setTag() {
        binding.writeModifyCertificateEdtTag.banSpaceInput()
        observeTag()
        binding.writeModifyCertificateEdtTag.setOnEditorActionListener { _, i, keyEvent ->
            if(i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
                saveTag()
            }
            return@setOnEditorActionListener true
        }
    }

    private fun saveTag() {
        viewModel.initTag()
        binding.writeModifyCertificateEdtTag.setText("")
    }

    private fun observeTag() {
        viewModel.userInputTagList.observe(this) {
            val count = it.size - binding.writeModifyCertificateChipgroupTag.childCount
            for (i in it.size - (count+1) until it.size) {
                addTagChip(it[i])
            }
        }
    }

    private fun addTagChip(chipText : String) {
        binding.writeModifyCertificateChipgroupTag.addView(Chip(this).apply {
            text = chipText
            tag = chipText
            setChipStyles()

            setOnCloseIconClickListener {
                binding.writeModifyCertificateChipgroupTag.apply {
                    viewModel.removeFromTagList(this.indexOfChild(it))
                    removeView(it)
                }
            }
        })
    }

    private fun Chip.setChipStyles() {
        this.apply {
            setTextAppearance(R.style.Certificate_Chip_Text_Style)
            setChipBackgroundColorResource(R.color.bg_default)
            setChipStrokeColorResource(R.color.icon_disabled)
            setCloseIconResource(R.drawable.ic_edt_delete)
            closeIconTint = null
            chipStrokeWidth = 0.5F
            isCloseIconVisible = true
        }
    }

    private fun observeExercises() {
        viewModel.exercises.observe(this) {
            exerciseAdapter.sports = it
            exerciseAdapter.selected = returnSelectedList(it.size)
            exerciseAdapter.notifyItemRangeChanged(0, it.size)
        }

        viewModel.userSelectExercise.observe(this) {
            val position = exerciseAdapter.sports.indexOf(exerciseAdapter.sports.find { it2 -> it2.name == it.name })
            exerciseAdapter.selected[position] = true
            exerciseAdapter.notifyItemChanged(position)
        }
    }

    private fun onExerciseClicked(exercise : ResponseExercises.ExercisesList) {
        viewModel.setUserSelectedExercise(exercise)
    }

    private fun observeUserInputContent() {
        viewModel.userInputContent.observe(this) {
            viewModel.checkSaveEnabled()
        }
    }

    fun onSaveClicked() {


        CoroutineScope(Dispatchers.Default).launch {
            viewModel.requestPostCertificate(Convert().also { viewModel.setPathForDelete(it) }.getAbsolutePath())
        }

        viewModel.requestPostCertificate((viewModel.userSelectedImage.value as Uri).getAbsolutePath())
        observeSaveState()
    }

    private fun Convert() : Uri {
        val res = (viewModel.userSelectedImage.value!!.toString().toUri().getAbsolutePath()).ConvertWhenSingle(this@WriteOrModifyCertificateActivity)

        return "content://${res.substring(9)}".toUri()
    }



    private fun Uri.getAbsolutePath() : String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c : Cursor = contentResolver.query(this, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    private fun observeSaveState() {
        viewModel.saveState.observe(this) {
            if(it == 2000) {
                viewModel.imagePaths.value?.deletePic(this@WriteOrModifyCertificateActivity)
                finishActivity()
            }
        }
    }

    private fun finishActivity() {
        finish()
    }

    private fun EditText.banSpaceInput() {
        this.filters = arrayOf(object : InputFilter {
            override fun filter(
                p0: CharSequence,
                p1: Int,
                p2: Int,
                p3: Spanned?,
                p4: Int,
                p5: Int
            ): CharSequence {
                if(p0 == "" || !p0.contains(" ")) {
                    return p0
                }
                return p0.trim()
            }
        })
    }

    /** Dummy **/

    private fun returnSelectedList(range : Int) : MutableList<Boolean> = mutableListOf<Boolean>().apply {
        for(i in 0 until range) {
            add(false)
        }
    }

}