package com.example.mynews.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentNewsListBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import com.example.mynews.utils.hasLocationPermissions
import com.example.mynews.utils.observeInLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class NewsListFragment : Fragment(), EasyPermissions.PermissionCallbacks,NewsAdapter.OnArticleClick {

    lateinit var binding:FragmentNewsListBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var newsAdapter = NewsAdapter()

    val viewModel:NewsViewModel by sharedViewModel()

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_news_list,container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())


        viewModel.toastFlow.onEach {
            Toast.makeText(this.requireContext(),it, Toast.LENGTH_SHORT).show()
        }.observeInLifecycle(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.news.observe(viewLifecycleOwner,{
            newsAdapter.submitList(it)
        })

        binding.newsRecycler.adapter = newsAdapter


    }
    private fun requestPermissions()
    {
        if (hasLocationPermissions(requireContext()))
        {
            getUserLocation()
        }
        else
        {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permission to get News From your Area.",
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>)
    {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms.toString()))
        {
            SettingsDialog.Builder(this.requireContext()).build().show()
        } else
        {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>)
    {
        return
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation()
    {
        if (hasLocationPermissions(requireContext()))
        {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                    }
                    .addOnFailureListener {

                    }
        }
    }

    override fun clickArticle(article: Article)
    {

    }

}
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34