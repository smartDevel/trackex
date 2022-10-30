package eu.ways4.trackex.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.ways4.trackex.Application
import eu.ways4.trackex.R
import eu.ways4.trackex.home.presentation.HomeActivity
import eu.ways4.trackex.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (isUserOnboarded()) {
            HomeActivity.start(this)
        } else {
            OnboardingActivity.start(this)
        }

        finish()
    }

    private fun isUserOnboarded(): Boolean =
        (application as eu.ways4.trackex.Application).preferenceDataSource.getIsUserOnboarded(applicationContext)
}