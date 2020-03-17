package ipca.example.lastestnews

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    //https://newsapi.org/v2/top-headlines?country=pt&apiKey=1765f87e4ebc40229e80fd0f75b6416c

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : AsyncTask<Void,Void,String>(){
            override fun doInBackground(vararg params: Void?): String {

                val url = URL(BASE_API + PATH + API_KEY)
                val urlContent = url.readText(Charset.defaultCharset())
                Log.d("lastestnews", urlContent)



                return urlContent
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                val jsonResult = JSONObject(result)
                var okResult = jsonResult.getString("status")
                if (okResult.compareTo("ok")==0){
                    Toast.makeText(this@MainActivity, "Boas Noticias", Toast.LENGTH_LONG).show()

                    val articleJsonArray = jsonResult.getJSONArray("articles")




                }else{
                    Toast.makeText(this@MainActivity, "Erro ao descrregar noticias", Toast.LENGTH_LONG).show()
                }




            }

        }.execute()




    }

    companion object{

        val BASE_API = "https://newsapi.org/v2/"
        val PATH     = "top-headlines?country=pt"
        val API_KEY  = "&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

    }
}
