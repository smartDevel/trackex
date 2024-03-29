package eu.ways4.trackex.home.presentation

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import eu.ways4.trackex.R
import eu.ways4.trackex.common.presentation.widget.CirclePagerIndicatorDecoration
import kotlinx.android.synthetic.main.item_summary.view.*

class SummaryItemHolder(itemView: View) : HomeItemHolder(itemView) {

    private var adapter = SummaryAdapter()
    private val layoutManager = LinearLayoutManager(itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false)
    private val indicatorDecoration =
        CirclePagerIndicatorDecoration(
            itemView.context,
            R.color.icon_active_light,
            R.color.icon_inactive_light
        )
    private val snapHelper = PagerSnapHelper()

    init {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        itemView.recycler_view.adapter = adapter
        itemView.recycler_view.layoutManager = layoutManager
        itemView.recycler_view.itemAnimator = null
        itemView.recycler_view.addItemDecoration(indicatorDecoration)
        snapHelper.attachToRecyclerView(itemView.recycler_view)
    }

    fun bind(model: SummaryItemModel) {
        itemView.chip_date_range.text = model.dateRangeText
        itemView.chip_date_range.setOnClickListener { showPopupMenu(model) }
        itemView.text_message.isVisible = model.itemModels.isEmpty()
        adapter.submitList(model.itemModels)
    }

    private fun showPopupMenu(model: SummaryItemModel) {
        val popupMenu = PopupMenu(itemView.context, itemView.chip_date_range)
        popupMenu.inflate(R.menu.menu_date_range)
        popupMenu.setOnMenuItemClickListener { menuItemSelected(it, model) }
        popupMenu.show()
    }

    private fun menuItemSelected(item: MenuItem, model: SummaryItemModel): Boolean {
        return when (item.itemId) {
            R.id.today -> { model.onTodayClick(); true }
            R.id.this_week -> { model.onThisWeekClick(); true }
            R.id.this_month -> { model.onThisMonthClick(); true }
            R.id.last_month -> { model.onLastMonthClick(); true }
            R.id.month_minus02 -> { model.onMonthMinus02Click(); true }
            R.id.month_minus03 -> { model.onMonthMinus03Click(); true }
            R.id.month_minus12 -> { model.onMonthMinus12Click(); true }
            R.id.month_january -> { model.onJanuaryClick(); true }
            R.id.month_february -> { model.onFebruaryClick(); true }
            R.id.month_march -> { model.onMarchClick(); true }
            R.id.month_september -> { model.onSeptemberClick(); true }
            R.id.month_october -> { model.onOctoberClick(); true }
            R.id.month_november -> { model.onNovemberClick(); true}
            R.id.month_december -> { model.onDecemberClick(); true}
            R.id.all_time -> { model.onAllTimeClick(); true }

//            R.id.october -> {model.onOctoberClick(); true}
//            R.id.november -> {model.onNovemberClick(); true}
            else -> false
        }
    }

    fun recycle() {
        itemView.chip_date_range.text = ""
        itemView.chip_date_range.setOnClickListener(null)
        itemView.text_message.isVisible = false
        adapter.submitList(emptyList())
    }
}