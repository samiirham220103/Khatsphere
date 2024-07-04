package com.example.khatsphere

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var launcher: ActivityResultLauncher<ScanOptions>
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var cardList: ArrayList<CardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnScan = findViewById(R.id.btnScan)
        recyclerView = findViewById(R.id.recyclerView)

        btnScan.setOnClickListener {
            startScanner()
        }

        launcher = registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                AlertDialog.Builder(this)
                    .setTitle("Scan Result")
                    .setMessage(result.contents)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        setupRecyclerView()
    }

    private fun startScanner() {
        val options = ScanOptions().apply {
            setPrompt("Volume up to flash on")
            setBeepEnabled(true)
            setOrientationLocked(true)
            setCaptureActivity(StartScan::class.java)
        }
        launcher.launch(options)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)

        cardList = arrayListOf(
            CardItem(R.drawable.naskah_1, "Naskah"),
            CardItem(R.drawable.mushaf, "Mushaf"),
            CardItem(R.drawable.dekorasi, "Dekorasi"),
            CardItem(R.drawable.naskah_1, "Kontemporer")
            // Tambahkan item lainnya sesuai kebutuhan
        )

        cardAdapter = CardAdapter(cardList)
        recyclerView.adapter = cardAdapter
    }
    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle Home action
                    true
                }
                R.id.nav_scan -> {
                    startScanner()
                    true
                }
                R.id.nav_explore -> {
                    // Handle Explore action
                    true
                }
                else -> false
            }
        }
    }
}
