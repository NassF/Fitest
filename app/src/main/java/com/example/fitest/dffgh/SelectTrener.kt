package com.example.fitest.dffgh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitest.R


import android.util.Log
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitest.ListClient.ClientAdapter
import com.example.fitest.ProfileTrenerView
import com.example.fitest.TrenerSelectClientView

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class SelectTrener : AppCompatActivity() {

    companion object {
        const val SORT_NAME = "name"
        const val SORT_POPULATION = "price"
    }

    private lateinit var root: ViewGroup
    private lateinit var adapter: ItemAdapter

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val refStates by lazy {
        firestore.collection("treners")
    }

    private var sort = SORT_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        root = findViewById(R.id.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.inflateMenu(R.menu.refresh)
        toolbar.inflateMenu(R.menu.sort)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_refresh -> {
                    adapter.clear()
                    adapter.startListening()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_sort -> {
                    if (sort == SORT_NAME) sort = SORT_POPULATION else sort = SORT_NAME
                    snackbar("Sorting by $sort")
                    adapter.clear()
                    adapter.startListening()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }

        val query = Firebase.auth.currentUser!!.uid.let {

            refStates.limit(10)
                .orderBy(sort, Query.Direction.ASCENDING)
        }


        adapter =
            ItemAdapter {
                query
            }


        adapter.onClickListener = { position, email ->
            Snackbar.make(root, "$position clicked", Snackbar.LENGTH_SHORT)
                .show()
            firestore.collection("treners").get().addOnSuccessListener { documents ->
                var value = ""
                for (document in documents) {
                    if (document.data.containsValue(email)) {
                        value = document.id
                        Log.i("Collection", "${email}=> ${document.data}")
                    } else {
                        Log.i("Collection", "${document.id}=> ${document.data}")
                    }


                }
                val intent = Intent(this, TrenerSelectClientView::class.java)
                Log.i("DocId", value)
                intent.putExtra("id", value)
                Log.i("Intent", value)

                startActivity(intent)
            }
                .addOnFailureListener { exception ->
                    Log.w("CollectionError", "Error getting documents: ", exception)
                }

        }
        /*    adapter.onDeleteListener = { position ->
            //assume success, otherwise it will be updated in the next query
            val state = adapter.get(position)
            val snapshot = adapter.getSnapshot(position)
           // delete(state, snapshot.reference)
        }

        adapter.onUpListener = { position ->
            val state = adapter.get(position)
            val snapshot = adapter.getSnapshot(position)
          //  incrementPopulation(state, snapshot.reference)
            //shows us waiting for the update
        }*/

        val list = findViewById<RecyclerView>(R.id.list)
        val layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        list.layoutManager = layoutManager
        adapter.setupOnScrollListener(list, layoutManager)

        adapter.onLoadingMore = {
            log("onLoadingMore")
        }
        adapter.onLoadingMoreComplete = {
            log("onLoadingMoreComplete")
        }
        adapter.onHasLoadedAll = {
            log("onHasLoadedAll")
        }

    }

        override fun onStart() {
            super.onStart()
            adapter.clear()
            adapter.startListening()
        }

        override fun onStop() {
            super.onStop()
            adapter.stopListening()
        }

        fun log(string: String) {
            Log.d("TEST", string)
        }

        fun snackbar(string: String) {
            Snackbar.make(root, string, Snackbar.LENGTH_SHORT)
                .show()
        }


        /* fun incrementPopulation(state: State, docRef: DocumentReference) {
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val newPopulation = snapshot.getDouble("price")!! + 1
            transaction.update(docRef, "price", newPopulation)

            // Success
            null
        }.addOnSuccessListener {
            log("Transaction success!")
        }.addOnFailureListener { e ->
            e.printStackTrace()
            snackbar("Failed to increment ${state.name}")
        }
    }*/

        /*fun delete(state: State, docRef: DocumentReference) {
        docRef.delete()
            .addOnSuccessListener {
                log("Transaction success!")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                snackbar("Failed to delete ${state.name}")
            }
    }*/

}