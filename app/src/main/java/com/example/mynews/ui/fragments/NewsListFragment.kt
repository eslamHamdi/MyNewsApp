package com.example.mynews.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentNewsListBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import com.example.mynews.utils.hasLocationPermissions
import com.example.mynews.utils.observeInLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class NewsListFragment : Fragment(), EasyPermissions.PermissionCallbacks,NewsAdapter.OnArticleClick
{

    lateinit var binding: FragmentNewsListBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
   lateinit var newsAdapter:NewsAdapter
    lateinit var geocoder: Geocoder
    var isoCode:String? = null
    var countryName =""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor


    var locationCallback: LocationCallback? =null
    val viewModel: NewsViewModel by sharedViewModel()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //to save user country code for later
        sharedPreferences = this.requireContext().getSharedPreferences("code", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        isoCode = sharedPreferences.getString("code","us")

        isoCode?.let { viewModel.getNews(it) }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_news_list, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        geocoder = Geocoder(this.requireContext())

        binding.refreshLayOut.setOnRefreshListener {
            isoCode?.let { viewModel.getNews(it) }
            binding.refreshLayOut.isRefreshing = false
        }

        return binding.root
    }


    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.toastFlow.onEach {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
        }.observeInLifecycle(viewLifecycleOwner)


        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(binding.newsRecycler)

        viewModel.news.observe(viewLifecycleOwner, {
            newsAdapter = NewsAdapter()
            binding.newsRecycler.adapter = newsAdapter
           it?.let {
               if (it.isNotEmpty())
               {
                   newsAdapter.submitList(it)
                   newsAdapter.articleClickListener = this
               }

           }
        })

        binding.filterByLocation.setOnClickListener {
            requestPermissions()


        }


    }


    private fun requestPermissions()
    {
        if (hasLocationPermissions(requireContext()))
        {
            getUserLocation()
        } else
        {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permission to get News From your Area.",
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION

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
        getUserLocation()
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
        if (hasLocationPermissions(this.requireContext()))
        {

            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                smallestDisplacement = 100f
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                Log.d(null, "getuserlocation: entered1")
            }

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this.requireActivity())
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener {

                val flag = it.locationSettingsStates?.isLocationUsable
                if (flag == true)
                {
                    locationCallback = object : LocationCallback()
                    {
                        override fun onLocationResult(locationResult: LocationResult)
                        {
                            val location = locationResult.lastLocation
                            location?.let {
                                getCountry(location)
                                Log.e(null, "onLocationResult: $location" )
                                Log.e(null, "onLocationResult: $countryName" )

                            }

                        }
                    }
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, Looper.getMainLooper())
                }

            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException)
                {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    Log.d(null, "setuserlocation: location failure")
                    try
                    {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(this.requireActivity(), 0x1)
                    } catch (sendEx: IntentSender.SendIntentException)
                    {
                        // Ignore the error.
                    }
                }
            }


        } else
        {
            requestPermissions()
        }


    }

    override fun clickArticle(article: Article)
    {
         findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToArticleFragment(article))
    }

    //used structured concurrency to ensure syncronized background tasks
    private fun getCountry(location:Location?)
    {

            lifecycleScope.launch(Dispatchers.Main) {

                    countryName = async {
                        try
                        {
                            geocoder.getFromLocation(location!!.latitude, location.longitude, 1).first()
                                    .run {
                                        val sb = StringBuilder()
//                                            for (i in 0 until this.maxAddressLineIndex)
//                                            {
//                                                sb.append(this.getAddressLine(i)).append("\n")
//                                            }
                                        //sb.append(this.locality).append("\n")
                                        //sb.append(this.thoroughfare).append("\n")
                                        sb.append(this.countryName)
                                        sb.toString()


                                    }
                        } catch (e: Exception)
                        {

                            Log.d("adressList", "onLocationSelected: $e.localizedMessage")
                            e.localizedMessage!!
                        }
                    }.await()

                isoCode =  getCountryCode(countryName)
                Log.d(null, "onLocationResult:$isoCode ")

                if (!isoCode.isNullOrEmpty())
                {
                    editor.putString("code",isoCode)
                    editor.commit()
                    viewModel.getNews(isoCode!!)
                    //stop tracking user location once code is retrieved
                    stopLocationUpdates()
                }


                }

            }





    //convert countryName i got from location to isoCode
    fun getCountryCode(Name: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == Name }

    private fun stopLocationUpdates()
    {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }

    }






}
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

