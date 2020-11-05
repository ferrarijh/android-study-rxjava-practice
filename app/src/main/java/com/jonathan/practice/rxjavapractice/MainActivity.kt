package com.jonathan.practice.rxjavapractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

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
        val observable2 = Observable
            .range(0, 20)
            .subscribeOn(Schedulers.io())
            .map{   //map Int to MyTask - combo with takeWhile() below
                MyTask("task $it", it)
            }
            .takeWhile{
                it.priority < 10
            }
            .observeOn(AndroidSchedulers.mainThread())

        observable2.subscribe(object: Observer<MyTask>{
            override fun onSubscribe(d: Disposable?) {
                disposables.add(d)
            }

            override fun onNext(t: MyTask?) {
                Log.d("","onNext called with: ")
                t?.let{Log.d("", "task with priority[${it.priority}], isComplete[${it.isComplete}]")}
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onComplete() {
                Log.d("", "onComplete.")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}
