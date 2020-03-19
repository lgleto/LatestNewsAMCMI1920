package ipca.example.lastestnews

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    //https://newsapi.org/v2/top-headlines?country=pt&apiKey=1765f87e4ebc40229e80fd0f75b6416c

    val articles : MutableList<Article> = ArrayList<Article>()

    val articlesAdapter = ArticlesAdapter()

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


                    for (index in 0 until articleJsonArray.length()) {
                        val jsonArticle = articleJsonArray[index] as JSONObject
                        articles.add(Article.parseJson(jsonArticle))
                    }

                    articlesAdapter.notifyDataSetChanged()

                }else{
                    Toast.makeText(this@MainActivity, "Erro ao descrregar noticias", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()


        listViewArticles.adapter = articlesAdapter
    }

    inner class ArticlesAdapter : BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view  = layoutInflater.inflate(R.layout.row_article,parent,false)

            val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
            val textViewDate  = view.findViewById<TextView>(R.id.textViewDate)

            textViewTitle.text = articles[position].title
            textViewDate.text  = articles[position].publishedAt

            view.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)

                intent.putExtra("title", articles[position].title)
                intent.putExtra("url", articles[position].url)



                startActivity(intent)
            }


            return view
        }

        override fun getItem(position: Int): Any {
            return articles[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return articles.size
        }

    }

    companion object{

        val BASE_API = "https://newsapi.org/v2/"
        val PATH     = "top-headlines?country=pt"
        val API_KEY  = "&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

    }
}
