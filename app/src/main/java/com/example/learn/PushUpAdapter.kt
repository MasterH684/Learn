package com.example.learn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PushupAdapter(
    private val pushupList: List<PushUpEntry>,
    private val updateTotalCallback: () -> Unit
) : RecyclerView.Adapter<PushupAdapter.PushupViewHolder>() {

    class PushupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val editTextSet1: EditText = itemView.findViewById(R.id.editTextSet1)
        val editTextSet2: EditText = itemView.findViewById(R.id.editTextSet2)
        val editTextSet3: EditText = itemView.findViewById(R.id.editTextSet3)
        val editTextSet4: EditText = itemView.findViewById(R.id.editTextSet4)
        val editTextSet5: EditText = itemView.findViewById(R.id.editTextSet5)
        val buttonUpdate: Button = itemView.findViewById(R.id.buttonUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushupViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pushups, parent, false)
        return PushupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PushupViewHolder, position: Int) {
        val currentItem = pushupList[position]
        holder.textViewDate.text = currentItem.date

        // Set existing sets to EditTexts
        val sets = listOf(
            holder.editTextSet1,
            holder.editTextSet2,
            holder.editTextSet3,
            holder.editTextSet4,
            holder.editTextSet5
        )

        sets.forEach { it.setText("") }

        currentItem.sets.forEachIndexed { index, setCount ->
            if (index < 5) {
                sets[index].setText(setCount.toString())
            }
        }

        // Save updated sets when update button is clicked
        holder.buttonUpdate.setOnClickListener {
            currentItem.sets.clear()
            sets.forEach { editText ->
                val setCount = editText.text.toString().toIntOrNull()
                if (setCount != null) {
                    currentItem.sets.add(setCount)
                }
            }
            System.out.println("Postition: $position")
            // Onderstaande onderdelen zijn onderdeel van recyvlerview.jave
            // Deze zorgen ervoor dat de 'pushupadapter' lambda functie uitgevoerd worden!!
            notifyItemChanged(position)
            updateTotalCallback()
        }
    }

    override fun getItemCount() = pushupList.size
}
