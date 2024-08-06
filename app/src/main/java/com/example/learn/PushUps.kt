package com.example.learn

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Intent


class PushUps : AppCompatActivity() {

    private lateinit var editTextPushups: EditText
    private lateinit var textViewDate: TextView
    private lateinit var buttonSelectDate: Button
    private lateinit var buttonSavePushups: Button
    private lateinit var buttonWeekOverview: Button
    private lateinit var textViewTotalL7D: TextView
    private lateinit var textViewTotalP7D: TextView
    private lateinit var recyclerViewPushups: RecyclerView
    private lateinit var pushupAdapter: PushupAdapter
    private lateinit var pushupList: MutableList<PushUpEntry>

    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pushups)

        // Koppel de UI-elementen
        editTextPushups = findViewById(R.id.editTextPushups)
        textViewDate = findViewById(R.id.textViewDate)
        buttonSelectDate = findViewById(R.id.buttonSelectDate)
        buttonSavePushups = findViewById(R.id.buttonSavePushups)
        buttonWeekOverview = findViewById(R.id.buttonWeekOverview)
        textViewTotalL7D = findViewById(R.id.textViewTotalL7D)
        textViewTotalP7D = findViewById(R.id.textViewTotalP7D)
        recyclerViewPushups = findViewById(R.id.recyclerViewPushups)

        // Laad de opgeslagen gegevens
        loadData()

        // Initialiseer de RecyclerView
        recyclerViewPushups.layoutManager = LinearLayoutManager(this)

        // Blijkbaar, wordt wat hieronder staat uitgevoerd als er een update is gepusht door de adapter
        pushupAdapter = PushupAdapter(pushupList) {
                updateLast7DaysTotal() // deze 2 onderdelen zijn onderdeel van de callback lambda aanroep
                saveData()
            }

        recyclerViewPushups.adapter = pushupAdapter

        // Update de datumweergave
        updateDate()

        // Toevoegen van de OnClickListener voor de datum selecteren
        buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        buttonSavePushups.setOnClickListener {
            savePushupEntry()
        }

        buttonWeekOverview.setOnClickListener {
            PushupData.pushupList = pushupList
            val intent = Intent(this, WeekOverviewActivity::class.java)
            startActivity(intent)
        }


        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()  // Terug naar de vorige Activity
        }

        // Update de totale push-ups na het laden van de gegevens
        updateLast7DaysTotal()
    }

    private fun updateDate(calendar: Calendar = selectedDate) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        textViewDate.text = dateFormat.format(calendar.time)
    }

    private fun savePushupEntry() {
        val pushupCount = editTextPushups.text.toString().toIntOrNull()
        val date = textViewDate.text.toString()

        if (pushupCount != null && pushupCount > 0) {
            val existingEntry = pushupList.find { it.date == date }

            if (existingEntry != null) {
                if (existingEntry.sets.size < 5) {
                    existingEntry.sets.add(pushupCount)
                } else {
                    // Notify user that maximum of 5 sets is reached
                    return
                }
            } else {
                val newEntry = PushUpEntry(date, mutableListOf(pushupCount))
                pushupList.add(newEntry)
            }

            sortPushupList()
            pushupAdapter.notifyDataSetChanged()
            updateLast7DaysTotal()
            saveData()
        }
    }

    private fun updateLast7DaysTotal() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.time

        val totalLast7Days = pushupList.filter {
            val entryDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date)
            entryDate != null && entryDate.after(sevenDaysAgo)
        }.sumOf { it.sets.sum() }

        textViewTotalL7D.text = "Total Push-ups Last 7 Days: $totalLast7Days"
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val fourteenDaysAgo = calendar.time

        val totalPrev7Days = pushupList.filter {
            val entryDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date)
            entryDate != null && entryDate.after(fourteenDaysAgo) && entryDate.before(sevenDaysAgo)
        }.sumOf { it.sets.sum() }

        textViewTotalP7D.text = "Total Push-ups Previous 7 Days: $totalPrev7Days"
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            updateDate()
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("pushupData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(pushupList)
        editor.putString("pushupList", json)
        editor.apply()
        Log.d("PushUps", "Data saved: $json")
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("pushupData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("pushupList", null)
        val type = object : TypeToken<MutableList<PushUpEntry>>() {}.type
        if (json != null) {
            pushupList = gson.fromJson(json, type)
            Log.d("PushUps", "Data loaded: $json")
        } else {
            pushupList = mutableListOf()
        }
        sortPushupList() // Sorteer de lijst na het laden van de gegevens
    }

    private fun sortPushupList() {
        pushupList.sortByDescending {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date)
        }
    }

}
