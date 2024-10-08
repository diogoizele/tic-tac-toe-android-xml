package com.diogoizele.ticTacToeGame

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.diogoizele.ticTacToeGame.databinding.FragmentGameScreenBinding
import kotlinx.coroutines.launch
import java.util.Objects


class GameScreenFragment : Fragment() {

    private lateinit var binding: FragmentGameScreenBinding
    private lateinit var localStorageService: LocalStorageService
    private var storage: LocalStorage? = null
    private var currentPlayer = Player.X
    private var winner: Player? = null
    private var hasDraw = false
    private var remainingMoves = 9
    private val board: Array<Player?> = arrayOfNulls(9)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameScreenBinding.inflate(inflater, container, false)
        localStorageService = LocalStorageService(requireContext())

        binding.scoreboardViewPlayerX.isSelected = true

        binding.scoreboardViewPlayerX.initializeScore()
        binding.scoreboardViewPlayerO.initializeScore()

        lifecycleScope.launch {
            localStorageService.localStorageFlow.collect { localStorage ->
                val playerX = localStorage.playerX
                val playerO = localStorage.playerO

                storage = localStorage

                binding.tvPlayerNameX.text = playerX.name
                binding.tvPlayerNameO.text = playerO.name

                binding.scoreboardViewPlayerX.setScore(playerX.score)
                binding.scoreboardViewPlayerO.setScore(playerO.score)

                binding.tvPlayerNameX.visibility = View.VISIBLE
                binding.tvPlayerNameO.visibility = View.VISIBLE
            }
        }

        bindingButtons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRestart.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_gameScreenFragment_to_identificationFragment)
        }
    }

    private fun bindingButtons() {
        val buttons = listOf(
            binding.btnSlot1,
            binding.btnSlot2,
            binding.btnSlot3,
            binding.btnSlot4,
            binding.btnSlot5,
            binding.btnSlot6,
            binding.btnSlot7,
            binding.btnSlot8,
            binding.btnSlot9,
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                if (canPlay(button))  {
                    remainingMoves--
                    updateBoard(buttons.indexOf(button))
                    button.setImageResource(getCurrentPlayerIcon())
                    val winner = checkResult()

                    if (Objects.isNull(winner)) {
                        toggleCurrentPlayer()
                    }
                }
            }
        }
    }

    private fun canPlay(button: ImageButton): Boolean {
        return button.drawable == null && winner == null && remainingMoves > 0
    }

    private fun getCurrentPlayerIcon(): Int {
        return if (isX()) R.drawable.player_x else R.drawable.player_o
    }

    private fun toggleCurrentPlayer() {
        if (isX()) {
            currentPlayer = Player.O
            binding.scoreboardViewPlayerO.isSelected = true
            binding.scoreboardViewPlayerX.isSelected = false
        } else {
            currentPlayer = Player.X
            binding.scoreboardViewPlayerX.isSelected = true
            binding.scoreboardViewPlayerO.isSelected = false
        }
    }


    private fun isX(): Boolean {
        return currentPlayer == Player.X
    }

    private fun updateBoard(index: Int) {
        if (board[index] == null) {
            board[index] = currentPlayer
        }
    }

    private fun checkResult(): Player? {
        var winner: Player? = null
        val buttons = listOf(
            binding.btnSlot1,
            binding.btnSlot2,
            binding.btnSlot3,
            binding.btnSlot4,
            binding.btnSlot5,
            binding.btnSlot6,
            binding.btnSlot7,
            binding.btnSlot8,
            binding.btnSlot9,
        )

        val combinations = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6),
        )

        combinations.forEach { combination ->
            run {
                if (compareCombinations(combination[0], combination[1]) &&
                    compareCombinations(combination[1],combination[2])) {
                    winner = currentPlayer
                    this.winner = currentPlayer

                    binding.scoreboardViewPlayerX.isSelected = false
                    binding.scoreboardViewPlayerO.isSelected = false

                    lifecycleScope.launch {
                        storage?.let {
                            val currentScore = if (winner == Player.X) it.playerX.score else it.playerO.score
                            val newScore = currentScore + 1
                            if (winner == Player.X) binding.scoreboardViewPlayerX.setScore(newScore) else binding.scoreboardViewPlayerO.setScore(newScore)
                            localStorageService.savePlayerScore(winner!!, newScore)

                        }
                    }


                    val color = R.color.green

                    for (i: Int in 0..8) {
                        if (i != combination[0] && i != combination[1] && i != combination[2]) {
                            println(i)
                            buttons[i].alpha = .7f
                        }
                    }
                    buttons[combination[0]].setColorFilter(ContextCompat.getColor(requireContext(), color), PorterDuff.Mode.SRC_IN)
                    buttons[combination[1]].setColorFilter(ContextCompat.getColor(requireContext(), color), PorterDuff.Mode.SRC_IN)
                    buttons[combination[2]].setColorFilter(ContextCompat.getColor(requireContext(), color), PorterDuff.Mode.SRC_IN)


                    Handler().postDelayed(this::showBackdropResult, 2500)
                }
            }
        }

        return winner
    }

    private fun compareCombinations(combination1: Int, combination2: Int): Boolean {
        return board[combination1] != null && board[combination1] == board[combination2]
    }

    private fun showBackdropResult() {

        val playerIcon = if (winner == Player.X) R.drawable.player_x else R.drawable.player_o
        val viewContent = binding.clBackdropContent

        binding.ivIcon.setImageResource(playerIcon)

        binding.backdrop.visibility = View.VISIBLE
        viewContent.visibility = View.VISIBLE

        viewContent.alpha = 0f
        viewContent.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(null)

    }
}