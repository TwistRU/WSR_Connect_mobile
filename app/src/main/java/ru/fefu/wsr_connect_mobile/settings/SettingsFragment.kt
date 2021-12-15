package ru.fefu.wsr_connect_mobile.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSettingsBinding


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewPagerSettings.adapter = SettingsViewPagerAdapter(this@SettingsFragment)
            dotsIndicator.setViewPager2(viewPagerSettings)
        }
    }

    inner class SettingsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SettingsSilenceFragment()
                1 -> SettingsPlug1Fragment()
                else -> SettingsPlug2Fragment()
            }
        }
    }
}