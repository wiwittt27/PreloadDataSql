package com.alawiyaa.preloaddata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alawiyaa.preloaddata.model.MahasiswaModel
import kotlinx.android.synthetic.main.item_mahasiswa_row.view.*

class MahasiswaAdapter:RecyclerView.Adapter<MahasiswaAdapter.MyAdapter>() {

    private val listMahasiswa = ArrayList<MahasiswaModel>()
    fun setData(listMahasiswa: ArrayList<MahasiswaModel>) {
        if (listMahasiswa.size > 0) {
            this.listMahasiswa.clear()
        }
        this.listMahasiswa.addAll(listMahasiswa)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaAdapter.MyAdapter {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa_row,parent,false)

        return MyAdapter(view)
    }

    override fun getItemCount(): Int = listMahasiswa.size
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int =position

    override fun onBindViewHolder(holder: MahasiswaAdapter.MyAdapter, position: Int) {
        holder.bind(listMahasiswa[position])
    }

    inner class MyAdapter(itemView:View) : RecyclerView.ViewHolder(itemView){

        fun bind(mahasiswa: MahasiswaModel) {
            with(itemView){
                txt_nim.text = mahasiswa.nim
                txt_name.text = mahasiswa.name
            }
        }

    }
}