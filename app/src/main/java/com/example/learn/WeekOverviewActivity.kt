package com.example.learn

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class WeekOverviewActivity : AppCompatActivity() {

    private lateinit var recyclerViewWeekOverview: RecyclerView
    private lateinit var weekOverviewAdapter: WeekOverviewAdapter
    private lateinit var weekOverviewList: MutableList<WeekOverviewEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_overview)

        recyclerViewWeekOverview = findViewById(R.id.recyclerViewWeekOverview)

        // Haal de pushupList op uit de Singleton klasse
        val pushupList: MutableList<PushUpEntry> = PushupData.pushupList

        // Bereken de weekoverzicht data
        weekOverviewList = calculateWeeklyTotals(pushupList)

        // Initialiseer de RecyclerView
        recyclerViewWeekOverview.layoutManager = LinearLayoutManager(this)
        weekOverviewAdapter = WeekOverviewAdapter(weekOverviewList)
        recyclerViewWeekOverview.adapter = weekOverviewAdapter

        // Terug knop
        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun calculateWeeklyTotals(pushupList: MutableList<PushUpEntry>): MutableList<WeekOverviewEntry> {
        val weekMap = mutableMapOf<String, Int>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (entry in pushupList) {
            val date = dateFormat.parse(entry.date)
            calendar.time = date!!
            val weekYear = "Week " + calendar.get(Calendar.WEEK_OF_YEAR).toString() + ", " + calendar.get(Calendar.YEAR).toString()

            weekMap[weekYear] = weekMap.getOrDefault(weekYear, 0) + entry.sets.sum()
        }

        val weekOverviewList = mutableListOf<WeekOverviewEntry>()
        for ((week, total) in weekMap) {
            weekOverviewList.add(WeekOverviewEntry(week, total))
        }

        return weekOverviewList
    }
}

data class WeekOverviewEntry(val week: String, val total: Int)
