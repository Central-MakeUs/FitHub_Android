package com.proteam.fithub.presentation.ui.write.board

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityWriteModifyBoardBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.gallery.view.CustomGalleryActivity
import com.proteam.fithub.presentation.ui.write.board.adapter.WriteOrModifyBoardImageAdapter
import com.proteam.fithub.presentation.ui.write.board.viewmodel.WriteOrModifyBoardViewModel
import com.proteam.fithub.presentation.ui.write.certificate.adapter.WriteOrModifyCertificateExerciseAdapter
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenList
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
import com.proteam.fithub.presentation.util.LoadingDialog
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

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initType()
        initUi()
        initBackPressed()
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

            if (it == "Write") initWhenWrite() else requestLegacyData()
        }
    }

    private fun initWhenWrite() {
        observeTagInserted()
        observeExercises()
        observeTag()
        observeUserInputContent()
    }

    fun openGallery() {
        val intent = Intent(this, CustomGalleryActivity::class.java)
        this.requestGalleryActivity.launch(intent)
        observeSelectedImage()
    }

    private fun observeSelectedImage() {
        viewModel.userSelectedImages.observe(this) {
            imageAdapter.paths = it.map { it.toString() } as MutableList
            imageAdapter.notifyDataSetChanged()


            val params = binding.writeModifyBoardRvImages.layoutParams
            params.width = (it.size * 110).toDp()
            binding.writeModifyBoardRvImages.layoutParams = params
        }
    }

    private fun Int.toDp() : Int {
        return (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    private fun onDropSelectedImage(position: Int) {
        viewModel.dropSelectedImages(position)
        imageAdapter.notifyItemChanged(position)
    }

    private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.setSelectedImages(
                    it.data?.extras?.getStringArrayList("Images")
                        ?.map { it.toUri() } as MutableList<Uri>)
            }
        }

    private fun observeTagInserted() {
        binding.writeModifyBoardEdtTag.banSpaceInput()
        binding.writeModifyBoardEdtTag.setOnKeyListener { view, i, keyEvent ->
            if(binding.writeModifyBoardEdtTag.text.isNotEmpty() && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                initTag()
            }
            return@setOnKeyListener true
        }
    }


    private fun addTagChip(chipText: String) {
        binding.writeModifyBoardChipgroupTag.addView(Chip(this).apply {
            text = chipText
            tag = chipText
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
        when(intent.type) {
            "Write" -> requestPostArticle()
            else -> requestModifyArticle()
        }
        observeSaveState()
    }

    private fun requestPostArticle() {
        showLoadingDialog()

        if (viewModel.userSelectedImages.value.isNullOrEmpty()) {
            viewModel.requestPostArticle(null)
        } else {
            CoroutineScope(Dispatchers.Default).launch {
                viewModel.requestPostArticle(Convert().also { viewModel.setPathForDelete(it) }
                    .map { it.getAbsolutePath() })
            }
        }
    }

    suspend fun Convert(): MutableList<Uri> {
        val a = CoroutineScope(Dispatchers.Default).async {
            viewModel.userSelectedImages.value!!.map { it.toString() }
                .ConvertWhenList(this@WriteOrModifyBoardActivity).map { it.toUri() }
        }
        val res = a.await().toMutableList()
        return res.map { "content://${it.toString().substring(9)}".toUri() } as MutableList
    }


    private fun Uri.getAbsolutePath(): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor = contentResolver.query(this, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }


    private fun observeSaveState() {
        viewModel.saveState.observe(this) {

            dismissLoadingDialog()

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


    /** Modify **/

    private fun requestLegacyData() {
        viewModel.requestLegacyData()
        showLoadingDialog()
        observeLegacy()
        observeSelectedImage()
        observeExercises()
        observeTag()
        observeTagInserted()
    }

    private fun observeLegacy() {
        viewModel.legacyArticleData.observe(this) {
            dismissLoadingDialog()

            viewModel.setLegacyToUi()

            val selectedPosition =
                exerciseAdapter.sports.indexOfFirst { it2 -> it2.id == it.articleCategory.categoryId }
            exerciseAdapter.selected[selectedPosition] = true
            exerciseAdapter.notifyItemChanged(selectedPosition)
        }
    }

    private fun observeTag() {
        viewModel.userInputTagList.observe(this) {
            Log.e("----", "observeTag: $it", )
            if (!it.isNullOrEmpty()) {
                val count = it.size - binding.writeModifyBoardChipgroupTag.childCount
                for (i in it.size - (count + 1) until it.size) {
                    addTagChip(it[i])
                }
            }
        }
    }

    private fun requestModifyArticle() {
        showLoadingDialog()

        viewModel.userSelectedImages.value.let {
            if(it.isNullOrEmpty()) {
                viewModel.requestModifyArticle(null)
            } else if(it.first().toString().contains("https://")) {
                viewModel.requestModifyArticle(null)
            }else {
                CoroutineScope(Dispatchers.Default).launch {
                    viewModel.requestModifyArticle(Convert().also { viewModel.setPathForDelete(it) }
                        .map { it.getAbsolutePath() })
                }
            }
        }
    }


    /** Dummy **/

    private fun returnSelectedList(range: Int): MutableList<Boolean> =
        mutableListOf<Boolean>().apply {
            for (i in 0 until range) {
                add(false)
            }
        }

    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()

}