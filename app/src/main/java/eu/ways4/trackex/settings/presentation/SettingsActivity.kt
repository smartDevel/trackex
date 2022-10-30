package eu.ways4.trackex.settings.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.ways4.trackex.R
import eu.ways4.trackex.common.presentation.BaseActivity

class SettingsActivity : BaseActivity() {

    override var animationKind = ANIMATION_SLIDE_FROM_RIGHT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}