package com.pocketherbarium.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.navigation.Navigator
import com.pocketherbarium.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    val navigator: Navigator by lazy {
        Navigator(supportFragmentManager, R.id.fragment_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ServiceLocator.init(applicationContext)
        super.onCreate(savedInstanceState)
        val inflated = ActivityMainBinding.inflate(layoutInflater)
        binding = inflated
        setContentView(inflated.root)
        if (savedInstanceState == null) {
            navigator.showSplash()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
