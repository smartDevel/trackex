package eu.ways4.trackex.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import eu.ways4.trackex.R
import eu.ways4.trackex.common.presentation.BaseActivity
import eu.ways4.trackex.util.extensions.application
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {
    override var animationKind = ANIMATION_SLIDE_FROM_RIGHT

    private lateinit var model: eu.ways4.trackex.about.AboutActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupModel()
        setupActionBar()
    }

    private fun setupModel() {
        val factory =
            eu.ways4.trackex.about.AboutActivityModel.Factory(applicationContext.application)
        model = ViewModelProviders.of(this, factory).get(eu.ways4.trackex.about.AboutActivityModel::class.java)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, eu.ways4.trackex.about.AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}