package com.ijiki16.messengerapp.searchview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.ijiki16.messengerapp.databinding.ViewSearchMessengerBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MessengerSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ViewSearchMessengerBinding.inflate(LayoutInflater.from(context), this)
    private var searchRequestListener: OnNewSearchRequestListener? = null
    val searchTerm = if(binding.searchText.text?.length?:0 > 2) binding.searchText.text.toString() else ""

    init {
        binding
            .searchText
            .textChanges()
            .debounce(1500)
            .onEach {
                searchRequestListener?.onSearchRequest(it)
            }.launchIn(GlobalScope)

    }

    @ExperimentalCoroutinesApi
    fun AppCompatEditText.textChanges(): Flow<String> = callbackFlow {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 2)
                    offer(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(null) }
    }

    fun setOnNewSearchRequestListener(listener: OnNewSearchRequestListener) {
        searchRequestListener = listener
    }

}