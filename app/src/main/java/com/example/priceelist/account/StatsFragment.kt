package com.example.priceelist.account

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Client
import com.example.priceelist.data.Stats
import com.example.priceelist.databinding.FragmentStatsBinding
import com.example.priceelist.databinding.HistoryListItemBinding
import java.text.NumberFormat


class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Stats

    private val viewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    private fun bindInvNumCreated(stat: Int) {
        binding.invoicesCreated.text = stat.toString()
    }

    private fun bindRctNumCreated(stat: Int) {
        binding.receiptsCreated.text = stat.toString()
    }

    private fun bindInvNumSent(stat: Int) {
        binding.invoicesSent.text = stat.toString()
    }

    private fun bindRctNumSent(stat: Int) {
        binding.receiptsSent.text = stat.toString()
    }

    private fun bindInvNumCleared(stat: Int) {
        binding.invoicesCleared.text = stat.toString()
    }


    private fun bindInvSumCreated(stat: Double) {
        binding.invAmtCreated.text = displayCurrencyValue(stat)
    }

    private fun bindRctSumCreated(stat: Double) {
        binding.rctTotalCreated.text = displayCurrencyValue(stat)
    }

    private fun bindInvSumSent(stat: Double) {
        binding.invAmountSent.text = displayCurrencyValue(stat)
    }

    private fun bindRctSumSent(stat: Double) {
        binding.rctAmountSent.text = displayCurrencyValue(stat)
    }

    private fun bindInvSumCleared(stat: Double) {
        binding.invAmountCleared.text = displayCurrencyValue(stat)
    }

    private fun displayCurrencyValue(number: Double): String {
        return NumberFormat.getCurrencyInstance().format(number)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatsHistoryListAdapter {        }
        binding.historyRecyclerView.adapter = adapter
        viewModel.allHistory.observe(this.viewLifecycleOwner) { stats ->
            stats.let {
                adapter.submitList(it)
            }
        }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.historyRecyclerView.setHasFixedSize(true)

        binding.allHistoryList.setOnClickListener {
            binding.scrollView.visibility = View.GONE
            binding.historyRecyclerView.visibility = View.VISIBLE
        }

        viewModel.getInvNumCreated().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvNumCreated(0)
            } else {
                bindInvNumCreated(stat)
            }
        }

        viewModel.getRctNumCreated().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindRctNumCreated(0)
            } else {
                bindRctNumCreated(stat)
            }
        }

        viewModel.getInvNumSent().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvNumSent(0)
            } else {
                bindInvNumSent(stat)
            }
        }

        viewModel.getRctNumSent().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindRctNumSent(0)
            } else {
                bindRctNumSent(stat)
            }
        }

        viewModel.getInvNumCleared().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvNumCleared(0)
            } else {
                bindInvNumCleared(stat)
            }
        }

        viewModel.getInvSumCreated().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvSumCreated(0.00)
            } else {
                bindInvSumCreated(stat)
            }
        }

        viewModel.getRctSumCreated().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindRctSumCreated(0.00)
            } else {
                bindRctSumCreated(stat)
            }
        }

        viewModel.getInvSumSent().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvSumSent(0.00)
            } else {
                bindInvSumSent(stat)
            }
        }

        viewModel.getRctSumSent().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindRctSumSent(0.00)
            } else {
                bindRctSumSent(stat)
            }
        }

        viewModel.getInvSumCleared().observe(this.viewLifecycleOwner) { stat ->
            if (stat == null) {
                bindInvSumCleared(0.00)
            } else {
                bindInvSumCleared(stat)
            }
        }

        binding.backButton.setOnClickListener {
            if (binding.historyRecyclerView.visibility == View.VISIBLE) {
                binding.historyRecyclerView.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } else {
                this.findNavController().navigateUp()
            }
        }

    }

}

private class StatsHistoryListAdapter(private val onItemClicked: (Stats) -> Unit) :
    ListAdapter<Stats, StatsHistoryListAdapter.StatsHistoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatsHistoryViewHolder {
        return StatsHistoryViewHolder(
            HistoryListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: StatsHistoryViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class StatsHistoryViewHolder(private var binding: HistoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Stats) {
            //val liDrawable: Drawable = R.drawable.id_circle_pl.toDrawable()
            //val inDrawable: Drawable = R.drawable.id_circle_in.toDrawable()
            //val reDrawable: Drawable = R.drawable.id_circle_re.toDrawable()

            when (item.itemType) {
                "Li" -> {
                    binding.imageView.setImageResource(R.drawable.id_circle_pl)
                    binding.nameInitial.text = "Li"
                }
                "In" -> {
                    binding.imageView.setImageResource(R.drawable.id_circle_in)
                    binding.nameInitial.text = "In"
                }
                "Re" -> {
                    binding.imageView.setImageResource(R.drawable.id_circle_re)
                    binding.nameInitial.text = "Re"
                }
            }

            when (item.action) {
                "cleared" -> binding.actionIcon.setImageResource(R.drawable.ic_check_clear)
                "sent" -> binding.actionIcon.setImageResource(R.drawable.ic_menu_send)
                "downloaded" -> binding.actionIcon.setImageResource(R.drawable.ic_download)
            }

            binding.title.text = item.itemName
            binding.date.text = item.actionDate
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Stats>() {
            override fun areContentsTheSame(oldItem: Stats, newItem: Stats): Boolean {
                return oldItem.itemName == newItem.itemName
            }

            override fun areItemsTheSame(oldItem: Stats, newItem: Stats): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}