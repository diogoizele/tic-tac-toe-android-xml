package com.diogoizele.ticTacToeGame

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.diogoizele.ticTacToeGame.databinding.FragmentIdentificationBinding
import kotlinx.coroutines.launch

class IdentificationFragment : Fragment() {

    private lateinit var binding: FragmentIdentificationBinding
    private lateinit var localStorageService: LocalStorageService

    private var currentPlayer = Player.X
    private var playerXName: String? = null
    private var playerOName: String? = null
    private var firstNameConfirmed = false
    private var isEnableTextChange = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentificationBinding.inflate(inflater, container, false)
        localStorageService = LocalStorageService(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableConfirmBtn()
        bindHandlers(view)
    }

    override fun onResume() {
        super.onResume()
        selectX()
    }

    private fun disableConfirmBtn() {
        binding.btnConfirm.isEnabled = false
        binding.btnConfirm.alpha = .7f
    }

    private fun enableConfirmBtn() {
        binding.btnConfirm.isEnabled = true
        binding.btnConfirm.alpha = 1f
    }

    private fun selectPlayer(player: Player) {
        currentPlayer = player
        binding.etPlayerName.clearFocus()

        if (player == Player.X) {
            selectX()
            if (!TextUtils.isEmpty(playerXName)) {
                enableConfirmBtn()
            }
        } else {
            selectO()
            if (TextUtils.isEmpty(playerOName)) {
                disableConfirmBtn()
            }
        }
    }

    private fun selectX() {
        binding.etPlayerName.hint = getString(R.string.player_x_name)
        binding.llPlayerXIdentification.alpha = 1f
        binding.llPlayerOIdentification.alpha = .7f

        isEnableTextChange = false
        playerXName.let {
            binding.etPlayerName.setText(it)
        }
        isEnableTextChange = true
    }

    private fun selectO() {
        binding.etPlayerName.hint = getString(R.string.player_o_name)
        binding.llPlayerOIdentification.alpha = 1f
        binding.llPlayerXIdentification.alpha = .7f


        isEnableTextChange = false
        playerOName.let {
            binding.etPlayerName.setText(it)
        }
        isEnableTextChange = true
    }

    private fun setPlayerName(name: String) {
        if (isBothNamesFilled()) {
            enableConfirmBtn()
        }

        if (currentPlayer == Player.X) {
            playerXName = name
            firstNameConfirmed = true
        } else {
            playerOName = name
        }

        if (!TextUtils.isEmpty(name)) {
            enableConfirmBtn()
        } else {
            disableConfirmBtn()
        }
    }

    private fun bindHandlers(view: View) {
        binding.llPlayerXIdentification.setOnClickListener{
            selectPlayer(Player.X)
        }

        binding.llPlayerOIdentification.setOnClickListener {
            selectPlayer(Player.O)
        }

        binding.etPlayerName.addTextChangedListener {
            if (isEnableTextChange) {
                setPlayerName(it.toString())
            }
        }

        binding.btnConfirm.setOnClickListener {
            if (isBothNamesFilled()) {
                saveNamesInLocalStorage()
                Navigation.findNavController(view)
                    .navigate(R.id.action_identificationFragment_to_confirmFragment2)
            } else if (!TextUtils.isEmpty(playerXName)) {
                selectPlayer(Player.O)
            } else if (TextUtils.isEmpty(playerXName) && !TextUtils.isEmpty(playerOName)) {
                selectPlayer(Player.X)
            }
        }
    }

    private fun isBothNamesFilled(): Boolean {
        return !TextUtils.isEmpty(playerXName) && !TextUtils.isEmpty(playerOName)

    }

    private fun saveNamesInLocalStorage() {
        if (isBothNamesFilled()) {
            lifecycleScope.launch {
                localStorageService.savePlayersName(playerXName!!, playerOName!!)
            }
        }
    }
}