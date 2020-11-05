## Dependencies
* RxJava - Main components
* RxKotlin - additional components for Kotlin
* RxAndroid - connects android threads with RxJava scheduler
* RxBinding - bind with android view components

### Observables

One way to create Observable:
```kotlin
val myObservable: Observable<MyTask> = Observable
    .fromIterable(MyDataSource.createTasksList())
    .subscribeOn(Schedulers.io())   //task ran on io thread
    .filter(object: Predicate<MyTasK>(){
        override fun test(task: MyTask): Boolean{   //returns true if task is complete
            //(...)
            return task.isComplete()
        }
    }).observeOn(AndriodSchedulers.mainThread())    //observe on android's main thread
```

## Disposable
* Enables cleaning up of unnecessary observables.
* Keep track of observables in ViewModel.

One way of adding observable to disposables:
```kotlin
class MyActivity: AppCompatActivity(){

    val disposables = CompositeDisposable()
    
    override fun onCreate(){
        //(...)
        myObservable.subscribe(object: Observer<MyTask>(){
            override fun onSubscribe(d: Disposable){
                //(...)
                disposables.add(d)
            }
            //(...)
        })
    }

    //(...)
    
    override fun onDestroy(){
        super.onDestroy()
        disposables.clear()
        //disposables.dispose() - Not recommended. Hardcore version of .clear()
    }
}
```

The other way of adding observable to disposables:
```kotlin
class MyActivity: AppCompatActivity(){

    private val disposables = CompositeDisposable()
   
    override fun onCreate(){
        //(...)
        val d1 = taskObservable.subscribe(object: Consumer<MyTask>{
            override fun accept(task: Task){
                //do something with task
            }
        })
        disposables.add(d1)
    }
}
```

# Operators

## Creeate, Just, Range, Repeat
* ```create()``` - most flexible to determine what kind of object to observe on and what to do on subscription
* ```just()``` - observe on most 10 result (Mostly for observing on single ```Observable```)

## FlatMap