package com.example.todolist.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.AddNewTask
import com.example.todolist.MainActivity
import com.example.todolist.Model.ToDoModel
import com.example.todolist.R
import com.example.todolist.Utils.DataBaseHelper

class ToDoAdapter(private val myDB: DataBaseHelper, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = toBoolean(item.status)
        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myDB.updateStatus(item.id, 1)
            } else {
                myDB.updateStatus(item.id, 0)
            }
        }
    }

    private fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    fun context(): Context {
        return activity
    }

    fun setTasks(mList: List<ToDoModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun deletTask(position: Int) {
        val item = mList[position]
        myDB.deleteTask(item.id)
        val mutableList = mList.toMutableList()
        mutableList.removeAt(position)
        notifyDataSetChanged()
    }

    fun editItem(position: Int) {
        val item = mList[position]
        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }
        val task = AddNewTask()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
    }
}
