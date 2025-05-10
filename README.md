# Instabug-task
### App screenshots
<p align="center">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.39 PM.jpeg" height="400" width="200">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.38 PM (1).jpeg" height="400" width="200">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.38 PM.jpeg" height="400" width="200">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.37 PM.jpeg" height="400" width="200">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.36 PM (1).jpeg" height="400" width="200">
  <img src="images/WhatsApp Image 2025-05-10 at 11.10.36 PM.jpeg" height="400" width="200">
    <img src="images/WhatsApp Image 2025-05-10 at 11.10.34 PM.jpeg" height="400" width="200">
      <img src="images/WhatsApp Image 2025-05-10 at 11.10.35 PM.jpeg" height="400" width="200">
</p>





### Tools & APIs:
- Kotlin
- Compose  
- ViewModel
- Live data
- I didn't use Dagger hilt for DI as it's considered as 3rd parties.



### Code Architecture:
Mvvm
<p align="left">
  <img src="images/Screenshot 2025-05-10 232018.png" height="600" width="350">
</p>


### Requriments:
Summary:
The goal of this task is to build a weather tracking app. The app should do
the following:
1. Get the user's location using the device's GPS (latitude, longitude)
2. Get current weather and 5-day forecast using this API (You need to
create an API key first)
3. The app must have 2 screens. 1 for displaying the current weather
and another one for displaying the 5-day weather forecast
4. The first screen should be refreshable to get updated data with each
refresh
5. The app must handle the offline state and show an error message
Notes:
● Your app must not use any third-party libraries. We consider Retrofit,
volley, coroutines, room....etc, as 3rd parties

### Notes:
Your app must not use any third party libraries. We consider (Retrofit,
volley, coroutines, room….etc) as 3rd parties



### For background tasks:
used: [Executers](https://developer.android.com/reference/java/util/concurrent/Executors)

### For Network Calls:
used: [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection)


