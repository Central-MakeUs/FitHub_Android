package com.proteam.fithub.presentation.ui.managewrite.certificate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestDeleteMyCertificate
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.domain.repository.CertificateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMyCertificateViewModel @Inject constructor(private val certificateRepository: CertificateRepository) :
    ViewModel() {

    private val _selectItems = MutableLiveData<MutableList<Int>>().apply { value = mutableListOf() }
    val selectItems: LiveData<MutableList<Int>> = _selectItems

    private val _deleteStatus = MutableLiveData<Int>()
    val deleteStatus: LiveData<Int> = _deleteStatus

    var isAllClicked = MutableLiveData<Boolean>(false)

    fun requestMyCertificateData(filter: Int): Flow<PagingData<ResponseMyCertificateData.ResultMyCertificateData>> {
        return certificateRepository.requestMyCertificateData(filter)
    }

    fun manageSelectItems(index: Int, selected: Boolean) {
        if (selected) _selectItems.value?.add(index)
        else _selectItems.value?.remove(index)
    }

    fun manageAllSelectedItems(indexes: List<Int>) {
        if (isAllClicked.value!!) _selectItems.value = indexes.toMutableList()
        else _selectItems.value = mutableListOf()
    }

    fun requestDeleteMyCertificate() {
        viewModelScope.launch {
            selectItems.value?.let {
                Log.e("----", "requestDeleteMyCertificate: $it", )
                certificateRepository.requestDeleteMyCertificateData(RequestDeleteMyCertificate(it))
                    .onSuccess { _deleteStatus.value = it.code }
            }
        }
    }


}