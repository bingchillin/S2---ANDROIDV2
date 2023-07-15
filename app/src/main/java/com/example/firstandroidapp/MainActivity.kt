package com.example.firstandroidapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
    }
}

class ProductsListFragment : Fragment() {
    // Equivalent du setContentView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list)
        val data =
            mutableListOf(
                generateFakeProduct(),
                generateFakeProduct(),
                generateFakeProduct(),
                generateFakeProduct(),
                generateFakeProduct(),
            )
        val daysAdapter = ProductsAdapter(data, object : OnProductClickListener {
            override fun onProductClicked(product: Product) {
                // Ouvrir l'écran
                findNavController().navigate(ProductsListFragmentDirections.actionProductsListFragmentToProductFragment(product.barcode))
            }
        })
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = daysAdapter
    }
}

class ProductFragment : Fragment() {

    private val model: ProductViewModel by viewModels()

    // Equivalent du setContentView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.response_api, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barcode = ProductFragmentArgs.fromBundle(requireArguments()).barcode
        model.findProduct(barcode)

        lifecycleScope.launch {
            model.productFlow.collect{ response ->
                if (response != null) {

                    Glide.with(this@ProductFragment).load(response.picture)
                        .into(view.findViewById<ImageView>(R.id.image_product))
                    //view.findViewById<TextView>(R.id.image_product).text = response.picture
                    view.findViewById<TextView>(R.id.name_product).text = response.name
                    view.findViewById<TextView>(R.id.altName_product).text = response.altName
                    view.findViewById<TextView>(R.id.barcode_product).text = response.barcode
                }
            }
        }
    }
}


class ProductsAdapter(val list: List<Product>, val callback: OnProductClickListener) : RecyclerView.Adapter<DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        Log.d("ESGI", position.toString())
        val product = list[position]
        holder.update(product)
        holder.itemView.setOnClickListener {
            callback.onProductClicked(product)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val picture: ImageView = v.findViewById(R.id.product_image)
    private val name: TextView = v.findViewById(R.id.product_name)
    private val brand: TextView = v.findViewById(R.id.product_brand)

    fun update(product: Product) {
        Glide.with(itemView).load(product.thumbnail).into(picture)
        name.text = product.name
        brand.text = product.brand
    }

}

interface OnProductClickListener {
    fun onProductClicked(product: Product)
}


class ProductViewModel: ViewModel() {
    //val productLiveData = MutableLiveData<Age>()
    val productFlow = MutableStateFlow<Response?>(null)

    fun findProduct(barecode: String) {
        viewModelScope.launch {
            val product = NetworkManager2.getFood(barecode).await()
            //ageLiveData.postValue(age)
            productFlow.emit(product.response)
        }
    }
}
