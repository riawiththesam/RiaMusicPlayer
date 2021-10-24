package jp.riawithapps.riamusicplayer.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.plus

fun ViewModel.createScope() = viewModelScope + CoroutineExceptionHandler()