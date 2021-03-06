package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView


class HillfortView : BaseView(), AnkoLogger {

    lateinit var presenter: HillfortPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        init(toolbar, true, false)
        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }
        btn_chooseImage.setOnClickListener { presenter.doSelectImage() }
        chkbox_visited.setOnClickListener { presenter.doSetDateVisited(chkbox_visited.isChecked) }
        rate_hillfort.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            presenter.doSetRating(rating)
        }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        txt_hillfortTitle.setText(hillfort.title)
        txt_hillfortDescription.setText(hillfort.description)
        rate_hillfort.rating = hillfort.rating
        chkbox_visited.isChecked = hillfort.visited
        // load image from remote storage
        Glide.with(this).load(hillfort.image).into(img_hillfortImage)
        // when image it detected, change the label (change image)
        if (hillfort.image.isNotEmpty()) {
            btn_chooseImage.setText(R.string.change_hillfort_image)
        }
    }

    // load the menu resource (inflate the menu)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        // check "edit" flag when inflating the menu, and display delete only if we are in edit mode
        if (presenter.edit && menu != null) menu.getItem(1).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    // the menu event handler
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_save -> {
                if (txt_hillfortTitle.text.toString().isEmpty()) {
                    toast(R.string.toast_enter_hillford_title)
                } else {
                    presenter.doAddOrSave(
                        txt_hillfortTitle.text.toString(),
                        txt_hillfortDescription.text.toString()
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}


