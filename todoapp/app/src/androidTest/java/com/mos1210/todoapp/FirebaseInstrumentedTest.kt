package com.mos1210.todoapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.google.gson.JsonObject
import com.mos1210.todoapp.data.FirebaseService
import com.mos1210.todoapp.data.Todo
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FirebaseInstrumentedTest {

    companion object {
        val TAG: String = FirebaseInstrumentedTest::class.java.simpleName
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.mos1210.todoapp", appContext.packageName)
    }

    /**
     *
     */
    @Test
    fun getTodoList() {
        val build = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://todolist-f7571.firebaseio.com/")
            .build()

        //FirebaseのJSONフォーマット
//            {
//                "-LRqZKONzcnEiQjOC33R": {
//                    "complete": false,
//                    "description": "aaa",
//                    "timestamp": 1543071697177
//                },
//                "-LRq_sJHOBnmrFyxHFiX": {
//                    "complete": false,
//                    "description": "bbb",
//                    "timestamp": 1543071698177
//                }
//            }


        val service = build.create(FirebaseService::class.java)

        //Firebaseからデータを取得
        val response = service.getTodoList().execute()
        if (response.isSuccessful) {
            //取得成功

            val body: JsonObject? = response.body()

            val list = mutableListOf<Todo>()

            body?.keySet()?.forEach {
                val data: JsonObject = body.getAsJsonObject(it)
                list.add(
                    Todo(
                        it,
                        data.get("complete").asBoolean,
                        data.get("description").asString,
                        data.get("timestamp").asLong
                    )
                )
            }

            //ログで出力確認
            for (a in list) {
                Log.d(TAG, "key:${a.key} complete:${a.complete} description:${a.description} timestamp:${a.timestamp}")
            }

        } else {
            //取得失敗
            fail()
        }
    }

    @Test
    fun update(){

//        {
//            "name": "-LS5RpoAlhDS5HGqce2G"
//        }

    }
}
