package com.diogoizele.ticTacToeGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.diogoizele.ticTacToeGame.databinding.FragmentConfirmBinding
import kotlinx.coroutines.launch

class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding
    private lateinit var localStorageService: LocalStorageService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmBinding.inflate(inflater, container, false)
        localStorageService = LocalStorageService(requireContext())

        lifecycleScope.launch {
            localStorageService.localStorageFlow.collect { localStorage ->
                binding.tvPlayerNameO.text = localStorage.playerO.name
                binding.tvPlayerNameX.text = localStorage.playerX.name

                binding.tvPlayerNameO.visibility = View.VISIBLE
                binding.tvPlayerNameX.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.btnStart.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_confirmFragment_to_gameScreenFragment)
        }

    }
}