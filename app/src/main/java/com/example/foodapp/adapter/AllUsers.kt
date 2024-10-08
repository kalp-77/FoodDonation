package com.example.foodapp.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.UserItemBinding
import com.example.foodapp.model.User
import com.example.foodapp.repository.RepositoryImpl
import com.example.foodapp.utils.Resource
import com.example.foodapp.fragments.home.AdminHomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AllUsers(val instance: AdminHomeFragment) : ListAdapter<User, AllUsers.UsersViewHolderl>(
    UsersDiffUtil
) {
    class UsersViewHolderl (private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.userName.text = "Name:- " +item.name
            binding.phoneNumber.text = "Phone:- " +item.phone
        }
    }


    object UsersDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.phone == newItem.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolderl {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolderl(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolderl, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to delete this user?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes"
            ) { _: DialogInterface?, _: Int ->
                CoroutineScope(Dispatchers.IO).launch {
                    var id = ""
                    RepositoryImpl.getInstance().getUserId(item.email!!){
                       id = it
                    }
                    Log.d("UserID", id)
                    RepositoryImpl.getInstance().deleteUser(id){
                        when(it){
                            is Resource.Error -> {}
                            is Resource.Success -> {
                                Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged()
                                instance.fetchData()
                            }
                            is Resource.Error -> {}
                            Resource.Loading -> {}
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
}