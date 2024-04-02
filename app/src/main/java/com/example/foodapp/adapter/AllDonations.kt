package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.DonationItemBinding
import com.example.foodapp.model.Donation
import com.example.foodapp.repository.RepositoryImpl
import com.example.foodapp.utils.Resource
import com.example.foodapp.fragments.home.AdminHomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllDonations(private val instance: AdminHomeFragment) : ListAdapter<Donation, AllDonations.DonationsViewHolder>(
    DonationDiffCallback
) {
    object DonationDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationsViewHolder {
        return DonationsViewHolder.from(parent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DonationsViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to delete this Donation?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes"
            ) { _: DialogInterface?, _: Int ->
                CoroutineScope(Dispatchers.IO).launch {
                    var id = item.id
                    RepositoryImpl.getInstance().deleteDonation(id!!){
                        when(it){
                            is Resource.Error -> {

                            }
                            is Resource.Success -> {
                                Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged()
                                instance.fetchData()
                            }
                            is Resource.Error -> {

                            }
                            Resource.Loading -> {

                            }
                        }
                    }
                }
            }
            builder.setNegativeButton("No"
            ) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
            true
        }
        holder.bind(item)
    }

    class DonationsViewHolder private constructor(private val binding: DonationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Donation) {
            binding.foodItemName.text = item.name
            binding.description.text = item.foodItem
            binding.phoneNumber.text = item.phoneNumber
        }

        companion object {
            fun from(parent: ViewGroup): DonationsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DonationItemBinding.inflate(layoutInflater, parent, false)
                return DonationsViewHolder(binding)
            }
        }
    }
}
