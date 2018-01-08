package com.emrealtunbilek.havadurumuapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url="http://api.openweathermap.org/data/2.5/weather?q=Ankara,tr&appid=abbcd2fcfec741ec783669c98b7f39d1&lang=tr&units=metric"
        val havaDurumuObjeRequest = JsonObjectRequest(Request.Method.GET, url,null, object:Response.Listener<JSONObject>{


            override fun onResponse(response: JSONObject?) {

                var main = response?.getJSONObject("main")
                var sicaklik= main?.getString("temp")
                tvSicaklik.text=sicaklik

                Log.e("EMRE", sicaklik)

                var sehirAdi = response?.getString("name")
                tvSehir.text=sehirAdi

                var weather=response?.getJSONArray("weather")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                tvAciklama.text=aciklama

                var icon = weather?.getJSONObject(0)?.getString("icon")

                if(icon?.last() == 'd'){
                    rootLayout.background=getDrawable(R.drawable.bg)
                }else {
                    rootLayout.background=getDrawable(R.drawable.gece)
                    tvAciklama.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSehir.setTextColor(resources.getColor(R.color.colorAccent))
                    tvTarih.setTextColor(resources.getColor(R.color.colorAccent))
                    tvDerece.setTextColor(resources.getColor(R.color.colorAccent))
                }

                var resimDosyaAdi=resources.getIdentifier("icon_"+icon?.sonKarakteriSil(),"drawable", packageName) //R.drawable.icon_50n
                imgHavaDurumu.setImageResource(resimDosyaAdi)

                Log.e("EMRE", sicaklik + " " + sehirAdi + " " + aciklama+" " + icon)

            }


        }, object : Response.ErrorListener{

            override fun onErrorResponse(error: VolleyError?) {

            }

        })


    MySingleton.getInstance(this)?.addToRequestQueue(havaDurumuObjeRequest)

    }
}

private fun String.sonKarakteriSil(): String {
    //50n ifadeyi 50 olarak geriye yollar
    return this.substring(0, this.length-1)
}
