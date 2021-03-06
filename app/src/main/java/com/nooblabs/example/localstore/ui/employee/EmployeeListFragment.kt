package com.nooblabs.example.localstore.ui.employee

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nooblabs.example.localstore.R
import com.nooblabs.example.localstore.adapters.EmployeeListAdapter
import com.nooblabs.example.localstore.database.AppDatabase
import com.nooblabs.example.localstore.ui.base.BaseFragment
import com.nooblabs.example.localstore.util.DatabaseBuilder
import kotlinx.android.synthetic.main.fragment_employee_list.*

class EmployeeListFragment : BaseFragment(), EmployeeListView {

    private lateinit var presenter: EmployeeListPresenter

    private lateinit var employeeListAdapter: EmployeeListAdapter

    private lateinit var mDatabase: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_employee_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            mDatabase = DatabaseBuilder.getDatabase(activity.applicationContext)
            init()
            loadInitData()
        }
    }

    fun init() {
        employeeListAdapter = EmployeeListAdapter()
        presenter = EmployeeListPresenterImpl(this)
        employee_list.layoutManager = LinearLayoutManager(activity?.applicationContext)
        employee_list.adapter = employeeListAdapter
        search_emp_btn.setOnClickListener {
            hideSoftKeyboard()
            val searchName = employee_name_search_term.text.toString()
            presenter.searchEmployeesByName(mDatabase, searchName)
        }
    }

    fun loadInitData() {
        presenter.searchEmployeesByName(mDatabase, "")
    }

    override fun getRootView() = view

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun setItems(items: List<EmployeeListView.EmployeeDetail>) {
        val searchResultSize = items.size
        when (searchResultSize) {
            0 -> {
                displayMessage("No results found")
            }
            else -> {
                displayMessage("$searchResultSize results found")
            }
        }
        employeeListAdapter.values = items
    }

    override fun clearItems() {
        displayMessage("")
        employeeListAdapter.values = emptyList()
    }

    override fun displayMessage(message: String) {
        search_result.text = message
    }

}
