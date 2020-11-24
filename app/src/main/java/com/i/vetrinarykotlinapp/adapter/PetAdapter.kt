package com.i.vetrinarykotlinapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.i.vetrinarykotlinapp.R
import com.i.vetrinarykotlinapp.utils.Util
import com.i.vetrinarykotlinapp.model.Pet

class PetAdapter(
    petList: ArrayList<Pet>, private val listener: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.MyViewHolder>() {
    private var arrayListDetails: ArrayList<Pet> = petList


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        val item = arrayListDetails[listPosition]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount(): Int {
        return arrayListDetails.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.textView_pet)
        private var imgPet: ImageView = itemView.findViewById(R.id.img_pet)
        fun bind(item: Pet) {
            title.text = item.title
            Util.displayImage(item.imgUrl, imgPet)

        }

    }

}