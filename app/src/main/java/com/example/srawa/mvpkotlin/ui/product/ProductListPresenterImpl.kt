package com.example.srawa.mvpkotlin.ui.product

import com.example.srawa.mvpkotlin.database.AppDatabase
import com.example.srawa.mvpkotlin.database.product.ProductRepo
import com.example.srawa.mvpkotlin.database.product.ProductRepoImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductListPresenterImpl(var productListView: ProductListView) : ProductListPresenter {

    override fun searchProductByName(database: AppDatabase, name: String) {
        productListView.clearItems()
        productListView.showProgress()

        val productRepo: ProductRepo = ProductRepoImpl(database.productDao())
        productRepo.getProductsByName(name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    productListView.setItems(items = it)
                    productListView.hideProgress()
                }
    }
}