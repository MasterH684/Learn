package com.example.learn

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class PushupWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val sharedPreferences = context.getSharedPreferences("pushupData", Context.MODE_PRIVATE)
            val last7DaysPushups = sharedPreferences.getInt("totalLast7Days", 0)
            val prev7DaysPushups = sharedPreferences.getInt("totalPrev7Days", 0)

            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.textViewLast7DaysPushups, "L7D: $last7DaysPushups")
            views.setTextViewText(R.id.textViewPrev7DaysPushups, "P7D: $prev7DaysPushups")

            val intent = Intent(context, PushUps::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.textViewLast7DaysPushups, pendingIntent)
            views.setOnClickPendingIntent(R.id.textViewPrev7DaysPushups, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
