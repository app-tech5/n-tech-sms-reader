package com.ntech.smsreader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntech.smsreader.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SmsDatabaseHelper
    private lateinit var adapter: SmsAdapter

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = REQUIRED_PERMISSIONS.all { results[it] == true }
        if (granted) {
            showPermissionState(granted = true)
            refreshSmsList()
        } else {
            showPermissionState(granted = false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = SmsDatabaseHelper(this)
        adapter = SmsAdapter()
        binding.smsList.layoutManager = LinearLayoutManager(this)
        binding.smsList.adapter = adapter

        binding.refreshButton.setOnClickListener { refreshSmsList() }
        binding.grantPermissionButton.setOnClickListener { requestSmsPermissions() }

        ensurePermissions()
    }

    override fun onResume() {
        super.onResume()
        if (hasSmsPermissions()) {
            refreshSmsList()
        }
    }

    private fun ensurePermissions() {
        if (hasSmsPermissions()) {
            showPermissionState(granted = true)
            refreshSmsList()
        } else {
            showPermissionState(granted = false)
            requestSmsPermissions()
        }
    }

    private fun requestSmsPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun hasSmsPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showPermissionState(granted: Boolean) {
        binding.permissionCard.visibility = if (granted) View.GONE else View.VISIBLE
        binding.statusText.text = if (granted) {
            getString(R.string.status_listening)
        } else {
            getString(R.string.status_permission_required)
        }
    }

    private fun refreshSmsList() {
        if (!hasSmsPermissions()) return

        val records = database.getAll()
        adapter.submitList(records)

        binding.emptyText.visibility = if (records.isEmpty()) View.VISIBLE else View.GONE
        binding.smsList.visibility = if (records.isEmpty()) View.GONE else View.VISIBLE
        binding.smsCountText.text = getString(R.string.sms_count, records.size)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )
    }
}

class SmsAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    private val items = mutableListOf<SmsRecord>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE)

    fun submitList(records: List<SmsRecord>) {
        items.clear()
        items.addAll(records)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SmsViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sms, parent, false)
        return SmsViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.bind(items[position], dateFormat)
    }

    class SmsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val senderText: android.widget.TextView = itemView.findViewById(R.id.senderText)
        private val bodyText: android.widget.TextView = itemView.findViewById(R.id.bodyText)
        private val dateText: android.widget.TextView = itemView.findViewById(R.id.dateText)

        fun bind(record: SmsRecord, dateFormat: SimpleDateFormat) {
            senderText.text = record.sender
            bodyText.text = record.body
            dateText.text = dateFormat.format(Date(record.receivedAt))
        }
    }
}
