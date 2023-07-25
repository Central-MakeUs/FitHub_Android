package com.proteam.fithub.presentation.ui.write.board

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityWriteModifyBoardBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.gallery.view.CustomGalleryActivity
import com.proteam.fithub.presentation.ui.write.board.adapter.WriteOrModifyBoardImageAdapter
import com.proteam.fithub.presentation.ui.write.board.viewmodel.WriteOrModifyBoardViewModel
import com.proteam.fithub.presentation.ui.write.certificate.adapter.WriteOrModifyCertificateExerciseAdapter
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenList
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        ComponentDialogYesNo(::finishActivity).show(
            supportFragmentManager,
            "BACK_PRESSED_WHILE_WRITE"
        )
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
            binding.writeModifyBoardTvToolbar.text =
                if (it == "Write") resources.getString(R.string.write_board) else resources.getString(
                    R.string.modify_board
                )
        }
    }

    fun openGallery() {
        val intent = Intent(this, CustomGalleryActivity::class.java)
        this.requestGalleryActivity.launch(intent)
        observeSelectedImage()
        /*val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        this.requestGalleryActivity.launch(intent)
        */*/
    }

    /*private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val clipData = it?.data?.clipData
                val test = mutableListOf<Uri>()
                for(i in 0 until clipData!!.itemCount) {
                    test.add(clipData.getItemAt(i).uri)
                }
                viewModel.setSelectedImages(test)
            }
        }*/



    private fun observeSelectedImage() {
        viewModel.userSelectedImages.observe(this) {
            imageAdapter.paths = it.map { it.toString() } as MutableList
            imageAdapter.notifyItemChanged(0, it.size)
        }
    }

    private fun onDropSelectedImage(position: Int) {
        viewModel.dropSelectedImages(position)
    }

    private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.setSelectedImages(it.data?.extras?.getStringArrayList("Images")?.map { it.toUri() } as MutableList<Uri>)
            }
        }

    private fun observeTagInserted() {
        setTag()
    }

    private fun setTag() {
        binding.writeModifyBoardEdtTag.banSpaceInput()
        binding.writeModifyBoardEdtTag.setOnEditorActionListener { text, i, keyEvent ->
            if (text.text.isNotEmpty() && i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
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

    private fun onExerciseClicked(exercise: ResponseExercises.ExercisesList) {
        viewModel.setUserSelectedExercise(exercise)
    }

    private fun observeUserInputContent() {
        viewModel.userInputContent.observe(this) {
            viewModel.checkSaveEnabled()
        }
    }

    fun onSaveClicked() {
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.postArticle(Convert().also { viewModel.setPathForDelete(it) }.map { it.getAbsolutePath() })
        }

        observeSaveState()
    }

    suspend fun Convert() : MutableList<Uri> {
        val a = CoroutineScope(Dispatchers.Default).async {
            viewModel.userSelectedImages.value!!.map { it.toString() }
                .ConvertWhenList(this@WriteOrModifyBoardActivity).map { it.toUri() }
        }
        val res = a.await().toMutableList()
        return res.map { "content://${it.toString().substring(9)}".toUri() } as MutableList
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
            if (it == 2000) {
                viewModel.imagePaths.value?.map { it.deletePic(this@WriteOrModifyBoardActivity) }
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
                if (p0 == "" || !p0.contains(" ")) {
                    return p0
                }
                return p0.trim()
            }
        })
    }

    /** Dummy **/

    private fun returnSelectedList(range: Int): MutableList<Boolean> =
        mutableListOf<Boolean>().apply {
            for (i in 0 until range) {
                add(false)
            }
        }

}