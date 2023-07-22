package com.proteam.fithub.presentation.ui.write.board

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityWriteModifyBoardBinding
import com.proteam.fithub.databinding.ActivityWriteModifyCertificateBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.gallery.view.CustomGalleryActivity
import com.proteam.fithub.presentation.ui.write.board.adapter.WriteOrModifyBoardImageAdapter
import com.proteam.fithub.presentation.ui.write.board.viewmodel.WriteOrModifyBoardViewModel
import com.proteam.fithub.presentation.ui.write.certificate.adapter.WriteOrModifyCertificateExerciseAdapter
import com.proteam.fithub.presentation.ui.write.certificate.viewmodel.WriteOrModifyCertificateViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
@AndroidEntryPoint
class WriteOrModifyBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteModifyBoardBinding
    private val viewModel: WriteOrModifyBoardViewModel by viewModels()

    private val exerciseAdapter by lazy {
        WriteOrModifyCertificateExerciseAdapter(::onExerciseClicked)
    }

    private val imageAdapter by lazy {
        WriteOrModifyBoardImageAdapter(::onDropSelectedImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_modify_board)

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
        binding.writeModifyBoardRvSelectExercise.apply {
            itemAnimator = null
            adapter = exerciseAdapter
        }
        binding.writeModifyBoardRvImages.apply {
            itemAnimator = null
            adapter = imageAdapter
        }
    }

    private fun validateType() {
        viewModel.type.observe(this) {
            binding.writeModifyBoardTvToolbar.text = if (it == "Write") resources.getString(R.string.write_board) else resources.getString(R.string.modify_board)
        }
    }

    fun openGallery() {
        val intent = Intent(this, CustomGalleryActivity::class.java)
        this.requestGalleryActivity.launch(intent)
        observeSelectedImage()
    }

    private fun observeSelectedImage() {
        viewModel.userSelectedImages.observe(this) {
            imageAdapter.paths = it.map { it.toString().toUri() } as MutableList
            imageAdapter.notifyDataSetChanged()
        }
    }

    private fun onDropSelectedImage(position : Int) {
        viewModel.dropSelectedImages(position)
    }

    private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.setSelectedImages(it.data?.extras?.getStringArrayList("Images") as MutableList<Any>)
            }
        }

    private fun observeTagInserted() {
        setTag()
    }

    private fun setTag() {
        binding.writeModifyBoardEdtTag.banSpaceInput()
        binding.writeModifyBoardEdtTag.setOnEditorActionListener { _, i, keyEvent ->
            if(i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
                addTagChip()
                initTag()
            }
            return@setOnEditorActionListener true
        }
    }

    private fun addTagChip() {
        binding.writeModifyBoardChipgroupTag.addView(Chip(this).apply {
            text = viewModel.userInputTag.value
            setChipStyles()

            setOnCloseIconClickListener {
                binding.writeModifyBoardChipgroupTag.apply {
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

    private fun initTag() {
        viewModel.initTag()
        binding.writeModifyBoardEdtTag.setText("")
    }

    private fun observeExercises() {
        viewModel.exercises.observe(this) {
            exerciseAdapter.sports = it
            exerciseAdapter.selected = returnSelectedList(it.size)
            exerciseAdapter.notifyItemRangeChanged(0, it.size)
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
        /*viewModel.postCertificate((viewModel.userSelectedImage.value as Uri).getAbsolutePath())
        observeSaveState() */
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