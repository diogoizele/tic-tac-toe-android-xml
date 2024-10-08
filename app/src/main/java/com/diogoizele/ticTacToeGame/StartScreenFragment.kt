package com.diogoizele.ticTacToeGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.diogoizele.ticTacToeGame.databinding.FragmentStartScreenBinding
import kotlinx.coroutines.launch


class StartScreenFragment : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding
    private lateinit var localStorageService: LocalStorageService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartScreenBinding.inflate(inflater, container, false)
        localStorageService = LocalStorageService(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewGame.setOnClickListener {
            lifecycleScope.launch {
                localStorageService.clearData()
            }
            Navigation.findNavController(view).navigate(R.id.action_startScreenFragment_to_identificationFragment)
        }
    }
}