package com.emrealtunbilek.havadurumuapp

import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.spinner_tek_satir.*
import kotlinx.android.synthetic.main.spinner_tek_satir.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var tvSehir:TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     var spinnerAdapter=ArrayAdapter.createFromResource(this,R.array.sehirler, R.layout.spinner_tek_satir)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnSehirler.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)

        spnSehirler.setTitle("Şehir Seçin")
        spnSehirler.setPositiveButton("SEÇ")
        spnSehirler.adapter=spinnerAdapter

        spnSehirler.setOnItemSelectedListener(this)


        verileriGetir("Ankara")



    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var secilenSehir=parent?.getItemAtPosition(position).toString()
        tvSehir = view as TextView
        verileriGetir(secilenSehir)
    }

    fun verileriGetir(sehir:String){

        val url="http://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=abbcd2fcfec741ec783669c98b7f39d1&lang=tr&units=metric"

        val havaDurumuObjeRequest = JsonObjectRequest(Request.Method.GET, url,null, object:Response.Listener<JSONObject>{


            override fun onResponse(response: JSONObject?) {

                var main = response?.getJSONObject("main")
                var sicaklik= main?.getInt("temp")
                tvSicaklik.text=sicaklik.toString()



                var sehirAdi = response?.getString("name")


                var weather=response?.getJSONArray("weather")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                tvAciklama.text=aciklama

                var icon = weather?.getJSONObject(0)?.getString("icon")

                if(icon?.last() == 'd'){
                    rootLayout.background=getDrawable(R.drawable.bg)
                    spnSehirler.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
                    tvSehir?.setTextColor(resources.getColor(R.color.colorAccent))
                    tvAciklama.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                    tvTarih.setTextColor(resources.getColor(R.color.colorAccent))
                    tvDerece.setTextColor(resources.getColor(R.color.colorAccent))


                }else {
                    rootLayout.background=getDrawable(R.drawable.gece)
                    tvSehir?.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    spnSehirler.background.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)
                    tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvDerece.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                }

                var resimDosyaAdi=resources.getIdentifier("icon_"+icon?.sonKarakteriSil(),"drawable", packageName) //R.drawable.icon_50n
                imgHavaDurumu.setImageResource(resimDosyaAdi)

                tvTarih.text=tarihYazdir()



            }


        }, object : Response.ErrorListener{

            override fun onErrorResponse(error: VolleyError?) {

            }

        })


        MySingleton.getInstance(this)?.addToRequestQueue(havaDurumuObjeRequest)
    }

    fun tarihYazdir():String{

        var takvim=Calendar.getInstance().time
        var formatlayici=SimpleDateFormat("EEEE, MMMM yyyy", Locale("tr"))
        var tarih=formatlayici.format(takvim)

        return tarih


    }
}

private fun String.sonKarakteriSil(): String {
    //50n ifadeyi 50 olarak geriye yollar
    return this.substring(0, this.length-1)
}
