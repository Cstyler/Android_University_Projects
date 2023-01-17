package bmstu.ru.lab4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_daughter.*

class DaughterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daughter)

        daughter_done_button.setOnClickListener {
            val intent = Intent()
            intent.putExtra("Login", editTextDaughter.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
