package com.jonathan.practice.rxjavapractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** practice basics - create observable and filter emission **/
//        val tasks = MyDataSource.createTasks(5)
//        val observable = Observable
//            .fromIterable(tasks)            //1) observe what?
//            .subscribeOn(Schedulers.io())   //2) observe of where?
//            .filter{                        //3) when to observe?
//                try{
//                    Thread.sleep(1000)
//                }catch(e: InterruptedException){
//                    e.printStackTrace()
//                }
//                it.setComplete(true)
//                it.isComplete   //emit data ONLY when return value is true
//            }.observeOn(AndroidSchedulers.mainThread()) //4) observe from where?
//
//        observable.subscribe(object: Observer<MyTask>{
//            override fun onSubscribe(d: Disposable?) {
//                disposables.add(d)
//            }
//
//            override fun onNext(t: MyTask?) { //called when resulting data is posted to observer
//                Log.d("", "onNext called with: ${t?.description}")
//                t?.let{
//                    Log.d("", "task status: ${it.description}, ${t.isComplete}")
//                }
//            }
//
//            override fun onError(e: Throwable?) {
//                e?.printStackTrace()
//            }
//
//            override fun onComplete() {
//                Log.d("", "onComplete.")
//            }
//
//        })

        /** practice range-map combo **/
//        val observable2 = Observable
//            .range(0, 20)
//            .subscribeOn(Schedulers.io())
//            .map{   //map Int to MyTask - combo with takeWhile() below
//                MyTask("task $it", it)
//            }
//            .takeWhile{
//                it.priority < 10
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//
//        observable2.subscribe(object: Observer<MyTask>{
//            override fun onSubscribe(d: Disposable?) {
//                disposables.add(d)
//            }
//
//            override fun onNext(t: MyTask?) {
//                Log.d("","onNext called with: ")
//                t?.let{Log.d("", "task with priority[${it.priority}], isComplete[${it.isComplete}]")}
//            }
//
//            override fun onError(e: Throwable?) {
//                e?.printStackTrace()
//            }
//
//            override fun onComplete() {
//                Log.d("", "onComplete.")
//            }
//        })

        /** practice buffer operator **/
//        btn.clicks()
//            .map{ _ ->
//                Log.d("", "clicked!")
//                1
//            }
//            .buffer(4, TimeUnit.SECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object: Observer<List<Int>>{
//                override fun onSubscribe(d: Disposable?) {
//                    disposables.add(d)
//                }
//
//                override fun onNext(t: List<Int>?) {
//                    val res = "clicks: ${t?.size}"
//                    tv.text = res
//                    Log.d("", "clicks: ${t?.size}")
//                }
//
//                override fun onError(e: Throwable?) {
//                }
//
//                override fun onComplete() {
//                }
//            })

        /** practice debounce operator **/
        val observableQueryText = Observable
            .create(object: ObservableOnSubscribe<String>{
                override fun subscribe(emitter: ObservableEmitter<String>?) {
                    sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false    //true if the query has been handled by the listener, false to let the SearchView perform the default action.
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if(emitter != null && !emitter.isDisposed)
                                emitter.onNext(newText)
                            return false    //true if the action was handled by the listener, false if the SearchView should perform the default action of showing any suggestions if available.
                        }
                    })
                }
            })
            .debounce(1, TimeUnit.SECONDS)    //emit 500ms after last emitter.onNext()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        observableQueryText.subscribe(object: Observer<String>{
            override fun onSubscribe(d: Disposable?) {
                d?.let{disposables.add(it)}
            }

            override fun onNext(t: String?) {
                tv_sv_result.text = t
                requestToServer(t)
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onComplete() {
                tv.text = "onCopmlete."
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun requestToServer(s: String?){

    }

}
