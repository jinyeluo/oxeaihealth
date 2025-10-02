package com.oxeai.health.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oxeai.health.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    private var binding: FragmentNotificationsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()

        val textView = binding!!.textNotifications
        notificationsViewModel.text.observe(getViewLifecycleOwner(), Observer { text: String? -> textView.text = text })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}