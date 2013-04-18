Android Data Core
=================

This projects aims to be a generic template to implement network availability-aware cached webservices on Android. Remote resource are fetched using Spring's open-source RestTemplate library. Local resources are cached using serialized JSON objects that are recreated entirely every change on the database, but SQLite implementation is on my roadmap.

## How it works

The project is divided into __local__ and __remote__ packages. 

Every  `Resource` that you want to consume and cache must have a concrete implementation of `RemoteResourceProvider` and `LocalResourceProvider` into respective package.

This template also provides error handling, returning a `Bundle` with the Entity type, HTTP method and status code. The implementation is highly based on ellipsis/varargs (the famous `...` ).

### `Resource.java` enforces resource IDs
Contract that your entities must conform to. It only forces resources to implement an `id` method to provide a _resource identifier_ to be used on requests.

### `ResourceProvider.java` is the root of all evil
Abstract class that resource providers must extend. Built-in this repository there's two types of providers, `RemoteResourceProvider` and `LocalResourceProvider`. Both implements a generic way to request resources by querying a URI using `getResourcePath`. By extending this classes, every entity you want to fetch and cache must return a formatted URI by leveraging the number of parameters received.

### `AsyncProviderHelper.java` don't let requests block the UI

An `AsyncTask` subclass that fetches remote or local data asynchronously and notify requesters via `OnDataReadyListener` interface.

### `DataProvider.java` as an abstraction layer

This class wrapps all "low-level" logic that would go into Activities for checking network state and choosing the right store to fetch the data.

## How to use

1. Create your model as you would. `Person`, for example.
2. Implement `Resource` on `Person`.
3. Create `RemotePersonResourceProviderImpl` class into __remote__ package.
4. Implement `getResourcePath` method to return the URI based on parameters.
5. Implement `get` method to return one or a list of resources.
6. (Optional) Implement methods (`delete`, `post`, etc) that need special treatment.
7. Go into `DataProvider.java` and implement a high-level method for returning a `AsyncProviderHelper`.
8. Implement `OnDataReadyListener` on Activity.
9. On Activity call `DataProvider.getInstance().personFor(this).doGet()` to get a list of people.
10. Call `DataProvider.getInstance().personFor(this).doGet(1)` to get the person with identifier 1.