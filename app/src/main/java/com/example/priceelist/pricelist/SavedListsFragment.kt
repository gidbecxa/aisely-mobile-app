package com.example.priceelist.pricelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priceelist.PricelistApplication
import com.example.priceelist.data.Pricelist
import com.example.priceelist.databinding.FragmentSavedListsBinding

const val TAGMAIN = "SavedListsFragment"

/**
 * A simple [Fragment] subclass.
 */
class SavedListsFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        Log.d(TAGMAIN, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAGMAIN, "onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAGMAIN, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAGMAIN, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAGMAIN, "onDestroy Called")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAGMAIN, "onViewStateRestored Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAGMAIN, "onCreate Called")
    }

    //Fragment's core parts commence here...

    private val viewModel: PricelistViewModel by activityViewModels {
        PricelistViewModelFactory(
            (activity?.application as PricelistApplication).database.pricelistDao()
        )
    }

    private var _binding: FragmentSavedListsBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Pricelist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAGMAIN, "onCreateView Called")
        _binding = FragmentSavedListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAGMAIN, "onViewCreated Called")

        val adapter = PricelistsListAdapter ({
            val action = SavedListsFragmentDirections.actionSavedListsFragmentToMakeListFragment(it.id) //id is the argument to be retrieved in MakeListFragment
            this.findNavController().navigate(action)
        },
            {
                val action = SavedListsFragmentDirections.actionSavedListsFragmentToMenuDialogFragment(it.id)
                findNavController().navigate(action)
            })

        binding.recyclerView.adapter = adapter
            viewModel.allLists.observe(this.viewLifecycleOwner) {lists -> lists.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getLastList().observe(this.viewLifecycleOwner) {list ->
            if (list == null) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
        }


        binding.fab.setOnClickListener {
            val action = SavedListsFragmentDirections.actionSavedListsFragmentToMakeListFragment(
                //default value defined in nav_graph is passed in as the argument to be retrieved by MakeListFragment
            )
            this.findNavController().navigate(action)
        }

    }

}