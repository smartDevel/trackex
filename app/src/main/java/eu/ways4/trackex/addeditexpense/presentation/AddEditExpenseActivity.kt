package eu.ways4.trackex.addeditexpense.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import eu.ways4.trackex.R
import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.common.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_add_edit_expense.*

/**
 * The `AddEditExpenseActivity` class displays the UI for adding or editing expenses.
 * It also sets up the navigation graph and the `AddEditExpenseActivityModel` for this activity.
 */
class AddEditExpenseActivity : BaseActivity() {

    override var animationKind = ANIMATION_SLIDE_FROM_BOTTOM

    /**
     * A [AddEditExpenseActivityModel] instance to store and manage the data for this activity.
     */
    private lateinit var model: AddEditExpenseActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_expense)
        setSupportActionBar(findViewById(R.id.toolbar))
        setGraph()
        initializeModel()
    }

    /**
     * Sets up the navigation graph for this activity.
     */
    private fun setGraph() {
        val navController = (fragment_navigation_host as NavHostFragment).navController
        navController.setGraph(makeGraph(navController), makeStartDestinationBundle())
    }

    /**
     * Inflates the navigation graph for this activity and sets the start destination.
     *
     * @param navController The [NavController] for this activity.
     * @return The inflated navigation graph.
     */
    private fun makeGraph(navController: NavController): NavGraph {
        val graph = navController.navInflater.inflate(R.navigation.navigation_add_edit_expense)
        graph.startDestination = R.id.fragment_add_edit_expense
        return graph
    }

    /**
     * Creates the start destination bundle for this activity's navigation graph.
     *
     * @return The start destination bundle.
     */
    private fun makeStartDestinationBundle(): Bundle {
        val expense = intent.getParcelableExtra<Expense>(EXTRA_EXPENSE)
        return AddEditExpenseFragmentArgs.Builder(expense).build().toBundle()
    }

    /**
     * Initializes the `AddEditExpenseActivityModel` instance for this activity.
     */
    private fun initializeModel() {
        model = ViewModelProviders.of(this).get(AddEditExpenseActivityModel::class.java)
    }

    companion object {

        /**
         * The key for the `Expense` object in the intent extras.
         */
        private const val EXTRA_EXPENSE = "expense"

        /**
         * Starts the `AddEditExpenseActivity` with the given `Expense` object as an extra.
         *
         * @param context The context from which the activity is started.
         * @param expense The `Expense` object to be passed as an extra in the intent.
         */
        fun start(context: Context, expense: Expense?) {
            val intent = Intent(context, AddEditExpenseActivity::class.java).apply {
                expense?.let { putExtra(EXTRA_EXPENSE, it) }
            }
            context.startActivity(intent)
        }
    }

}