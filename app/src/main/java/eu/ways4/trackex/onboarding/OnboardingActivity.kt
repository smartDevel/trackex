package eu.ways4.trackex.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.ways4.trackex.R
import eu.ways4.trackex.common.presentation.BaseActivity

class OnboardingActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(intent)
        }
    }
}