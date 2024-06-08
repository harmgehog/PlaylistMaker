package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.recycleView.APIItunes
import com.practicum.playlistmaker.recycleView.ItunesResponse
import com.practicum.playlistmaker.recycleView.ResultResponse
import com.practicum.playlistmaker.recycleView.Track
import com.practicum.playlistmaker.recycleView.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchValue = TEXT_DEF
    private val baseUrl = "https://itunes.apple.com/"

    private lateinit var adapter: TrackAdapter
    private val tracks = ArrayList<Track>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var updateButton: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(APIItunes::class.java)

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TEXT_DEF = ""
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val cancelBtn = findViewById<ImageView>(R.id.cancel_button)
        val searchBar = findViewById<EditText>(R.id.search_bar)
        searchBar.requestFocus()
        searchBar.setText(searchValue)
        cancelBtn.setOnClickListener {
            searchBar.text.clear()
            tracks.clear()
            adapter.notifyDataSetChanged()
            showMessage("", "", ResultResponse.SUCCESS)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonVisibility(s, cancelBtn)
                searchValue = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchBar.addTextChangedListener(simpleTextWatcher)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TrackAdapter()
        adapter.trackList = tracks
        recyclerView.adapter = adapter

        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        updateButton = findViewById(R.id.updateResponse)
        updateButton.setOnClickListener {
            search()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_TEXT, TEXT_DEF)
    }
    private fun clearButtonVisibility(s: CharSequence?, v: ImageView) {
        if (s.isNullOrEmpty()) {
            v.visibility = GONE
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
        } else {
            v.visibility = VISIBLE
        }
    }
    private fun search() {
        iTunesService.search(searchValue)
            .enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>,
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showMessage("", "", ResultResponse.SUCCESS)
                            } else {
                                showMessage(
                                    getString(R.string.no_data),
                                    "",
                                    ResultResponse.EMPTY
                                )
                            }
                        }

                        else -> showMessage(
                            getString(R.string.no_internet),
                            response.code().toString(),
                            ResultResponse.ERROR
                        )
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    showMessage(
                        getString(R.string.no_internet),
                        t.message.toString(),
                        ResultResponse.ERROR
                    )
                }

            })
    }

    private fun showMessage(text: String, additionalMessage: String, errorType: ResultResponse) {
        when (errorType) {
            ResultResponse.SUCCESS -> {
                placeholderMessage.visibility = GONE
                placeholderImage.visibility = GONE
                recyclerView.visibility = VISIBLE
                updateButton.visibility = GONE
            }

            ResultResponse.EMPTY -> {
                recyclerView.visibility = GONE
                placeholderMessage.visibility = VISIBLE
                placeholderImage.visibility = VISIBLE
                updateButton.visibility = GONE
                tracks.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
                placeholderImage.setImageResource(R.drawable.ic_no_data)
                if (additionalMessage.isNotEmpty()) {
                    Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }

            ResultResponse.ERROR -> {
                recyclerView.visibility = GONE
                placeholderMessage.visibility = VISIBLE
                placeholderImage.visibility = VISIBLE
                updateButton.visibility = VISIBLE
                tracks.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
                placeholderImage.setImageResource(R.drawable.ic_no_internet)
                if (additionalMessage.isNotEmpty()) {
                    Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

}