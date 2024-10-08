package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.databinding.ReceiveRowLayoutBinding
import com.example.foodapp.fragments.receive.ReceiveFragmentDirections
import com.example.foodapp.model.Donation
import com.example.foodapp.repository.MainRepository
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

import androidx.appcompat.app.AppCompatActivity

class ReceiveAdapter :
    ListAdapter<Donation, ReceiveAdapter.ReceiveViewHolder>(ReceiveViewHolder.ReceiveDiffUtil) {


    class ReceiveViewHolder(private val binding: ReceiveRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        object ReceiveDiffUtil : DiffUtil.ItemCallback<Donation>() {
            override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
                return oldItem == newItem
            }
        }

        @Inject
        lateinit var mainRepository: MainRepository

        @SuppressLint("SetTextI18n")
        fun bind(donation: Donation?) {
            binding.donatedFoodItem.text = "Item:- "+ donation?.foodItem?.capitalize()
            binding.donatedFoodItemDescription.text = "Address:- "+ donation?.description?.capitalize()
            binding.donorPhoneNumber.setOnClickListener {
                //start phone call
                val phoneNumber = donation?.phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                binding.root.context.startActivity(intent)
            }
            binding.donorLocation.setOnClickListener {

                //navigate to maps fragment
                val action =
                    ReceiveFragmentDirections.actionReceiveFragmentToDonorLocationFragment(
                    donation!!
                )

                binding.root.findNavController().navigate(action)
            }

            binding.receiveCheckBox.setOnClickListener {

                val alertDialog = AlertDialog.Builder(binding.root.context)
                    .setTitle("Are you sure?")
                    .setMessage("Marking this as received means that you have already contacted the donor and organized on how to receive the donation or you have already received it. \n\nProceed?")
                    .setIcon(R.drawable.ic_warning)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        binding.receiveCheckBox.isChecked = true
                        binding.receiveCheckBox.isEnabled = false
                        binding.receiveCheckBox.text = "Received"

                        val hashMap = HashMap<String, Any>()
                        hashMap["received"] = true
                        hashMap["id"] = donation?.id!!
                        hashMap["foodItem"] = donation.foodItem!!
                        hashMap["description"] = donation.description!!
                        hashMap["phoneNumber"] = donation.phoneNumber!!
                        hashMap["location"] = donation.location!!
                        hashMap["name"] = donation.name!!
                        val db = FirebaseFirestore.getInstance()
                        //update the database
                        db.collection("donations").document(donation.donorId!!)
                            .update(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "Donation marked as received",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "${it.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                    .setNegativeButton("No") { _, i ->
                        binding.receiveCheckBox.isChecked = false
                        binding.receiveCheckBox.isEnabled = true

                    }.create()
                alertDialog.show()

            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiveViewHolder {
        return ReceiveViewHolder(
            ReceiveRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReceiveViewHolder, position: Int) {
        val item = getItem(position)
        val cardView: MaterialCardView = holder.itemView.findViewById(R.id.main_card)

        //Set the checkbox to true if the donation is received
        if (item.received == true) {
            val cb = holder.itemView.findViewById<CheckBox>(R.id.receiveCheckBox)


            cb.isChecked = true
            cb.isEnabled = false
            cb.text = "Received"
            cardView.strokeColor = Color.GREEN
        }
        else{
            cardView.strokeColor = Color.parseColor("#FF474C")
        }
        holder.bind(item)
    }


}