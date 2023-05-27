package com.example.pocsmsfill

import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.pocsmsfill.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), MyBroadcastListener {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Not Granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            validatePermission()
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun validatePermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECEIVE_SMS
            ) -> {
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.RECEIVE_SMS
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MySmsReceiver.setBroadcastListener(this)
        requireContext().registerReceiver(
            MySmsReceiver(),
            IntentFilter("com.example.ACTION_SMS_RECEIVED")
        )
    }

    override fun onPause() {
        super.onPause()
        MySmsReceiver.setBroadcastListener(this)
        requireContext().unregisterReceiver(MySmsReceiver())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBroadcastReceived(data: String) {
        binding.txtCode1.setText(data[0].toString())
        binding.txtCode2.setText(data[1].toString())
        binding.txtCode3.setText(data[2].toString())
        binding.txtCode4.setText(data[3].toString())
        binding.txtCode5.setText(data[4].toString())
        binding.txtCode6.setText(data[5].toString())
    }

}